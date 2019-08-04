package com.umeng.soexample.socialize.dashboard;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;

public class MockDataHelper {
	private static final String BASE_PATH = "socialize_data/";

	public static List<TV> getTVs(Context context) {
		List<TV> listData = new ArrayList<MockDataHelper.TV>();
		InputStream is = null;
		ByteArrayOutputStream bos = null;
		try {
			is = context.getAssets().open(BASE_PATH + "jsonData.txt");
			bos = new ByteArrayOutputStream();
			byte[] bytes = new byte[4 * 1024];
			int len = 0;
			while ((len = is.read(bytes)) != -1) {
				bos.write(bytes, 0, len);
			}
			String json = new String(bos.toByteArray());
			JSONObject jsonObj = new JSONObject(json);
			JSONArray jsonArray = jsonObj.getJSONArray("data");
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				TV mTv = createByJson(jsonObject);
				listData.add(mTv);
			}
			return listData;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null) is.close();
				if (bos != null) bos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	public static abstract class BindIMGTAsk extends AsyncTask<Void, Void, Drawable> {
		private TV mTv;
		private Context mContext;

		public BindIMGTAsk(Context context, TV mTv) {
			super();
			this.mTv = mTv;
			this.mContext = context;
		}

		@Override
		protected Drawable doInBackground(Void... params) {
			InputStream is = null;
			ByteArrayOutputStream bos = null;
			try {
				 is = mContext.getAssets().open(BASE_PATH + mTv.img);
				 Drawable createFromStream = Drawable.createFromStream(is, System.currentTimeMillis() + "");
				 return createFromStream;
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
				try {
					if (is != null) is.close();
					if (bos != null) bos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			return null;
		}

		@Override
		protected void onPostExecute(Drawable result) {
			super.onPostExecute(result);
			onComplate(result);
		}

		public abstract void onComplate(Drawable drawable);

	}
	
	public static abstract class BindNETiMGTask extends AsyncTask<Void, Void, Drawable> {
		private String url;

		public BindNETiMGTask(String  url) {
			super();
			this.url = url;
		}

		@Override
		protected Drawable doInBackground(Void... params) {
			InputStream in =null;
			ByteArrayOutputStream bos = null ;
			try {
				in = (InputStream) new URL(url).openConnection().getContent();
				bos = new ByteArrayOutputStream();
				byte[] buf = new byte[4 * 1024]; // 4K buffer
				int bytesRead;
				while ((bytesRead = in.read(buf)) != -1) {
					bos.write(buf, 0, bytesRead);
				}
				byte[] array = bos.toByteArray();
				
				Bitmap bitmap = BitmapFactory.decodeByteArray(array, 0, array.length);
				if(bitmap != null)
					return new BitmapDrawable(bitmap);
			}
			catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					if (in != null)
						in.close();
					if (bos != null)
						bos.close();
				}
				catch (Exception e2) {
				}
			}

			return null;
		}

		@Override
		protected void onPostExecute(Drawable result) {
			super.onPostExecute(result);
			onComplate(result);
		}

		public abstract void onComplate(Drawable drawable);

	}

	public static class TV implements Parcelable{
		public String name;
		public String tam;
		public String time;
		public String tv;
		public String img;
		public String des;
		@Override
		public int describeContents() {
			return 0;
		}
		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeString(name);
			dest.writeString(tam);
			dest.writeString(time);
			dest.writeString(tv);
			dest.writeString(img);
			dest.writeString(des);
		}
		
		private TV(Parcel in) {
			name = in.readString();
			tam = in.readString();
			time = in.readString();
			tv = in.readString();
			img = in.readString();
			des = in.readString();
		}
		public TV() {

		}
		
		public static final Parcelable.Creator<TV> CREATOR = new Parcelable.Creator<TV>() {
			@Override
			public TV createFromParcel(Parcel in) {
				return new TV(in);
			}

			@Override
			public TV[] newArray(int size) {
				return new TV[size];
			}
		};
	}

	public static TV createByJson(JSONObject json) {
		TV tv = new TV();
		tv.name = json.optString("name");
		tv.tam = json.optString("tam");
		tv.time = json.optString("time");
		tv.tv = json.optString("tv");
		tv.img = json.optString("img");
		tv.des = json.optString("des");
		return tv;
	}
}
