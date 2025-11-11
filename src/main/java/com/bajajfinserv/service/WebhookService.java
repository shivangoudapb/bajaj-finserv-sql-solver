package com.bajajfinserv.service;

import com.bajajfinserv.config.AppConfig;
import com.bajajfinserv.dto.GenerateWebhookRequest;
import com.bajajfinserv.dto.GenerateWebhookResponse;
import com.bajajfinserv.dto.SolutionSubmissionRequest;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WebhookService {
    
    private final RestTemplate restTemplate;
    private final AppConfig appConfig;
    private final SqlProblemSolverService sqlSolverService;
    
    public WebhookService(RestTemplate restTemplate, AppConfig appConfig, 
                         SqlProblemSolverService sqlSolverService) {
        this.restTemplate = restTemplate;
        this.appConfig = appConfig;
        this.sqlSolverService = sqlSolverService;
    }
    
    public void executeCompleteFlow() {
        try {
            System.out.println("Starting Bajaj Finserv Health SQL Solver...");
            
            GenerateWebhookResponse webhookResponse = generateWebhook();
            if (webhookResponse == null) {
                System.err.println("Failed to generate webhook. Exiting...");
                return;
            }
            
            System.out.println("Webhook generated successfully");
            System.out.println("Webhook URL: " + webhookResponse.getWebhookUrl());
            
            String finalQuery = sqlSolverService.solveSqlProblem(appConfig.getStudentRegNo());
            System.out.println("Generated SQL Query: " + finalQuery);
            
            boolean submissionSuccess = submitSolution(webhookResponse, finalQuery);
            
            if (submissionSuccess) {
                System.out.println("✅ Solution submitted successfully!");
            } else {
                System.err.println("❌ Failed to submit solution");
            }
            
        } catch (Exception e) {
            System.err.println("Error during execution: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private GenerateWebhookResponse generateWebhook() {
        try {
            GenerateWebhookRequest request = new GenerateWebhookRequest(
                appConfig.getStudentName(),
                appConfig.getStudentRegNo(), 
                appConfig.getStudentEmail()
            );
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<GenerateWebhookRequest> entity = new HttpEntity<>(request, headers);
            
            ResponseEntity<GenerateWebhookResponse> response = restTemplate.exchange(
                appConfig.getGenerateWebhookUrl(),
                HttpMethod.POST,
                entity,
                GenerateWebhookResponse.class
            );
            
            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            }
            
        } catch (Exception e) {
            System.err.println("Error generating webhook: " + e.getMessage());
        }
        return null;
    }
    
    private boolean submitSolution(GenerateWebhookResponse webhookResponse, String finalQuery) {
        try {
            SolutionSubmissionRequest submissionRequest = new SolutionSubmissionRequest(finalQuery);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(webhookResponse.getAccessToken());
            
            HttpEntity<SolutionSubmissionRequest> entity = new HttpEntity<>(submissionRequest, headers);
            
            ResponseEntity<String> response = restTemplate.exchange(
                webhookResponse.getWebhookUrl(),
                HttpMethod.POST,
                entity,
                String.class
            );
            
            return response.getStatusCode().is2xxSuccessful();
            
        } catch (Exception e) {
            System.err.println("Error submitting solution: " + e.getMessage());
            return false;
        }
    }
}