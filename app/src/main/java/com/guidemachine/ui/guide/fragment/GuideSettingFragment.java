package com.guidemachine.ui.guide.fragment;


import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.guidemachine.R;
import com.guidemachine.base.ui.BaseFragment;
import com.guidemachine.ui.guide.EmergencyContactActivity;
import com.guidemachine.ui.guide.EmergencyLinkListActivity;
import com.guidemachine.ui.guide.GuideJobModeActivity;
import com.guidemachine.ui.guide.GuideModifyPwdActivity;
import com.guidemachine.ui.view.CustomClearCacheDialog;
import com.guidemachine.ui.view.CustomLogOutDialog;
import com.guidemachine.ui.view.CustomPowerOffDialog;
import com.guidemachine.util.IntentUtils;
import com.guidemachine.util.StatusBarUtils;
import com.guidemachine.util.ToastUtils;

import java.io.File;
import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 导游设置
 */
public class GuideSettingFragment extends BaseFragment implements View.OnClickListener, CustomClearCacheDialog.ClearCache {

    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.tv_title_center)
    TextView tvTitleCenter;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.ll_job_mode)
    LinearLayout llJobMode;
    @BindView(R.id.ll_first)
    LinearLayout llFirst;
    @BindView(R.id.ll_second)
    LinearLayout llSecond;
    @BindView(R.id.ll_emergency_contact)
    LinearLayout llEmergencyContact;
    Unbinder unbinder;
    @BindView(R.id.ll_clear_cache)
    LinearLayout llClearCache;
    //清除缓存对话框
    CustomClearCacheDialog customClearCacheDialog;
    @BindView(R.id.ll_power_off)
    LinearLayout llPowerOff;
    //设备关机对话框
    CustomPowerOffDialog customPowerOffDialog;
    //退出登录对话框
    CustomLogOutDialog customLogOutDialog;
    @BindView(R.id.tv_log_out)
    TextView tvLogOut;
    @BindView(R.id.ll_modify_pwd)
    LinearLayout llModifyPwd;

    @Override
    protected int setRootViewId() {
        return R.layout.fragment_guide_setting;
    }

    @Override
    protected void initView(View view, LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        StatusBarUtils.setWindowStatusBarColor(getActivity(), R.color.title_tour_color);
        llJobMode.setOnClickListener(this);
        llEmergencyContact.setOnClickListener(this);
        llClearCache.setOnClickListener(this);
        llPowerOff.setOnClickListener(this);
        tvLogOut.setOnClickListener(this);
        llModifyPwd.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_job_mode:
                IntentUtils.openActivity(getActivity(), GuideJobModeActivity.class);
                break;
            case R.id.ll_emergency_contact:
                IntentUtils.openActivity(getActivity(), EmergencyLinkListActivity.class);
                break;
            case R.id.ll_clear_cache:
                try {
                    customClearCacheDialog = new CustomClearCacheDialog(getContext(), "缓存大小为：" + getTotalCacheSize(getContext()) + "\n，确定清理？");
                    customClearCacheDialog.setClearCache(this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                customClearCacheDialog.show();
                break;
            case R.id.ll_power_off:
                if (customPowerOffDialog == null) {
                    customPowerOffDialog = new CustomPowerOffDialog(getActivity(), "确定远程关机所有设备？");
                }
                customPowerOffDialog.show();
                break;
            case R.id.tv_log_out:
                if (customLogOutDialog == null) {
                    customLogOutDialog = new CustomLogOutDialog(getActivity(), "确定退出登录？");
                }
                customLogOutDialog.show();
                break;
            case R.id.ll_modify_pwd:
                IntentUtils.openActivity(getActivity(), GuideModifyPwdActivity.class);
                break;
        }
    }

    @Override
    public void clear() {
        clearAllCache(getActivity());
        ToastUtils.msg("清除成功");
    }

    /**
     * 获取缓存大小
     *
     * @param context
     * @return
     * @throws Exception
     */
    public static String getTotalCacheSize(Context context) throws Exception {
        long cacheSize = getFolderSize(context.getCacheDir());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            cacheSize += getFolderSize(context.getExternalCacheDir());
        }
        return getFormatSize(cacheSize);
    }


    // 获取文件大小
    //Context.getExternalFilesDir() --> SDCard/Android/data/你的应用的包名/files/ 目录，一般放一些长时间保存的数据
    //Context.getExternalCacheDir() --> SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据
    public static long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                // 如果下面还有文件
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);
                } else {
                    size = size + fileList[i].length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 格式化单位
     *
     * @param size
     * @return
     */
    public static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
//            return size + "Byte";
            return "0K";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "K";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "M";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                + "TB";
    }

    /**
     * 清除缓存
     *
     * @param context
     */
    public static void clearAllCache(Context context) {
        deleteDir(context.getCacheDir());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            deleteDir(context.getExternalCacheDir());
        }
    }

    private static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
