package com.harshpandeyz.qualifier.service;

import com.harshpandeyz.qualifier.config.QualifierProperties;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class FinalQueryProvider {
    private final QualifierProperties properties;

    public FinalQueryProvider(QualifierProperties properties) {
        this.properties = properties;
    }

    public QuerySelection selectQuery() {
        String regNo = requireText(properties.getRegNo(), "qualifier.reg-no");
        int lastTwoDigits = extractLastTwoDigits(regNo);
        boolean even = lastTwoDigits % 2 == 0;

        String query = even ? properties.getEvenFinalQuery() : properties.getOddFinalQuery();
        String questionKey = even ? "question-2-even" : "question-1-odd";

        if (!StringUtils.hasText(query)) {
            String message = "No final SQL query configured for " + questionKey + ". "
                + "Set the matching qualifier question property before running the submission.";
            if (properties.isFailOnMissingQuery()) {
                throw new IllegalStateException(message);
            }
            return new QuerySelection(questionKey, "-- Missing SQL query for " + questionKey);
        }

        return new QuerySelection(questionKey, query.trim());
    }

    private static String requireText(String value, String propertyName) {
        if (!StringUtils.hasText(value)) {
            throw new IllegalStateException("Missing required property: " + propertyName);
        }
        return value.trim();
    }

    private static int extractLastTwoDigits(String regNo) {
        String digits = regNo.replaceAll("\\D", "");
        if (digits.length() < 2) {
            throw new IllegalStateException("Registration number must contain at least two digits: " + regNo);
        }
        return Integer.parseInt(digits.substring(digits.length() - 2));
    }

    public record QuerySelection(String questionKey, String finalQuery) {
    }
}
