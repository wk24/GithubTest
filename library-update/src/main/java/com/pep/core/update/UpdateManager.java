package com.pep.core.update;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;

import com.downloader.Error;
import com.downloader.OnDownloadListener;
import com.downloader.OnProgressListener;
import com.downloader.Progress;
import com.pep.core.libcommon.PermissionUtils;
import com.pep.core.libnet.PEPDownloadManager;
import com.pep.core.libnet.PEPHttpManager;
import com.pep.core.update.bean.UpdateBaseData;
import com.pep.core.update.http.ApiServiceUpdate;
import com.pep.core.update.view.NumberProgressBar;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author wuk
 * @date 2020/1/9
 */
public class UpdateManager {

    private Context mContext;
    private String mBaseUrl;
    private String mVersionName;
    private String mClientType;
    //产品ID 智慧教学客户端 11120101  智慧教学平台-上海项目 111301 人教点读移动端 1213 人教数字教材-河北 17101
    private String mProductId;

    private NumberProgressBar bnp;
    Dialog dialog;
    private String isForce ;
    private View view;
    //确认更新
    private TextView tvConfirm;
    //版本
    private TextView tvVersion ;
    //取消更新
    private TextView tvCancel ;
    //更新内容
    private TextView tvContent ;
    //发布日期
    private TextView versionDate ;
    //文件大小
    private TextView versionSize ;
    private View vv1;
    private LinearLayout llResult ;
    //抬头图片
    private ImageView updateImage;
    //更新信息
    private LinearLayout updateMessage;
    private LinearLayout llProgressbar;
    private boolean isVisible = false;
    private CheckUpdateCallback mCallback;

    public UpdateManager(Context context, String baseUrl, String mProductId, String clientType) {
        this.mContext = context;
        this.mBaseUrl = baseUrl;
        this.mProductId = mProductId;
        this.mClientType = clientType;
    }

    public UpdateManager(Context context, String baseUrl, String mProductId, String clientType, String versionName) {
        this.mContext = context;
        this.mBaseUrl = baseUrl;
        this.mProductId = mProductId;
        this.mClientType = clientType;
        this.mVersionName = versionName;
    }

    /**
     * 更新
     */
    public void update() {
        getUpDate(true);
    }

    /**
     * 检查更新
     * @param callback
     */
    public void checkUpdate(CheckUpdateCallback callback){
        mCallback = callback;
        getUpDate(false);
    }

    /**
     * 获取跟新数据
     * @param isUpdate true:更新  false:检查是否需要更新
     */
    private void getUpDate(final boolean isUpdate) {
        HashMap<String, Object> bookListParams = new HashMap<>(3);
        bookListParams.put("client_version", mVersionName);
        bookListParams.put("product_id", mProductId);
        bookListParams.put("client_type", mClientType);
        PEPHttpManager.getInstance().getService(ApiServiceUpdate.class).getUpdateInfo(mBaseUrl+"/api/a/release/client/versionCheck/",bookListParams).enqueue(new Callback<UpdateBaseData>() {
            @Override
            public void onResponse(Call<UpdateBaseData> call, Response<UpdateBaseData> response) {
                UpdateBaseData updateBaseData = response.body();
                if (isUpdate){
                    if (updateBaseData != null && updateBaseData.getDataMap() != null) {
                        if (updateBaseData.getDataMap().getIs_current()!=null && updateBaseData.getDataMap().getIs_current().equals("1") && isVisible){
                            Toast.makeText(mContext, "已是最新版本", Toast.LENGTH_SHORT).show();
                        }
                        if (updateBaseData != null && !checkIsNew(updateBaseData.getDataMap().getVersion())) {
                            showNoticeDialog(updateBaseData.getDataMap().getVersion() + "", updateBaseData.getDataMap().getDescription(), updateBaseData.getDataMap().getPackage_url(), updateBaseData.getDataMap().getIs_coerce());
                        }
                    }
                }else{
                    if (mCallback !=null){
                        if (updateBaseData != null && updateBaseData.getDataMap() != null) {
                            if (updateBaseData != null && !checkIsNew(updateBaseData.getDataMap().getVersion())) {
                                mCallback.onResult(true);
                                return;
                            }
                        }
                        mCallback.onResult(false);
                    }
                }
            }

            @Override
            public void onFailure(Call<UpdateBaseData> call, Throwable t) {
                if (mCallback !=null){
                    mCallback.onResult(false);
                }
            }
        });
    }

    private void initDialogView() {
        view = LayoutInflater.from(mContext).inflate(R.layout.dialog_update_verison, null);
        llProgressbar = (LinearLayout) view.findViewById(R.id.ll_progressbar);
        tvConfirm = (TextView) view.findViewById(R.id.tv_confirm);
        tvVersion = (TextView) view.findViewById(R.id.tv_version);
        tvCancel = (TextView) view.findViewById(R.id.tv_cancel);
        tvContent = (TextView) view.findViewById(R.id.tv_content);
        versionDate = (TextView) view.findViewById(R.id.version_date);
        versionSize = (TextView) view.findViewById(R.id.version_size);
        llResult = (LinearLayout) view.findViewById(R.id.ll_result);
        updateImage = view.findViewById(R.id.update_image);
        updateMessage = (LinearLayout) view.findViewById(R.id.update_message);
        bnp = (NumberProgressBar) view.findViewById(R.id.number_bar);
        vv1 =  view.findViewById(R.id.view_);
    }

    /**
     * 显示软件更新对话框
     */
    public void showNoticeDialog(String version, String content, final String url, final String isForce) {
        this.isForce = isForce;
        initDialogView();
        tvContent.setText(content);
        tvVersion.setText("版本：" + version);
        versionDate.setVisibility(View.GONE);
        versionSize.setVisibility(View.GONE);
        // 设置style 控制默认dialog带来的边距问题
        dialog = new Dialog(mContext, R.style.common_dialog);
        dialog.setContentView(view);
        dialog.setCancelable(false);
        dialog.show();
        if (isForce.equals("1")){
            tvCancel.setVisibility(View.GONE);
            vv1.setVisibility(View.GONE);
        }
        // 监听
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = v.getId();
                if (i == R.id.tv_cancel) {
                    dialog.dismiss();
                } else if (i == R.id.tv_confirm) {
                    downLoadApk(updateMessage, llProgressbar, tvConfirm, url);
                }
            }
        };

        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK){
                    if (!isForce.equals("1")){
                        dialog.dismiss();
                    }
                }
                return false;
            }
        });

        tvCancel.setOnClickListener(listener);
        tvConfirm.setOnClickListener(listener);
        // 设置相关位置，一定要在 show()之后
        Window window = dialog.getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
    }

    /**
     * 下载Apk文件
     * @param update_message
     * @param ll_progressbar
     * @param tv_confirm
     * @param url
     */
    private void downLoadApk(LinearLayout update_message, LinearLayout ll_progressbar, final TextView tv_confirm, String url) {
        boolean granted = PermissionUtils.isGranted(mContext,"android.permission.WRITE_EXTERNAL_STORAGE");
        if (granted){
            //已授权, 进行具体操作
            update_message.setVisibility(View.GONE);
            ll_progressbar.setVisibility(View.VISIBLE);
            tv_confirm.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onClick(View v) {
                    if (tv_confirm.getText().equals("安装")){
                        File f = Environment.getExternalStorageDirectory();
                        final String path = f.getPath() + "/pep/pep.apk";
                        checkIsAndroidO(path);
                    }else {
                        Toast.makeText(mContext, "App正在下载", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            downloadFile(update_message,ll_progressbar,tv_confirm,url);
        }else{
            PermissionUtils.permission(mContext,"android.permission.WRITE_EXTERNAL_STORAGE")
                    .callback(new PermissionUtils.FullCallback() {
                        @Override
                        public void onGranted(List<String> permissionsGranted) {

                        }

                        @Override
                        public void onDenied(List<String> permissionsDeniedForever, List<String> permissionsDenied) {
                            if (permissionsDeniedForever.size()>0){
                                // permissionsDeniedForever: 禁止后不再提示的list, 此处可以打开一个dialog,让用户进行确认
                                Toast.makeText(mContext,"必须打开"+permissionsDeniedForever.get(0)+"授权,软件才能正常使用",Toast.LENGTH_SHORT).show();
                                PermissionUtils.launchAppDetailsSettings(mContext);
                            }
                        }
                    }).request();
        }
    }

    public void downloadFile(LinearLayout update_message, LinearLayout ll_progressbar, final TextView tv_confirm, String url){
        final AtomicLong startTime = new AtomicLong(System.currentTimeMillis());
        //获取SD卡目录
        File f = Environment.getExternalStorageDirectory();
        final String path = f.getPath() + "/pep/pep.apk";
        PEPDownloadManager.downLoad(url, f.getPath() + "/pep/", "pep.apk").setOnProgressListener(new OnProgressListener() {
            @Override
            public void onProgress(Progress progress) {
                long endTime = System.currentTimeMillis();
                if (endTime - startTime.get() > 200) {
                    int currentProgress = (int) (((double) progress.currentBytes / (double) progress.totalBytes) * 100);
                    bnp.setProgress(currentProgress);
                    startTime.set(endTime);
                }
            }
        }).start(new OnDownloadListener() {
            @Override
            public void onDownloadComplete() {
                try{
                    bnp.setProgress(100);
                    tv_confirm.setText("安装");
                    checkIsAndroidO(path);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Error error) {
                try{
                    Toast.makeText(mContext,"安装包下载失败,请稍后再试",Toast.LENGTH_SHORT).show();
                    if (!isForce.equals("1")){
                        dialog.dismiss();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 获取版本号
     * @return
     */
    private String getVersion() {
        if(mVersionName == null){
            mVersionName = getPackageInfo(mContext).versionName;
        }
        return mVersionName;
    }

    /**
     * 校验版本号
     * @param version
     * @return
     */
    private boolean checkIsNew(String version) {
        return version.equals(getVersion());
    }

    //通过PackageInfo得到的想要启动的应用的包名
    private PackageInfo getPackageInfo(Context context) {
        PackageInfo pInfo = null;

        try {
            //通过PackageManager可以得到PackageInfo
            PackageManager pManager = context.getPackageManager();
            pInfo = pManager.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);

            return pInfo;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pInfo;
    }

    private void checkIsAndroidO(final String apkPath) {
        if (android.os.Build.VERSION.SDK_INT >= 26) {
            boolean hasInstallPermissions = mContext.getPackageManager().canRequestPackageInstalls();
            if (hasInstallPermissions) {
                installApk(apkPath);
            } else {
                //请求安装未知应用来源的权限
                PermissionUtils.permission(mContext,"android.permission.REQUEST_INSTALL_PACKAGES")
                        .callback(new PermissionUtils.FullCallback() {
                            @Override
                            public void onGranted(List<String> permissionsGranted) {

                            }

                            @Override
                            public void onDenied(List<String> permissionsDeniedForever, List<String> permissionsDenied) {
                                if (permissionsDeniedForever.size()>0 || permissionsDenied.size()>0){
                                    Toast.makeText(mContext,"请允许安装应用",Toast.LENGTH_SHORT).show();
                                    //跳转到授予安装未知来源应用开关界面
                                    Uri uri = Uri.parse("package:"+mContext.getPackageName());
                                    Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES,uri);
                                    mContext.startActivity(intent);
                                }
                            }
                        }).request();
            }
        } else {
            installApk(apkPath);
        }
    }

    /**
     * 安装apk
     */
    private void installApk(String apkPath) {
        File file = getFileByPath(apkPath);
        Intent intent = new Intent();
        //intent.setDataAndType(Uri.fromFile(new File(String.valueOf(apkPath))), "application/vnd.android.package-archive");
        // 由于没有在Activity环境下启动Activity,设置下面的标签
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);

        if (Build.VERSION.SDK_INT >= 24){
            Uri apkUri =
                    FileProvider.getUriForFile(mContext, getFileProviderName(mContext), file);
            mContext.grantUriPermission(mContext.getPackageName(),apkUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri,"application/vnd.android.package-archive");
        }else {
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
        }
        mContext.startActivity(intent);
    }

    private String getFileProviderName(Context context){
        return context.getPackageName()+".myfileprovider.pep";
    }

    private static File getFileByPath(final String filePath) {
        return isSpace(filePath) ? null : new File(filePath);
    }

    private static boolean isSpace(final String s) {
        if (s == null){
            return true;
        }
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 设置toast是否可见
     * @param isShowToast
     */
    public void isShowToast(boolean isShowToast){
        isVisible = isShowToast;
    }
}
