package com.enterprise.mobile.driver;

import com.enterprise.mobile.enums.Platform;

/**
 * Device configuration model for parallel execution and device farm support.
 */
public class DeviceConfig {

    private Platform platform;
    private String deviceName;
    private String platformVersion;
    private String udid;
    private int systemPort;
    private int wdaLocalPort;
    private String osVersion;
    private String browserStackDevice;

    public DeviceConfig() {
    }

    public DeviceConfig(Platform platform, String deviceName, String platformVersion) {
        this.platform = platform;
        this.deviceName = deviceName;
        this.platformVersion = platformVersion;
    }

    // Builder pattern for fluent API
    public static DeviceConfigBuilder builder() {
        return new DeviceConfigBuilder();
    }

    public Platform getPlatform() {
        return platform;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getPlatformVersion() {
        return platformVersion;
    }

    public void setPlatformVersion(String platformVersion) {
        this.platformVersion = platformVersion;
    }

    public String getUdid() {
        return udid;
    }

    public void setUdid(String udid) {
        this.udid = udid;
    }

    public int getSystemPort() {
        return systemPort;
    }

    public void setSystemPort(int systemPort) {
        this.systemPort = systemPort;
    }

    public int getWdaLocalPort() {
        return wdaLocalPort;
    }

    public void setWdaLocalPort(int wdaLocalPort) {
        this.wdaLocalPort = wdaLocalPort;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getBrowserStackDevice() {
        return browserStackDevice;
    }

    public void setBrowserStackDevice(String browserStackDevice) {
        this.browserStackDevice = browserStackDevice;
    }

    // ===== Builder =====

    public static class DeviceConfigBuilder {
        private final DeviceConfig config = new DeviceConfig();

        public DeviceConfigBuilder platform(Platform platform) {
            config.setPlatform(platform);
            return this;
        }

        public DeviceConfigBuilder deviceName(String deviceName) {
            config.setDeviceName(deviceName);
            return this;
        }

        public DeviceConfigBuilder platformVersion(String version) {
            config.setPlatformVersion(version);
            return this;
        }

        public DeviceConfigBuilder udid(String udid) {
            config.setUdid(udid);
            return this;
        }

        public DeviceConfigBuilder systemPort(int port) {
            config.setSystemPort(port);
            return this;
        }

        public DeviceConfigBuilder wdaLocalPort(int port) {
            config.setWdaLocalPort(port);
            return this;
        }

        public DeviceConfigBuilder osVersion(String osVersion) {
            config.setOsVersion(osVersion);
            return this;
        }

        public DeviceConfigBuilder browserStackDevice(String device) {
            config.setBrowserStackDevice(device);
            return this;
        }

        public DeviceConfig build() {
            return config;
        }
    }
}
