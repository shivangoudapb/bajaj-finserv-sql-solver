package com.bajajfinserv.runner;

import com.bajajfinserv.service.WebhookService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartupRunner implements CommandLineRunner {
    
    private final WebhookService webhookService;
    
    public ApplicationStartupRunner(WebhookService webhookService) {
        this.webhookService = webhookService;
    }
    
    @Override
    public void run(String... args) throws Exception {
        webhookService.executeCompleteFlow();
    }
}