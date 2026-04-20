package com.harshpandeyz.qualifier.dto;

public record GenerateWebhookRequest(
    String name,
    String regNo,
    String email
) {
}
