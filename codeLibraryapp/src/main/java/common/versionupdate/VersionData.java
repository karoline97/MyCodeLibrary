package common.versionupdate;

/**
 * Created by ${Karoline} on 2017/4/24.
 */

public class VersionData {
    private String status;
    private Result result;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public class Result{
        private String statusCode; //000000成功
        private String statusMsg;
        private Data data;

        public Data getData() {
            return data;
        }

        public void setData(Data data) {
            this.data = data;
        }

        public String getStatusCode() {
            return statusCode;
        }

        public void setStatusCode(String statusCode) {
            this.statusCode = statusCode;
        }

        public String getStatusMsg() {
            return statusMsg;
        }

        public void setStatusMsg(String statusMsg) {
            this.statusMsg = statusMsg;
        }
    }

    public class Data{
        private String appName;//快递企业平台",
        private String version;//  //最新版本号
        private String minVersion;//	//可兼容的最低版本
        private String os;// ios或者android
        private String downloadUrl;//http://xxxx",
        private String updateTime;////时间戳，毫秒
        private String description;////更新描述

        public String getAppName() {
            return appName;
        }

        public void setAppName(String appName) {
            this.appName = appName;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getDownloadUrl() {
            return downloadUrl;
        }

        public void setDownloadUrl(String downloadUrl) {
            this.downloadUrl = downloadUrl;
        }

        public String getMinVersion() {
            return minVersion;
        }

        public void setMinVersion(String minVersion) {
            this.minVersion = minVersion;
        }

        public String getOs() {
            return os;
        }

        public void setOs(String os) {
            this.os = os;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }
    }
}
