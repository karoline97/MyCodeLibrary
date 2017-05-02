package common.versionupdate;

import org.json.JSONObject;

/**
 * Created by ${Karoline} on 2017/4/24.
 */

public class JsonUtils {

    public String emptyJson(){
        JSONObject object = new JSONObject();
        return object.toString();
    }

    public String versionReqToJson(VersionReq req){
        JSONObject object = new JSONObject();
        try{
            object.put("env",req.getEnv());
            object.put("os",req.getOs());
        }catch (Exception e){
            e.printStackTrace();
        }
        return object.toString();
    }

    public VersionData jsonToVersionData(String json){
        VersionData versionData = new VersionData();
        try{
            JSONObject object = new JSONObject(json);
            versionData.setStatus(object.optString("status"));

            VersionData.Result result = versionData.new Result();
            JSONObject object1 = object.optJSONObject("result");
            result.setStatusCode(object1.optString("statusCode"));
            result.setStatusMsg(object1.getString("statusMsg"));
            if(result.getStatusCode().equals("000000")){
                JSONObject object2 = object1.getJSONObject("data");
                VersionData.Data data = versionData.new Data();
                data.setOs(object2.optString("os"));
                data.setAppName(object2.optString("appName"));
                data.setDescription(object2.optString("description"));
                data.setDownloadUrl(object2.optString("downloadUrl"));
                data.setMinVersion(object2.optString("minVersion"));
                data.setVersion(object2.optString("version"));
                data.setUpdateTime(object2.optString("updateTime"));
                result.setData(data);
            }
            versionData.setResult(result);
        }catch (Exception e){
            e.printStackTrace();
        }

        return versionData;
    }
}
