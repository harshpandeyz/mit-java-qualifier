package com.harshpandeyz.qualifier.service;

import com.harshpandeyz.qualifier.config.QualifierProperties;
import com.harshpandeyz.qualifier.dto.GenerateWebhookRequest;
import com.harshpandeyz.qualifier.dto.GenerateWebhookResponse;
import com.harshpandeyz.qualifier.dto.SubmissionResult;
import com.harshpandeyz.qualifier.dto.SubmitSolutionRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.time.OffsetDateTime;

@Component
public class QualifierFlowRunner implements ApplicationRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(QualifierFlowRunner.class);

    private final QualifierProperties properties;
    private final RestTemplate restTemplate;
    private final FinalQueryProvider finalQueryProvider;
    private final SubmissionAuditWriter submissionAuditWriter;

    public QualifierFlowRunner(
        QualifierProperties properties,
        RestTemplate restTemplate,
        FinalQueryProvider finalQueryProvider,
        SubmissionAuditWriter submissionAuditWriter
    ) {
        this.properties = properties;
        this.restTemplate = restTemplate;
        this.finalQueryProvider = finalQueryProvider;
        this.submissionAuditWriter = submissionAuditWriter;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (!properties.isSubmitOnStartup()) {
            LOGGER.info("Startup submission is disabled. Set qualifier.submit-on-startup=true to execute the webhook flow.");
            return;
        }

        FinalQueryProvider.QuerySelection querySelection = finalQueryProvider.selectQuery();
        GenerateWebhookResponse webhookResponse = generateWebhook();
        String responseBody = submitQuery(webhookResponse, querySelection.finalQuery());

        SubmissionResult result = new SubmissionResult(
            properties.getRegNo(),
            querySelection.questionKey(),
            querySelection.finalQuery(),
            webhookResponse.webhook(),
            "SUBMITTED",
            OffsetDateTime.now(),
            responseBody
        );
        submissionAuditWriter.write(result);

        LOGGER.info("Submission completed for {} using {}.", properties.getRegNo(), querySelection.questionKey());
        LOGGER.info("Webhook used: {}", webhookResponse.webhook());
    }

    private GenerateWebhookResponse generateWebhook() {
        requireText(properties.getName(), "qualifier.name");
        requireText(properties.getRegNo(), "qualifier.reg-no");
        requireText(properties.getEmail(), "qualifier.email");
        requireText(properties.getGenerateWebhookUrl(), "qualifier.generate-webhook-url");

        GenerateWebhookRequest request = new GenerateWebhookRequest(
            properties.getName(),
            properties.getRegNo(),
            properties.getEmail()
        );

        ResponseEntity<GenerateWebhookResponse> response = restTemplate.postForEntity(
            properties.getGenerateWebhookUrl(),
            request,
            GenerateWebhookResponse.class
        );

        GenerateWebhookResponse body = response.getBody();
        if (body == null || !StringUtils.hasText(body.webhook()) || !StringUtils.hasText(body.accessToken())) {
            throw new IllegalStateException("generateWebhook response did not contain both webhook and accessToken");
        }
        return body;
    }

    private String submitQuery(GenerateWebhookResponse webhookResponse, String finalQuery) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(HttpHeaders.AUTHORIZATION, formatAuthorizationHeader(webhookResponse.accessToken()));

        HttpEntity<SubmitSolutionRequest> request = new HttpEntity<>(new SubmitSolutionRequest(finalQuery), headers);
        ResponseEntity<String> response = restTemplate.exchange(
            webhookResponse.webhook(),
            HttpMethod.POST,
            request,
            String.class
        );

        return response.getBody();
    }

    private String formatAuthorizationHeader(String accessToken) {
        String prefix = properties.getAuthorizationPrefix();
        if (!StringUtils.hasText(prefix)) {
            return accessToken;
        }
        return prefix.trim() + " " + accessToken;
    }

    private static void requireText(String value, String propertyName) {
        if (!StringUtils.hasText(value)) {
            throw new IllegalStateException("Missing required property: " + propertyName);
        }
    }
}
