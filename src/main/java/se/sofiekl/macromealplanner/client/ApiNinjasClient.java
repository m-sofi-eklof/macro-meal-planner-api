package se.sofiekl.macromealplanner.client;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;
import se.sofiekl.macromealplanner.dto.ApiNinjasNutritionItemDTO;

import java.util.List;

@Component
public class ApiNinjasClient {

    private final RestClient restClient;
    private final String baseUrl;
    private final String apiKey;

    public ApiNinjasClient(
            @Value("${api-ninjas.base-url}") String baseUrl,
            @Value("${api-ninjas.api-key}") String apiKey
    ) {
        this.baseUrl = baseUrl;
        this.apiKey = apiKey;
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("X-Api-Key", apiKey)
                .build();
    }

    @PostConstruct
    void validateConfig() {
        if(!StringUtils.hasText(baseUrl)) {
            throw new IllegalStateException("baseUrl has not been set");
        }
        if (!StringUtils.hasText(apiKey)) {
            throw new IllegalStateException("api-ninjas.api-key is missing");
        }
    }

    public List<ApiNinjasNutritionItemDTO> searchNutrition(String query){
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/nutrition")
                        .queryParam("query", query)
                        .build())
                .retrieve()
                .body(new ParameterizedTypeReference<>(){});
    }
}
