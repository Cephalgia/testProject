import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Karina on 26.02.2017.
 */
class JsonUtils {

    private static File usersFile = new File("users.json");

    private static List<User> getUsers() {
        List<User> users = new LinkedList<User>();
        JsonArray usersJson;
        try {
            usersJson = (new Gson()).fromJson(new BufferedReader(new FileReader(usersFile)), JsonArray.class);
            for (int i = 0; i < usersJson.size(); i++) {
                JsonElement userJson = usersJson.get(i);
                User user = new Gson().fromJson(userJson, User.class);
                users.add(user);
            }
        } catch (FileNotFoundException | NullPointerException ignored) {
        }
        return users;
    }

    static void addUser(User user) throws RuntimeException {
        try {
            List<User> users = getUsers();
            users.add(user);
            Gson gson = new GsonBuilder().create();
            String json = gson.toJson(users);
            FileWriter fileWriter = new FileWriter(usersFile);
            fileWriter.write(json);
            fileWriter.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    static User getUser(String login) {
        for (User user : getUsers()) {
            if (user.getLogin().equalsIgnoreCase(login)) {
                return user;
            }
        }
        return null;
    }

    static boolean isUserExist(String login, byte[] pass) throws FileNotFoundException {
        for (User user : getUsers()) {
            if (user.getLogin().equalsIgnoreCase(login)) {
                    return Arrays.equals(user.getPassword(), pass);
            }
        }
        return false;
    }
}
