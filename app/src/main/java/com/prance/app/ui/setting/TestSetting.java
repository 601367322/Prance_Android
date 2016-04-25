package com.prance.app.ui.setting;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.prance.app.R;
import com.prance.app.base.BaseActivity;
import com.prance.app.util.ToastUtil;
import com.prance.app.util.UrlUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Properties;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * URL动态设置
 * 便于开发切换环境界面
 * Created by shenbingbing on 16/4/18.
 */
public class TestSetting extends BaseActivity {

    @Bind(R.id.ip)
    AutoCompleteTextView ip;
    @Bind(R.id.port)
    AutoCompleteTextView port;
    @Bind(R.id.log_switch)
    SwitchCompat logSwitch;

    String[] ipArr = {"192.168.1.22", "www.tengyue.com", "115.28.134.163"};
    String[] portArr = {"8081", "8091", "80", "8080"};
    String[] socketArr = {"30003", "30004"};

    @Override
    protected void init() {
        initTextView(ipArr, ip);
        initTextView(portArr, port);

        ip.setText(UrlUtil.getPropertiesValue("ip"));
        port.setText(UrlUtil.getPropertiesValue("port"));
        if (Boolean.parseBoolean(UrlUtil.getPropertiesValue("debug"))) {
            logSwitch.setChecked(true);
        } else {
            logSwitch.setChecked(false);
        }
    }

    @Override
    protected int layoutRes() {
        return R.layout.activity_test_set_ip;
    }

    public void initTextView(String[] data, AutoCompleteTextView view) {
        ArrayAdapter ipAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data);
        view.setAdapter(ipAdapter);
        view.setThreshold(1);
        view.setOnClickListener(clickListener);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ((AutoCompleteTextView) v).showDropDown();
        }
    };

    public void rightClick(View view) {
        String ip_str = ip.getText().toString();
        String port_str = port.getText().toString();
        boolean logEnable = logSwitch.isChecked();

        Properties prop = new Properties();
        prop.put("ip", ip_str);
        prop.put("port", port_str);
        prop.put("debug", String.valueOf(logEnable));

        saveChangeData(prop);
    }

    private void saveChangeData(Properties prop) {

        if (UrlUtil.checkSaveLocationExists()) {

            File file = new File(UrlUtil.getBaseDir(this));
            if (!file.exists()) {
                file.mkdirs();
            }
            try {
                FileOutputStream fos = new FileOutputStream(UrlUtil.getBaseDir(this) + UrlUtil.getPathName(), false);
                prop.store(fos, null);
            } catch (Exception e) {
                e.printStackTrace();

            }
            ToastUtil.getInstance().showToast("保存成功，请清除数据重新启动");
            showInstalledAppDetails(this, getPackAgeName());

        } else {
            ToastUtil.getInstance().showToast("保存失败，无SD卡");
        }
    }

    private String getPackAgeName() {
        String packageNames = "";
        try {
            PackageInfo info = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
            packageNames = info.packageName;
        } catch (PackageManager.NameNotFoundException e) {

            packageNames = "";
            e.printStackTrace();
        }
        return packageNames;
    }

    private void showInstalledAppDetails(Context context, String packageName) {

        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", packageName, null);
        intent.setData(uri);
        context.startActivity(intent);
    }

    @OnClick({R.id.btn1})
    public void onBtnClick(View view) {
        Properties prop = new Properties();
        switch (view.getId()) {
            case R.id.btn1:
                prop.put("ip", ipArr[0]);
                prop.put("port", portArr[0]);
                prop.put("socket", socketArr[1]);
                prop.put("debug", "true");
                break;
        }
        saveChangeData(prop);
    }

}
