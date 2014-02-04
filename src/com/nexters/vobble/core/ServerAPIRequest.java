package com.nexters.vobble.core;

import org.json.JSONException;
import org.json.JSONObject;

public class ServerAPIRequest {
    private static String SERVER_URL = "http://vobble.herokuapp.com";
    private static String SIGN_UP_URI = SERVER_URL + "/users";
    private static String SIGN_IN_URI = SERVER_URL + "/tokens";

    public static boolean signUpUser(String username, String email, String password) {
        HttpConnectionModule conn = new HttpConnectionModule("POST", SIGN_UP_URI);
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

    
}
