package com.sms.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.mail")
public class MailProperties {

    // Email address used as the application sender.
    private String from;

    // Frontend URL used for password reset.
    private String resetPasswordUrl;
}
