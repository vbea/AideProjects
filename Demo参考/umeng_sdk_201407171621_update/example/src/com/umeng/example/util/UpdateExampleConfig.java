package com.umeng.example.util;

import com.umeng.update.UpdateResponse;

import android.content.Context;


public class UpdateExampleConfig {
	private static boolean mUpdateOnlyWifi = true;
	private static boolean mUpdateAutoPopup = true;
	private static boolean mDeltaUpdate = true;
	private static boolean mRichNotification = true;
	private static boolean mDialogStyle = true;
	private static boolean mUpdateListener = false;
	private static boolean mDialogListener = false;
	private static boolean mDownloadListener = false;
	public static Context mContext;
	public static UpdateResponse mResponse = null;

	public static boolean isUpdateOnlyWifi() {
		return mUpdateOnlyWifi;
	}

	public static void setUpdateOnlyWifi(boolean mUpdateOnlyWifi) {
		UpdateExampleConfig.mUpdateOnlyWifi = mUpdateOnlyWifi;
	}

	public static boolean isUpdateAutoPopup() {
		return mUpdateAutoPopup;
	}

	public static void setUpdateAutoPopup(boolean mUpdateAutoPopup) {
		UpdateExampleConfig.mUpdateAutoPopup = mUpdateAutoPopup;
	}

	public static boolean isDeltaUpdate() {
		return mDeltaUpdate;
	}

	public static void setDeltaUpdate(boolean mDeltaUpdate) {
		UpdateExampleConfig.mDeltaUpdate = mDeltaUpdate;
	}

	public static boolean isDialogStyle() {
		return mDialogStyle;
	}

	public static void setDialogStyle(boolean mDialogStyle) {
		UpdateExampleConfig.mDialogStyle = mDialogStyle;
	}

	public static boolean hasUpdateListener() {
		return mUpdateListener;
	}

	public static void setUpdateListener(boolean mUpdateListener) {
		UpdateExampleConfig.mUpdateListener = mUpdateListener;
	}

	public static boolean hasDialogListener() {
		return mDialogListener;
	}

	public static void setDialogListener(boolean mDialogListener) {
		UpdateExampleConfig.mDialogListener = mDialogListener;
	}

	public static boolean hasDownloadListener() {
		return mDownloadListener;
	}

	public static void setDownloadListener(boolean mDownloadListener) {
		UpdateExampleConfig.mDownloadListener = mDownloadListener;
	}

	public static boolean isRichNotification() {
		return mRichNotification;
	}

	public static void setRichNotification(boolean mRichNotification) {
		UpdateExampleConfig.mRichNotification = mRichNotification;
	}
}
