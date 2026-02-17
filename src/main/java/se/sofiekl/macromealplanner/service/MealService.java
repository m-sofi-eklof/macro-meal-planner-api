package se.sofiekl.macromealplanner.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import se.sofiekl.macromealplanner.dto.MealRequestDTO;
import se.sofiekl.macromealplanner.dto.MealResponseDTO;
import se.sofiekl.macromealplanner.mapper.MealMapper;
import se.sofiekl.macromealplanner.model.Day;
import se.sofiekl.macromealplanner.model.Meal;
import se.sofiekl.macromealplanner.model.MealType;
import se.sofiekl.macromealplanner.model.User;
import se.sofiekl.macromealplanner.repository.DayRepository;
import se.sofiekl.macromealplanner.repository.MealRepository;
import se.sofiekl.macromealplanner.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MealService {

    private final MealRepository mealRepository;
    private final DayRepository dayRepository;
    private final UserRepository userRepository;
    private final MealMapper mealMapper;

    public MealService(MealRepository mealRepository, DayRepository dayRepository, UserRepository userRepository, MealMapper mealMapper) {
        this.mealRepository = mealRepository;
        this.dayRepository = dayRepository;
        this.userRepository = userRepository;
        this.mealMapper = mealMapper;
    }

    /**
     * Create a meal for a specific day
     * @param dayId The day the meal belongs to
     * @param request The request data of the meal
     * @return A meal response dto
     */
    @Transactional
    public MealResponseDTO createMeal(Long dayId, MealRequestDTO request){

        //get logged-in user
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(()-> new EntityNotFoundException("User not found with username: " + username));

        Long userId = user.getId();

        //get day
        Day day = dayRepository.findById(dayId)
                .orElseThrow(()->new EntityNotFoundException("Day not found with id:" + dayId));

        //verify day belongs to user
        if(!day.getUser().getId().equals(user.getId())){
            throw new IllegalArgumentException("Day does not belong to user");
        }

        //create meal
        Meal meal = new Meal();
        meal.setType(MealType.valueOf(request.type().toUpperCase()));
        meal.setDay(day);
        meal.setUser(user);

        Meal savedMeal = mealRepository.save(meal);
        return mealMapper.toMealResponseDTO(savedMeal);
    }

    /**
     * Get all meals for a specific day.
     * @param dayId The id of the day
     * @return A list of meal response data transfer objects
     */
    public List<MealResponseDTO> getAllMealsForDay( Long dayId){
        //get logged-in user
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(()-> new EntityNotFoundException("User not found with username: " + username));

        Long userId = user.getId();

        //verify day exists and belongs to user
        Day day = dayRepository.findById(dayId)
                .orElseThrow(()->new EntityNotFoundException("Day not found with id:" + dayId));
        if (!day.getUser().getId().equals(userId)){
            throw new IllegalArgumentException("Day does not belong to user");
        }

        //get meals
        List<Meal> meals = mealRepository.findAllByDayId(dayId);

        return meals.stream()
                .map(mealMapper::toMealResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get a specific meal.
     * @param mealId The id of the meal.
     * @return A MealResponseDTO of the meal
     */
    public MealResponseDTO getMeal(Long mealId){
        //get logged-in user
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(()-> new EntityNotFoundException("User not found with username: " + username));

        Long userId = user.getId();

        //find or throw exception
        Meal meal = mealRepository.findById(mealId)
                .orElseThrow(()->new EntityNotFoundException("Meal not found with id:" + mealId));

        //verify meal belongs to user
        if (!meal.getUser().getId().equals(userId)){
            throw new IllegalArgumentException("Meal does not belong to user");
        }

        return mealMapper.toMealResponseDTO(meal);

    }

    @Transactional
    public void deleteMeal(Long mealId){
        //get logged-in user
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(()-> new EntityNotFoundException("User not found with username: " + username));

        Long userId = user.getId();

        Meal meal = mealRepository.findById(mealId)
                .orElseThrow(()->new EntityNotFoundException("Meal not found with id:" + mealId));
        if (!meal.getUser().getId().equals(userId)){
            throw new IllegalArgumentException("Meal does not belong to user");
        }
        mealRepository.delete(meal);
    }
}
