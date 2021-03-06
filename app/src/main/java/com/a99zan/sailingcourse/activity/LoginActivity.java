package com.a99zan.sailingcourse.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.a99zan.sailingcourse.R;
import com.a99zan.sailingcourse.utils.GetPathFromUri4kitkat;
import com.a99zan.sailingcourse.utils.PickPhotoUtil;

import java.io.File;

public class LoginActivity extends BaseActivity {

    private static final String TAG = "LoginActivity";

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
        initPermissionForCamera();

    }

    /**
     * Android 6.0以上版本，需求添加运行时权限申请；否则，可能程序崩溃
     */
    private static final int REQUEST_CODE_PERMISSION = 0x110;
    private void initPermissionForCamera() {
        int flag = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int flag1 = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (PackageManager.PERMISSION_GRANTED != flag||PackageManager.PERMISSION_GRANTED != flag1) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (REQUEST_CODE_PERMISSION == requestCode) {
            switch (grantResults[0]) {
                case PackageManager.PERMISSION_DENIED:
                    boolean isSecondRequest = ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA);
                    boolean isSecondRequest1 = ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    if (isSecondRequest||isSecondRequest1)
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION);
                    else
                        Toast.makeText(this, "拍照或SD卡权限被禁用，请在权限管理修改", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    private void init() {

        webView = (WebView) findViewById(R.id.webview);

        //声明WebSettings子类
        WebSettings webSettings = webView.getSettings();

        /**
         * 缓存策略
         */
//        webView.setCacheStrategy(WebViewCache.CacheStrategy.FORCE);
////        webView.setBlockNetworkImage(true);
//        webView.setEnableCache(true);

        //支持插件
//        webSettings.setPluginsEnabled(true);

        //开启 database storage API 功能
        webSettings.setDatabaseEnabled(true);
        String cacheDirPath = getFilesDir().getAbsolutePath() + "cache";
        //设置数据库缓存路径
        webSettings.setDatabasePath(cacheDirPath);
        //设置  Application Caches 缓存目录
        webSettings.setAppCachePath(cacheDirPath);
        //开启 Application Caches 功能
        webSettings.setAppCacheEnabled(true);

        //支持javascript
        webSettings.setJavaScriptEnabled(true);
        //扩大比例的缩webSettings
        webSettings.setUseWideViewPort(true);
        //自适应屏幕 webSettings
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setLoadWithOverviewMode(true);

        //缩放操作
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件

        // 开启 DOM storage API 功能
        webSettings.setDomStorageEnabled(true);

        webSettings.setAllowContentAccess(true); // 是否可访问Content Provider的资源，默认值 true
        webSettings.setAllowFileAccess(true);    // 是否可访问本地文件，默认值 true

        //其他细节操作
//        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT); //webview中缓存模式
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式

        webView.loadUrl("http://bm.99zan.vip/web/index");
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                view.loadUrl(url);
//                Log.e("111", url + "");
//                return true;
                //Android8.0以下的需要返回true 并且需要loadUrl；8.0之后效果相反
                if(Build.VERSION.SDK_INT<26) {
                    view.loadUrl(url);
                    return true;
                }
                return false;
            }
        });

        webView.setWebChromeClient(new MyWebChromeClient(new PickPhotoUtil(LoginActivity.this)){
            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                AlertDialog.Builder b = new AlertDialog.Builder(LoginActivity.this);
                b.setTitle("提示");
                b.setMessage(message);
                b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.confirm();
                    }
                });
                b.setCancelable(false);
                b.create().show();
                return true;
            }

            //设置响应js 的Confirm()函数
            @Override
            public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
                AlertDialog.Builder b = new AlertDialog.Builder(LoginActivity.this);
                b.setTitle("Confirm");
                b.setMessage(message);
                b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.confirm();
                    }
                });
                b.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.cancel();
                    }
                });
                b.create().show();
                return true;
            }
            //设置响应js 的Prompt()函数
//            @Override
//            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, final JsPromptResult result) {
//                final View v = View.inflate(TestAlertActivity.this, R.layout.prompt_dialog, null);
//                ((TextView) v.findViewById(R.id.prompt_message_text)).setText(message);
//                ((EditText) v.findViewById(R.id.prompt_input_field)).setText(defaultValue);
//                AlertDialog.Builder b = new AlertDialog.Builder(TestAlertActivity.this);
//                b.setTitle("Prompt");
//                b.setView(v);
//                b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        String value = ((EditText) v.findViewById(R.id.prompt_input_field)).getText().toString();
//                        result.confirm(value);
//                    }
//                });
//                b.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        result.cancel();
//                    }
//                });
//                b.create().show();
//                return true;
//            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            Log.e("1111", "2222");
            return true;
        }
        Log.e("1111", "3333"+webView.canGoBack());
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webView.clearHistory();

            ((ViewGroup) webView.getParent()).removeView(webView);
            webView.destroy();
            webView = null;
        }
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**
         * 处理页面返回或取消选择结果
         */
        switch (requestCode) {
            case PickPhotoUtil.REQUEST_FILE_PICKER:
                pickPhotoResult(resultCode, data);
                break;
            case PickPhotoUtil.REQUEST_CODE_PICK_PHOTO:
                pickPhotoResult(resultCode, data);
                break;
            case PickPhotoUtil.REQUEST_CODE_TAKE_PHOTO:
                takePhotoResult(resultCode);
                break;
            case PickPhotoUtil.REQUEST_CODE_PREVIEW_PHOTO:
                cancelFilePathCallback();
                break;
            default:

                break;
        }
    }

    private void pickPhotoResult(int resultCode, Intent data) {
        if (PickPhotoUtil.mFilePathCallback != null) {
            Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
            if (result != null) {
                String path = GetPathFromUri4kitkat.getPath(this, result);
                Uri uri = Uri.fromFile(new File(path));
                PickPhotoUtil.mFilePathCallback.onReceiveValue(new Uri[]{uri});
                /**
                 * 将路径赋值给常量photoFile4，记录第一张上传照片路径
                 */
                PickPhotoUtil.photoPath = path;

                Log.d(TAG, "onActivityResult: " + path);
            } else {
                /**
                 * 点击了file按钮，必须有一个返回值，否则会卡死
                 */
                PickPhotoUtil.mFilePathCallback.onReceiveValue(null);
                PickPhotoUtil.mFilePathCallback = null;
            }
            /**
             * 针对API 19之前的版本
             */
        } else if (PickPhotoUtil.mFilePathCallback4 != null) {
            Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
            if (result != null) {
                String path = GetPathFromUri4kitkat.getPath(this, result);
                Uri uri = Uri.fromFile(new File(path));
                PickPhotoUtil.mFilePathCallback4.onReceiveValue(uri);
                /**
                 * 将路径赋值给常量photoFile
                 */
                Log.d(TAG, "onActivityResult: " + path);
            } else {
                /**
                 * 点击了file按钮，必须有一个返回值，否则会卡死
                 */
                PickPhotoUtil.mFilePathCallback4.onReceiveValue(null);
                PickPhotoUtil.mFilePathCallback4 = null;
            }
        }
    }

    private void takePhotoResult(int resultCode) {
        if (PickPhotoUtil.mFilePathCallback != null) {
            if (resultCode == RESULT_OK) {
                String path = PickPhotoUtil.photoPath;
                Uri uri = Uri.fromFile(new File(path));
                PickPhotoUtil.mFilePathCallback.onReceiveValue(new Uri[]{uri});

                Log.d(TAG, "onActivityResult: " + path);
            } else {
                /**
                 * 点击了file按钮，必须有一个返回值，否则会卡死
                 */
                PickPhotoUtil.mFilePathCallback.onReceiveValue(null);
                PickPhotoUtil.mFilePathCallback = null;
            }
            /**
             * 针对API 19之前的版本
             */
        } else if (PickPhotoUtil.mFilePathCallback4 != null) {
            if (resultCode == RESULT_OK) {
                String path = PickPhotoUtil.photoPath;
                Uri uri = Uri.fromFile(new File(path));
                PickPhotoUtil.mFilePathCallback4.onReceiveValue(uri);

                Log.d(TAG, "onActivityResult: " + path);
            } else {
                /**
                 * 点击了file按钮，必须有一个返回值，否则会卡死
                 */
                PickPhotoUtil.mFilePathCallback4.onReceiveValue(null);
                PickPhotoUtil.mFilePathCallback4 = null;
            }
        }
    }

    private void cancelFilePathCallback() {
        if (PickPhotoUtil.mFilePathCallback4 != null) {
            PickPhotoUtil.mFilePathCallback4.onReceiveValue(null);
            PickPhotoUtil.mFilePathCallback4 = null;
        } else if (PickPhotoUtil.mFilePathCallback != null) {
            PickPhotoUtil.mFilePathCallback.onReceiveValue(null);
            PickPhotoUtil.mFilePathCallback = null;
        }
    }

}
