package se.sofiekl.macromealplanner.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import se.sofiekl.macromealplanner.dto.WeekRequestDTO;
import se.sofiekl.macromealplanner.dto.WeekResponseDTO;
import se.sofiekl.macromealplanner.mapper.WeekMapper;
import se.sofiekl.macromealplanner.model.User;
import se.sofiekl.macromealplanner.model.Week;
import se.sofiekl.macromealplanner.repository.UserRepository;
import se.sofiekl.macromealplanner.repository.WeekRepository;

import java.time.LocalDate;
import java.time.temporal.IsoFields;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WeekService {

    private final WeekRepository weekRepository;
    private final WeekMapper weekMapper;
    private final UserRepository userRepository;

    public WeekService(WeekRepository weekRepository, WeekMapper weekMapper,  UserRepository userRepository) {
        this.weekRepository = weekRepository;
        this.weekMapper = weekMapper;
        this.userRepository = userRepository;
    }


    /**
     * Get a specific week by ID
     * @param userId The user who owns the week
     * @param weekId The id of the Week
     * @return WeekResponseDTO
     */
    public WeekResponseDTO getWeekById(Long userId, Long weekId) {
        Week week = weekRepository.findById(weekId)
                .orElseThrow(() -> new EntityNotFoundException("Week not found with id: " + weekId));

        // Verifiera att week tillhör rätt user
        if (!week.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("Week does not belong to user");
        }

        return weekMapper.toDTO(week);
    }


    /**
     * Get or create the current week
     * @param userId The user for whom to create the week / user who owns the week
     * @return WeekResponseDTO for the current week and passed user
     */
    public WeekResponseDTO getOrCreateCurrentWeek(Long userId){
        LocalDate today = LocalDate.now();
        return createOrGetWeek(new WeekRequestDTO(today),  userId);
    }

    /**
     * Create or get a week
     * @param request A request DTO containing a date in the week
     * @param userId The user the week belongs to
     * @return The week's response data
     */
    @Transactional
    public WeekResponseDTO createOrGetWeek(WeekRequestDTO request, Long userId) {

        //check user exists
        User user = userRepository.findById(userId)
                .orElseThrow(()->new EntityNotFoundException("User not found with id: " + userId));

        //check week exists
        Optional<Week> existingWeek = weekRepository.findByUserIdAndWeekNumberAndYear(
                userId,
                request.date().get(IsoFields.WEEK_OF_WEEK_BASED_YEAR),
                request.date().get(IsoFields.WEEK_BASED_YEAR)
        );
        if (existingWeek.isPresent()) {
            return weekMapper.toDTO(existingWeek.get());
        }

        //calculate start of week
        Integer differenceFromMondayDayValue = request.date().getDayOfWeek().getValue()-1;
        LocalDate startOfWeek = request.date().minusDays(differenceFromMondayDayValue);

        //create week
        Week week = new Week();
        week.setUser(user);
        week.setStartDate(startOfWeek);
        week.setEndDate(startOfWeek.plusDays(6));
        week.setYear(request.date().get(IsoFields.WEEK_BASED_YEAR));
        week.setWeekNumber(request.date().get(IsoFields.WEEK_OF_WEEK_BASED_YEAR));

        //save and return
        weekRepository.save(week);
        return weekMapper.toDTO(week);
    }

    /**
     * Get all weeks for a User
     * @param userId The Id of the User
     * @return List of all WeekResponseDTOs belonging the User
     */
    public List<WeekResponseDTO> getAllWeeksForUser(Long userId) {
        //check if user exists
        userRepository.findById(userId)
                .orElseThrow(()->new EntityNotFoundException("User not found with id: " + userId));
        //find all weeks
        List<Week> weeks = weekRepository.findAllByUserId(userId);
        //return
        return weeks.stream().map(weekMapper::toDTO).collect(Collectors.toList());
    }

    /**
     * Delete a Week
     * @param id The ID of the Week
     * @param userId The ID of the User
     */
    public void deleteWeek(Long id, Long userId) {
        //check if week exists
        Week week = weekRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException("Week not found with id: " + id));
        //confirm week belongs to user
        if(!week.getUser().getId().equals(userId)){
            throw new IllegalArgumentException("Week does not belong to this user");
        }
        //delete
        weekRepository.deleteById(id);
    }
}
