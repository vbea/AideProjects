
package com.umeng.soexample.socialize.fragments;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.SensorEvent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.umeng.scrshot.UMScrShotController.OnScreenshotListener;
import com.umeng.scrshot.adapter.UMAppAdapter;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMEvernoteHandler;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.controller.media.EvernoteShareContent;
import com.umeng.socialize.facebook.controller.UMFacebookHandler;
import com.umeng.socialize.facebook.controller.UMFacebookHandler.PostType;
import com.umeng.socialize.facebook.media.FaceBookShareContent;
import com.umeng.socialize.instagram.controller.UMInstagramHandler;
import com.umeng.socialize.instagram.media.InstagramShareContent;
import com.umeng.socialize.laiwang.controller.UMLWHandler;
import com.umeng.socialize.laiwang.media.LWDynamicShareContent;
import com.umeng.socialize.laiwang.media.LWShareContent;
import com.umeng.socialize.linkedin.controller.UMLinkedInHandler;
import com.umeng.socialize.linkedin.media.LinkedInShareContent;
import com.umeng.socialize.media.GooglePlusShareContent;
import com.umeng.socialize.media.MailShareContent;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.RenrenShareContent;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.SmsShareContent;
import com.umeng.socialize.media.TencentWbShareContent;
import com.umeng.socialize.media.TwitterShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMVideo;
import com.umeng.socialize.media.UMusic;
import com.umeng.socialize.pinterest.controller.UMPinterestHandler;
import com.umeng.socialize.pinterest.media.PinterestShareContent;
import com.umeng.socialize.pocket.controller.UMPocketHandler;
import com.umeng.socialize.pocket.media.PocketShareContent;
import com.umeng.socialize.sensor.UMSensor.OnSensorListener;
import com.umeng.socialize.sensor.UMSensor.WhitchButton;
import com.umeng.socialize.sensor.beans.ShakeMsgType;
import com.umeng.socialize.sensor.controller.UMShakeService;
import com.umeng.socialize.sensor.controller.impl.UMShakeServiceFactory;
import com.umeng.socialize.sso.EmailHandler;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.RenrenSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.SmsHandler;
import com.umeng.socialize.sso.TencentWBSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;
import com.umeng.socialize.yixin.controller.UMYXHandler;
import com.umeng.socialize.ynote.controller.UMYNoteHandler;
import com.umeng.socialize.ynote.media.YNoteShareContent;
import com.umeng.soexample.R;
import com.umeng.soexample.socialize.SocializeConfigDemo;

/**
 * @功能描述 : 自定义平台Fragment, 包含添加微信和QQ平台
 * @原 作 者 :
 * @版 本 号 : [版本号, Aug 8, 2013]
 * @修 改 人 : mrsimple
 * @修改内容 :
 */
public class CustomPlatformFragment extends Fragment implements OnClickListener {

    // 整个平台的Controller, 负责管理整个SDK的配置、操作等处理
    private UMSocialService mController = UMServiceFactory
            .getUMSocialService(SocializeConfigDemo.DESCRIPTOR);
    /**
     * 摇一摇控制器
     */
    private UMShakeService mShakeController = UMShakeServiceFactory
            .getShakeService(SocializeConfigDemo.DESCRIPTOR);

    private ImageView mImageView = null;
    private Button mWeiXinBtn = null;
    private Button mQQBtn = null;
    private Button mYiXinBtn = null;
    private Button mShakeOpenShareBtn = null;
    private Button mShakeShareBtn = null;
    private Button mShakeScrShotBtn = null;
    private Button mGotoFacebookBtn = null;
    private Button mGotoLaiWangBtn = null;
    private Button mInstagramBtn = null;
    /**
     * 
     */
    private UMFacebookHandler mFacebookHandler = null;

    /**
     * @功能描述 :
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // 配置sso
        setShareContent();
        // 初始化视图
        View rootView = initViews(inflater, container);
        rootView.setOnKeyListener(new OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                mController.dismissShareBoard();
                return true;
            }
        });
        return rootView;
    }

    /**
     * @Title: initViews
     * @Description: 初始化views
     * @return View
     * @throws
     */
    private View initViews(LayoutInflater inflater, ViewGroup container) {
        // patent容器
        final View root = inflater.inflate(
                R.layout.umeng_example_socialize_customplatform_example,
                container, false);
        mImageView = (ImageView) root.findViewById(R.id.scrshot_imgview);

        // 打开分享面板
        mWeiXinBtn = (Button) root.findViewById(R.id.share_by_weixin);
        mWeiXinBtn.setOnClickListener(this);

        // 添加QQ平台
        mQQBtn = (Button) root.findViewById(R.id.share_by_qq);
        mQQBtn.setOnClickListener(this);

        // 添加易信平台
        mYiXinBtn = (Button) root.findViewById(R.id.share_by_yixin);
        mYiXinBtn.setOnClickListener(this);

        // 摇一摇打开分享面板
        mShakeOpenShareBtn = (Button) root.findViewById(R.id.shake_openshare);
        mShakeOpenShareBtn.setOnClickListener(this);

        // 摇一摇截图、分享
        mShakeShareBtn = (Button) root.findViewById(R.id.shake_share);
        mShakeShareBtn.setOnClickListener(this);

        // 摇一摇截图
        mShakeScrShotBtn = (Button) root.findViewById(R.id.shake_scrshot);
        mShakeScrShotBtn.setOnClickListener(this);

        mGotoFacebookBtn = (Button) root.findViewById(R.id.goto_facebook_btn);
        mGotoFacebookBtn.setOnClickListener(this);

        mGotoLaiWangBtn = (Button) root.findViewById(R.id.goto_laiwang_btn);
        mGotoLaiWangBtn.setOnClickListener(this);

        mInstagramBtn = (Button) root.findViewById(R.id.share_by_instagram_btn);
        mInstagramBtn.setOnClickListener(this);

        root.findViewById(R.id.share_by_pocket).setOnClickListener(this);
        root.findViewById(R.id.share_by_linkedin).setOnClickListener(this);
        root.findViewById(R.id.share_by_ynote).setOnClickListener(this);
        root.findViewById(R.id.share_by_evernote_btn).setOnClickListener(this);
        root.findViewById(R.id.share_by_pinterest).setOnClickListener(this);
        return root;
    }

    /**
     * @Title: configSso
     * @Description: 配置sso授权Handler
     * @return void
     * @throws
     */
    private void setShareContent() {

        // 配置SSO
        mController.getConfig().setSsoHandler(new SinaSsoHandler());
        mController.getConfig().setSsoHandler(new TencentWBSsoHandler());

        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(getActivity(),
                "100424468", "c7394704798a158208a74ab60104f0ba");
        qZoneSsoHandler.addToSocialSDK();
        mController.setShareContent("友盟社会化组件（SDK）让移动应用快速整合社交分享功能");

        // APP ID：201874, API
        // * KEY：28401c0964f04a72a14c812d6132fcef, Secret
        // * Key：3bf66e42db1e4fa9829b955cc300b737.
        RenrenSsoHandler renrenSsoHandler = new RenrenSsoHandler(getActivity(),
                "201874", "28401c0964f04a72a14c812d6132fcef",
                "3bf66e42db1e4fa9829b955cc300b737");
        mController.getConfig().setSsoHandler(renrenSsoHandler);

        // 添加短信
        SmsHandler smsHandler = new SmsHandler();
        smsHandler.addToSocialSDK();

        // 添加email
        EmailHandler emailHandler = new EmailHandler();
        emailHandler.addToSocialSDK();


        UMImage localImage = new UMImage(getActivity(), R.drawable.device);
        UMImage urlImage = new UMImage(getActivity(),
                "http://www.umeng.com/images/pic/home/social/img-1.png");
        // UMImage resImage = new UMImage(getActivity(), R.drawable.icon);

        // 视频分享
        UMVideo video = new UMVideo(
                "http://v.youku.com/v_show/id_XNTc0ODM4OTM2.html");
        // vedio.setThumb("http://www.umeng.com/images/pic/home/social/img-1.png");
        video.setTitle("友盟社会化组件视频");
        video.setThumb(urlImage);

        UMusic uMusic = new UMusic(
                "http://music.huoxing.com/upload/20130330/1364651263157_1085.mp3");
        uMusic.setAuthor("umeng");
        uMusic.setTitle("天籁之音");
        uMusic.setThumb(urlImage);
        // uMusic.setThumb("http://www.umeng.com/images/pic/social/chart_1.png");

        WeiXinShareContent weixinContent = new WeiXinShareContent();
        weixinContent.setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能，微信");
        weixinContent.setTitle("友盟社会化分享组件-微信");
        weixinContent.setTargetUrl("http://www.umeng.com");
        weixinContent.setShareMedia(urlImage);
        mController.setShareMedia(weixinContent);

        // 设置朋友圈分享的内容
        CircleShareContent circleMedia = new CircleShareContent();
        circleMedia.setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能，朋友圈");
        circleMedia.setTitle("友盟社会化分享组件-朋友圈");
        circleMedia.setShareImage(urlImage);
//         circleMedia.setShareMedia(uMusic);
        // circleMedia.setShareMedia(video);
        circleMedia.setTargetUrl("http://www.umeng.com");
        mController.setShareMedia(circleMedia);


        // 设置renren分享内容
        RenrenShareContent renrenShareContent = new RenrenShareContent();
        renrenShareContent.setShareContent("人人分享内容");
        UMImage image = new UMImage(getActivity(),
                BitmapFactory.decodeResource(getResources(), R.drawable.device));
        image.setTitle("thumb title");
        image.setThumb("http://www.umeng.com/images/pic/social/integrated_3.png");
        renrenShareContent.setShareImage(image);
        renrenShareContent.setAppWebSite("http://www.umeng.com/social");
        mController.setShareMedia(renrenShareContent);

        UMImage qzoneImage = new UMImage(getActivity(),
                "http://www.umeng.com/images/pic/social/integrated_3.png");
        qzoneImage
                .setTargetUrl("http://www.umeng.com/images/pic/social/integrated_3.png");


        // 设置QQ空间分享内容
        QZoneShareContent qzone = new QZoneShareContent();
        qzone.setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能 -- QZone");
        qzone.setTargetUrl("http://www.umeng.com/social");
        qzone.setTitle("QZone title");
        qzone.setShareImage(urlImage);
        mController.setShareMedia(qzone);

        video.setThumb(new UMImage(getActivity(), BitmapFactory.decodeResource(
                getResources(), R.drawable.device)));

         QQShareContent qqShareContent = new QQShareContent();
         qqShareContent.setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能 -- QQ");
         qqShareContent.setTitle("hello, title");
         qqShareContent
         .setShareImage(urlImage);
         // qqShareContent.setShareMusic(uMusic);
         // qqShareContent.setShareVideo(video);
         qqShareContent.setTargetUrl("http://www.umeng.com/social");
         mController.setShareMedia(qqShareContent);

        // 视频分享
        UMVideo umVideo = new UMVideo(
                "http://v.youku.com/v_show/id_XNTc0ODM4OTM2.html");
        umVideo.setThumb("http://www.umeng.com/images/pic/home/social/img-1.png");
        umVideo.setTitle("友盟社会化组件视频");

        TencentWbShareContent tencent = new TencentWbShareContent();
        tencent.setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能，腾讯微博");
        // 设置tencent分享内容
        mController.setShareMedia(tencent);

        // 设置邮件分享内容， 如果需要分享图片则只支持本地图片
        MailShareContent mail = new MailShareContent(localImage);
        mail.setTitle("share form umeng social sdk");
        mail.setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能，email");
        // 设置tencent分享内容
        mController.setShareMedia(mail);

        // 设置短信分享内容
        SmsShareContent sms = new SmsShareContent();
        sms.setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能，短信");
        sms.setShareImage(urlImage);
        mController.setShareMedia(sms);

        SinaShareContent sinaContent = new SinaShareContent(urlImage);
        sinaContent.setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能，新浪微博");
        mController.setShareMedia(sinaContent);

        TwitterShareContent twitterShareContent = new TwitterShareContent();
        twitterShareContent
                .setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能，TWITTER");
        twitterShareContent.setShareMedia(localImage);
        mController.setShareMedia(twitterShareContent);

        GooglePlusShareContent googlePlusShareContent = new GooglePlusShareContent();
        googlePlusShareContent
                .setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能，G+");
        googlePlusShareContent.setShareMedia(localImage);
        mController.setShareMedia(googlePlusShareContent);
        
        // 来往分享内容
        LWShareContent lwShareContent = new LWShareContent();
//        lwShareContent.setShareImage(urlImage);
//        lwShareContent.setShareMedia(uMusic);
        lwShareContent.setShareMedia(umVideo);
        lwShareContent.setTitle("友盟社会化分享组件-来往");
        lwShareContent.setMessageFrom("友盟分享组件");
        lwShareContent.setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能，来往");
        mController.setShareMedia(lwShareContent);

        // 来往动态分享内容
        LWDynamicShareContent lwDynamicShareContent = new LWDynamicShareContent();
//        lwDynamicShareContent.setShareImage(urlImage);
//        lwDynamicShareContent.setShareMedia(uMusic);
        lwDynamicShareContent.setShareMedia(umVideo);
        lwDynamicShareContent.setTitle("友盟社会化分享组件-来往动态");
        lwDynamicShareContent.setMessageFrom("来自友盟");
        lwDynamicShareContent.setShareContent("来往动态分享测试");
        lwDynamicShareContent.setTargetUrl("http://www.umeng.com");
        mController.setShareMedia(lwDynamicShareContent);

        // mController.getConfig().supportAppPlatform(getActivity(),
        // SHARE_MEDIA.TWITTER, "com.umeng.share", true);
        // mController.getConfig().supportAppPlatform(getActivity(),
        // SHARE_MEDIA.GOOGLEPLUS, "com.umeng.share", true);

    }

    /**
     * @Title: onClick
     * @Description: 点击事件
     * @param v
     * @return void
     * @throws
     */
    @Override
    public void onClick(View v) {
        if (v == mWeiXinBtn) {
            // 添加微信支持, 并且打开平台选择面板
            addWXPlatform();
        } else if (v == mQQBtn) {
            // 添加QQ平台,并且打开平台选择面板
            addQQPlatform();

            // mController.doOauthVerify(getActivity(), SHARE_MEDIA.QQ,null);
        } else if (v == mShakeOpenShareBtn) {
            mController.setShareContent("友盟社会化组件（SDK）让移动应用快速整合社交分享功能");
            // 摇一摇打开分享面板
            mShakeController
                    .registerShakeToOpenShare(getActivity(), 2000, true);
        } else if (v == mShakeShareBtn) {
            // 注册摇一摇截图分享
            registerShakeToShare();
            Toast.makeText(getActivity(), "注册摇一摇分享", Toast.LENGTH_SHORT).show();
        } else if (v == mShakeScrShotBtn) {
            // 摇一摇截图
            mShakeController.registerShakeToScrShot(getActivity(),
                    new UMAppAdapter(getActivity()), 1500, true,
                    mScreenshotListener);
            Toast.makeText(getActivity(), "注册摇一摇截图", Toast.LENGTH_SHORT).show();
        } else if (v == mYiXinBtn) { // 添加易信平台
            // 添加易信平台
            addYXPlatform();
            mController.openShare(getActivity(), false);
        } else if (v == mGotoFacebookBtn) {
            addFacebook();
        } else if (v == mGotoLaiWangBtn) {
            // 添加来往平台的支持
            addLaiWang();
        } else if (v == mInstagramBtn) {
            // 添加Instagram平台的支持
            addInstagram();
        } else if (v.getId() == R.id.share_by_pocket) {
            addPocket();
        } else if (v.getId() == R.id.share_by_linkedin) {
            addLinkedIn();
        } else if (v.getId() == R.id.share_by_ynote) {
            addYNote();
        } else if (v.getId() == R.id.share_by_evernote_btn) {
            addEverNote();
        } else if (v.getId() == R.id.share_by_pinterest) {
            addPinterest();
        }
    }

    /**
     * Pocket分享。pockect只支持分享网络链接</br>
     */
    private void addPocket() {
        UMPocketHandler pocketHandler = new UMPocketHandler(getActivity());
        pocketHandler.addToSocialSDK();
        PocketShareContent pocketShareContent = new PocketShareContent();
        pocketShareContent.setShareContent("http://www.umeng.com/social");
        mController.setShareMedia(pocketShareContent);
        mController.openShare(getActivity(), false);
    }

    /**
     * LinkedIn分享。LinkedIn只支持图片，文本，图文分享</br>
     */
    private void addLinkedIn() {
        UMLinkedInHandler linkedInHandler = new UMLinkedInHandler(getActivity());
        linkedInHandler.addToSocialSDK();
        LinkedInShareContent linkedInShareContent = new LinkedInShareContent();
        linkedInShareContent
                .setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能，LinkedIn");
        mController.setShareMedia(linkedInShareContent);
        mController.openShare(getActivity(), false);
    }

    /**
     * 有道云笔记分享。有道云笔记只支持图片，文本，图文分享</br>
     */
    private void addYNote() {
        UMYNoteHandler yNoteHandler = new UMYNoteHandler(getActivity());
        yNoteHandler.addToSocialSDK();
        YNoteShareContent yNoteShareContent = new YNoteShareContent();
        yNoteShareContent
                .setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能，云有道笔记");
        yNoteShareContent.setTitle("友盟分享组件");
        yNoteShareContent.setShareImage(new UMImage(getActivity(), new File(
                "/storage/sdcard0/test12.png")));
        mController.setShareMedia(yNoteShareContent);
        mController.openShare(getActivity(), false);
    }

    /**
     * 添加印象笔记平台
     */
    private void addEverNote() {
        UMEvernoteHandler evernoteHandler = new UMEvernoteHandler(getActivity());
        evernoteHandler.addToSocialSDK();

        // 设置evernote的分享内容
        EvernoteShareContent shareContent = new EvernoteShareContent("印象笔记文本内容");
        shareContent.setShareMedia(new UMImage(getActivity(), R.drawable.test));
        mController.setShareMedia(shareContent);
        // 打开分享面板
        mController.openShare(getActivity(), new SnsPostListener() {

            @Override
            public void onStart() {

            }

            @Override
            public void onComplete(SHARE_MEDIA platform, int eCode,
                    SocializeEntity entity) {
                Toast.makeText(getActivity(), "分享成功", Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    /**
     * 添加Pinterest平台
     */
    private void addPinterest() {
        /**
         * app id需到pinterest开发网站( https://developers.pinterest.com/ )自行申请.
         */
        UMPinterestHandler pinterestHandler = new UMPinterestHandler(
                getActivity(), "1439206");
        pinterestHandler.addToSocialSDK();

        // 设置Pinterest的分享内容
        PinterestShareContent shareContent = new PinterestShareContent(
                "Pinterest文本内容");
        shareContent.setShareMedia(new UMImage(getActivity(), R.drawable.test));
        mController.setShareMedia(shareContent);
        // 打开分享面板
        mController.openShare(getActivity(), new SnsPostListener() {

            @Override
            public void onStart() {

            }

            @Override
            public void onComplete(SHARE_MEDIA platform, int eCode,
                    SocializeEntity entity) {
                Toast.makeText(getActivity(), "分享成功", Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    /**
     * 添加来往和来往动态平台</br>
     */
    private void addLaiWang() {

        // laiwangd497e70d4:来往appToken,d497e70d4c3e4efeab:来往secretID
        // 添加来往的支持
        UMLWHandler umlwHandler = new UMLWHandler(getActivity(), "", "");
        umlwHandler.addToSocialSDK();

        // 添加来往动态的支持
        UMLWHandler lwDynamicHandler = new UMLWHandler(getActivity(),
                "laiwangd497e70d4", "d497e70d4c3e4efeab1381476bac4c5e");
        lwDynamicHandler.setToCircle(true);
        lwDynamicHandler.addToSocialSDK();

        mController.openShare(getActivity(), false);

    }

    /**
     * @Title: registerShakeToShare
     * @Description:
     * @throws
     */
    private void registerShakeToShare() {
        /**
         * 摇一摇截图,直接分享 参数1: 当前所属的Activity 参数2: 截图适配器 参数3: 要用户可选的平台,最多支持五个平台 参数4:
         * 传感器监听器，包括摇一摇完成后的回调函数onActionComplete, 可在此执行类似于暂停游戏、视频等操作;
         * 还有分享完成、取消的回调函数onOauthComplete、onShareCancel。
         */
        UMAppAdapter appAdapter = new UMAppAdapter(getActivity());
        // 配置平台
        List<SHARE_MEDIA> platforms = new ArrayList<SHARE_MEDIA>();
        // 通过摇一摇控制器来设置文本分享内容
        mShakeController.setShareContent("美好瞬间，摇摇分享——来自友盟社会化组件");
        mShakeController.setShakeMsgType(ShakeMsgType.PLATFORM_SCRSHOT);

        mShakeController.registerShakeListender(getActivity(), appAdapter,
                2000, false, platforms, mSensorListener);
        mShakeController.enableShakeSound(false);
    }

    /**
     * 传感器监听器
     */
    private OnSensorListener mSensorListener = new OnSensorListener() {

        @Override
        public void onStart() {

        }

        /**
         * 分享完成后回调 (non-Javadoc)
         * 
         * @see com.umeng.socialize.controller.listener.SocializeListeners.DirectShareListener#onOauthComplete(java.lang.String,
         *      com.umeng.socialize.bean.SHARE_MEDIA)
         */
        @Override
        public void onComplete(SHARE_MEDIA platform, int eCode,
                SocializeEntity entity) {
            Toast.makeText(getActivity(), "分享完成 000000", Toast.LENGTH_SHORT)
                    .show();
        }

        /**
         * (非 Javadoc)
         * 
         * @Title: onActionComplete
         * @Description: 摇一摇动作完成后回调 (non-Javadoc)
         * @param event
         * @see com.umeng.socialize.sensor.UMSensor.OnSensorListener#onActionComplete(android.hardware.SensorEvent)
         */
        @Override
        public void onActionComplete(SensorEvent event) {
            Toast.makeText(getActivity(), "游戏暂停", Toast.LENGTH_SHORT).show();
        }

        /**
         * (非 Javadoc)
         * 
         * @Title: onButtonClick
         * @Description: 用户点击分享窗口的取消和分享按钮触发的回调
         * @param button
         * @see com.umeng.socialize.sensor.UMSensor.OnSensorListener#onButtonClick(com.umeng.socialize.sensor.UMSensor.WhitchButton)
         */
        @Override
        public void onButtonClick(WhitchButton button) {
            if (button == WhitchButton.BUTTON_CANCEL) {
                Toast.makeText(getActivity(), "取消分享,游戏重新开始", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // 分享中
            }
        }
    };

    /**
     * 截图监听器，返回屏幕截图
     */
    private OnScreenshotListener mScreenshotListener = new OnScreenshotListener() {

        @Override
        public void onComplete(Bitmap bmp) {
            if (bmp != null && mImageView != null) {
                mImageView.setImageBitmap(bmp);
            }
        }
    };

    /**
     * @功能描述 : 添加微信平台分享
     * @return
     */
    private void addWXPlatform() {

        // wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
        String appId = "wx967daebe835fbeac";
        // 添加微信平台
        UMWXHandler wxHandler = new UMWXHandler(getActivity(), appId);
        wxHandler.addToSocialSDK();

        // 支持微信朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(getActivity(), appId);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();
        mController.openShare(getActivity(), false);
        
    }

    /**
     * @功能描述 : 添加QQ平台支持 QQ分享的内容， 包含四种类型， 即单纯的文字、图片、音乐、视频. 参数说明 : title, summary,
     *       image url中必须至少设置一个, targetUrl必须设置,网页地址必须以"http://"开头 . title :
     *       要分享标题 summary : 要分享的文字概述 image url : 图片地址 [以上三个参数至少填写一个] targetUrl
     *       : 用户点击该分享时跳转到的目标地址 [必填] ( 若不填写则默认设置为友盟主页 )
     * @return
     */
    private void addQQPlatform() {
        // 添加QQ支持, 并且设置QQ分享内容的target url
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(getActivity(),
                "100424468", "c7394704798a158208a74ab60104f0ba");
        qqSsoHandler.setTargetUrl("http://www.umeng.com");
        qqSsoHandler.addToSocialSDK();
        mController.openShare(getActivity(), false);
    }

    /**
     * @Title: addYXPlatform
     * @Description:
     * @throws
     */
    private void addYXPlatform() {

        // 添加易信平台
        UMYXHandler yixinHandler = new UMYXHandler(getActivity(),
                "yxc0614e80c9304c11b0391514d09f13bf");
        // 关闭分享时的等待Dialog
        yixinHandler.enableLoadingDialog(false);
        // 设置target Url, 必须以http或者https开头
        yixinHandler.setTargetUrl("http://www.umeng.com");
        yixinHandler.addToSocialSDK();

        // 易信朋友圈平台
        UMYXHandler yxCircleHandler = new UMYXHandler(getActivity(),
                "yxc0614e80c9304c11b0391514d09f13bf");
        yxCircleHandler.setToCircle(true);
        yxCircleHandler.addToSocialSDK();

    }

    /**
     * @Title: addFacebook
     * @Description:
     * @throws
     */
    private void addFacebook() {

        // // 图片分享
        // UMImage mUMImgBitmap = new UMImage(getActivity(),
        // "/mnt/sdcard/test.jpg");

        UMImage mUMImgBitmap = new UMImage(getActivity(), R.drawable.test);
        mFacebookHandler = new UMFacebookHandler(getActivity(),
                "567261760019884", PostType.FEED);
        mFacebookHandler.addToSocialSDK();

        FaceBookShareContent fbContent = new FaceBookShareContent(
                "facebook 分享组件");
        fbContent.setShareImage(mUMImgBitmap);
        fbContent.setShareContent("This is my facebook social share sdk."
                + new Date().toString());
        fbContent.setTitle("Title - FB");
        fbContent.setCaption("Caption - Fb");
        fbContent.setDescription("独立拆分测试");
        fbContent.setTargetUrl("http://www.umeng.com/");
        mController.setShareMedia(fbContent);

        mController.setShareContent("facebook share");
        mController.setShareMedia(mUMImgBitmap);

        mController.openShare(getActivity(), false);
    }

    /**
     * </br> Instagram只支持图片分享, 只支持纯图片分享.</br>
     */
    private void addInstagram() {
        // 构建Instagram的Handler
        UMInstagramHandler instagramHandler = new UMInstagramHandler(
                getActivity());
        instagramHandler.addToSocialSDK();

        UMImage localImage = new UMImage(getActivity(), R.drawable.device);

        // // 添加分享到Instagram的内容
        InstagramShareContent instagramShareContent = new InstagramShareContent(
                localImage);
        mController.setShareMedia(instagramShareContent);
        // 打开分享面板
        mController.openShare(getActivity(), false);
        setPlatformOrder();
    }

    /**
     * @功能描述 : 自定义平台排序,.分享平台会按照参数传递的顺序来排列, 如果没有指定顺序，则默认排序
     */
    private void setPlatformOrder() {
        mController.getConfig().setPlatformOrder(SHARE_MEDIA.SINA,
                SHARE_MEDIA.QZONE, SHARE_MEDIA.QQ, SHARE_MEDIA.RENREN);
    }

    @Override
    public void onResume() {
        Log.d("", "fragment onResume");
        // 注册摇一摇截图分享
        registerShakeToShare();
        super.onResume();

    }

    @Override
    public void onStop() {
        Log.d("", "fragment onStop");
        mShakeController.unregisterShakeListener(getActivity());
        super.onStop();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("", "### onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
    }
}
