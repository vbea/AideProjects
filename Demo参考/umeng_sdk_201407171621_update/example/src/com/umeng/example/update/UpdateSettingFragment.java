package com.umeng.example.update;

import com.umeng.example.R;
import com.umeng.example.util.UpdateExampleConfig;
import com.umeng.update.UmengDialogButtonListener;
import com.umeng.update.UmengDownloadListener;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class UpdateSettingFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.umeng_example_update_setting,
				container, false);
		UpdateExampleConfig.mContext = getActivity().getApplicationContext();

		final TextView setOnlyWifiText = (TextView) root
				.findViewById(R.id.wifi_only_text);
		final RadioGroup setOnlyWifiRadioGroup = (RadioGroup) root
				.findViewById(R.id.wifi_only_radiogroup);
		setOnlyWifiRadioGroup
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						switch (checkedId) {
						case R.id.btn_wifi_only_true:
							UmengUpdateAgent.setUpdateOnlyWifi(true);
							setOnlyWifiText
									.setText(R.string.wifi_only_text_true);
							UpdateExampleConfig.setUpdateOnlyWifi(true);
							break;
						case R.id.btn_wifi_only_false:
							UmengUpdateAgent.setUpdateOnlyWifi(false);
							setOnlyWifiText
									.setText(R.string.wifi_only_text_false);
							UpdateExampleConfig.setUpdateOnlyWifi(false);
							break;
						}
					}
				});
		setOnlyWifiRadioGroup
				.check(UpdateExampleConfig.isUpdateOnlyWifi() ? R.id.btn_wifi_only_true
						: R.id.btn_wifi_only_false);

		final TextView setDeltaText = (TextView) root
				.findViewById(R.id.delta_text);
		final RadioGroup setDeltaRadioGroup = (RadioGroup) root
				.findViewById(R.id.delta_radiogroup);
		setDeltaRadioGroup
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						switch (checkedId) {
						case R.id.btn_delta_true:
							UmengUpdateAgent.setDeltaUpdate(true);
							setDeltaText.setText(R.string.delta_text_true);
							UpdateExampleConfig.setDeltaUpdate(true);
							break;
						case R.id.btn_delta_false:
							UmengUpdateAgent.setDeltaUpdate(false);
							setDeltaText.setText(R.string.delta_text_false);
							UpdateExampleConfig.setDeltaUpdate(false);
							break;
						}
					}
				});
		setDeltaRadioGroup
				.check(UpdateExampleConfig.isDeltaUpdate() ? R.id.btn_delta_true
						: R.id.btn_delta_false);

		final TextView setAutoPopupText = (TextView) root
				.findViewById(R.id.auto_popup_text);
		final RadioGroup setAutoPopupRadioGroup = (RadioGroup) root
				.findViewById(R.id.auto_popup_radiogroup);
		setAutoPopupRadioGroup
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						switch (checkedId) {
						case R.id.btn_auto_popup_true:
							UmengUpdateAgent.setUpdateAutoPopup(true);
							setAutoPopupText
									.setText(R.string.auto_popup_text_true);
							UpdateExampleConfig.setUpdateAutoPopup(true);
							break;
						case R.id.btn_auto_popup_false:
							UmengUpdateAgent.setUpdateAutoPopup(false);
							setAutoPopupText
									.setText(R.string.auto_popup_text_false);
							UpdateExampleConfig.setUpdateAutoPopup(false);
							break;
						}
					}
				});
		setAutoPopupRadioGroup
				.check(UpdateExampleConfig.isUpdateAutoPopup() ? R.id.btn_auto_popup_true
						: R.id.btn_auto_popup_false);
		
		final TextView setRichNotificationText = (TextView) root
				.findViewById(R.id.rich_notification_text);
		final RadioGroup setRichNotificationRadioGroup = (RadioGroup) root
				.findViewById(R.id.rich_notification_radiogroup);
		setRichNotificationRadioGroup
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						switch (checkedId) {
						case R.id.btn_rich_notification_true:
							UmengUpdateAgent.setUpdateAutoPopup(true);
							setRichNotificationText
									.setText(R.string.rich_notification_text_true);
							UpdateExampleConfig.setRichNotification(true);;
							break;
						case R.id.btn_rich_notification_false:
							UmengUpdateAgent.setRichNotification(false);
							setRichNotificationText
									.setText(R.string.rich_notification_text_false);
							UpdateExampleConfig.setRichNotification(false);;
							break;
						}
					}
				});
		setRichNotificationRadioGroup
				.check(UpdateExampleConfig.isRichNotification() ? R.id.btn_rich_notification_true
						: R.id.btn_rich_notification_false);

		final TextView setStyleText = (TextView) root
				.findViewById(R.id.style_text);
		final RadioGroup setStyleRadioGroup = (RadioGroup) root
				.findViewById(R.id.style_radiogroup);
		setStyleRadioGroup
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						switch (checkedId) {
						case R.id.btn_style_dialog:
							UmengUpdateAgent
									.setUpdateUIStyle(UpdateStatus.STYLE_DIALOG);
							setStyleText.setText(R.string.style_text_dialog);
							UpdateExampleConfig.setDialogStyle(true);
							break;
						case R.id.btn_style_notification:
							UmengUpdateAgent
									.setUpdateUIStyle(UpdateStatus.STYLE_NOTIFICATION);
							setStyleText
									.setText(R.string.style_text_notification);
							UpdateExampleConfig.setDialogStyle(false);
							break;
						}
					}
				});
		setStyleRadioGroup
				.check(UpdateExampleConfig.isDialogStyle() ? R.id.btn_style_dialog
						: R.id.btn_style_notification);

		final TextView updateListenerText = (TextView) root
				.findViewById(R.id.update_listener_code);
		final CheckBox updateListenerCheckBox = (CheckBox) root
				.findViewById(R.id.update_listener_check);
		updateListenerCheckBox
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							UmengUpdateAgent
									.setUpdateListener(new UmengUpdateListener() {

										@Override
										public void onUpdateReturned(
												int updateStatus,
												UpdateResponse updateInfo) {
											switch (updateStatus) {
											case UpdateStatus.Yes: // has update
												Toast.makeText(
														UpdateExampleConfig.mContext,
														"发现更新",
														Toast.LENGTH_SHORT)
														.show();
												break;
											case UpdateStatus.No: // has no
																	// update
												Toast.makeText(
														UpdateExampleConfig.mContext,
														"没有更新",
														Toast.LENGTH_SHORT)
														.show();
												break;
											case UpdateStatus.NoneWifi: // none
																		// wifi
												Toast.makeText(
														UpdateExampleConfig.mContext,
														"没有wifi连接， 只在wifi下更新",
														Toast.LENGTH_SHORT)
														.show();
												break;
											case UpdateStatus.Timeout: // time
																		// out
												Toast.makeText(
														UpdateExampleConfig.mContext,
														"超时",
														Toast.LENGTH_SHORT)
														.show();
												break;
											}
										}

									});
							updateListenerText
									.setText(R.string.update_listener_not_null);
							UpdateExampleConfig.setUpdateListener(true);
						} else {
							UmengUpdateAgent.setUpdateListener(null);
							updateListenerText
									.setText(R.string.update_listener_null);
							UpdateExampleConfig.setUpdateListener(false);
						}
					}
				});
		updateListenerCheckBox.setChecked(UpdateExampleConfig
				.hasUpdateListener());

		final TextView dialogListenerText = (TextView) root
				.findViewById(R.id.dialog_listener_code);
		final CheckBox dialogListenerCheckBox = (CheckBox) root
				.findViewById(R.id.dialog_listener_check);
		dialogListenerCheckBox
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							UmengUpdateAgent
									.setDialogListener(new UmengDialogButtonListener() {

										@Override
										public void onClick(int status) {
											switch (status) {
											case UpdateStatus.Update:
												Toast.makeText(
														UpdateExampleConfig.mContext,
														"用户选择更新",
														Toast.LENGTH_SHORT)
														.show();
												break;
											case UpdateStatus.Ignore:
												Toast.makeText(
														UpdateExampleConfig.mContext,
														"用户选择忽略",
														Toast.LENGTH_SHORT)
														.show();
												break;
											case UpdateStatus.NotNow:
												Toast.makeText(
														UpdateExampleConfig.mContext,
														"用户选择取消",
														Toast.LENGTH_SHORT)
														.show();
												break;
											}
										}
									});
							dialogListenerText
									.setText(R.string.dialog_listener_not_null);
							UpdateExampleConfig.setDialogListener(true);
						} else {
							UmengUpdateAgent.setDialogListener(null);
							dialogListenerText
									.setText(R.string.dialog_listener_null);
							UpdateExampleConfig.setDialogListener(false);
						}
					}
				});
		dialogListenerCheckBox.setChecked(UpdateExampleConfig
				.hasDialogListener());

		final TextView downloadListenerText = (TextView) root
				.findViewById(R.id.download_listener_code);
		final CheckBox downloadListenerCheckBox = (CheckBox) root
				.findViewById(R.id.download_listener_check);
		downloadListenerCheckBox
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							UmengUpdateAgent
									.setDownloadListener(new UmengDownloadListener() {

										@Override
										public void OnDownloadStart() {
											Toast.makeText(
													UpdateExampleConfig.mContext,
													"下载开始", Toast.LENGTH_SHORT)
													.show();
										}

										@Override
										public void OnDownloadUpdate(
												int progress) {
											Toast.makeText(
													UpdateExampleConfig.mContext,
													"下载进度 : " + progress + "%",
													Toast.LENGTH_SHORT).show();
										}

										@Override
										public void OnDownloadEnd(int result,
												String file) {
											switch (result) {
											case UpdateStatus.DOWNLOAD_COMPLETE_FAIL:
												Toast.makeText(
														UpdateExampleConfig.mContext,
														"下载失败",
														Toast.LENGTH_SHORT)
														.show();
												break;
											case UpdateStatus.DOWNLOAD_COMPLETE_SUCCESS:
												Toast.makeText(
														UpdateExampleConfig.mContext,
														"下载成功\n下载文件位置 : "
																+ file,
														Toast.LENGTH_SHORT)
														.show();
												break;
											case UpdateStatus.DOWNLOAD_NEED_RESTART:
												// 增量更新请求全包更新(请勿处理这种情况)
												break;
											}
										}
									});
							downloadListenerText
									.setText(R.string.download_listener_not_null);
							UpdateExampleConfig.setDownloadListener(true);
						} else {
							UmengUpdateAgent.setDownloadListener(null);
							downloadListenerText
									.setText(R.string.download_listener_null);
							UpdateExampleConfig.setDownloadListener(false);
						}
					}
				});
		downloadListenerCheckBox.setChecked(UpdateExampleConfig
				.hasDownloadListener());

		Button setDefault = (Button) root
				.findViewById(R.id.umeng_example_update_btn_default);
		setDefault.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setOnlyWifiRadioGroup.check(R.id.btn_wifi_only_true);
				setDeltaRadioGroup.check(R.id.btn_delta_true);
				setAutoPopupRadioGroup.check(R.id.btn_auto_popup_true);
				setStyleRadioGroup.check(R.id.btn_style_dialog);
				updateListenerCheckBox.setChecked(false);
				dialogListenerCheckBox.setChecked(false);
				downloadListenerCheckBox.setChecked(false);
				UmengUpdateAgent.setDefault();
			}
		});
		setDefault.performClick();

		Button autoUpdate = (Button) root
				.findViewById(R.id.umeng_example_update_btn_auto_update);
		autoUpdate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				UmengUpdateAgent.update(getActivity());
			}
		});

		Button forceUpdate = (Button) root
				.findViewById(R.id.umeng_example_update_btn_force_update);
		forceUpdate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				UmengUpdateAgent.forceUpdate(getActivity());
			}
		});

		Button silentUpdate = (Button) root
				.findViewById(R.id.umeng_example_update_btn_silent_update);
		silentUpdate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				UmengUpdateAgent.silentUpdate(getActivity());
			}
		});
		return root;
	}
}
