package com.cchao.simplelib.model;

import android.os.Build;

import com.cchao.simplelib.core.AndroidHelper;
import com.cchao.simplelib.core.UiHelper;

import static org.apache.commons.lang3.StringUtils.trim;


/**
 * 设备信息
 *
 * @author cchao
 * @version 18-5-13.
 */
public class DeviceInfo {
    private static DeviceInfo mInstance;

    public String manufacturer;
    public String appVersion;
    public int appVersionCode;
    public String osName;
    public String osVersion;
    public String deviceId;
    public int screenWidth;
    public int screenHeight;

    /**
     * 读取机器的相关的信息
     *
     * @return DeviceInfo
     */
    public static DeviceInfo getInfo() {
        if (mInstance != null) {
            return mInstance;
        }

        mInstance = new DeviceInfo();
        mInstance.osName = Build.VERSION_CODES.class.getFields()[Build.VERSION.SDK_INT].getName();
        mInstance.osVersion = Build.VERSION.RELEASE;
        mInstance.manufacturer = Build.MANUFACTURER;
        mInstance.appVersion = trim(AndroidHelper.getVersionName());
        mInstance.appVersionCode = AndroidHelper.getVersionCode();
        mInstance.deviceId = trim(AndroidHelper.getDeviceID());
        mInstance.screenWidth = UiHelper.getScreenWidth();
        mInstance.screenHeight = UiHelper.getScreenHeight();

        return mInstance;
    }
}
