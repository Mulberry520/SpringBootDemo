package com.mulberry.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.aliyun.oss.*;
import com.aliyun.oss.common.auth.*;
import com.aliyun.oss.common.comm.SignVersion;

@Configuration
public class OssConfig {
    @Value("${oss.endpoint}")
    private String ENDPOINT;
    @Value("${oss.region}")
    private String REGION;
    @Value("${oss.access-key-id}")
    private String ACCESS_KEY_ID;
    @Value("${oss.access-key-secret}")
    private String ACCESS_KEY_SECRET;


    @Bean
    public OSS ossClient() {
        CredentialsProvider credentialsProvider = new DefaultCredentialProvider(ACCESS_KEY_ID, ACCESS_KEY_SECRET);
        ClientBuilderConfiguration clientBuilderConfiguration = new ClientBuilderConfiguration();
        clientBuilderConfiguration.setSignatureVersion(SignVersion.V4);
        OSS ossClient = OSSClientBuilder.create()
                .endpoint(ENDPOINT)
                .credentialsProvider(credentialsProvider)
                .clientConfiguration(clientBuilderConfiguration)
                .region(REGION)
                .build();
        return ossClient;
    }
}
