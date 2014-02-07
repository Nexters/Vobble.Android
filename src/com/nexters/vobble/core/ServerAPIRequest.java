package com.nexters.vobble.core;

import org.json.JSONException;
import org.json.JSONObject;

public class ServerAPIRequest {
    private static String URL_SERVER = "http://vobble.herokuapp.com";
    private static String URL_SIGN_UP = URL_SERVER + "/users";
    private static String URL_SIGN_IN = URL_SERVER + "/tokens";
    private static String URL_GET_ALL_VOBBLES = URL_SERVER + "/vobbles/count";

    public static boolean signUp(String username, String email, String password) {
        HttpConnectionModule conn = new HttpConnectionModule(HttpConnectionModule.POST, URL_SIGN_UP);
        conn.addParameter("username", username);
        conn.addParameter("email", email);
        conn.addParameter("password", password);
        String responseData = conn.execute();

        int result = 0;
        try {
            JSONObject obj = new JSONObject(responseData);
            result = obj.getInt("result");
        } catch (JSONException e) {
        }

        if (result == 0) return false;
        return true;
    }

    public static int signIn(String email, String password) {
        HttpConnectionModule conn = new HttpConnectionModule(HttpConnectionModule.POST, URL_SIGN_IN);
        conn.addParameter("email", email);
        conn.addParameter("password", password);
        String responseData = conn.execute();

        int result = 0;
        int userId = -1;
        try {
            JSONObject obj = new JSONObject(responseData);
            result = obj.getInt("result");
            userId = obj.getInt("user_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (result == 0) return -1;
        return userId;
    }

    public static int getAllVobblesCount() {
        HttpConnectionModule conn = new HttpConnectionModule(HttpConnectionModule.GET, URL_GET_ALL_VOBBLES);
        String responseData = conn.execute();

        int result = 0;
        int count = 0;
        try {
            JSONObject obj = new JSONObject(responseData);
            result = obj.getInt("result");
            count = obj.getInt("count");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (result == 0) return -1;
        return count;
    }

    public static int getMyVobblesCount(Integer userId) {
        String url = URL_SERVER + "/users/" + userId + "/vobbles/count";
        HttpConnectionModule conn = new HttpConnectionModule(HttpConnectionModule.GET, url);
        String responseData = conn.execute();

        int result = 0;
        int count = 0;
        try {
            JSONObject obj = new JSONObject(responseData);
            result = obj.getInt("result");
            count = obj.getInt("count");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (result == 0) return -1;
        return count;
    }
}
