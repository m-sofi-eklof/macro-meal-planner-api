package se.sofiekl.macromealplanner.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import se.sofiekl.macromealplanner.dto.FavoriteMealDTO;
import se.sofiekl.macromealplanner.dto.FavoriteMealItemDTO;
import se.sofiekl.macromealplanner.dto.FoodItemResponseDTO;
import se.sofiekl.macromealplanner.mapper.FoodItemMapper;
import se.sofiekl.macromealplanner.model.*;
import se.sofiekl.macromealplanner.repository.*;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteMealService {

    private final FavoriteMealRepository favoriteMealRepository;
    private final FavoriteMealItemRepository favoriteMealItemRepository;
    private final MealRepository mealRepository;
    private final FoodItemRepository foodItemRepository;
    private final UserRepository userRepository;
    private final FoodItemMapper foodItemMapper;

    public FavoriteMealService(FavoriteMealRepository favoriteMealRepository,
                               FavoriteMealItemRepository favoriteMealItemRepository,
                               MealRepository mealRepository,
                               FoodItemRepository foodItemRepository,
                               UserRepository userRepository,
                               FoodItemMapper foodItemMapper) {
        this.favoriteMealRepository = favoriteMealRepository;
        this.favoriteMealItemRepository = favoriteMealItemRepository;
        this.mealRepository = mealRepository;
        this.foodItemRepository = foodItemRepository;
        this.userRepository = userRepository;
        this.foodItemMapper = foodItemMapper;
    }

    public List<FavoriteMealDTO> getUserFavoriteMeals() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found with username: " + username));

        return favoriteMealRepository.findAllByUserId(user.getId()).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public FavoriteMealDTO saveMealAsFavorite(Long mealId, String name) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found with username: " + username));

        Meal meal = mealRepository.findById(mealId)
                .orElseThrow(() -> new EntityNotFoundException("Meal not found with id: " + mealId));

        if (!meal.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You lack permission to save this meal as a favorite");
        }

        FavoriteMeal favoriteMeal = new FavoriteMeal();
        favoriteMeal.setUser(user);
        favoriteMeal.setName(name);

        FavoriteMeal saved = favoriteMealRepository.save(favoriteMeal);

        List<FavoriteMealItem> items = meal.getFoodItems().stream()
                .map(foodItem -> {
                    FavoriteMealItem item = new FavoriteMealItem();
                    item.setFavoriteMeal(saved);
                    item.setName(foodItem.getName());
                    item.setSource(foodItem.getSource());
                    item.setFdcId(foodItem.getFdcId());
                    item.setCalories(foodItem.getCalories());
                    item.setProtein(foodItem.getProtein());
                    item.setServings(foodItem.getServings());
                    item.setServingDescription(null);
                    return item;
                })
                .collect(Collectors.toList());

        favoriteMealItemRepository.saveAll(items);
        saved.setItems(items);

        return toDTO(saved);
    }

    @Transactional
    public void deleteFavoriteMeal(Long id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found with username: " + username));

        FavoriteMeal favoriteMeal = favoriteMealRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Favorite meal not found with id: " + id));

        favoriteMealRepository.delete(favoriteMeal);
    }

    @Transactional
    public List<FoodItemResponseDTO> quickAddMeal(Long mealId, Long favoriteMealId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found with username: " + username));

        Meal meal = mealRepository.findById(mealId)
                .orElseThrow(() -> new EntityNotFoundException("Meal not found with id: " + mealId));

        if (!meal.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You lack permission to add food to this meal");
        }

        FavoriteMeal favoriteMeal = favoriteMealRepository.findByIdAndUserId(favoriteMealId, user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Favorite meal not found with id: " + favoriteMealId));

        List<FoodItem> newItems = favoriteMeal.getItems().stream()
                .map(favItem -> {
                    FoodItem foodItem = new FoodItem();
                    foodItem.setUser(user);
                    foodItem.setMeal(meal);
                    foodItem.setName(favItem.getName());
                    foodItem.setSource(favItem.getSource());
                    foodItem.setFdcId(favItem.getFdcId());
                    foodItem.setCalories(favItem.getCalories());
                    foodItem.setProtein(favItem.getProtein());
                    foodItem.setServings(favItem.getServings());
                    return foodItem;
                })
                .collect(Collectors.toList());

        List<FoodItem> saved = foodItemRepository.saveAll(newItems);

        return saved.stream()
                .map(foodItemMapper::toFoodItemResponseDTO)
                .collect(Collectors.toList());
    }

    private FavoriteMealDTO toDTO(FavoriteMeal favoriteMeal) {
        List<FavoriteMealItemDTO> itemDTOs = favoriteMeal.getItems().stream()
                .map(item -> new FavoriteMealItemDTO(
                        item.getId(),
                        item.getName(),
                        item.getSource(),
                        item.getFdcId(),
                        item.getCalories(),
                        item.getProtein(),
                        item.getServings(),
                        item.getServingDescription()
                ))
                .collect(Collectors.toList());

        return new FavoriteMealDTO(
                favoriteMeal.getId(),
                favoriteMeal.getName(),
                itemDTOs
        );
    }
}
