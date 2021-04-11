package com.fmStore.core.service;

import com.fmStore.core.pojo.user.User;

public interface UserService {
    void sendCode(String phone);

    Boolean checkCode(String phone, String smsCode);

    void saveUser(User user);
}
