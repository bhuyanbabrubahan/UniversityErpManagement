package com.sms.security.serviceImpl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sms.exception.ResourceAlreadyExistsException;
import com.sms.security.entity.User;
import com.sms.security.enums.Role;
import com.sms.security.enums.UserStatus;
import com.sms.security.repository.UserRepository;
import com.sms.security.service.UserAccountService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserAccountServiceImpl implements UserAccountService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User createUser(
            String email,
            String password,
            String mobileNumber,
            Role role,
            UserStatus status
    ) {

        String normalizedEmail = email.trim().toLowerCase();
        String normalizedMobile = mobileNumber.trim();

        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new ResourceAlreadyExistsException(
                    "Email already exists : " + normalizedEmail
            );
        }

        if (userRepository.existsByMobileNumber(normalizedMobile)) {
            throw new ResourceAlreadyExistsException(
                    "Mobile number already exists : " + normalizedMobile
            );
        }

        User user = new User();

        user.setEmail(normalizedEmail);
        user.setPassword(passwordEncoder.encode(password));
        user.setMobileNumber(normalizedMobile);
        user.setRole(role);
        user.setStatus(status);

        user.setEmailVerified(false);
        user.setMobileVerified(false);

        user.setFailedLoginAttempts(0);
        user.setAccountLockedUntil(null);

        return userRepository.save(user);
    }

    @Override
    public void updateUser(
            User user,
            String email,
            String password,
            String mobileNumber
    ) {

        String normalizedEmail = email.trim().toLowerCase();
        String normalizedMobile = mobileNumber.trim();

        if (!user.getEmail().equalsIgnoreCase(normalizedEmail)
                && userRepository.existsByEmail(normalizedEmail)) {

            throw new ResourceAlreadyExistsException(
                    "Email already exists : " + normalizedEmail
            );
        }

        if (!user.getMobileNumber().equals(normalizedMobile)
                && userRepository.existsByMobileNumber(normalizedMobile)) {

            throw new ResourceAlreadyExistsException(
                    "Mobile number already exists : " + normalizedMobile
            );
        }

        user.setEmail(normalizedEmail);
        user.setMobileNumber(normalizedMobile);

        if (password != null && !password.isBlank()) {
            user.setPassword(passwordEncoder.encode(password));
        }

        userRepository.save(user);
    }

    @Override
    public void disableUser(User user) {
        user.setStatus(UserStatus.INACTIVE);
        userRepository.save(user);
    }

    @Override
    public void activateUser(User user) {
        user.setStatus(UserStatus.ACTIVE);
        userRepository.save(user);
    }
}
