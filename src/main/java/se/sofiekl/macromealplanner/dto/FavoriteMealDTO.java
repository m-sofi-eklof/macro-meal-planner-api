package se.sofiekl.macromealplanner.dto;

import java.util.List;

public record FavoriteMealDTO(
        Long id,
        String name,
        List<FavoriteMealItemDTO> items
) {}
