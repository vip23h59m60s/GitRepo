package com.chinapalms.kwobox.dao;

import com.chinapalms.kwobox.javabean.SignIn;

public interface SignInDAO {

    public boolean addSignIn(SignIn signIn);

    public SignIn findSignInByPhoneNumber(String phoneNumber);

    public boolean resetSignInContiuneTimes(String phoneNumber);

    public boolean updateSignIn(SignIn signIn);

}
