package se.sofiekl.macromealplanner.service;

import org.springframework.stereotype.Service;
import se.sofiekl.macromealplanner.client.ApiNinjasClient;
import se.sofiekl.macromealplanner.dto.NutritionSearchResponseDTO;

import java.util.List;

@Service
public class NutritionService{

    private final ApiNinjasClient apiNinjasClient;

    public NutritionService(ApiNinjasClient apiNinjasClient){
        this.apiNinjasClient = apiNinjasClient;
    }

    private Integer safeParseCalories(Object calories){
        if(calories instanceof Number number){
            return number.intValue();
        }
        if(calories instanceof String string){
            String trimmed = string.trim();
            try{
                return (int) Math.round(Double.parseDouble(trimmed));
            }catch(NumberFormatException e){
                return null;
            }
        }
        return null;
    }
    private Double safeParseProtein(Object protein){
        if(protein instanceof Number number){
            return number.doubleValue();
        }
        if(protein instanceof String string){

            String trimmed = string.trim();
            try{
                return Double.parseDouble(trimmed);
            }catch(NumberFormatException e){
                return null;
            }
        }
        return null;
    }

    public List<NutritionSearchResponseDTO> search(String query){
        return apiNinjasClient.searchNutrition(query)
                .stream()
                .map(item -> new NutritionSearchResponseDTO(
                        item.name(),
                        safeParseCalories(item.calories()),
                        safeParseProtein(item.protein_g())
                )).toList();
    }
}
