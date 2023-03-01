package com.furkant.springbootawssecretmanager.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.regions.Region;
import java.util.Base64;
import java.util.Properties;

public class SecretsConfig {

    private static final String SECRET_ID = "secrets/your-secret-path";

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final Properties properties = new Properties();

    private SecretsConfig() {
    }

    public static Properties getProperties() throws JsonProcessingException {
        setSecretsAsProperties();
        return properties;
    }

    private static void setSecretsAsProperties() throws JsonProcessingException {
        try {
            var secret = getSecretsFromAWS(SECRET_ID);
            var secretsJson = objectMapper.readTree(secret);

            var secretKey = secretsJson.get("example.secret.message").textValue();

            properties.put("example.secret.message", secretKey);
        } catch (Exception e) {
            throw e;
        }
    }

    public static String getSecretsFromAWS(String secretName) {
        try {
            var client = SecretsManagerClient.builder()
                    .region(Region.EU_WEST_1) // your aws region
                    .build();

            var getSecretValueRequest = GetSecretValueRequest.builder()
                    .secretId(secretName)
                    .build();
            var getSecretValueResponse = client.getSecretValue(getSecretValueRequest);
            if (getSecretValueResponse != null) {
                if (getSecretValueResponse.secretString() != null) {
                    return getSecretValueResponse.secretString();
                } else {
                    return new String(Base64.getDecoder().decode(getSecretValueResponse.secretBinary().asByteBuffer()).array());
                }
            }
        } catch (Exception e) {
            System.out.println("exception occurred at getSecretsFromAWS secretName:{} Ex:"+ secretName+ e);
        }
        return null;
    }
}
