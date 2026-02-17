package se.sofiekl.macromealplanner.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import se.sofiekl.macromealplanner.dto.usda.NutritionSearchRequestDTO;
import se.sofiekl.macromealplanner.dto.usda.NutritionSearchResponseDTO;
import se.sofiekl.macromealplanner.service.NutritionService;

import java.util.List;

@RestController
@RequestMapping("/api/nutrition")
public class NutritionController {

    private final NutritionService nutritionService;

    public NutritionController( NutritionService nutritionService) {
        this.nutritionService = nutritionService;
    }

    @GetMapping("/search")
    public List<NutritionSearchResponseDTO> search(
            @Valid
            @RequestBody
            NutritionSearchRequestDTO dto
    ){
        return nutritionService.search(dto.query());
    }
}
