package task3;

import java.util.HashMap;
import java.util.Map;

public class CurrentUserParams{
    private Map<String, String> params = new HashMap<>();


    // useCode: null if empty, -1 if user cancelled authorization
    // userId: 0 if not authorised
    public CurrentUserParams() {
        params.put("userCode", "null");
        params.put("userId", "0");
    }

    String getUserCode() {
        return params.get("userCode");
    }

    String getUserID() {
        return params.get("userId");
    }

    void setUserID(int userId) {
        params.put("userId", String.valueOf(userId));
    }

    void setUserCode(String userCode) {
        params.put("userCode", userCode);
    }

    public String getParamValue(String key) {
        return params.getOrDefault(key, "null");
    }

    void putParam(String key, String value) {
        params.put(key, value);
    }
}
