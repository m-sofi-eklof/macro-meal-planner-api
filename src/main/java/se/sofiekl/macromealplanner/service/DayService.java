package se.sofiekl.macromealplanner.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import se.sofiekl.macromealplanner.dto.DayRequestDTO;
import se.sofiekl.macromealplanner.dto.DayResponseDTO;
import se.sofiekl.macromealplanner.mapper.DayMapper;
import se.sofiekl.macromealplanner.mapper.MealMapper;
import se.sofiekl.macromealplanner.model.Day;
import se.sofiekl.macromealplanner.model.User;
import se.sofiekl.macromealplanner.model.Week;
import se.sofiekl.macromealplanner.repository.DayRepository;
import se.sofiekl.macromealplanner.repository.UserRepository;
import se.sofiekl.macromealplanner.repository.WeekRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DayService {
    private final DayRepository dayRepository;
    private final UserRepository userRepository;
    private final DayMapper dayMapper;
    private final MealMapper mealMapper;
    private final WeekRepository weekRepository;
    private final WeekService weekService;

    public DayService(DayRepository dayRepository, UserRepository userRepository, DayMapper dayMapper, MealMapper mealMapper, WeekRepository weekRepository, WeekService weekService) {
        this.dayRepository = dayRepository;
        this.userRepository = userRepository;
        this.mealMapper = mealMapper;
        this.dayMapper = dayMapper;
        this.weekRepository = weekRepository;
        this.weekService = weekService;
    }

    /**
     * Create or get a day for a specific user
     * If day already exists, returns existing day
     */
    @Transactional
    public DayResponseDTO createOrGetDay(DayRequestDTO request, Long weekId) {

        //get logged-in user
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(()-> new EntityNotFoundException("User not found with username: " + username));

        Long userId = user.getId();

        //Check day already exists
        Day existingDay = dayRepository.findByUserIdAndDate(userId, request.date())
                .orElse(null);
        if (existingDay != null) {
            return dayMapper.toDTO(existingDay);
        }

        //get week
        Week week = weekRepository.findByIdAndUserId(weekId, userId)
                .orElseThrow(()-> new EntityNotFoundException("Week not found with weekId: " + weekId + " and userId: " + userId));

        //check date is within week ange
        if(request.date().isBefore(week.getStartDate()) || request.date().isAfter(week.getEndDate())) {
            throw new IllegalArgumentException("This date is not within the date range of this week");
        }
        //Create new day
        Day newDay = new Day();
        newDay.setDate(request.date());
        newDay.setUser(user);
        newDay.setWeek(week);

        Day savedDay = dayRepository.save(newDay);
        return dayMapper.toDTO(savedDay);
    }

    public DayResponseDTO getDayById(Long id, Long weekId) {
        //get logged-in user
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(()-> new EntityNotFoundException("User not found with username: " + username));

        Long userId = user.getId();

        //get day
        Day day = dayRepository.findByIdAndUserId(id, userId)
                .orElseThrow(()-> new EntityNotFoundException("Day not found with dayId: " + id + " and userId: " + userId));
        //return
        return dayMapper.toDTO(day);
    }

    /**
     * Get all days for a specific user
     */
    public List<DayResponseDTO> getAllDaysForUser(){
        //get logged-in user
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(()-> new EntityNotFoundException("User not found with username: " + username));

        Long userId = user.getId();

        List<Day> allDays = dayRepository.findByUserId(userId);
        return allDays.stream()
                .map(dayMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get days for a specific week
     */
    public List<DayResponseDTO>getDaysForWeek(Long weekId){
        //get logged-in user
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(()-> new EntityNotFoundException("User not found with username: " + username));

        Long userId = user.getId();

        //get week
        Week week = weekRepository.findById(weekId)
                .orElseThrow(()-> new EntityNotFoundException("Week not found with id: " + weekId));
        LocalDate startOfWeek = week.getStartDate();
        LocalDate endOfWeek = week.getEndDate();

        List<Day> allDays = dayRepository.findDayByUserIdAndDateBetween(userId, startOfWeek, endOfWeek);

        return allDays.stream()
                .map(dayMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Delete a day
     */
    @Transactional
    public void deleteDay(Long dayId){
        //get logged-in user
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(()-> new EntityNotFoundException("User not found with username: " + username));

        Long userId = user.getId();

        Day day = dayRepository.findById(dayId)
                .orElseThrow(()-> new EntityNotFoundException("Day not found with id: " + dayId));

        // Check day belongs to user
        if(!day.getUser().getId().equals(userId)){
            throw new IllegalArgumentException("This day does not belong to this User");
        }

        dayRepository.delete(day);
    }
}
