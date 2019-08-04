package com.umeng.soexample.socialize.fragments;

import static com.umeng.soexample.socialize.SocializeConfigDemo.DESCRIPTOR;

import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.umeng.socialize.bean.MultiStatus;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeConfig;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.MulStatusListener;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.controller.listener.SocializeListeners.SocializeClientListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMAuthListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMDataListener;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.media.TencentWbShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMVideo;
import com.umeng.socialize.media.UMusic;
import com.umeng.socialize.sso.RenrenSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.TencentWBSsoHandler;
import com.umeng.socialize.utils.OauthHelper;
import com.umeng.soexample.R;

/**
 * @功能描述 : 分享页面Fragment,包括分享、图文分享、快速分享等
 * 
 * @原 作 者 :
 * @版 本 号 : [版本号, Aug 8, 2013]
 * 
 * @修 改 人 : mrsimple
 * @修改内容 :
 */
/**
 *
 */
public class ShareFragment extends Fragment implements OnClickListener {

	private Context mContext = null;
	// sdk controller
	private UMSocialService mController = null;
	// 布局view
	private View mMainView = null;

	// 要分享的文字内容
	private String mShareContent = "";
	private final SHARE_MEDIA mTestMedia = SHARE_MEDIA.SINA;
	// 要分享的图片
	private UMImage mUMImgBitmap = null;

	// 分享(先选择平台)
	private Button mShareBtn = null;
	// 图文分享（呼出编辑页）
	private Button mDirectShareBtn = null;
	// 快速分享接口（呼出编辑页）
	private Button mQuickShareBtn = null;
	// 图文分享（功能底层接口）
	private Button mPicShareBtn = null;
	// 分享多个已授权平台(功能底层接口)
	private Button mMultiPlatformShareBtn = null;
	// 授权（功能底层接口）
	private Button mOauthBtn = null;
	// 解除（功能底层接口）
	private Button mDeleteOauthBtn = null;

	private final String TAG = "TestData";

	/**
	 * @功能描述 : create View for the fragment
	 * @param inflater
	 * @param container
	 * @param savedInstanceState
	 * @return
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		mMainView = inflater.inflate(
				R.layout.umeng_example_socialize_sharemod_example, container,
				false);

		// 初始化与SDK相关的成员变量
		initConfig();
		initViews();

		com.umeng.socialize.utils.Log.LOG = true;
		mMainView.setTag("分享接口");
		return mMainView;
	}

	/**
	 * @功能描述 : 初始化与SDK相关的成员变量
	 */
	private void initConfig() {

		mContext = getActivity();

		mController = UMServiceFactory.getUMSocialService(DESCRIPTOR);
		mController.getConfig().enableSIMCheck(false);

		// 要分享的文字内容
		mShareContent = getResources().getString(
				R.string.umeng_socialize_share_content);
		mController
				.setShareContent("友盟社会化组件还不错，让移动应用快速整合社交分享功能。www.umeng.com/social");

		mUMImgBitmap = new UMImage(getActivity(),
				"http://www.umeng.com/images/pic/banner_module_social.png");

		UMusic uMusic = new UMusic("http://sns.whalecloud.com/test_music.mp3");
		uMusic.setAuthor("zhangliyong");
		uMusic.setTitle("天籁之音");

		UMVideo umVedio = new UMVideo(
				"http://v.youku.com/v_show/id_XNTE5ODAwMDM2.html?f=19001023");
		umVedio.setThumb("http://historyhots.com/uploadfile/2013/0110/20130110064307373.jpg");
		umVedio.setTitle("哇喔喔喔！");

		// 添加新浪和QQ空间的SSO授权支持
		mController.getConfig().setSsoHandler(new SinaSsoHandler());
		// 添加腾讯微博SSO支持
		mController.getConfig().setSsoHandler(new TencentWBSsoHandler());

		/************************************* 设置授权完成后马上添加一个好友 **************************/
		final SocializeConfig config = mController.getConfig();

		config.addFollow(mTestMedia, "5189710768");
		config.setOauthDialogFollowListener(new MulStatusListener() {
			@Override
			public void onStart() {
				Log.d(TAG, "Follow Start");
			}

			@Override
			public void onComplete(MultiStatus multiStatus, int st,
					SocializeEntity entity) {
				if (st == 200) {
					Map<String, Integer> allChildren = multiStatus
							.getAllChildren();
					Set<String> set = allChildren.keySet();
					for (String fid : set)
						Log.d(TAG, fid + "    " + allChildren.get(fid));
				}
			}
		});
	}

	/**
	 * @功能描述 : 初始化视图控件，比如Button
	 */
	private void initViews() {

		// 分享(先选择平台)
		mShareBtn = (Button) mMainView.findViewById(R.id.share_bt);
		mShareBtn.setOnClickListener(this);

		// 图文分享（呼出编辑页）
		mDirectShareBtn = (Button) mMainView.findViewById(R.id.direct_share_bt);
		mDirectShareBtn.setOnClickListener(this);

		// 快速分享接口（呼出编辑页）
		mQuickShareBtn = (Button) mMainView.findViewById(R.id.quick_share);
		mQuickShareBtn.setOnClickListener(this);

		// 图文分享（功能底层接口）
		mPicShareBtn = (Button) mMainView.findViewById(R.id.interface_share_bt);
		mPicShareBtn.setOnClickListener(this);

		// 授权（功能底层接口）
		mOauthBtn = (Button) mMainView.findViewById(R.id.interface_oauth);
		mOauthBtn.setOnClickListener(this);

		// 解除（功能底层接口）
		mDeleteOauthBtn = (Button) mMainView
				.findViewById(R.id.interface_deleteoauth);
		mDeleteOauthBtn.setOnClickListener(this);

		// 分享多个已授权平台(功能底层接口)
		mMultiPlatformShareBtn = (Button) mMainView
				.findViewById(R.id.share_multi_bt);
		mMultiPlatformShareBtn.setOnClickListener(this);

		mMainView.findViewById(R.id.sort).setOnClickListener(this);
		mMainView.findViewById(R.id.remove_platform).setOnClickListener(this);
		mMainView.findViewById(R.id.close_sso).setOnClickListener(this);
		mMainView.findViewById(R.id.open_sso).setOnClickListener(this);
		mMainView.findViewById(R.id.check_token).setOnClickListener(this);

		// 授权加分享
		mMainView.findViewById(R.id.oauth_directshare).setOnClickListener(this);

	}

	/**
	 * @功能描述 : 点击事件
	 * @param v
	 */
	@Override
	public void onClick(View v) {
		if (v == mShareBtn) {
			openShareBoard();
		} else if (v == mDirectShareBtn) {
			directShare();
		} else if (v == mQuickShareBtn) {
			quickShare();
		} else if (v == mPicShareBtn) {
			textAndPicShare();
		} else if (v == mOauthBtn) {
			doOauth();
		} else if (v == mMultiPlatformShareBtn) {
			shareToMultiPlatform();
		} else if (v == mDeleteOauthBtn) {
			deleteOauth();
		} else if (v.getId() == R.id.sort) {
			sortPlatform();
		} else if (v.getId() == R.id.remove_platform) {
			removePlatform();
		} else if (v.getId() == R.id.close_sso) {
			closeSSO();
		} else if (v.getId() == R.id.open_sso) {
			openSSO();
		} else if (v.getId() == R.id.check_token) {
			checkToken();
		} else if (v.getId() == R.id.oauth_directshare) {
			shareAfterOauth();
		}

	}

	/**
	 * 授权后直接分享
	 */
	private void shareAfterOauth() {

		// 授权
		mController.doOauthVerify(getActivity(), SHARE_MEDIA.TENCENT,
				new UMAuthListener() {

					@Override
					public void onStart(SHARE_MEDIA platform) {

					}

					@Override
					public void onError(SocializeException e,
							SHARE_MEDIA platform) {
						Toast.makeText(getActivity(), "授权错误",
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onComplete(Bundle value, SHARE_MEDIA platform) {
						directShare();
					}

					@Override
					public void onCancel(SHARE_MEDIA platform) {
						Toast.makeText(getActivity(), "授权取消",
								Toast.LENGTH_SHORT).show();
					}
				});
	}

	private void checkToken() {
		SHARE_MEDIA[] platforms = new SHARE_MEDIA[] { SHARE_MEDIA.SINA,
				SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.DOUBAN,
				SHARE_MEDIA.RENREN, SHARE_MEDIA.TENCENT };
		mController.checkTokenExpired(getActivity(), platforms,
				new UMDataListener() {

					@Override
					public void onStart() {

					}

					@Override
					public void onComplete(int status, Map<String, Object> info) {
						if (info != null) {
							Set<String> keys = info.keySet();
							StringBuilder builder = new StringBuilder();
							for (String key : keys) {
								builder.append(key).append("=")
										.append(info.get(key)).append(",");
							}
							if (builder.length() > 0) {
								builder.deleteCharAt(builder.length() - 1);
							}
							Toast.makeText(getActivity(), builder.toString(),
									Toast.LENGTH_SHORT).show();
						}
					}
				});
	}

	/**
	 * 打开新浪和腾讯微薄的SSO授权
	 */
	private void openSSO() {
		mController.getConfig().setSsoHandler(new SinaSsoHandler());
		mController.getConfig().setSsoHandler(new TencentWBSsoHandler());
		mController.getConfig().setSsoHandler(
				new RenrenSsoHandler(getActivity(), "201874",
						"28401c0964f04a72a14c812d6132fcef",
						"3bf66e42db1e4fa9829b955cc300b737"));
	}

	/**
	 * 关闭sina微博SSO，QQ zone SSO，腾讯微博SSO，
	 */
	private void closeSSO() {
		mController.getConfig().removeSsoHandler(SHARE_MEDIA.SINA);
		mController.getConfig().removeSsoHandler(SHARE_MEDIA.TENCENT);
		mController.getConfig().removeSsoHandler(SHARE_MEDIA.RENREN);
	}

	/**
	 * @功能描述 :
	 */
	public void removePlatform() {
		mController.getConfig().removePlatform(SHARE_MEDIA.RENREN,
				SHARE_MEDIA.DOUBAN);
		mController.openShare(getActivity(), false);
	}

	/**
	 * 根据开发者设置的顺序对平台排序
	 */
	private void sortPlatform() {
		mController.getConfig().setPlatformOrder(SHARE_MEDIA.WEIXIN_CIRCLE,
				SHARE_MEDIA.RENREN, SHARE_MEDIA.SINA, SHARE_MEDIA.QQ,
				SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.GOOGLEPLUS,
				SHARE_MEDIA.EMAIL, SHARE_MEDIA.TENCENT, SHARE_MEDIA.GENERIC);
		mController.openShare(getActivity(), false);
	}

	/**
	 * @功能描述 : 分享(先选择平台)
	 */
	private void openShareBoard() {
		mController
				.setShareContent("友盟社会化组件还不错，让移动应用快速整合社交分享功能。http://www.umeng.com/social");
		mController.setShareMedia(mUMImgBitmap);
		mController.openShare(getActivity(), false);
	}

	/**
	 * @功能描述 : 图文分享（呼出编辑页）
	 */
	private void directShare() {

		// mController.setShareMedia(new TencentWbShareMedia(new
		// UMImage(mContext,
		// BitmapFactory.decodeResource(getResources(),
		// R.drawable.actionbar_compat_logo))));

		// 视频分享
		UMVideo umVedio = new UMVideo(
				"http://v.youku.com/v_show/id_XNTc0ODM4OTM2.html");
		umVedio.setThumb("http://www.umeng.com/images/pic/banner_module_social.png");
		umVedio.setTitle("友盟社会化组件视频");
		TencentWbShareContent tencentWbShareContent = new TencentWbShareContent(
				umVedio);
		tencentWbShareContent
				.setShareContent("腾讯微博分享内容 Direct --来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能");
		// 设置tencent分享内容
		mController.setShareMedia(tencentWbShareContent);

		mController.directShare(getActivity(), SHARE_MEDIA.TENCENT,
				new SnsPostListener() {

					@Override
					public void onStart() {

					}

					@Override
					public void onComplete(SHARE_MEDIA platform, int eCode,
							SocializeEntity entity) {
						Toast.makeText(getActivity(), "分享完成",
								Toast.LENGTH_SHORT).show();
					}
				});

		// mController.getConfig().supportQQPlatform(getActivity(),"http://www.umeng.com/social");
		// UMImage mUMImgBitmap = new UMImage(getActivity(),
		// "http://www.umeng.com/images/pic/banner_module_social.png");
		// // 视频分享
		// UMVedio umVedio = new UMVedio(
		// "http://v.youku.com/v_show/id_XNTc0ODM4OTM2.html");
		// umVedio.setThumb("http://www.umeng.com/images/pic/banner_module_social.png");
		// umVedio.setTitle("哇喔喔喔！");
		// // 要分享的文字内容
		// mController.setShareContent("友盟社会化组件还不错，让移动应用快速整合社交分享功能。www.umeng.com/social");
		// // 设置多媒体内容
		// mController.setShareMedia( mUMImgBitmap );
		//
		// mController.directShare(mContext, SHARE_MEDIA.WEIXIN,
		// directShareListener);
	}

	/**
	 * @功能描述 : 快速分享接口（呼出编辑页）
	 */
	private void quickShare() {
		// 快速分享接口
		mController.shareTo(getActivity(), mShareContent, null);
	}

	/**
	 * @功能描述 : 图文分享（功能底层接口）
	 */
	private void textAndPicShare() {

		mController.postShare(mContext, mTestMedia, new SnsPostListener() {

			@Override
			public void onComplete(SHARE_MEDIA arg0, int arg1,
					SocializeEntity arg2) {
				Toast.makeText(mContext, "分享完成", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onStart() {
				Toast.makeText(mContext, "开始分享", Toast.LENGTH_SHORT).show();
			}
		});
	}

	/**
	 * @功能描述 : 授权（功能底层接口）
	 */
	private void doOauth() {
		if (OauthHelper.isAuthenticated(getActivity(), mTestMedia)) {
			Toast.makeText(mContext, "新浪平台已经授权.", Toast.LENGTH_SHORT).show();
		} else {
			mController.doOauthVerify(mContext, mTestMedia,
					new UMAuthListener() {
						@Override
						public void onError(SocializeException e,
								SHARE_MEDIA platform) {
						}

						@Override
						public void onComplete(Bundle value,
								SHARE_MEDIA platform) {
							mController.directShare(getActivity(),
									SHARE_MEDIA.QQ, null);
							// if (value != null
							// && !TextUtils.isEmpty(value
							// .getString("uid"))) {
							// Toast.makeText(mContext, "授权成功.",
							// Toast.LENGTH_SHORT).show();
							// } else {
							// Toast.makeText(mContext, "授权失败",
							// Toast.LENGTH_SHORT).show();
							// }
						}

						@Override
						public void onCancel(SHARE_MEDIA arg0) {
						}

						@Override
						public void onStart(SHARE_MEDIA arg0) {
						}

					});
		}
	}

	/**
	 * @功能描述 : 分享多个已授权平台(功能底层接口)
	 */
	private void shareToMultiPlatform() {
		// <<<<<<< HEAD
		// UMShareMsg shareMsg = new UMShareMsg();
		// shareMsg.mText = mShareContent;
		//
		// mController.postShareMulti(mContext, shareMsg, new
		// MulStatusListener() {
		// =======
		mController.postShareMulti(mContext, new MulStatusListener() {
			@Override
			public void onStart() {
				Toast.makeText(mContext, "开始分享.", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onComplete(MultiStatus snsSt, int st,
					SocializeEntity entity) {
				Toast.makeText(mContext, snsSt.toString(), Toast.LENGTH_LONG)
						.show();
			}
		}, SHARE_MEDIA.SINA, SHARE_MEDIA.RENREN, SHARE_MEDIA.DOUBAN,
				SHARE_MEDIA.TENCENT, SHARE_MEDIA.QZONE, SHARE_MEDIA.FACEBOOK);
	}

	/**
	 * @功能描述 : 解除（功能底层接口）
	 */
	private void deleteOauth() {
		mController.deleteOauth(mContext, mTestMedia,
				new SocializeClientListener() {
					@Override
					public void onStart() {
						Log.d(TAG,
								"sina="
										+ OauthHelper.isAuthenticated(
												getActivity(), mTestMedia));

					}

					@Override
					public void onComplete(int status, SocializeEntity entity) {
						Log.d(TAG,
								status
										+ "      sina="
										+ OauthHelper.isAuthenticated(
												getActivity(), mTestMedia));
					}
				});
	}

	/**
	 * @功能描述 :
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}
}
