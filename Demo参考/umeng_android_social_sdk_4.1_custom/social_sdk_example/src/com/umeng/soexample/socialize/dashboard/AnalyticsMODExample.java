package com.umeng.soexample.socialize.dashboard;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.umeng.socialize.bean.MultiStatus;
import com.umeng.socialize.bean.RequestType;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.bean.UMComment;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.MulStatusListener;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.controller.listener.SocializeListeners.SocializeClientListener;
import com.umeng.soexample.R;
import com.umeng.ui.BaseSinglePaneActivity;



public class AnalyticsMODExample extends BaseSinglePaneActivity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected Fragment onCreatePane() {
		return new ContentFragment();
	}

	private StringBuilder sb;
	private TextView bCountTv, eCountTv, requestInfoTv, taskInfoTv;
	private EditText desEt, repeatEt;

	/**
	 * 
	 * @author Jhen
	 * 
	 */
	@SuppressLint("ValidFragment")
	public class ContentFragment extends Fragment {
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View root = inflater.inflate(R.layout.umeng_example_socialize_analyticsmod_example,
					container, false);
			taskInfoTv = (TextView) root.findViewById(R.id.textinfo);
			requestInfoTv = (TextView) root.findViewById(R.id.reqeust_info);
			bCountTv = (TextView) root.findViewById(R.id.counting_before);
			eCountTv = (TextView) root.findViewById(R.id.counting_after);

			desEt = (EditText) root.findViewById(R.id.des_edit);
			repeatEt = (EditText) root.findViewById(R.id.repeat_count);

			sb = new StringBuilder();

			final Handler handler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);
					taskInfoTv.setText(sb.toString());
				}
			};

			integration(root, handler);

			root.findViewById(R.id.reflush_entity).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					String descript = desEt.getText().toString();
					final UMSocialService controller = UMServiceFactory.getUMSocialService(
							descript, RequestType.ANALYTICS);
					SocializeEntity entity = controller.getEntity();
					String bStr = buildCountingInfo(entity);
					bCountTv.setText(bStr);

					controller.initEntity(getActivity(), new SocializeClientListener() {
						@Override
						public void onStart() {
							eCountTv.setText("正在刷新数据中.....");
						}

						@Override
						public void onComplete(int status, SocializeEntity entity) {
							eCountTv.setText(buildCountingInfo(controller.getEntity()));
						}
					});

				}

				private String buildCountingInfo(SocializeEntity entity) {
					StringBuilder sb = new StringBuilder();
					sb.append("分享数量：" + entity.getShareCount() + "\r\n");
					sb.append("评论数量：" + entity.getCommentCount() + "\r\n");
					sb.append("喜欢数量：" + entity.getLikeCount() + "\r\n");
					return sb.toString();
				}
			});

			return root;
		}

		private void integration(View root, final Handler handler) {
			root.findViewById(R.id.analytics_comment).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					int count = Integer.parseInt(repeatEt.getText().toString());
					String descript = desEt.getText().toString();
					UMSocialService controller = UMServiceFactory.getUMSocialService(descript,
							RequestType.ANALYTICS);
					final int[] sended = { 1 };
					for (int i = 0; i < count; i++) {
					    UMComment socializeComment = new UMComment();
						//设置文本内容
						socializeComment.mText = "Analytics Comment test.";
						/**
						 * 评论
						 * 
						 * @param context
						 *            （不可null）
						 * @param comment
						 *            评论内容封装对象 （不可null）
						 * @param platforms
						 *            评论并分享的平台信息（可多选，可null）
						 * @param imageBytes
						 *            评论并分享的内容图片 （可null）
						 * @param listener
						 *            异步回调（可null）
						 */
						controller.postComment(getActivity(),socializeComment,
								new MulStatusListener() {
									@Override
									public void onStart() {

									}

									@Override
									public void onComplete(MultiStatus multiStatus,int status, SocializeEntity entity) {
										synchronized (sb) {
											String string = sb.toString();
											sb = new StringBuilder();
											sb.append("已发送第" + (sended[0]++)
													+ "条    status_code = " + status + "\r\n"
													+ string);
										}
										handler.sendEmptyMessage(0);
									}
								});
					}
				}
			});

			root.findViewById(R.id.analytics_share).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					int count = Integer.parseInt(repeatEt.getText().toString());
					String descript = desEt.getText().toString();
					/*
					 * descript 参数是对controller 的描述，保证唯一性。 RequestType.ANALYTICS
					 * 参数用于标记controller对象用于统计
					 */
					UMSocialService controller = UMServiceFactory.getUMSocialService(descript,
							RequestType.ANALYTICS);
					final int[] sended = { 1 };
					for (int i = 0; i < count; i++) {
//						UMShareMsg shareMsg = new UMShareMsg();
//						shareMsg.mText = "Analytics Share test.";
						/**
						 * 统计分享分享内容
						 * 
						 * @param context
						 *            (不可null)
						 * @param content
						 *            分享的文字内容(可null)
						 * @param usid
						 *            分享平台ID(可null)
						 * @param imageBytes
						 *            分享的图片内容 (可null)
						 * @param platform
						 *            分享的平台 (不可null)
						 * @param ULocation
						 *           分享的地理位置（可null）
						 * @param listener
						 *            分享操作异步回调 (可null)
						 * 
						 * 
						 */
						controller.postShare(getActivity(),"usid",
								SHARE_MEDIA.SINA,new SnsPostListener() {
									@Override
									public void onStart() {
									}

									@Override
									public void onComplete(SHARE_MEDIA platform, int statusCode,
											SocializeEntity entity) {
										//statusCode = 200 表示分享成功
										synchronized (sb) {
											String string = sb.toString();
											sb = new StringBuilder();
											sb.append("已发送第" + (sended[0]++)
													+ "条    status_code = " + statusCode + "\r\n"
													+ string);
										}
										handler.sendEmptyMessage(0);
									}

								});
					}
				}
			});

			root.findViewById(R.id.analytics_share_wid).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					int count = Integer.parseInt(repeatEt.getText().toString());
					String descript = desEt.getText().toString();
					UMSocialService controller = UMServiceFactory.getUMSocialService(descript,
							RequestType.ANALYTICS);
					final int[] sended = { 1 };
					for (int i = 0; i < count; i++) {

						/**
						 * 根据weiboid 统计分享内容
						 * 
						 * @param context
						 *            (不可null)
						 * @param wid
						 *            weiboId（不可null）
						 * @param platform
						 *            分享平台(不可null)
						 * @param listener
						 *            (可null)
						 */
						controller.postShareByID(getActivity(), "1234567890","usid",SHARE_MEDIA.SINA,
								new SnsPostListener() {
									@Override
									public void onStart() {
									}

									@Override
									public void onComplete(SHARE_MEDIA platform, int eCode,
											SocializeEntity entity) {
										synchronized (sb) {
											String string = sb.toString();
											sb = new StringBuilder();
											sb.append("已发送第" + (sended[0]++)
													+ "条    status_code = " + eCode + "\r\n"
													+ string);
										}
										handler.sendEmptyMessage(0);
									}
								});
					}
				}
			});
			root.findViewById(R.id.analytics_like).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					int count = Integer.parseInt(repeatEt.getText().toString());
					String descript = desEt.getText().toString();
					UMSocialService controller = UMServiceFactory.getUMSocialService(descript,
							RequestType.ANALYTICS);
					final int[] sended = { 1 };
					for (int i = 0; i < count; i++) {

						/**
						 * 喜欢
						 * 
						 * @param context
						 *            (不可null)
						 * @param listener
						 *            异步回调(可null)
						 */
						controller.postLike(getActivity(), new SocializeClientListener() {
							@Override
							public void onStart() {

							}

							@Override
							public void onComplete(int status, SocializeEntity entity) {
								synchronized (sb) {
									String string = sb.toString();
									sb = new StringBuilder();
									sb.append("已发送第" + (sended[0]++) + "条    status_code = "
											+ status + "\r\n" + string);
								}
								handler.sendEmptyMessage(0);
							}
						});
					}
				}
			});
			root.findViewById(R.id.analytics_unlike).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					int count = Integer.parseInt(repeatEt.getText().toString());
					String descript = desEt.getText().toString();
					UMSocialService controller = UMServiceFactory.getUMSocialService(descript,
							RequestType.ANALYTICS);
					final int[] sended = { 1 };
					for (int i = 0; i < count; i++) {
						
						/**
						 * 喜欢
						 * 
						 * @param context
						 *            (不可null)
						 * @param listener
						 *            异步回调(可null)
						 */
						controller.postUnLike(getActivity(), new SocializeClientListener() {
							@Override
							public void onStart() {
								
							}
							
							@Override
							public void onComplete(int status, SocializeEntity entity) {
								synchronized (sb) {
									String string = sb.toString();
									sb = new StringBuilder();
									sb.append("已发送第" + (sended[0]++) + "条    status_code = "
											+ status + "\r\n" + string);
								}
								handler.sendEmptyMessage(0);
							}
						});
					}
				}
			});
		}
	}
}
