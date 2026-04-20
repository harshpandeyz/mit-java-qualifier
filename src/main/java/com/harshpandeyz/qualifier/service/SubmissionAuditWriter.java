package com.harshpandeyz.qualifier.service;

import com.harshpandeyz.qualifier.dto.SubmissionResult;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class SubmissionAuditWriter {

    public void write(SubmissionResult result) {
        Path outputPath = Path.of("target", "submission-result.json");
        try {
            Files.createDirectories(outputPath.getParent());
            Files.writeString(outputPath, toJson(result));
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to write submission audit file", exception);
        }
    }

    private String toJson(SubmissionResult result) {
        return """
            {
              "regNo": "%s",
              "selectedQuestion": "%s",
              "finalQuery": "%s",
              "webhookUrl": "%s",
              "submissionStatus": "%s",
              "submittedAt": "%s",
              "responseBody": "%s"
            }
            """.formatted(
            escape(result.regNo()),
            escape(result.selectedQuestion()),
            escape(result.finalQuery()),
            escape(result.webhookUrl()),
            escape(result.submissionStatus()),
            escape(String.valueOf(result.submittedAt())),
            escape(String.valueOf(result.responseBody()))
        );
    }

    private String escape(String value) {
        return value
            .replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("\r", "\\r")
            .replace("\n", "\\n");
    }
}
