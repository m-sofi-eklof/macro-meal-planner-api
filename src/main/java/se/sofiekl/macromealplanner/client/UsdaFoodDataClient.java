package se.sofiekl.macromealplanner.client;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;
import se.sofiekl.macromealplanner.dto.foodSearchFlow.UsdaFoodDTO;
import se.sofiekl.macromealplanner.dto.foodSearchFlow.UsdaSearchResponseDTO;

@Component
public class UsdaFoodDataClient {
    private final RestClient restClient;
    private final String baseUrl;
    private final String apiKey;

    public UsdaFoodDataClient(@Value("${usda.base-url}") String baseUrl,
                              @Value("${usda.api-key}") String apiKey){
                this.baseUrl = baseUrl;
                this.apiKey = apiKey;
                this.restClient= RestClient.builder()
                        .baseUrl(baseUrl)
                        .build();
    }

    @PostConstruct
    void validateConfig(){
        if(!StringUtils.hasText(baseUrl)){
            throw new IllegalStateException("usda.base-url has not been set");
        }
        if(!StringUtils.hasText(apiKey)){
            throw new IllegalStateException("usda.api-key is missing.");
        }
    }

    /**
     * Searches fooditems based on name/query
     * USDA endpoint GET /foods/search
     */
    public UsdaSearchResponseDTO searchFoods(String query) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/foods/search")
                        .queryParam("api_key", apiKey)
                        .queryParam("query", query)
                        .queryParam("pageSize", 10)  // Limit results
                        .build())
                .retrieve()
                .body(UsdaSearchResponseDTO.class);
    }

    /**
     * Gets detailed nutrient info for a specific food item.
     * USDA endpoint: GET /food/{fdcId}
     */
    public UsdaFoodDTO getFoodById(Long fdcId) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/food/{fdcId}")
                        .queryParam("api_key", apiKey)
                        .build(fdcId))
                .retrieve()
                .body(UsdaFoodDTO.class);
    }
}
