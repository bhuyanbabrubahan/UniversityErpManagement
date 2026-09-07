package com.sms.notification.serviceImpl;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.sms.notification.dto.NotificationRequestDto;
import com.sms.notification.service.NotificationService;
import com.sms.otp.service.SmsService;

import lombok.RequiredArgsConstructor;

@Service("notificationServiceImpl")
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final JavaMailSender mailSender;
    private final SmsService smsService;

    @Override
    public void send(NotificationRequestDto request) {

        if (request.getChannel() == NotificationRequestDto.Channel.SMS) {

            smsService.sendOtp(
                    request.getRecipient(),
                    request.getMessage()
            );

            return;
        }

        SimpleMailMessage mail = new SimpleMailMessage();

        mail.setTo(request.getRecipient());
        mail.setSubject(request.getSubject());
        mail.setText(request.getMessage());

        mailSender.send(mail);
    }
}