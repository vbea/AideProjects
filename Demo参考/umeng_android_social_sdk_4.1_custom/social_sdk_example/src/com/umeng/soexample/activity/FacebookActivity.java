
package com.umeng.soexample.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AppEventsLogger;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.LoggingBehavior;
import com.facebook.Request;
import com.facebook.Request.Callback;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.Settings;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphUser;
import com.facebook.widget.FacebookDialog;
import com.umeng.soexample.R;

/**
 * @Copyright: Umeng.com, Ltd. Copyright 2011-2015, All rights reserved
 * @Title: FacebookActivity.java
 * @Package com.umeng.soexample.activity
 * @Description:
 * @author Honghui He
 * @version V1.0
 */

public class FacebookActivity extends Activity {
    private Button mFacebookLoginBtn = null;
    private Button mFacebookShareBtn = null;
    private Session mFbSession = null;
    private TextView mTokenTextView = null;
    private boolean canPresentShareDialog;
    private TextView mIdTextView = null;
    private TextView mFirstNameTextView = null;
    /**
     * REST请求地址
     */
    private final String FACEBOOK_URL = "https://graph.facebook.com/?";

    private enum PendingAction {
        NONE,
        POST_PHOTO,
        POST_STATUS_UPDATE
    }

    private PendingAction pendingAction = PendingAction.NONE;
    private static final String PERMISSION = "publish_actions, user_photos, read_stream";
    private GraphUser user;

    /**
     * 
     */
    private UiLifecycleHelper uiHelper;

    private Session.StatusCallback statusCallback = new SessionStatusCallback();

    /**
     * 
     */
    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };

    private FacebookDialog.Callback dialogCallback = new FacebookDialog.Callback() {
        @Override
        public void onError(FacebookDialog.PendingCall pendingCall, Exception error, Bundle data) {
            Log.d("HelloFacebook", String.format("Error: %s", error.toString()));
        }

        @Override
        public void onComplete(FacebookDialog.PendingCall pendingCall, Bundle data) {
            Log.d("HelloFacebook", "Success!");
        }
    };

    /**
     * (非 Javadoc)
     * 
     * @Title: onCreate
     * @Description:
     * @param savedInstanceState
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uiHelper = new UiLifecycleHelper(this, callback);
        uiHelper.onCreate(savedInstanceState);

        initViews();
        initSession(savedInstanceState);
        updateView();
    }

    /**
     * @Title: initViews
     * @Description:
     * @throws
     */
    private void initViews() {
        setContentView(R.layout.facebook_activity);
        mFacebookLoginBtn = (Button) findViewById(R.id.facebook_login_btn);
        mFacebookShareBtn = (Button) findViewById(R.id.facebook_share_btn);
        mFacebookShareBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                shareToFacebook();
            }
        });
        mTokenTextView = (TextView) findViewById(R.id.access_token_tv);

        mIdTextView = (TextView) findViewById(R.id.id_tv);

        mFirstNameTextView = (TextView) findViewById(R.id.firstname_tv);
    }

    /**
     * @Title: setupSession
     * @Description: 
     * @throws
     */
    private void initSession(Bundle savedInstanceState) {
        Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);

        mFbSession = Session.getActiveSession();
        if (mFbSession == null) {
            if (savedInstanceState != null) {
                mFbSession = Session.restoreSession(this, null, statusCallback, savedInstanceState);
            }
            if (mFbSession == null) {
                mFbSession = new Session(this);
            }
            Session.setActiveSession(mFbSession);
            if (mFbSession.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {
                mFbSession.openForRead(new Session.OpenRequest(this).setCallback(statusCallback));
            }
        }
        assert (mFbSession != null);

        canPresentShareDialog = FacebookDialog.canPresentShareDialog(this,
                FacebookDialog.ShareDialogFeature.SHARE_DIALOG);
    }

    /**
     * @Title: onSessionStateChange
     * @Description:
     * @param session
     * @param state
     * @param exception
     * @throws
     */
    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if (pendingAction != PendingAction.NONE &&
                (exception instanceof FacebookOperationCanceledException ||
                exception instanceof FacebookAuthorizationException)) {
            new AlertDialog.Builder(FacebookActivity.this)
                    .setTitle(R.string.cancelled)
                    .setMessage(R.string.permission_not_granted)
                    .setPositiveButton(R.string.ok, null)
                    .show();
            pendingAction = PendingAction.NONE;
        } else if (state == SessionState.OPENED_TOKEN_UPDATED) {
            handlePendingAction();
        }
        updateUI();
    }

    /**
     * @Title: updateUI
     * @Description:
     * @throws
     */
    private void updateUI() {
        Session session = Session.getActiveSession();
        boolean enableButtons = (session != null && session.isOpened());
        Log.d("", "### data : " + session.getAuthorizationBundle());
        if (enableButtons && user != null) {
            mIdTextView.setText(user.getId());
            mFirstNameTextView.setText("hello, " + user.getFirstName());
        } else {
            mIdTextView.setText("");
            mFirstNameTextView.setText("");
        }

    }

    /**
     * (非 Javadoc)
     * 
     * @Title: onStart
     * @Description:
     * @see android.app.Activity#onStart()
     */
    @Override
    public void onStart() {
        super.onStart();
        Session.getActiveSession().addCallback(statusCallback);
    }

    /**
     * (非 Javadoc)
     * 
     * @Title: onStop
     * @Description:
     * @see android.app.Activity#onStop()
     */
    @Override
    public void onStop() {
        super.onStop();
        Session.getActiveSession().removeCallback(statusCallback);
        uiHelper.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        uiHelper.onResume();
        AppEventsLogger.activateApp(this);

        updateUI();
    }

    /**
     * (非 Javadoc)
     * 
     * @Title: onActivityResult
     * @Description:
     * @param requestCode
     * @param resultCode
     * @param data
     * @see android.app.Activity#onActivityResult(int, int,
     *      android.content.Intent)
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data, dialogCallback);
    }

    /**
     * (非 Javadoc)
     * 
     * @Title: onSaveInstanceState
     * @Description:
     * @param outState
     * @see android.app.Activity#onSaveInstanceState(android.os.Bundle)
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Session session = Session.getActiveSession();
        Session.saveSession(session, outState);
        uiHelper.onSaveInstanceState(outState);
    }

    /**
     * @Title: loginFacebook
     * @Description:login to facebook
     * @throws
     */
    private void loginFacebook() {
        Session session = Session.getActiveSession();
        if (!session.isOpened() && !session.isClosed()) {
            session.openForRead(new Session.OpenRequest(this).setCallback(statusCallback));
        } else {
            Session.openActiveSession(this, true, statusCallback);
        }
    }

    /**
     * @Title: logoutFacebook
     * @Description:
     * @throws
     */
    private void logoutFacebook() {
        Session session = Session.getActiveSession();
        if (!session.isClosed()) {
            session.closeAndClearTokenInformation();
        }
    }

    /**
     * @Title: shareToFacebook
     * @Description: 分享到facebook
     * @throws
     */
    private void shareToFacebook() {
        performPublish(PendingAction.POST_PHOTO, false);
        // performPublish(PendingAction.POST_STATUS_UPDATE, false);
    }

    /**
     * @Title: hasPublishPermission
     * @Description:
     * @return
     * @throws
     */
    private boolean hasPublishPermission() {
        Session session = Session.getActiveSession();
        return session != null && session.getPermissions().contains("publish_actions");
    }

    /**
     * @Title: performPublish
     * @Description:
     * @param action
     * @param allowNoSession
     * @throws
     */
    private void performPublish(PendingAction action, boolean allowNoSession) {
        Session session = Session.getActiveSession();
        if (session != null) {
            pendingAction = action;
            if (hasPublishPermission()) {
                // We can do the action right away.
                handlePendingAction();
                return;
            } else if (session.isOpened()) {
                // We need to get new permissions, then complete the action when
                // we get called back.
                session.requestNewPublishPermissions(new Session.NewPermissionsRequest(this,
                        PERMISSION));
                return;
            }
        }

        if (allowNoSession) {
            pendingAction = action;
            handlePendingAction();
        }
    }

    @SuppressWarnings("incomplete-switch")
    private void handlePendingAction() {
        PendingAction previouslyPendingAction = pendingAction;
        // These actions may re-set pendingAction if they are still pending, but
        // we assume they
        // will succeed.
        pendingAction = PendingAction.NONE;

        switch (previouslyPendingAction) {
            case POST_PHOTO:
                postPhoto();
                break;
            case POST_STATUS_UPDATE:
                postStatusUpdate();
                break;
        }
    }

    String mPostId = null;

    /**
     * @Title: postPhoto
     * @Description:
     * @throws
     */
    private void postPhoto() {
        if (hasPublishPermission()) {
            Bitmap image = BitmapFactory.decodeResource(this.getResources(),
                    R.drawable.socialize_banner_module);
            // // 上传图片的请求, 可以用
            Request request =
                    Request.newUploadPhotoRequest(Session.getActiveSession(), image,
                            new Request.Callback() {
                                @Override
                                public void onCompleted(Response response) {
                                    JSONObject jsonObject = response.getGraphObject()
                                            .getInnerJSONObject();
                                    Log.d("", "#### response : "
                                            +
                                            jsonObject.toString());
                                    try {
                                        mPostId = jsonObject.getString("post_id");
                                        Log.d("", "### id = " + mPostId);

                                        // Bundle params = new Bundle();
                                        // params.putString("access_token",
                                        // Session.getActiveSession()
                                        // .getAccessToken());
                                        // params.putString("id", mPostId);

                                        // 上传完后根据返回的id来获取图片的url地址
                                        Request getUrlRequest = new Request(Session
                                                .getActiveSession(),
                                                "me/feed", null, HttpMethod.GET);
                                        Log.d("", "#### path " + getUrlRequest.getGraphPath());
                                        getUrlRequest.setCallback(new Callback() {

                                            @Override
                                            public void onCompleted(Response response) {
                                                GraphObject imageObject = response.getGraphObject();
                                                if (imageObject != null) {
                                                    JSONObject jsonObject = imageObject
                                                            .getInnerJSONObject();
                                                    Log.d("",
                                                            "#### json : " + jsonObject.toString());
                                                    if (jsonObject != null) {
                                                        try {
                                                            String id = jsonObject.getString("id");
                                                            if (mPostId.equals(id)) {
                                                                // picture是小图,
                                                                // source是大图
                                                                String imageUrl = jsonObject
                                                                        .getString("picture");
                                                                Log.d("",
                                                                        "### uploaded image url : "
                                                                                + imageUrl);
                                                            }

                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                }
                                                Log.d("", "#### response : " + response.toString());
                                            }
                                        });
                                        getUrlRequest.executeAsync();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    showPublishResult(getString(R.string.photo_post),
                                            response.getGraphObject(), response.getError());
                                }
                            });
            request.executeAsync();

        } else {
            pendingAction = PendingAction.POST_PHOTO;
        }
    }

    /**
     * @ClassName: GraphObjectWithId
     * @Description:
     * @author Honghui He
     */
    private interface GraphObjectWithId extends GraphObject {
        String getId();
    }

    /**
     * @Title: showPublishResult
     * @Description:
     * @param message
     * @param result
     * @param error
     * @throws
     */
    private void showPublishResult(String message, GraphObject result, FacebookRequestError error) {
        String title = null;
        String alertMessage = null;
        if (error == null) {
            title = getString(R.string.success);
            String id = result.cast(GraphObjectWithId.class).getId();
            alertMessage = getString(R.string.successfully_posted_post, message, id);
        } else {
            title = getString(R.string.error);
            alertMessage = error.getErrorMessage();
        }

        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(alertMessage)
                .setPositiveButton(R.string.ok, null)
                .show();
    }

    /**
     * @Title: createShareDialogBuilder
     * @Description:
     * @return
     * @throws
     */
    private FacebookDialog.ShareDialogBuilder createShareDialogBuilder() {
        return new FacebookDialog.ShareDialogBuilder(this)
                .setName("Hello Facebook")
                .setDescription(
                        "The 'Hello Facebook' sample application showcases simple Facebook integration")
                .setLink("http://developers.facebook.com/android");
    }

    /**
     * @Title: postStatusUpdate
     * @Description: 更新状态, 可以包含图片, 参考地址:
     *               https://developers.facebook.com/docs/android
     *               /publish-to-feed/
     * @throws
     */
    private void postStatusUpdate() {
        if (canPresentShareDialog) {
            FacebookDialog shareDialog = createShareDialogBuilder().build();
            uiHelper.trackPendingDialogCall(shareDialog.present());
        } else if (hasPublishPermission()) {
            Toast.makeText(getApplicationContext(), "post status", Toast.LENGTH_SHORT).show();
            // final String message = getString(R.string.status_update) +
            // (new Date().toString());
            // Request request = Request
            // .newStatusUpdateRequest(Session.getActiveSession(), message,
            // place, tags,
            // new Request.Callback() {
            // @Override
            // public void onCompleted(Response response) {
            // showPublishResult(message, response.getGraphObject(),
            // response.getError());
            // }
            // });
            // request.executeAsync();

            Bundle postParams = new Bundle();
            postParams.putString("name", "Facebook SDK for Android");
            postParams.putString("message", "The Fucking Message  22.");
            postParams.putString("caption", "Build great social apps and get more installs.");
            postParams
                    .putString(
                            "description",
                            "The Facebook SDK for Android makes it easier and faster to develop Facebook integrated Android apps.");
            postParams.putString("link", "http://www.umeng.com/component_social");
            postParams
                    .putString("picture",
                            "https://raw.github.com/fbsamples/ios-3.x-howtos/master/Images/iossdk_logo.png");
            Request request = new Request(Session.getActiveSession(), "me/feed", postParams,
                    HttpMethod.POST, new Callback() {

                        @Override
                        public void onCompleted(Response response) {
                            Toast.makeText(getApplicationContext(), response.toString(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

            RequestAsyncTask task = new RequestAsyncTask(request);
            task.execute();

        } else {
            pendingAction = PendingAction.POST_STATUS_UPDATE;
        }
    }

    /**
     * @Title: updateView
     * @Description:
     * @throws
     */
    private void updateView() {
        Session session = Session.getActiveSession();
        if (session.isOpened()) {
            mFacebookLoginBtn.setText(R.string.fb_logout);
            mFacebookLoginBtn.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    logoutFacebook();
                }
            });
            mTokenTextView.setText("AccessToken : " + Session.getActiveSession().getAccessToken());
        } else {
            mFacebookLoginBtn.setText(R.string.fb_login);
            mFacebookLoginBtn.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    loginFacebook();
                }
            });
        }
    }

    /**
     * @ClassName: SessionStatusCallback
     * @Description:
     * @author Honghui He
     */
    private class SessionStatusCallback implements Session.StatusCallback {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            updateView();
        }
    }

}
