package com.tobbetu.en4s.backend;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class User implements Serializable {

    private static final long serialVersionUID = -8853898204431440970L;

    private String id;
    private String email;
    private String name;

    public User() {
    }

    public User(String id, String email, String name) {
        this.id = id;
        this.email = email;
        this.name = name;
    }

    public static User fromJSON(String response) {
        try {
            return User.fromJSON(new JSONObject(response));
        } catch (JSONException e) {
            Log.d("User", "JSONException throwed", e);
            return new User();
        }
    }

    public static User fromJSON(JSONObject obj) {
        return new User(obj.optString("_id"), obj.optString("email"),
                obj.optString("name"));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}