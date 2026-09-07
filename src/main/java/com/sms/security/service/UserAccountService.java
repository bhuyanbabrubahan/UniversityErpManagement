package com.sms.security.service;

import com.sms.security.entity.User;
import com.sms.security.enums.Role;
import com.sms.security.enums.UserStatus;

public interface UserAccountService {

    User createUser(
            String email,
            String password,
            String mobileNumber,
            Role role,
            UserStatus status
    );

    void updateUser(
            User user,
            String email,
            String password,
            String mobileNumber
    );

    void disableUser(User user);

    void activateUser(User user);
}
