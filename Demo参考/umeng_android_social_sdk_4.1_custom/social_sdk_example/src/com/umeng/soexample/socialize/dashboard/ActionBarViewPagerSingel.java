package com.umeng.soexample.socialize.dashboard;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.umeng.socialize.bean.RequestType;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.utils.Log;
import com.umeng.socialize.view.ActionBarView;
import com.umeng.soexample.R;
import com.umeng.soexample.socialize.dashboard.MockDataHelper.TV;
import com.umeng.ui.BaseSinglePaneActivity;

public class ActionBarViewPagerSingel extends BaseSinglePaneActivity {
	@Override
	protected Fragment onCreatePane() {
		FragmentManager fm = getSupportFragmentManager();
		return new ContentFragment(fm);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public static class ContentFragment extends Fragment {
		UMSocialService service;
		FragmentManager fm;
		ActionBarView socializeActionBar;

		public ContentFragment(FragmentManager fm) {
			this.fm = fm;
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
		}

		@Override
		public View onCreateView(	LayoutInflater inflater,
									ViewGroup container,
									Bundle savedInstanceState) {
			View root = inflater.inflate(	R.layout.umeng_example_socialize_actionbar_viewpager_singel,
											container,
											false);
			ViewPager vPager = (ViewPager) root.findViewById(R.id.pager);
			ViewGroup parent = (ViewGroup) root.findViewById(R.id.root);
			// 用于集成ActionBar 的ViewGroup
			// 创建ActionBar des参数是ActionBar的唯一标识，请确保不为空
			socializeActionBar = new ActionBarView(getActivity());
			LayoutParams layoutParams = new ViewGroup.LayoutParams(	LayoutParams.WRAP_CONTENT,
																	LayoutParams.FILL_PARENT);
			socializeActionBar.setLayoutParams(layoutParams);
			// 添加ActionBar
			parent.addView(socializeActionBar);

			List<TV> baseTv = MockDataHelper.getTVs(getActivity());
			final List<TV> tVs = new ArrayList<MockDataHelper.TV>();
			int id = 1000;

			for (int i = 0; i < 20; i++)
				for (TV tv : baseTv) {
					TV mTv = new TV();
					mTv.name = tv.name + (++id);
					mTv.des = "[" + id + "]" + tv.des;
					mTv.img = tv.img;
					mTv.tam = tv.tam;
					mTv.tv = tv.tv;
					tVs.add(mTv);
				}
			SectionsPagerAdapter pagerAdapter = new SectionsPagerAdapter(fm, tVs);
			vPager.setAdapter(pagerAdapter);

			vPager.setOnPageChangeListener(new OnPageChangeListener() {
				@Override
				public void onPageSelected(int arg0) {
					TV tv = tVs.get(arg0);
					Log.d("TestData", "onPageSelected " + arg0 + "   " + tv.name);
					socializeActionBar.resetEntity(tv.name);
				}

				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2) {}

				@Override
				public void onPageScrollStateChanged(int arg0) {}
			});
			TV tv = tVs.get(0);
			socializeActionBar.resetEntity(tv.name);
			return root;
		}

		@Override
		public void onDestroy() {
			super.onDestroy();
		}
	}

	public static class SectionsPagerAdapter extends FragmentPagerAdapter {
		private List<TV> tvs;

		public SectionsPagerAdapter(FragmentManager fm, List<TV> tvs) {
			super(fm);
			this.tvs = tvs;
		}

		@Override
		public Fragment getItem(int i) {
			TV tv = tvs.get(i);
			Fragment fragment = new DummySectionFragment();
			Bundle args = new Bundle();
			args.putParcelable(DummySectionFragment.KEY_TV, tv);
			fragment.setArguments(args);

			return fragment;
		}

		@Override
		public int getCount() {
			return tvs.size();
		}
	}

	public static class DummySectionFragment extends Fragment {
		public static final String KEY_TV = "tv";
		private UMSocialService service;

		@Override
		public View onCreateView(	LayoutInflater inflater,
									ViewGroup container,
									Bundle savedInstanceState) {
			Bundle args = getArguments();
			final TV tv = args.getParcelable(KEY_TV);

			View content = inflater.inflate(R.layout.umeng_example_socialize_actionbar_detail, null);

			TextView textView = (TextView) content.findViewById(R.id.textview);
			textView.setText(tv.des);
			
			textView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
				}
			});
			service = UMServiceFactory.getUMSocialService(tv.name, RequestType.SOCIAL);

			service.setShareContent(tv.des);
			if (!service.hasShareImage()) {
				new MockDataHelper.BindIMGTAsk(getActivity(), tv) {
					@Override
					public void onComplate(Drawable drawable) {
						if (drawable != null) {
							Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
							service.setShareImage(new UMImage(getActivity(), bitmap));
						}
					}
				}.execute();
			}

			return content;
		}

	}

}
