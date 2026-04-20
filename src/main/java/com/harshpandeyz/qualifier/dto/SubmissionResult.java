package com.harshpandeyz.qualifier.dto;

import java.time.OffsetDateTime;

public record SubmissionResult(
    String regNo,
    String selectedQuestion,
    String finalQuery,
    String webhookUrl,
    String submissionStatus,
    OffsetDateTime submittedAt,
    String responseBody
) {
}
