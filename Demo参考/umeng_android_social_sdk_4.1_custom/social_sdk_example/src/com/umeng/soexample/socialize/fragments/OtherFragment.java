package com.umeng.soexample.socialize.fragments;

import static com.umeng.soexample.socialize.SocializeConfigDemo.DESCRIPTOR;

import java.util.List;
import java.util.Map;
import java.util.Set;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.socialize.bean.Gender;
import com.umeng.socialize.bean.MultiStatus;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SnsAccount;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.bean.UMFriend;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.FetchFriendsListener;
import com.umeng.socialize.controller.listener.SocializeListeners.LoginListener;
import com.umeng.socialize.controller.listener.SocializeListeners.MulStatusListener;
import com.umeng.socialize.controller.listener.SocializeListeners.SocializeClientListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMDataListener;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.soexample.R;

@SuppressLint("ValidFragment")
public class OtherFragment extends Fragment {

	Context mContext;
	UMSsoHandler ssoHandler;

	public OtherFragment() {
	}

	// public OtherFragment(UMSsoHandler ssoHandler) {
	// this.ssoHandler = ssoHandler;
	// }

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mContext = activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = inflater.inflate(
				R.layout.umeng_example_socialize_othermod_example, container,
				false);
		final UMSocialService controller = UMServiceFactory
				.getUMSocialService(DESCRIPTOR);
		final TextView textInfo = (TextView) root.findViewById(R.id.textinfo);
		textInfo.setMovementMethod(new ScrollingMovementMethod());

		if (ssoHandler != null) {
			controller.getConfig().setSsoHandler(ssoHandler);
		}

		// QQ SSO handler
		// controller.getConfig().setQZoneSsoHandler(new
		// QZoneSsoHandler(getActivity()));

		root.findViewById(R.id.ucenter_info_interface).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
					}
				});

		root.findViewById(R.id.ucenter_button).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						controller.openUserCenter(mContext);
					}
				});

		root.findViewById(R.id.ucenter_native_login).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						controller.openShare(getActivity(), false);
					}
				});

		root.findViewById(R.id.ucenter_native_oauth).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						controller.showLoginDialog(mContext,
								new LoginListener() {
									@Override
									public void loginSuccessed(
											SHARE_MEDIA platform,
											boolean earlier) {
										Toast.makeText(getActivity(),
												platform.toString() + "登录成功", 1)
												.show();
									}

									@Override
									public void loginFailed(int errCode) {
										Toast.makeText(
												getActivity(),
												"登录失败, error_code = " + errCode,
												1).show();
									}
								});
					}
				});

		root.findViewById(R.id.friends_list).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						controller.getFriends(mContext,
								new FetchFriendsListener() {
									@Override
									public void onStart() {
									}

									@Override
									public void onComplete(int status,
											List<UMFriend> friends) {
										StringBuilder sb = new StringBuilder();
										if (status == 200 && friends != null) {
											for (UMFriend friend : friends) {
												sb.append(friend.getName()
														+ "\r\n");
											}
										} else {
											sb.append("status_code=" + status);
										}
										textInfo.setText(sb.toString());
									}
								}, SHARE_MEDIA.SINA);
					}
				});

		/* 查询已授权的平台信息 */
		root.findViewById(R.id.platform_info).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						controller.getPlatformInfo(mContext, SHARE_MEDIA.SINA,
								new UMDataListener() {
									@Override
									public void onStart() {

									}

									@Override
									public void onComplete(int status,
											Map<String, Object> info) {
										Toast.makeText(mContext,
												"mcode:" + status, 1).show();
										if (status == 200 && info != null) {
											StringBuilder sb = new StringBuilder();
											Set<String> keys = info.keySet();
											for (String kStr : keys) {
												sb.append(kStr
														+ "="
														+ info.get(kStr)
																.toString()
														+ "\r\n");
											}
											textInfo.setText(sb.toString());
											android.util.Log.d("TestData",
													sb.toString());
										} else
											textInfo.setText("发生错误：" + status);
									}
								});
					}
				});
		/* 添加好友 */
		root.findViewById(R.id.follow).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						controller.follow(mContext, SHARE_MEDIA.TENCENT,
								new MulStatusListener() {

									@Override
									public void onStart() {

									}

									@Override
									public void onComplete(
											MultiStatus multiStatus, int st,
											SocializeEntity entity) {
										if (st == 200) {
											StringBuilder sb = new StringBuilder();
											Map<String, Integer> allStatus = multiStatus
													.getAllChildren();
											Set<String> keys = allStatus
													.keySet();
											for (String kStr : keys) {
												sb.append(kStr
														+ "="
														+ allStatus.get(kStr)
																.toString()
														+ "\r\n");
											}
											textInfo.setText(sb.toString());
										} else
											textInfo.setText("发生错误：" + st);
									}

								}, "jhenxu", "UmengShare");
					}
				});

		/* 更新用户信息 */
		root.findViewById(R.id.update_user).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						SnsAccount snsAccount = new SnsAccount("近似神的人",
								Gender.MALE,
								"http://www.fosss.org/Images/ShiJia.jpg",
								"123456789");
						SocializeClientListener listener = new SocializeClientListener() {
							@Override
							public void onStart() {

							}

							@Override
							public void onComplete(int status,
									SocializeEntity entity) {
								if (status == 200) {
									textInfo.setText("更新用户成功");
								} else
									textInfo.setText("发生错误：" + status);
							}
						};
						controller.login(mContext, snsAccount, listener);

						// UMToken umToken = UMToken.buildToken(new
						// SNSPair(SHARE_MEDIA.SINA.toString(),
						// "2303004783"), "2.008kKrVC0idO6t0a8647ac8f86cjcC");
						// controller.uploadToken(mContext, umToken, new
						// SocializeClientListener() {
						// @Override
						// public void onStart() {
						//
						// }
						//
						// @Override
						// public void onComplete(int status, SocializeEntity
						// arg1) {
						// if (status == 200) {
						// textInfo.setText("更新成功");
						// } else
						// textInfo.setText("发生错误：" + status);
						// }
						// });

					}
				});

		root.findViewById(R.id.usercenter_logout).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						controller.loginout(mContext,
								new SocializeClientListener() {

									@Override
									public void onStart() {

									}

									@Override
									public void onComplete(int status,
											SocializeEntity entity) {
										if (status == 200) {
											Toast.makeText(mContext, "注销成功",
													Toast.LENGTH_SHORT).show();
										} else {
											Toast.makeText(mContext, "注销失败",
													Toast.LENGTH_SHORT).show();
										}
									}
								});
					}
				});

		return root;
	}
}
