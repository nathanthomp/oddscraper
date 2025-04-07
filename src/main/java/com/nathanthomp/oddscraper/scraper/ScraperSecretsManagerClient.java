package com.nathanthomp.oddscraper.scraper;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;

public class ScraperSecretsManagerClient {
    private static ScraperSecretsManagerClient instance = null;

    public static ScraperSecretsManagerClient getInstance() throws Exception {
        if (instance == null) {
            instance = new ScraperSecretsManagerClient();
        }
        return instance;
    }

    private final String SECRET_NAME = "OddscraperSecretsManager";
    private final Region SECRET_REGION = Region.of("us-east-2");

    private Map<String, String> secrets;

    public ScraperSecretsManagerClient() throws Exception {
        secrets = new HashMap<String, String>();

        SecretsManagerClient secretsClient = SecretsManagerClient.builder()
                .region(SECRET_REGION)
                .build();

        GetSecretValueRequest getSecretValueRequest = GetSecretValueRequest.builder()
                .secretId(SECRET_NAME)
                .build();

        String secretString = secretsClient.getSecretValue(getSecretValueRequest).secretString();

        Gson gson = new Gson();
        LinkedTreeMap<String, String> secrets = (LinkedTreeMap) gson.fromJson(secretString, Object.class);

        for (Map.Entry<String, String> secretEntry : secrets.entrySet()) {
            this.secrets.put(secretEntry.getKey(), secretEntry.getValue());
        }

    }

    public Map<String, String> getOddsApiSecrets() {
        Map<String, String> result = new HashMap<String, String>();
        for (Map.Entry<String, String> mapEntry : this.secrets.entrySet()) {
            if (mapEntry.getKey().startsWith("OddsApiKey")) {
                result.put(mapEntry.getKey(), mapEntry.getValue());
            }
        }
        return result;
    }
}
