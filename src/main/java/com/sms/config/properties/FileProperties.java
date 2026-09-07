package com.sms.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "file")
public class FileProperties {

    // Directory where uploaded files are stored.
    private String uploadDir;

    // Maximum allowed file size in bytes.
    private long maxSize;
}
