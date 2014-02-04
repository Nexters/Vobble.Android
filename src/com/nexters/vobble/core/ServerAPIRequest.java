package com.nexters.vobble.core;

import org.json.JSONException;
import org.json.JSONObject;

public class ServerAPIRequest {
    private static String SERVER_URL = "http://vobble.herokuapp.com";
    private static String SIGN_UP_URI = SERVER_URL + "/users";
    private static String SIGN_IN_URI = SERVER_URL + "/tokens";

    public static boolean signUp(String username, String email, String password) {
        HttpConnectionModule conn = new HttpConnectionModule(HttpConnectionModule.POST, SIGN_UP_URI);
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
        int userId = -1;
        HttpConnectionModule conn = new HttpConnectionModule(HttpConnectionModule.POST, SIGN_IN_URI);
        conn.addParameter("email", email);
        conn.addParameter("password", password);

        String responseData = conn.execute();
        int result = 0;
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
}
