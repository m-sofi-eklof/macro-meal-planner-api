package se.sofiekl.macromealplanner.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.sofiekl.macromealplanner.dto.FavoriteDTO;
import se.sofiekl.macromealplanner.dto.FoodItemResponseDTO;
import se.sofiekl.macromealplanner.service.FavoriteService;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @GetMapping
    public ResponseEntity<List<FavoriteDTO>> getUserFavorites() {
        return ResponseEntity.ok(favoriteService.getUserFavorites());
    }

    @PostMapping
    public ResponseEntity<FavoriteDTO> saveFavorite(@RequestBody FavoriteDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(favoriteService.saveFavorite(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFavorite(@PathVariable Long id) {
        favoriteService.deleteFavorite(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{favoriteId}/quick-add/{mealId}")
    public ResponseEntity<FoodItemResponseDTO> quickAdd(
            @PathVariable Long favoriteId,
            @PathVariable Long mealId
    ) {
        return ResponseEntity.ok(favoriteService.quickAdd(mealId, favoriteId));
    }
}
