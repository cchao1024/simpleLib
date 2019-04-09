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

        DeviceInfo info = new DeviceInfo();
        info.osName = Build.VERSION_CODES.class.getFields()[Build.VERSION.SDK_INT].getName();
        info.osVersion = Build.VERSION.RELEASE;
        info.manufacturer = Build.MANUFACTURER;
        info.appVersion = trim(AndroidHelper.getVersionName());
        info.appVersionCode = AndroidHelper.getVersionCode();
        info.deviceId = trim(AndroidHelper.getDeviceID());
        info.screenWidth = UiHelper.getScreenWidth();
        info.screenHeight = UiHelper.getScreenHeight();

        return info;
    }
}
