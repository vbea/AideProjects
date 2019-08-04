package com.umeng.example.update;

import java.io.File;
import java.text.DecimalFormat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.example.R;
import com.umeng.example.util.UpdateExampleConfig;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

public class UpdateProcessFragment extends Fragment {
	UpdateResponse response = null;
	boolean isIgnore = false;
	File file = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.umeng_example_update_process,
				container, false);

		final LinearLayout updateListenerLayout = (LinearLayout) root
				.findViewById(R.id.update_listener_layout);
		final LinearLayout checkUpdateLayout = (LinearLayout) root
				.findViewById(R.id.check_update_layout);
		final RelativeLayout updateReturnLayout = (RelativeLayout) root
				.findViewById(R.id.update_return_layout);
		final TextView updateResultCode = (TextView) root
				.findViewById(R.id.update_result_code);
		final TextView updateToastCode = (TextView) root
				.findViewById(R.id.update_toast_code);
		final TextView updateShowUiCode = (TextView) root
				.findViewById(R.id.update_show_ui_code);
		final LinearLayout diyUpdateLayout = (LinearLayout) root
				.findViewById(R.id.diy_update_layout);
		final RadioGroup updatePlanRadioGroup = (RadioGroup) root
				.findViewById(R.id.update_plan_radiogroup);
		final LinearLayout updateDefaultLayout = (LinearLayout) root
				.findViewById(R.id.update_default_layout);
		final LinearLayout updateCustomLayout = (LinearLayout) root
				.findViewById(R.id.update_custom_layout);
		final TextView updateTipText = (TextView) root
				.findViewById(R.id.update_tip_text);
		final Button startInstallButton = (Button) root
				.findViewById(R.id.umeng_example_update_btn_start_install);

		final TextView setAutoPopupText = (TextView) root
				.findViewById(R.id.auto_popup_text);
		final RadioGroup setAutoPopupRadioGroup = (RadioGroup) root
				.findViewById(R.id.auto_popup_radiogroup);
		setAutoPopupRadioGroup
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						switch (checkedId) {
						case R.id.btn_auto_popup_true:
							UmengUpdateAgent.setUpdateAutoPopup(true);
							setAutoPopupText
									.setText(R.string.auto_popup_text_true);
							UpdateExampleConfig.setUpdateAutoPopup(true);
							updateListenerLayout.setVisibility(View.GONE);
							checkUpdateLayout.setVisibility(View.GONE);
							updateReturnLayout.setVisibility(View.GONE);
							diyUpdateLayout.setVisibility(View.GONE);
							break;
						case R.id.btn_auto_popup_false:
							UmengUpdateAgent.setUpdateAutoPopup(false);
							setAutoPopupText
									.setText(R.string.auto_popup_text_false);
							UpdateExampleConfig.setUpdateAutoPopup(false);
							updateListenerLayout.setVisibility(View.VISIBLE);
							if (UpdateExampleConfig.hasUpdateListener()) {
								checkUpdateLayout.setVisibility(View.VISIBLE);
							}
							break;
						}
					}
				});
		setAutoPopupRadioGroup.check(R.id.btn_auto_popup_false);

		final TextView updateListenerText = (TextView) root
				.findViewById(R.id.update_listener_code);
		final CheckBox updateListenerCheckBox = (CheckBox) root
				.findViewById(R.id.update_listener_check);

		final UmengUpdateListener listener = new UmengUpdateListener() {

			@Override
			public void onUpdateReturned(int updateStatus,
					UpdateResponse updateInfo) {
				updateReturnLayout.setVisibility(View.VISIBLE);
				switch (updateStatus) {
				case UpdateStatus.Yes:
					response = updateInfo;
					isIgnore = false;
					file = null;
					updateResultCode.setText(R.string.update_result_yes);
					updateToastCode.setText(R.string.update_toast_yes);
					Toast.makeText(getActivity(), "发现更新", Toast.LENGTH_SHORT)
							.show();
					diyUpdateLayout.setVisibility(View.VISIBLE);
					startInstallButton.setEnabled(false);
					updatePlanRadioGroup.clearCheck();
					updateDefaultLayout.setVisibility(View.GONE);
					updateCustomLayout.setVisibility(View.GONE);
					break;
				case UpdateStatus.No:
					updateResultCode.setText(R.string.update_result_no);
					updateToastCode.setText(R.string.update_toast_no);
					Toast.makeText(getActivity(), "没有更新", Toast.LENGTH_SHORT)
							.show();
					diyUpdateLayout.setVisibility(View.GONE);
					break;
				case UpdateStatus.NoneWifi:
					updateResultCode.setText(R.string.update_result_nonewifi);
					updateToastCode.setText(R.string.update_toast_nonewifi);
					Toast.makeText(getActivity(), "没有wifi", Toast.LENGTH_SHORT)
							.show();
					diyUpdateLayout.setVisibility(View.GONE);
					break;
				case UpdateStatus.Timeout:
					updateResultCode.setText(R.string.update_result_timeout);
					updateToastCode.setText(R.string.update_toast_timeout);
					Toast.makeText(getActivity(), "超时", Toast.LENGTH_SHORT)
							.show();
					diyUpdateLayout.setVisibility(View.GONE);
					break;
				}
			}

		};
		updateListenerCheckBox
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							UmengUpdateAgent.setUpdateListener(listener);
							updateListenerText
									.setText(R.string.update_listener_not_null);
							UpdateExampleConfig.setUpdateListener(true);
							checkUpdateLayout.setVisibility(View.VISIBLE);
						} else {
							UmengUpdateAgent.setUpdateListener(null);
							updateListenerText
									.setText(R.string.update_listener_null);
							UpdateExampleConfig.setUpdateListener(false);
							checkUpdateLayout.setVisibility(View.GONE);
							updateReturnLayout.setVisibility(View.GONE);
							diyUpdateLayout.setVisibility(View.GONE);
						}
					}
				});
		updateListenerCheckBox.setChecked(true);

		Button checkUpdate = (Button) root
				.findViewById(R.id.umeng_example_update_btn_check_update);
		checkUpdate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				UmengUpdateAgent.update(getActivity());
			}
		});

		updatePlanRadioGroup
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						switch (checkedId) {
						case R.id.btn_default:
							updateDefaultLayout.setVisibility(View.VISIBLE);
							updateCustomLayout.setVisibility(View.GONE);
							break;
						case R.id.btn_custom:
							updateDefaultLayout.setVisibility(View.GONE);
							updateCustomLayout.setVisibility(View.VISIBLE);
							updateShowUiCode.setVisibility(View.GONE);
							break;
						}
					}
				});

		Button showDialogButton = (Button) root
				.findViewById(R.id.umeng_example_update_btn_show_dialog);
		showDialogButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				updateShowUiCode.setText(R.string.show_dialog_code);
				updateShowUiCode.setVisibility(View.VISIBLE);
				UmengUpdateAgent.showUpdateDialog(getActivity(), response);
			}
		});

		Button showNotificationButton = (Button) root
				.findViewById(R.id.umeng_example_update_btn_show_notification);
		showNotificationButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				updateShowUiCode.setText(R.string.show_notification_code);
				updateShowUiCode.setVisibility(View.VISIBLE);
				UmengUpdateAgent
						.showUpdateNotification(getActivity(), response);
			}
		});

		Button isIgnoreButton = (Button) root
				.findViewById(R.id.umeng_example_update_btn_is_ignore);
		isIgnoreButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				isIgnore = UmengUpdateAgent.isIgnore(getActivity(), response);
				if (isIgnore) {
					Toast.makeText(getActivity(), "该版本已被用户忽略",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getActivity(), "该版本未被用户忽略",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		Button downloadFileButton = (Button) root
				.findViewById(R.id.umeng_example_update_btn_download_file);
		downloadFileButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				file = UmengUpdateAgent.downloadedFile(getActivity(), response);
				if (file == null) {
					Toast.makeText(getActivity(), "文件未下载", Toast.LENGTH_SHORT)
							.show();
					startInstallButton.setEnabled(false);
				} else {
					Toast.makeText(getActivity(),
							"文件位置:" + file.getAbsolutePath(),
							Toast.LENGTH_SHORT).show();
					startInstallButton.setEnabled(true);
				}
			}
		});

		Button showUiButton = (Button) root
				.findViewById(R.id.umeng_example_update_btn_show_ui);
		showUiButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showUpdateTip(updateTipText);
			}
		});

		Button startDownloadButton = (Button) root
				.findViewById(R.id.umeng_example_update_btn_start_download);
		startDownloadButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				UmengUpdateAgent.startDownload(getActivity(), response);
			}
		});

		startInstallButton.setEnabled(false);
		startInstallButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				UmengUpdateAgent.startInstall(getActivity(), file);
			}
		});

		Button ignoreUpdateButton = (Button) root
				.findViewById(R.id.umeng_example_update_btn_ignore_update);
		ignoreUpdateButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				UmengUpdateAgent.ignoreUpdate(getActivity(), response);
			}
		});

		return root;
	}

	public void showUpdateTip(TextView updateTipText) {
		String updateContent;
		if (isIgnore) {
			Toast.makeText(getActivity(), "该版本已被用户忽略", Toast.LENGTH_SHORT)
					.show();
			updateTipText.setText("");
		} else {
			if (file == null) {
				String deltaContent;
				if (response.delta) {
					deltaContent = String.format("\n%s %s", "更新包大小:",
							getFileSizeDescription(response.size));
				} else {
					deltaContent = "";
				}

				updateContent = String.format("%s %s\n" + "%s %s%s\n\n"
						+ "%s\n" + "%s\n", "最新版本:", response.version, "新版本大小:",
						getFileSizeDescription(response.target_size),
						deltaContent, "更新内容:", response.updateLog);
				updateTipText.setText(updateContent);
			} else {
				updateContent = String.format("%s %s\n" + "%s\n\n" + "%s\n"
						+ "%s\n", "最新版本:", response.version, "最新版本已下载，是否安装？",
						"更新内容:", response.updateLog);
				updateTipText.setText(updateContent);
			}
		}
	}

	public static String getFileSizeDescription(String size) {
		String value = "";
		long bytes = 0;
		try {
			bytes = Long.valueOf(size).longValue();
		} catch (NumberFormatException e) {
			return size;
		}
		if (bytes < 1024) {
			value = (int) bytes + "B";
		} else if (bytes < 1048576) {
			DecimalFormat df = new DecimalFormat("#0.00");
			value = df.format((float) bytes / 1024.0) + "K";
		} else if (bytes < 1073741824) {
			DecimalFormat df = new DecimalFormat("#0.00");
			value = df.format((float) bytes / 1048576.0) + "M";
		} else {
			DecimalFormat df = new DecimalFormat("#0.00");
			value = df.format((float) bytes / 1073741824.0) + "G";
		}
		return value;
	}

	@Override
	public void onStop() {
		super.onStop();
		UmengUpdateAgent.setDefault();
	}

}
