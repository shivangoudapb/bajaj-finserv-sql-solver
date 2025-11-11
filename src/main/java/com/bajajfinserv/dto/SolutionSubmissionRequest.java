package com.bajajfinserv.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SolutionSubmissionRequest {
    @JsonProperty("finalQuery")
    private String finalQuery;
    
    public SolutionSubmissionRequest(String finalQuery) {
        this.finalQuery = finalQuery;
    }
    
    public String getFinalQuery() { return finalQuery; }
    public void setFinalQuery(String finalQuery) { this.finalQuery = finalQuery; }
}