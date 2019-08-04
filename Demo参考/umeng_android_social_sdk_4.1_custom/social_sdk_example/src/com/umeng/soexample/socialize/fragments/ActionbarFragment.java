package com.umeng.soexample.socialize.fragments;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SocializeClientListener;
import com.umeng.socialize.media.UMImage;
import com.umeng.soexample.R;
import com.umeng.soexample.socialize.dashboard.ActionBarExampleDetail;
import com.umeng.soexample.socialize.dashboard.MockDataHelper;
import com.umeng.soexample.socialize.dashboard.MockDataHelper.TV;

public class ActionbarFragment extends Fragment {
	Context mContext;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mContext = activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = inflater.inflate(
				R.layout.umeng_example_socialize_actionbar_hash, container,
				false);
		List<TV> tVs = MockDataHelper.getTVs(mContext);
		ListView listView = (ListView) root.findViewById(R.id.list);

		BaseAdapter adapter = getAdapter(mContext, tVs);

		listView.setAdapter(adapter);
		return root;
	}

	public BaseAdapter getAdapter(final Context context, List<TV> tVs) {
		final List<TV> tvData = tVs;
		float scale = context.getResources().getDisplayMetrics().density;
		final int height = (int) (460 * scale + 0.5f);
		BaseAdapter baseAdapter = new BaseAdapter() {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View view = View.inflate(context,
						R.layout.umeng_example_socialize_actionbar_hash_item,
						null);
				AbsListView.LayoutParams layoutParams = new android.widget.AbsListView.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				view.setLayoutParams(layoutParams);

				final TV tv = tvData.get(position);

				final Button likeBt = (Button) view.findViewById(R.id.likeBt);
				final Button shareBt = (Button) view.findViewById(R.id.shareBt);
				final Button commentBt = (Button) view
						.findViewById(R.id.commentBt);

				final ButtonPair buttonPair = new ButtonPair(likeBt, shareBt,
						commentBt);

				final UMSocialService controller = initAcitonBar(context,
						buttonPair, tv);

				likeBt.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						controller.likeChange(context,
								new SocializeClientListener() {
									@Override
									public void onStart() {
									}

									@Override
									public void onComplete(int status,
											SocializeEntity entity) {
										if (entity != null) {
											// LIKESTATUS likestatus =
											// entity.getLikeStatus();
										}
									}
								});
					}
				});

				shareBt.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						controller.openShare(getActivity(), false);
					}
				});

				commentBt.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						controller.openComment(context, false);
					}
				});

				TextView desTv = (TextView) view.findViewById(R.id.textview);
				desTv.setText(tv.des);
				final ImageView imv = (ImageView) view
						.findViewById(R.id.imageview);
				view.findViewById(R.id.content_area).setOnClickListener(
						new OnClickListener() {
							@Override
							public void onClick(View v) {
								Intent intent = new Intent(context,
										ActionBarExampleDetail.class);
								intent.putExtra("TV", tv);
								context.startActivity(intent);
							}
						});

				new MockDataHelper.BindIMGTAsk(context, tv) {
					@Override
					public void onComplate(Drawable drawable) {
						if (drawable != null) {
							imv.setImageDrawable(drawable);
							Bitmap bitmap = ((BitmapDrawable) drawable)
									.getBitmap();

							if (!controller.hasShareImage())
								controller.setShareMedia(new UMImage(context,
										bitmap));
						}
					}
				}.execute();

				// try {
				// String action =
				// UMBroadcastManager.getEntityChangeAction(context,
				// controller.getEntity().descriptor);
				// IntentFilter filter = new IntentFilter(action);
				// filter.setPriority(1000);
				// context.registerReceiver(new BroadcastReceiver() {
				// @Override
				// public void onReceive(Context context, Intent intent) {
				// likeBt.setText("喜欢：  " +
				// controller.getEntity().getLike_count());
				// commentBt.setText("评论:  " +
				// controller.getEntity().getComment_count());
				// shareBt.setText("分享:  " +
				// controller.getEntity().getShare_count());
				// }
				// }, filter);
				// Log.d("TestData", "register broadcast " + action);
				// }
				// catch (Exception e) {
				// Log.w("TestData", e);
				// }

				return view;
			}

			@Override
			public long getItemId(int position) {
				return 0;
			}

			@Override
			public Object getItem(int position) {
				return null;
			}

			@Override
			public int getCount() {
				return tvData == null ? 0 : tvData.size();
			}
		};
		return baseAdapter;
	}

	protected static UMSocialService initAcitonBar(Context context,
			ButtonPair buttonPair, TV tv) {
		final ButtonPair mPair = buttonPair;
		UMSocialService controller = UMServiceFactory
				.getUMSocialService(tv.name);

		if (!controller.getEntity().mInitialized) {
			controller.setShareContent(tv.des);
			controller.initEntity(context, new SocializeClientListener() {
				@Override
				public void onStart() {
				}

				@Override
				public void onComplete(int status, SocializeEntity entity) {
					matchEntity(mPair, entity);
				}

			});
		} else {
			matchEntity(mPair, controller.getEntity());
		}
		return controller;
	}

	private synchronized static void matchEntity(final ButtonPair mPair,
			SocializeEntity entity) {
		if (entity != null) {
			mPair.likeBt.setText("喜欢：  " + entity.getLikeCount());
			mPair.commentBt.setText("评论:  " + entity.getCommentCount());
			mPair.shareBt.setText("分享:  " + entity.getShareCount());
		}
	}

	private static class ButtonPair {
		Button likeBt, shareBt, commentBt;

		public ButtonPair(Button likeBt, Button shareBt, Button commentBt) {
			super();
			this.likeBt = likeBt;
			this.shareBt = shareBt;
			this.commentBt = commentBt;
		}

	}
}
