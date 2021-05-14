package task4;

import java.util.HashMap;
import java.util.Map;

public class UserDb {
    Map<User, String> database = new HashMap<>();

    public void addNewUser(User user, String code) {
        database.put(user, code);
    }

    public boolean containsUser(User user) {
        return database.containsKey(user);
    }

    public String getCode(User user) {
        return database.get(user);
    }

    public void setCode(User user, String code) {
        database.put(user, code);
    }
}
