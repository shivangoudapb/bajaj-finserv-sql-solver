package com.bajajfinserv.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    
    @Value("${app.generate-webhook-url:https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA}")
    private String generateWebhookUrl;
    
    @Value("${app.test-webhook-base-url:https://bfhldevapigw.healthrx.co.in/hiring/testWebhook/JAVA}")
    private String testWebhookBaseUrl;
    
    @Value("${app.student.name:John Doe}")
    private String studentName;
    
    @Value("${app.student.regNo:REG12347}")
    private String studentRegNo;
    
    @Value("${app.student.email:john@example.com}")
    private String studentEmail;

    public String getGenerateWebhookUrl() {
        return generateWebhookUrl;
    }

    public String getTestWebhookBaseUrl() {
        return testWebhookBaseUrl;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getStudentRegNo() {
        return studentRegNo;
    }

    public String getStudentEmail() {
        return studentEmail;
    }
}