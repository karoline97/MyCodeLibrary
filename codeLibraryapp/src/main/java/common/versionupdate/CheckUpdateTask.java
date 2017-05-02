package common.versionupdate;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.karoline.BuildConfig;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/7/28.
 */
public class CheckUpdateTask extends AsyncTask<String, Void, VersionData> {
    private final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private String versionUrl = "http://120.26.129.53:8080/commonplatform/rest/version/getCurrentVersion";
    private ProgressDialog dialog;
    private boolean mShowProgressDialog;
    private VersionData versionData = null;
    private Context mContext;
    private UpdateListener listener;
    private int Mode =0;//mode=1,必须下载，mode=2,可以下载

    private static String account_sid,auth_token,app_code;

    public CheckUpdateTask(Context mContext,boolean showProgressDialog,UpdateListener listener) {
        this.mContext = mContext;
        this.mShowProgressDialog = showProgressDialog;
        this.listener = listener;
    }

    public static void setAccountInfo(String accountsid,String authtoken,String appcode){
        account_sid = accountsid;
        auth_token = authtoken;
        app_code = appcode;
    }

    @Override
    protected VersionData doInBackground(String... params) {
        if(TextUtils.isEmpty(account_sid) || TextUtils.isEmpty(auth_token) || TextUtils.isEmpty(app_code)){
            listener.VersionUpdateFail("请设置账号信息");
        }
        VersionReq versionReq = new VersionReq("android","prod");
        try{
            if(!TextUtils.isEmpty(params[0])){
                versionReq.setOs(params[0]);
            }
            if(!TextUtils.isEmpty(params[1])){
                versionReq.setEnv(params[1]);
            }
        }catch (ArrayIndexOutOfBoundsException e){
            e.printStackTrace();
        }

        getClient()
                .newCall(getRequestWithBody(versionUrl,new JsonUtils().versionReqToJson(versionReq)))
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                        listener.VersionUpdateFail("没有网络连接");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        versionData = new JsonUtils().jsonToVersionData(response.body().string());
                        if(versionData.getStatus().equals("ok")){
                            if(versionData.getResult().getStatusCode().equals("000000")){
                                isDownLoadApk(versionData);
                            }else {
                                listener.VersionUpdateFail(versionData.getResult().getStatusMsg());
                            }
                        }else{
                            listener.VersionUpdateFail(versionData.getResult().getStatusMsg());
                        }
                    }
                });
        return versionData;
    }

    @Override
    protected void onPreExecute() {
        if (mShowProgressDialog) {
            dialog = new ProgressDialog(mContext);
            //  dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            dialog.setMessage("自动更新检测......");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }
    }


    @Override
    protected void onPostExecute(VersionData status) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    private void isDownLoadApk(VersionData status) {
        if (mContext != null) {
            String versionCode = BuildConfig.VERSION_NAME;
            try {
                if (CompareUtils.compareVersion(status.getResult().getData().getVersion(), versionCode) > 0
                        && CompareUtils.compareVersion(status.getResult().getData().getMinVersion(), versionCode) <= 0) {
                    Mode = 2;
                    showDownLoadSelectDialog(status.getResult().getData().getDownloadUrl());
                }else if(CompareUtils.compareVersion(status.getResult().getData().getMinVersion(), versionCode)>0){
                    Mode = 1;
                    goToDownload(status);
                }else{
                    Mode = 2;
                    listener.noUpdate();
                }
            } catch (Exception e) {
                e.printStackTrace();
                listener.VersionUpdateFail("版本比较失败");
            }
        }
    }


    private int  totalMount = 0;
    @TargetApi(17)
    private void goToDownload(final VersionData status){
        ((Activity)mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String downloadUrl = status.getResult().getData().getDownloadUrl();
                final ProgressDialog mProgressDialog = new ProgressDialog(mContext);
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                mProgressDialog.setTitle(status.getResult().getData().getDescription());
                //mProgressDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_DIALOG);
                mProgressDialog.setCanceledOnTouchOutside(false);
                mProgressDialog.setCancelable(false);
                if (mContext != null && !mProgressDialog.isShowing() &&
                        !((Activity)mContext).isFinishing()) {
                    mProgressDialog.show();

                    downLoadNewApk(new DownLoadProgressListener() {
                        @Override
                        public void onTotalCount(final int mount) {
                            totalMount = mount;
                            mProgressDialog.setMax(100);
                        }

                        @Override
                        public void onProgressChanged(final int downloaded) {
                            int per = totalMount/100;
                            mProgressDialog.setProgress(downloaded/per);
                        }

                        @Override
                        public void onDownloadFailed() {
                            mProgressDialog.dismiss();
                            downloadFail();
                        }

                        @Override
                        public void onDownloadFinished(File apkFile) {
                            mProgressDialog.dismiss();
                            //listener.VersionUpdateSuccess();
                            downloadsuccess(apkFile);
                        }
                    });
                }
            }
        });


        /*DownloadManager downloadManager = (DownloadManager)mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downloadUrl));
        request.setTitle(status.getResult().getData().getAppName());
        request.setDescription(status.getResult().getData().getDescription());
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        long downloadID = downloadManager.enqueue(request);*/
    }

    public void downLoadNewApk(final DownLoadProgressListener listener) {

        if (listener == null) {
        }

        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            listener.onDownloadFailed();
        }

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(versionData.getResult().getData().getDownloadUrl());
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    connection.setConnectTimeout(5000);

                    if (listener != null) {
                        listener.onTotalCount(connection.getContentLength());
                    }
                    String apkName = versionData.getResult().getData().getDownloadUrl()
                            .substring(versionData.getResult().getData().getDownloadUrl().lastIndexOf("/") + 1,
                                    versionData.getResult().getData().getDownloadUrl().length());
                    File apkFile = new File(Environment.getExternalStorageDirectory(), apkName);
                    InputStream inputStream = connection.getInputStream();

                    FileOutputStream fileOutputStream = new FileOutputStream(apkFile);

                    BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);

                    byte[] buffer = new byte[1024];

                    int readLen = 0;
                    int downloaded = 0;

                    while ((readLen = bufferedInputStream.read(buffer)) != -1) {
                        fileOutputStream.write(buffer, 0, readLen);
                        downloaded += readLen;
                        if (listener != null) {
                            listener.onProgressChanged(downloaded);
                        }
                    }

                    bufferedInputStream.close();
                    fileOutputStream.close();
                    inputStream.close();
                    connection.disconnect();
                    if (listener != null) {
                        listener.onDownloadFinished(apkFile);
                    }
                } catch (IOException e) {
                    listener.onDownloadFailed();
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    private void downloadFail() {
        ((Activity)mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = null;
                builder = new AlertDialog.Builder(mContext);
                builder.setTitle("下载失败");
                builder.setMessage("重新下载")
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                        goToDownload(versionData);

                                    }
                                })
                        .setNegativeButton("取消",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                        if(Mode ==1){
                                            listener.VersionUpdateFail("取消下载");
                                        }else if(Mode == 2){
                                            listener.noUpdate();
                                        }
                                    }
                                });
                AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(false);
                // dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                dialog.show();
            }
        });
    }

    private void showDownLoadSelectDialog(String url){
        ((Activity)mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = null;
                builder = new AlertDialog.Builder(mContext);
                builder.setTitle("发现新版本，是否更新？");
                builder.setMessage(versionData.getResult().getData().getDescription())
                        .setPositiveButton("现在更新",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                        goToDownload(versionData);

                                    }
                                })
                        .setNegativeButton("暂不更新",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                        listener.noUpdate();
                                    }
                                });
                AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            }
        });
    }

    private void downloadsuccess(File apkFile) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setDataAndType(Uri.fromFile(apkFile),
                "application/vnd.android.package-archive");
        mContext.startActivity(intent);
    }

    private OkHttpClient getClient(){
        return new OkHttpClient.Builder()
                .connectTimeout(10,TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();
    }

    private Request getRequestWithBody(String url, String jsonBody) throws NullPointerException {
        Log.d("APIClient",url+jsonBody);
        return new Request.Builder()
                .url(url)
                .addHeader("account_sid",account_sid)
                .addHeader("auth_token",auth_token)
                .addHeader("app_code",app_code)
                .post(RequestBody.create(JSON, jsonBody))
                .build();
    }

    public interface UpdateListener{
        void VersionUpdateFail(String msg);
        //void VersionUpdateSuccess();
        void noUpdate();
    }

}
