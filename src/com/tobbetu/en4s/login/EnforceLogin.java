package com.tobbetu.en4s.login;


public class EnforceLogin extends Login {

    public EnforceLogin(String email, String passwd, String regId) {
        super("/user/login", regId, "email", email, "password", passwd);
    }

}
