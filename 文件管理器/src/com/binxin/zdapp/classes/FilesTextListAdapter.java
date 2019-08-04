package com.binxin.zdapp.classes;
//文件管理
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.vbea.file.R;

//使用BaseAdapter来存储取得的文件
public class FilesTextListAdapter extends BaseAdapter
{
	private Context	mContext = null;
	private Activity mActivity = null;
	// 用于显示文件的列表
	private List<FilesText>	mItems	= new ArrayList<FilesText>();
	public FilesTextListAdapter(Context context,Activity v)
	{
		mContext = context;
		mActivity = v;
	}
	//添加一项（一个文件）
	public void addItem(FilesText it) { mItems.add(it); }
	//设置文件列表
	public void setListItems(List<FilesText> lit) { mItems = lit; }
	//得到文件的数目,列表的个数
	public int getCount() { return mItems.size(); }
	//得到一个文件
	public Object getItem(int position) { return mItems.get(position); }
	//能否全部选中
	public boolean areAllItemsSelectable() { return false; }
	//判断指定文件是否被选中
	public boolean isSelectable(int position) 
	{ 
		return mItems.get(position).isSelectable();
	}
	//得到一个文件的ID
	public long getItemId(int position) { return position; }
	//重写getView方法来返回一个FilesTextView（我们自定义的文件布局）对象
	public View getView(int position, View convertView, ViewGroup parent)
	{
		LayoutInflater inf = mActivity.getLayoutInflater().from(mActivity);
		View nview = inf.inflate(R.layout.file_layout,null);
		ImageView img = (ImageView) nview.findViewById(R.id.file_imgLayout);
		TextView ntxt = (TextView) nview.findViewById(R.id.file_txtNameLayout);
		TextView dtxt = (TextView) nview.findViewById(R.id.file_txtDetailLayout);
		img.setImageDrawable(mItems.get(position).getIcon());
		ntxt.setText(mItems.get(position).getText());
		dtxt.setText(mItems.get(position).getDetail());
		/*FilesTextView btv;
		if (convertView == null) 
		{
			btv = new FilesTextView(mContext, mItems.get(position));
		} 
		else 
		{
			btv = (FilesTextView) convertView;
			btv.setText(mItems.get(position).getText());
			btv.setIcon(mItems.get(position).getIcon());
		}*/
		return nview;
	}
}

