package common.versionupdate;

/**
 * Created by ${Karoline} on 2017/4/24.
 */

public class VersionReq {
    private String env;
    private String os;

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public VersionReq(String os , String env) {
        this.env = env;
        this.os = os;
    }
}
