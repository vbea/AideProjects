package com.umeng.soexample.socialize;

import static com.umeng.soexample.socialize.SocializeConfigDemo.DESCRIPTOR;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.Toast;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.view.ActionBarView;
import com.umeng.soexample.BitmapUtils;
import com.umeng.soexample.R;
import com.umeng.ui.BaseSinglePaneActivity;

public class SocializeHomeSimple extends BaseSinglePaneActivity {
	@Override
	protected Fragment onCreatePane() {
		return new BannerExampleFragment();
	}

	public static class BannerExampleFragment extends Fragment {
		Context mContext;

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			mContext = activity;
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			// =============Mock Data========================================

			BitmapDrawable bf = (BitmapDrawable) mContext.getResources()
					.getDrawable(R.drawable.device);
			Bitmap bitmap = bf.getBitmap();
			byte[] bytes = BitmapUtils.bitmap2Bytes(bitmap);
			View root = inflater.inflate(
					R.layout.umeng_example_socialize_simple, container, false);
			// =============Mock Data========================================

			// ============集成ActionBar=======================================
			// 用于集成ActionBar 的ViewGroup
			ViewGroup parent = (ViewGroup) root.findViewById(R.id.root);
			// 创建ActionBar des参数是ActionBar的唯一标识，请确保不为空
			ActionBarView socializeActionBar = new ActionBarView(mContext,
					DESCRIPTOR);

			LayoutParams layoutParams = new ViewGroup.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
			socializeActionBar.setLayoutParams(layoutParams);
			// 添加ActionBar
			parent.addView(socializeActionBar);
			// ============集成ActionBar=======================================

			// ============配置ActionBar（可选）=======================================
			// des 参数对应actionbar
			UMSocialService controller = UMServiceFactory
					.getUMSocialService(DESCRIPTOR);
			// 设置分享文字
			controller.setShareContent("   --- Tom & Jerry");

			// 设置分享图片(支持4种方式),一个ActionBar最多只能选择一种
			/*
			 * //Resource Id controller.setShareImage(new ShareImage(mContext,
			 * R.drawable.testimg)); //File controller.setShareImage(new
			 * SignatureImage(new File("mnt/sdcard/2mb.jpg"))); //Bitmap
			 * controller.setShareImage(new ShareImage(mContext, bitmap));
			 */
			// Raw
			controller.setShareMedia(new UMImage(mContext, bytes));

			// 配置分享平台，默认全部
			// SocializeConfig socializeConfig =
			// SocialDemoConfig.getSocialConfig();
			// socializeConfig.setPlatforms(
			// SHARE_MEDIA.TENCENT,
			// SHARE_MEDIA.SINA,
			// SHARE_MEDIA.RENREN,
			// SHARE_MEDIA.DOUBAN
			// );//设置分享平台
			// controller.setConfig(socializeConfig);//作用于单个des

			// 配置全局Congfig
			// SocializeController.setGlobalConfig(socializeConfig)
			// ============配置ActionBar（可选）=======================================

			// ============================单独功能调用============================================
			final EditText desEd = (EditText) root.findViewById(R.id.des_edit);
			root.findViewById(R.id.commment_bt).setOnClickListener(
					new OnClickListener() {
						@Override
						public void onClick(View v) {
							String string = desEd.getText().toString();
							if (TextUtils.isEmpty(string)) {
								Toast.makeText(mContext, "内容为空。", 1).show();
								return;
							}
							UMServiceFactory.getUMSocialService(string)
									.openComment(mContext, false);
						}
					});

			root.findViewById(R.id.share_bt).setOnClickListener(
					new OnClickListener() {
						@Override
						public void onClick(View v) {
							String string = desEd.getText().toString();
							if (TextUtils.isEmpty(string)) {
								Toast.makeText(mContext, "内容为空。", 1).show();
								return;
							}
							UMServiceFactory.getUMSocialService(string).openShare(
									getActivity(), false);
						}
					});

			root.findViewById(R.id.user_center_bt).setOnClickListener(
					new OnClickListener() {
						@Override
						public void onClick(View v) {
							String string = desEd.getText().toString();
							if (TextUtils.isEmpty(string)) {
								Toast.makeText(mContext, "内容为空。", 1).show();
								return;
							}
							UMServiceFactory.getUMSocialService(string)
									.openUserCenter(mContext);
						}
					});

			// Bitmap bitmap2 = ((BitmapDrawable) mContext.getResources()
			// .getDrawable(R.drawable.meiyu)).getBitmap();
			final UMImage shareImage = new UMImage(mContext, R.drawable.meiyu);
			// final DirectShareListener directShareListener = new
			// DirectShareListener() {
			//
			// @Override
			// public void onOauthComplete(String usid, SHARE_MEDIA platform) {
			// // if (!TextUtils.isEmpty(usid)) {
			// // // 授权成功（可打开分享页）
			// // Toast.makeText(mContext, "授权成功【usid:" + usid +
			// "】",Toast.LENGTH_SHORT).show();
			// // } else {
			// // // 授权失败（不可打开分享页）
			// // Toast.makeText(mContext, "授权失败,请重试！",
			// Toast.LENGTH_LONG).show();
			// // }
			// }
			//
			// @Override
			// public void onAuthenticated(SHARE_MEDIA platform) {
			// // Toast.makeText(mContext, "已授权，直接打开。",
			// Toast.LENGTH_SHORT).show();
			// }
			// };
			final SnsPostListener snsPostListener = new SnsPostListener() {

				@Override
				public void onStart() {
					Toast.makeText(getActivity(), "分享开始", Toast.LENGTH_SHORT)
							.show();
				}

				@Override
				public void onComplete(SHARE_MEDIA platform, int eCode,
						SocializeEntity entity) {
					Toast.makeText(getActivity(), "分享结束", Toast.LENGTH_SHORT)
							.show();
				}
			};
			root.findViewById(R.id.direct_share_bt).setOnClickListener(
					new OnClickListener() {
						@Override
						public void onClick(View v) {
							String string = desEd.getText().toString();
							if (TextUtils.isEmpty(string)) {
								Toast.makeText(mContext, "内容为空。", 1).show();
								return;
							}
							UMServiceFactory.getUMSocialService(string).setShareMedia(
									shareImage);
							UMServiceFactory.getUMSocialService(string).directShare(mContext,
									SHARE_MEDIA.TENCENT, snsPostListener);
						}
					});

			root.findViewById(R.id.dtest).setOnClickListener(
					new OnClickListener() {
						@Override
						public void onClick(View v) {
							// new UCenterDialog(mContext, "Android").show();
						}
					});

			return root;
		}
	}

}
