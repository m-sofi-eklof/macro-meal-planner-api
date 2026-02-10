package se.sofiekl.macromealplanner.service;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import se.sofiekl.macromealplanner.dto.DayRequestDTO;
import se.sofiekl.macromealplanner.dto.DayResponseDTO;
import se.sofiekl.macromealplanner.mapper.DayMapper;
import se.sofiekl.macromealplanner.mapper.MealMapper;
import se.sofiekl.macromealplanner.model.Day;
import se.sofiekl.macromealplanner.model.User;
import se.sofiekl.macromealplanner.repository.DayRepository;
import se.sofiekl.macromealplanner.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DayService {
    private final DayRepository dayRepository;
    private final UserRepository userRepository;
    private final DayMapper dayMapper;
    private final MealMapper mealMapper;

    public DayService(DayRepository dayRepository, UserRepository userRepository, DayMapper dayMapper, MealMapper mealMapper) {
        this.dayRepository = dayRepository;
        this.userRepository = userRepository;
        this.mealMapper = mealMapper;
        this.dayMapper = dayMapper;
    }

    /**
     * Create or get a day for a specific user
     * If day already exists, returns existing day
     */
    @Transactional
    public DayResponseDTO createOrGetDay(DayRequestDTO request, Long userId) {
        //Check user exists
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new EntityNotFoundException("User not found with id: " + userId));

        //Check day exists
        Day existingDay = dayRepository.findByUserIdAndDate(userId, request.date())
                .orElse(null);
        if (existingDay != null) {
            return dayMapper.toDTO(existingDay);
        }

        //Create new day
        Day newDay = new Day();
        newDay.setDate(request.date());
        newDay.setUser(user);

        Day savedDay = dayRepository.save(newDay);
        return dayMapper.toDTO(savedDay);
    }

    /**
     * Get a specific day from a date
     */
    public DayResponseDTO getDayByDate(Long userId, LocalDate date){
        Day day = dayRepository.findByUserIdAndDate(userId,date)
                .orElseThrow(()-> new EntityNotFoundException("No day found for user " + userId + "on date: " + date));
        return dayMapper.toDTO(day);
    }

    /**
     * Get all days for a specific user
     */
    public List<DayResponseDTO> getAllDaysForUser(Long userId){
        List<Day> allDays = dayRepository.findByUserId(userId);
        return allDays.stream()
                .map(dayMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get days for a specific week
     */
    public List<DayResponseDTO>getDaysForWeek(Long userId, LocalDate weekStart){
        LocalDate weekEnd = weekStart.plusDays(7);
        List<Day> allDays = dayRepository.findDayByUserIdAndDateBetween(userId, weekStart,weekEnd);

        return allDays.stream()
                .map(dayMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Delete a day
     */
    @Transactional
    public void deleteDay(Long dayId){
        Day day = dayRepository.findById(dayId)
                .orElseThrow(()-> new EntityNotFoundException("Day not found with id: " + dayId));
        dayRepository.delete(day);
    }
}
