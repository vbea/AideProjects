/*
 * Copyright 2011 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.umeng.ui;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.ActionProvider;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.AbstractAction;
import com.umeng.example.R;

/**
 * A {@link BaseActivity} that simply contains a single fragment. The intent
 * used to invoke this activity is forwarded to the fragment as arguments during
 * fragment instantiation. Derived activities should only need to implement
 * {@link com.google.android.apps.iosched.ui.BaseSinglePaneActivity#onCreatePane()}
 * .
 */
public abstract class BaseNavigationDrawerActivity extends FragmentActivity {
	// private Fragment mFragment;
	private ActionBar mActionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_navigation_drawer_empty);
		mActionBar = (ActionBar) findViewById(R.id.actionbar1);
		mActionBar.setTitle(getTitle());
		mActionBar.setHomeAction(new NavigationDrawerAction(R.drawable.icon));

	}

	/**
	 * Called in <code>onCreate</code> when the fragment constituting this
	 * activity is needed. The returned fragment's arguments will be set to the
	 * intent used to invoke this activity.
	 */
	// protected abstract Fragment onCreatePane();

	protected ActionBar getUActionBar() {
		return mActionBar;
	}

	public class NavigationDrawerAction extends AbstractAction {

		public NavigationDrawerAction(int drawable) {
			super(drawable);
		}

		@Override
		public void performAction(View view) {
//			changeNavigationDrawerImage();
			onOptionsItemSelected(new MenuItem() {

				@Override
				public MenuItem setVisible(boolean visible) {
					return null;
				}

				@Override
				public MenuItem setTitleCondensed(CharSequence title) {
					return null;
				}

				@Override
				public MenuItem setTitle(int title) {
					return null;
				}

				@Override
				public MenuItem setTitle(CharSequence title) {
					return null;
				}

				@Override
				public MenuItem setShowAsActionFlags(int actionEnum) {
					return null;
				}

				@Override
				public void setShowAsAction(int actionEnum) {

				}

				@Override
				public MenuItem setShortcut(char numericChar, char alphaChar) {
					return null;
				}

				@Override
				public MenuItem setOnMenuItemClickListener(
						OnMenuItemClickListener menuItemClickListener) {
					return null;
				}

				@Override
				public MenuItem setOnActionExpandListener(
						OnActionExpandListener listener) {
					return null;
				}

				@Override
				public MenuItem setNumericShortcut(char numericChar) {
					return null;
				}

				@Override
				public MenuItem setIntent(Intent intent) {
					return null;
				}

				@Override
				public MenuItem setIcon(int iconRes) {
					return null;
				}

				@Override
				public MenuItem setIcon(Drawable icon) {
					return null;
				}

				@Override
				public MenuItem setEnabled(boolean enabled) {
					return null;
				}

				@Override
				public MenuItem setChecked(boolean checked) {
					return null;
				}

				@Override
				public MenuItem setCheckable(boolean checkable) {
					return null;
				}

				@Override
				public MenuItem setAlphabeticShortcut(char alphaChar) {
					return null;
				}

				@Override
				public MenuItem setActionView(int resId) {
					return null;
				}

				@Override
				public MenuItem setActionView(View view) {
					return null;
				}

				@Override
				public MenuItem setActionProvider(ActionProvider actionProvider) {
					return null;
				}

				@Override
				public boolean isVisible() {
					return false;
				}

				@Override
				public boolean isEnabled() {
					return false;
				}

				@Override
				public boolean isChecked() {
					return false;
				}

				@Override
				public boolean isCheckable() {
					return false;
				}

				@Override
				public boolean isActionViewExpanded() {
					return false;
				}

				@Override
				public boolean hasSubMenu() {
					return false;
				}

				@Override
				public CharSequence getTitleCondensed() {
					return null;
				}

				@Override
				public CharSequence getTitle() {
					return null;
				}

				@Override
				public SubMenu getSubMenu() {
					return null;
				}

				@Override
				public int getOrder() {
					return 0;
				}

				@Override
				public char getNumericShortcut() {
					return 0;
				}

				@Override
				public ContextMenuInfo getMenuInfo() {
					return null;
				}

				@Override
				public int getItemId() {
					return 0x0102002c;
				}

				@Override
				public Intent getIntent() {
					return null;
				}

				@Override
				public Drawable getIcon() {
					return null;
				}

				@Override
				public int getGroupId() {
					return 0;
				}

				@Override
				public char getAlphabeticShortcut() {
					return 0;
				}

				@Override
				public View getActionView() {
					return null;
				}

				@Override
				public ActionProvider getActionProvider() {
					return null;
				}

				@Override
				public boolean expandActionView() {
					return false;
				}

				@Override
				public boolean collapseActionView() {
					return false;
				}
			});
		}

	}
}
