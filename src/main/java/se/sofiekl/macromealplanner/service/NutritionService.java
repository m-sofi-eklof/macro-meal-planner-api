package se.sofiekl.macromealplanner.service;

import org.springframework.stereotype.Service;
import se.sofiekl.macromealplanner.client.UsdaFoodDataClient;
import se.sofiekl.macromealplanner.dto.usda.*;
import se.sofiekl.macromealplanner.model.FoodItem;

import java.util.List;

@Service
public class NutritionService{

    private final UsdaFoodDataClient usdaClient;

    /**
     * USDA nutrition ID constants
     * These constants are used to access specific nutrient values of a food item
     * from the USDA API
     */
    //used in MVP
    private static Integer ENERGY_KCAL_ID = 1008;
    private static Integer PROTEIN_ID = 1003;
    //potential expand todo: implement or remove
    private static Integer TOTAL_FAT_ID = 1004;
    private static Integer TOTAL_CARBOHYDRATE_ID = 1005;

    /**
     * The constuctor for the NutritionService
     * @param usdaClient
     */
    public NutritionService(UsdaFoodDataClient usdaClient){
        this.usdaClient = usdaClient;
    }

    public List<NutritionSearchResponseDTO> search(String query){
        UsdaSearchResponseDTO searchResponse = usdaClient.searchFoods(query);
        if(searchResponse==null || searchResponse.foods()==null){
            return List.of();
        }

        return searchResponse.foods()
                .stream()
                .map(this::mapToNutritionResponseFromFoodItemDTO)
                .toList();
    }

    public NutritionSearchResponseDTO getByFdcId(Long fdcId){
         return mapToNutritionResponseFromFoodDTO(usdaClient.getFoodById(fdcId));
    }

    private NutritionSearchResponseDTO mapToNutritionResponseFromFoodItemDTO(UsdaFoodItemDTO food){

        Integer calories = extractNutrientValue(food.foodNutrients(), ENERGY_KCAL_ID);
        Double protein = extractNutrientValueDouble(food.foodNutrients(), PROTEIN_ID);

        return new NutritionSearchResponseDTO(food.fdcId(), food.description(), calories,protein);
    }

    private NutritionSearchResponseDTO mapToNutritionResponseFromFoodDTO(UsdaFoodDTO food){
        if (food.foodNutrients()==null || food.foodNutrients().isEmpty()){
            return null;
        }

        Integer calories = food.foodNutrients()
                .stream()
                .filter(n->n.nutrient()!=null && ENERGY_KCAL_ID.equals(n.nutrient().id()))
                .map(UsdaFoodNutrientDetailDTO::amount)
                .map(Double::intValue)
                .findFirst()
                .orElse(null);

        Double protein = food.foodNutrients()
                .stream()
                .filter(n->n.nutrient()!=null && PROTEIN_ID.equals(n.nutrient().id()))
                .map(UsdaFoodNutrientDetailDTO::amount)
                .findFirst()
                .orElse(null);

        return new NutritionSearchResponseDTO(food.fdcId(), food.description(), calories,protein);
    }

    private Integer extractNutrientValue(List<UsdaFoodNutrientDTO> nutrients, Integer nutrientId){
        if(nutrients==null || nutrients.isEmpty()){
            return null;
        }
        return nutrients
                .stream()
                .filter(n-> nutrientId.equals(n.nutrientId()))
                .findFirst()
                .map(UsdaFoodNutrientDTO::value)
                .map(Double::intValue)
                .orElse(null);
    }

    private Double extractNutrientValueDouble(List<UsdaFoodNutrientDTO> nutrients, Integer nutrientId) {
        if (nutrients == null) return null;

        return nutrients.stream()
                .filter(n -> nutrientId.equals(n.nutrientId()))
                .findFirst()
                .map(UsdaFoodNutrientDTO::value)
                .orElse(null);
    }
}
