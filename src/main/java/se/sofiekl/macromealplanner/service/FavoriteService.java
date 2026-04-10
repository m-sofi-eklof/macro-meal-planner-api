package se.sofiekl.macromealplanner.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import se.sofiekl.macromealplanner.dto.FavoriteDTO;
import se.sofiekl.macromealplanner.dto.FoodItemResponseDTO;
import se.sofiekl.macromealplanner.model.Favorite;
import se.sofiekl.macromealplanner.model.FoodItem;
import se.sofiekl.macromealplanner.model.Meal;
import se.sofiekl.macromealplanner.model.User;
import se.sofiekl.macromealplanner.repository.FavoriteRepository;
import se.sofiekl.macromealplanner.repository.FoodItemRepository;
import se.sofiekl.macromealplanner.repository.MealRepository;
import se.sofiekl.macromealplanner.repository.UserRepository;
import se.sofiekl.macromealplanner.mapper.FoodItemMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final FoodItemRepository foodItemRepository;
    private final MealRepository mealRepository;
    private final FoodItemMapper foodItemMapper;

    public FavoriteService(FavoriteRepository favoriteRepository, UserRepository userRepository,
                           FoodItemRepository foodItemRepository, MealRepository mealRepository,
                           FoodItemMapper foodItemMapper) {
        this.favoriteRepository = favoriteRepository;
        this.userRepository = userRepository;
        this.foodItemRepository = foodItemRepository;
        this.mealRepository = mealRepository;
        this.foodItemMapper = foodItemMapper;
    }

    public List<FavoriteDTO> getUserFavorites() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found with username: " + username));

        return favoriteRepository.findAllByUserId(user.getId()).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public FavoriteDTO saveFavorite(FavoriteDTO dto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found with username: " + username));

        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setName(dto.name());
        favorite.setSource(dto.source());
        favorite.setFdcId(dto.fdcId());
        favorite.setCalories(dto.calories());
        favorite.setProtein(dto.protein());
        favorite.setServings(dto.servings());
        favorite.setServingDescription(dto.servingDescription());

        return toDTO(favoriteRepository.save(favorite));
    }

    @Transactional
    public void deleteFavorite(Long favoriteId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found with username: " + username));

        Favorite favorite = favoriteRepository.findByIdAndUserId(favoriteId, user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Favorite not found with id: " + favoriteId));

        favoriteRepository.delete(favorite);
    }

    @Transactional
    public FoodItemResponseDTO quickAdd(Long mealId, Long favoriteId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found with username: " + username));

        Favorite favorite = favoriteRepository.findByIdAndUserId(favoriteId, user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Favorite not found with id: " + favoriteId));

        Meal meal = mealRepository.findById(mealId)
                .orElseThrow(() -> new EntityNotFoundException("Meal not found with id: " + mealId));

        if (!meal.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You lack permission to add food to this meal");
        }

        FoodItem foodItem = new FoodItem();
        foodItem.setName(favorite.getName());
        foodItem.setSource(favorite.getSource());
        foodItem.setFdcId(favorite.getFdcId());
        foodItem.setCalories(favorite.getCalories());
        foodItem.setProtein(favorite.getProtein());
        foodItem.setServings(favorite.getServings());
        foodItem.setMeal(meal);
        foodItem.setUser(user);

        FoodItem saved = foodItemRepository.save(foodItem);

        return foodItemMapper.toFoodItemResponseDTO(saved);
    }

    private FavoriteDTO toDTO(Favorite favorite) {
        return new FavoriteDTO(
                favorite.getId(),
                favorite.getName(),
                favorite.getSource(),
                favorite.getFdcId(),
                favorite.getCalories(),
                favorite.getProtein(),
                favorite.getServings(),
                favorite.getServingDescription()
        );
    }
}
