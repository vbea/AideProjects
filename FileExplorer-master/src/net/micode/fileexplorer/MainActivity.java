package net.micode.fileexplorer;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import net.micode.fileexplorer.FileExplorerTabActivity.IBackPressedListener;
import net.micode.fileexplorer.FileViewInteractionHub.Mode;

public class MainActivity extends Activity implements IFileInteractionListener, IBackPressedListener
{
	public static final String EXT_FILTER_KEY = "ext_filter";
    private static final String LOG_TAG = "FileViewActivity";
    public static final String EXT_FILE_FIRST_KEY = "ext_file_first";
    public static final String ROOT_DIRECTORY = "root_directory";
    public static final String PICK_FOLDER = "pick_folder";
    private ListView mFileListView;
    // private TextView mCurrentPathTextView;
    private ArrayAdapter<FileInfo> mAdapter;
    private FileViewInteractionHub mFileViewInteractionHub;
    private FileCategoryHelper mFileCagetoryHelper;
    private FileIconHelper mFileIconHelper;
    private ArrayList<FileInfo> mFileNameList = new ArrayList<FileInfo>();
    private Activity mActivity;
    //private View mRootView;
    private static final String sdDir = Util.getSdDirectory();
	private boolean mBackspaceExit;
	private LayoutInflater inflater;
	private ArrayList<PathScrollPositionItem> mScrollPositionList = new ArrayList<PathScrollPositionItem>();
    private String mPreviousPath;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.file_explorer_list);
		init();
		findViewById(R.id.path_pane_up_level).setOnClickListener(new OnClickListener()
		{
				public void onClick(View v) {
					//mActivity.finish();
					startActivity(new Intent(MainActivity.this, FileExplorerTabActivity.class));
				}
		});
	}
	
	private void init()
	{
		mActivity = this;
		inflater = getLayoutInflater().from(MainActivity.this);
		//mRootView = inflater.inflate(R.layout.file_explorer_list, null);
		mFileCagetoryHelper = new FileCategoryHelper(mActivity);
        mFileViewInteractionHub = new FileViewInteractionHub(this);
		Intent intent = mActivity.getIntent();
        String action = intent.getAction();
		if (!TextUtils.isEmpty(action)
			&& (action.equals(Intent.ACTION_PICK) || action.equals(Intent.ACTION_GET_CONTENT))) {
            mFileViewInteractionHub.setMode(Mode.Pick);

            boolean pickFolder = intent.getBooleanExtra(PICK_FOLDER, false);
            if (!pickFolder) {
                String[] exts = intent.getStringArrayExtra(EXT_FILTER_KEY);
                if (exts != null) {
                    mFileCagetoryHelper.setCustomCategory(exts);
                }
            } else {
                mFileCagetoryHelper.setCustomCategory(new String[]{} /*folder only*/);
                findViewById(R.id.pick_operation_bar).setVisibility(View.VISIBLE);

                findViewById(R.id.button_pick_confirm).setOnClickListener(new OnClickListener() {
						public void onClick(View v) {
							try {
								Intent intent = Intent.parseUri(mFileViewInteractionHub.getCurrentPath(), 0);
								mActivity.setResult(Activity.RESULT_OK, intent);
								mActivity.finish();
							} catch (URISyntaxException e) {
								e.printStackTrace();
							}
						}
					});

                findViewById(R.id.button_pick_cancel).setOnClickListener(new OnClickListener() {
						public void onClick(View v) {
							//mActivity.finish();
							startActivity(new Intent(MainActivity.this, FileExplorerTabActivity.class));
						}
					});
            }
        } else {
            mFileViewInteractionHub.setMode(Mode.View);
        }

        mFileListView = (ListView) findViewById(R.id.file_path_list);
        mFileIconHelper = new FileIconHelper(mActivity);
        mAdapter = new FileListAdapter(mActivity, R.layout.file_browser_item, mFileNameList, mFileViewInteractionHub,
									   mFileIconHelper);
		
        boolean baseSd = intent.getBooleanExtra(GlobalConsts.KEY_BASE_SD, !FileExplorerPreferenceActivity.isReadRoot(mActivity));
        Log.i(LOG_TAG, "baseSd = " + baseSd);

        String rootDir = intent.getStringExtra(ROOT_DIRECTORY);
        if (!TextUtils.isEmpty(rootDir)) {
            if (baseSd && this.sdDir.startsWith(rootDir)) {
                rootDir = this.sdDir;
            }
        } else {
            rootDir = baseSd ? this.sdDir : GlobalConsts.ROOT_PATH;
        }
        mFileViewInteractionHub.setRootPath(rootDir);

        String currentDir = FileExplorerPreferenceActivity.getPrimaryFolder(mActivity);
        Uri uri = intent.getData();
        if (uri != null) {
            if (baseSd && this.sdDir.startsWith(uri.getPath())) {
                currentDir = this.sdDir;
            } else {
                currentDir = uri.getPath();
            }
        }
        mFileViewInteractionHub.setCurrentPath(currentDir);
        Log.i(LOG_TAG, "CurrentDir = " + currentDir);

        mBackspaceExit = (uri != null)
			&& (TextUtils.isEmpty(action)
			|| (!action.equals(Intent.ACTION_PICK) && !action.equals(Intent.ACTION_GET_CONTENT)));

        mFileListView.setAdapter(mAdapter);
        mFileViewInteractionHub.refreshFileList();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_MEDIA_MOUNTED);
        intentFilter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
        intentFilter.addDataScheme("file");
        //mActivity.registerReceiver(mReceiver, intentFilter);

        updateUI();
        //setHasOptionsMenu(true);
	}
	
	private int computeScrollPosition(String path) {
        int pos = 0;
        if(mPreviousPath!=null) {
            if (path.startsWith(mPreviousPath)) {
                int firstVisiblePosition = mFileListView.getFirstVisiblePosition();
                if (mScrollPositionList.size() != 0
					&& mPreviousPath.equals(mScrollPositionList.get(mScrollPositionList.size() - 1).path)) {
                    mScrollPositionList.get(mScrollPositionList.size() - 1).pos = firstVisiblePosition;
                    Log.i(LOG_TAG, "computeScrollPosition: update item: " + mPreviousPath + " " + firstVisiblePosition
						  + " stack count:" + mScrollPositionList.size());
                    pos = firstVisiblePosition;
                } else {
                    mScrollPositionList.add(new PathScrollPositionItem(mPreviousPath, firstVisiblePosition));
                    Log.i(LOG_TAG, "computeScrollPosition: add item: " + mPreviousPath + " " + firstVisiblePosition
						  + " stack count:" + mScrollPositionList.size());
                }
            } else {
                int i;
                boolean isLast = false;
                for (i = 0; i < mScrollPositionList.size(); i++) {
                    if (!path.startsWith(mScrollPositionList.get(i).path)) {
                        break;
                    }
                }
                // navigate to a totally new branch, not in current stack
                if (i > 0) {
                    pos = mScrollPositionList.get(i - 1).pos;
                }

                for (int j = mScrollPositionList.size() - 1; j >= i-1 && j>=0; j--) {
                    mScrollPositionList.remove(j);
                }
            }
        }

        Log.i(LOG_TAG, "computeScrollPosition: result pos: " + path + " " + pos + " stack count:" + mScrollPositionList.size());
        mPreviousPath = path;
        return pos;
    }
    public boolean onRefreshFileList(String path, FileSortHelper sort) {
        File file = new File(path);
        if (!file.exists() || !file.isDirectory()) {
            return false;
        }
        final int pos = computeScrollPosition(path);
        ArrayList<FileInfo> fileList = mFileNameList;
        fileList.clear();

        File[] listFiles = file.listFiles(mFileCagetoryHelper.getFilter());
        if (listFiles == null)
            return true;

        for (File child : listFiles) {
            // do not show selected file if in move state
            if (mFileViewInteractionHub.isMoveState() && mFileViewInteractionHub.isFileSelected(child.getPath()))
                continue;

            String absolutePath = child.getAbsolutePath();
            if (Util.isNormalFile(absolutePath) && Util.shouldShowFile(absolutePath)) {
                FileInfo lFileInfo = Util.GetFileInfo(child,
													  mFileCagetoryHelper.getFilter(), Settings.instance().getShowDotAndHiddenFiles());
                if (lFileInfo != null) {
                    fileList.add(lFileInfo);
                }
            }
        }

        sortCurrentList(sort);
        showEmptyView(fileList.size() == 0);
        mFileListView.post(new Runnable() {
				@Override
				public void run() {
					mFileListView.setSelection(pos);
				}
			});
        return true;
    }
	
	private void updateUI() {
        boolean sdCardReady = Util.isSDCardReady();
        View noSdView = findViewById(R.id.sd_not_available_page);
        noSdView.setVisibility(sdCardReady ? View.GONE : View.VISIBLE);

        View navigationBar = findViewById(R.id.navigation_bar);
        navigationBar.setVisibility(sdCardReady ? View.VISIBLE : View.GONE);
        mFileListView.setVisibility(sdCardReady ? View.VISIBLE : View.GONE);

        if(sdCardReady) {
            mFileViewInteractionHub.refreshFileList();
        }
    }
	
	private void showEmptyView(boolean show) {
        View emptyView = findViewById(R.id.empty_view);
        if (emptyView != null)
            emptyView.setVisibility(show ? View.VISIBLE : View.GONE);
    }
	
	@Override
    public View getViewById(int id) {
        return findViewById(id);
    }
	
	@Override
    public boolean onBack() {
        if (mBackspaceExit || !Util.isSDCardReady() || mFileViewInteractionHub == null) {
            return false;
        }
        return mFileViewInteractionHub.onBackPressed();
    }
	
	private class PathScrollPositionItem {
        String path;
        int pos;
        PathScrollPositionItem(String s, int p) {
            path = s;
            pos = p;
        }
    }
	
	@Override
    public Context getContext() {
        return mActivity;
    }

    @Override
    public void onDataChanged() {
        runOnUiThread(new Runnable() {

				@Override
				public void run() {
					mAdapter.notifyDataSetChanged();
				}

			});
    }
	
	@Override
    public void onPick(FileInfo f) {
        try {
            Intent intent = Intent.parseUri(Uri.fromFile(new File(f.filePath)).toString(), 0);
            mActivity.setResult(Activity.RESULT_OK, intent);
            mActivity.finish();
            return;
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
	
	@Override
    public boolean shouldShowOperationPane() {
        return true;
    }

    @Override
    public boolean onOperation(int id) {
        return false;
    }

    //支持显示真实路径
    @Override
    public String getDisplayPath(String path) {
        if (path.startsWith(this.sdDir) && !FileExplorerPreferenceActivity.showRealPath(mActivity)) {
            return getString(R.string.sd_folder) + path.substring(this.sdDir.length());
        } else {
            return path;
        }
    }

    @Override
    public String getRealPath(String displayPath) {
        final String perfixName = getString(R.string.sd_folder);
        if (displayPath.startsWith(perfixName)) {
            return this.sdDir + displayPath.substring(perfixName.length());
        } else {
            return displayPath;
        }
    }

    @Override
    public boolean onNavigation(String path) {
        return false;
    }

    @Override
    public boolean shouldHideMenu(int menu) {
        return false;
    }

    public void copyFile(ArrayList<FileInfo> files) {
        mFileViewInteractionHub.onOperationCopy(files);
    }

    public void refresh() {
        if (mFileViewInteractionHub != null) {
            mFileViewInteractionHub.refreshFileList();
        }
    }

    public void moveToFile(ArrayList<FileInfo> files) {
        mFileViewInteractionHub.moveFileFrom(files);
    }

    public interface SelectFilesCallbackm {
        // files equals null indicates canceled
        void selected(ArrayList<FileInfo> files);
    }

    public void startSelectFiles(SelectFilesCallbackm callback) {
        mFileViewInteractionHub.startSelectFiles(callback);
    }

    @Override
    public FileIconHelper getFileIconHelper() {
        return mFileIconHelper;
    }

    public boolean setPath(String location) {
        if (!location.startsWith(mFileViewInteractionHub.getRootPath())) {
            return false;
        }
        mFileViewInteractionHub.setCurrentPath(location);
        mFileViewInteractionHub.refreshFileList();
        return true;
    }

    @Override
    public FileInfo getItem(int pos) {
        if (pos < 0 || pos > mFileNameList.size() - 1)
            return null;

        return mFileNameList.get(pos);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void sortCurrentList(FileSortHelper sort) {
        Collections.sort(mFileNameList, sort.getComparator());
        onDataChanged();
    }

    @Override
    public ArrayList<FileInfo> getAllFiles() {
        return mFileNameList;
    }

    @Override
    public void addSingleFile(FileInfo file) {
        mFileNameList.add(file);
        onDataChanged();
    }

    @Override
    public int getItemCount() {
        return mFileNameList.size();
    }

    //@Override
    public void runOnUiThreadi(Runnable r) {
        mActivity.runOnUiThread(r);
    }
}
