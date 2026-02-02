package com.insurance.manager.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("storage")
@Data
public class StorageProperties {

    /**
     * Folder location for storing files
     */
    private String location = "upload-dir";

}
