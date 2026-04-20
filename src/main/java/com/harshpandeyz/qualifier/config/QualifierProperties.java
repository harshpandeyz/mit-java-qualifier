package com.harshpandeyz.qualifier.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "qualifier")
public class QualifierProperties {
    private String name;
    private String regNo;
    private String email;
    private String generateWebhookUrl;
    private String authorizationPrefix;
    private String oddFinalQuery;
    private String evenFinalQuery;
    private boolean submitOnStartup;
    private boolean failOnMissingQuery;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGenerateWebhookUrl() {
        return generateWebhookUrl;
    }

    public void setGenerateWebhookUrl(String generateWebhookUrl) {
        this.generateWebhookUrl = generateWebhookUrl;
    }

    public String getAuthorizationPrefix() {
        return authorizationPrefix;
    }

    public void setAuthorizationPrefix(String authorizationPrefix) {
        this.authorizationPrefix = authorizationPrefix;
    }

    public String getOddFinalQuery() {
        return oddFinalQuery;
    }

    public void setOddFinalQuery(String oddFinalQuery) {
        this.oddFinalQuery = oddFinalQuery;
    }

    public String getEvenFinalQuery() {
        return evenFinalQuery;
    }

    public void setEvenFinalQuery(String evenFinalQuery) {
        this.evenFinalQuery = evenFinalQuery;
    }

    public boolean isSubmitOnStartup() {
        return submitOnStartup;
    }

    public void setSubmitOnStartup(boolean submitOnStartup) {
        this.submitOnStartup = submitOnStartup;
    }

    public boolean isFailOnMissingQuery() {
        return failOnMissingQuery;
    }

    public void setFailOnMissingQuery(boolean failOnMissingQuery) {
        this.failOnMissingQuery = failOnMissingQuery;
    }
}
