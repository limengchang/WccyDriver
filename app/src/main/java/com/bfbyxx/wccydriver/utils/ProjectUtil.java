package com.bfbyxx.wccydriver.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.bfbyxx.wccydriver.R;
import com.bfbyxx.wccydriver.wheel.AddressPickerView;
import com.mylhyl.circledialog.CircleDialog;
import com.mylhyl.circledialog.callback.ConfigButton;
import com.mylhyl.circledialog.callback.ConfigDialog;
import com.mylhyl.circledialog.params.ButtonParams;
import com.mylhyl.circledialog.params.DialogParams;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by kp
 * Date: 2019/4/25
 * Time: 9:58
 */
public class ProjectUtil {
    //版本名
    public static String getVersionName(Context context) {
        return getPackageInfo(context).versionName;
    }

    //版本号
    public static int getVersionCode(Context context) {
        return getPackageInfo(context).versionCode;
    }

    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;

        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);

            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pi;
    }

    /**
     * 判断是否是手机号码
     *
     * @param mobiles
     * @return
     */
    public static boolean isMobileNO(String mobiles) {
//        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9])|(14[0-9])|(17)[0-9])\\d{8}$");
//        Pattern p = Pattern.compile("^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\\d{8}$");
        // ^ 匹配输入字符串开始的位置
        // \d 匹配一个或多个数字，其中 \ 要转义，所以是 \\d
        // $ 匹配输入字符串结尾的位置
        String regExp = "^((13[0-9])|(14[5,7,9])|(15[0-3,5-9])|(166)|(17[3,5,6,7,8])" +
                "|(18[0-9])|(19[8,9]))\\d{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    public static void showUploadFileDialog(FragmentActivity context, AdapterView.OnItemClickListener onItemClickListener) {
        final String[] items = {"拍照", "从相册选择"};
        new CircleDialog.Builder(context)
                .configDialog(new ConfigDialog() {
                    @Override
                    public void onConfig(DialogParams params) {
                        //增加弹出动画
                        params.animStyle = android.R.style.Animation_Dialog;
                    }
                })
                .setItems(items, onItemClickListener)
                .setNegative("取消", null)
                .configNegative(new ConfigButton() {
                    @Override
                    public void onConfig(ButtonParams params) {
                        params.textColor = Color.RED;
                    }
                })
                .show();
    }

    /**
     * 显示吐司提示
     *
     * @param text
     * @param context
     */
    public static void show(Context context, String text) {
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }

    public static void getMsgShow(Context context, String data){
        Map<String,Object> map = HttpUtil.getMapForJson(data);
        if (map.get("msg")!=null){
            String str = map.get("msg").toString();
            Toast toast = Toast.makeText(context,str, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
        }
    }

    /**
     * 显示吐司提示
     *
     * @param text
     * @param context
     */
    public static void show1(Context context, String text, boolean b) {
        if (b) {
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 网络连接是否可用
     *
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnectedOrConnecting();
    }

    /**
     * 将px值转换为dip或dp值，保证尺寸大小不变
     *
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     *
     * @param dipValue
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 将字符串转成MD5值
     *
     * @param string
     * @return
     */
    public static String getMD5(String string) {
        byte[] hash;

        try {
            hash = MessageDigest.getInstance("MD5").digest(
                    string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10)
                hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }

        return hex.toString();
    }

    /**
     * 动态设置ListView的高度
     *
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        if (listView == null) return;
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    public static void checkCallPhone(Activity mActivity, String phone) {
        // 检查是否获得了权限（Android6.0运行时权限）
        if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
// 没有获得授权，申请授权
            if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.CALL_PHONE)) {
// 返回值：
//如果app之前请求过该权限,被用户拒绝, 这个方法就会返回true.
//如果用户之前拒绝权限的时候勾选了对话框中”Don’t ask again”的选项,那么这个方法会返回false.
//如果设备策略禁止应用拥有这条权限, 这个方法也返回false.
// 弹窗需要解释为何需要该权限，再次请求授权
                Toast.makeText(mActivity, "请授权！", Toast.LENGTH_LONG).show();
// 帮跳转到该应用的设置界面，让用户手动授权
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", mActivity.getPackageName(), null);
                intent.setData(uri);
                mActivity.startActivity(intent);
            } else {
// 不需要解释为何需要该权限，直接请求授权
                ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.CALL_PHONE}, 1);
            }
        } else {
// 已经获得授权，可以打电话
            CallPhone(mActivity, phone.replace("客服电话：","").trim());
        }


    }

    @SuppressLint("MissingPermission")
    private static void CallPhone(final Activity mActivity, final String phone) {

        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle("提示");
        builder.setMessage("您确定要拨打电话("+phone+")");

        builder.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builder.setPositiveButton("确认",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

//                        //跳转到拨号界面，同时传递电话号码
//                        Intent dialIntent =  new Intent(Intent.ACTION_DIAL,Uri.parse("tel:" + phone));
//                        mActivity.startActivity(dialIntent);

                        //直接拨打电话
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        Uri data = Uri.parse("tel:" + phone);
                        intent.setData(data);
                        mActivity.startActivity(intent);

                    }
                });

        builder.setCancelable(false);

        builder.show();
    }

    public static String nowTime(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return df.format(new Date());
    }

    //1.加载对话框
    public static ProgressDialog pd;
    public static void showProgress(Context context,String title){
        pd = new ProgressDialog(context);
        pd.setMessage(title);
        pd.setCancelable(false);
        if (pd != null && !pd.isShowing()) {
            pd.show();
        }
    }
    public static void dismissProgress() {
        if (pd != null && pd.isShowing()) {
            pd.dismiss();
        }
    }

    // 隐藏键盘
    private static void closeKeyboardHidden(Activity context) {
        View view = context.getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void showCityAddress(Context context,final Handler mHandler){
        //1、使用Dialog、设置style
        final Dialog dialog = new Dialog(context, R.style.DialogTheme);
        //2、设置布局
        View view = View.inflate(context,R.layout.pop_address_picker,null);
        dialog.setContentView(view);

        Window window = dialog.getWindow();
        //设置弹出位置
        window.setGravity(Gravity.BOTTOM);
        //设置弹出动画
        window.setWindowAnimations(R.style.AnimBottom);
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();

        AddressPickerView addressView = dialog.findViewById(R.id.apvAddress);
        addressView.setOnAddressPickerSure(new AddressPickerView.OnAddressPickerSureListener() {
            @Override
            public void onSureClick(String address, String provinceCode, String cityCode, String districtCode) {
                String code = provinceCode.replace("86","")+"\n"+cityCode.replace("86","")+"\n"+districtCode.replace("86","");
                String strCode = provinceCode;
                if (!TextUtils.isEmpty(districtCode)){
                    strCode = districtCode;
                }else if(!TextUtils.isEmpty(cityCode)){
                    strCode = cityCode;
                }else {
                    strCode = provinceCode;
                }
                Message msg = new Message();
                msg.what = 100;
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("address", address);
                map.put("alladdress", address + "\n"+code+"\n("+strCode.replace("86","")+")");
                msg.obj = map;
                mHandler.sendMessage(msg);
                dialog.dismiss();
            }
        });
        addressView.setOnAddressPickerCancel(new AddressPickerView.OnAddressPickerCancelListener() {
            @Override
            public void onSureClick() {
                dialog.dismiss();
            }
        });
    }

    /**
     * 点击分享的代码
     */
    @SuppressLint("NewApi")
    public static void shareMsg(Context context,boolean isWxOrQQ) {
        String packageName;
        String activityName;
        String appname;
        String msgTitle;
        String msgText;
        if (isWxOrQQ){
            //QQ
            appname = "QQ";
            msgTitle = "QQ分享";
            packageName = "com.tencent.mobileqq";
            activityName = "com.tencent.mobileqq.activity.JumpActivity";
            msgText = "无车承运-司机端：来自QQ分享";
        }else {
            //微信
            appname = "微信";
            msgTitle = "微信分享";
            packageName = "com.tencent.mm";
            activityName = "com.tencent.mm.ui.tools.ShareImgUI";
            msgText = "无车承运-司机端：来自微信分享";
        }
        if (!packageName.isEmpty() && !isAvilible(context, packageName)) {// 判断APP是否存在
            Toast.makeText(context, "请先安装" + appname, Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        Intent intent = new Intent("android.intent.action.SEND");
//        if (type == 0) {
//            intent.setType("text/plain");
//        } else if (type == 1) {
//            intent.setType("image/*");
////          BitmapDrawable bd = (BitmapDrawable) drawable;
////          Bitmap bt = bd.getBitmap();
//            final Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(
//                    context.getContentResolver(), drawable, null, null));
//            intent.putExtra(Intent.EXTRA_STREAM, uri);
//        }
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, msgTitle);
        intent.putExtra(Intent.EXTRA_TEXT, msgText);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (!packageName.isEmpty()) {
            intent.setComponent(new ComponentName(packageName, activityName));
            context.startActivity(intent);
        } else {
            context.startActivity(Intent.createChooser(intent, msgTitle));
        }
    }

    /**
     * 判断相对应的APP是否存在
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isAvilible(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();

        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        for (int i = 0; i < pinfo.size(); i++) {
            if (((PackageInfo) pinfo.get(i)).packageName
                    .equalsIgnoreCase(packageName))
                return true;
        }
        return false;
    }
}

