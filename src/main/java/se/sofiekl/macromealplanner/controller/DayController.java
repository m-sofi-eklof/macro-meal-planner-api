package se.sofiekl.macromealplanner.controller;

import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.sofiekl.macromealplanner.dto.DayRequestDTO;
import se.sofiekl.macromealplanner.dto.DayResponseDTO;
import se.sofiekl.macromealplanner.service.DayService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/users/{userId}/days")
public class DayController {

    private final DayService dayService;

    public DayController(DayService dayService) {
        this.dayService = dayService;
    }

    /**
     * Create or get a day
     */
    @PostMapping
    public ResponseEntity<DayResponseDTO> addOrGetDay(
            @PathVariable Long userId,
            @Valid @RequestBody DayRequestDTO request) {
        DayResponseDTO response = dayService.createOrGetDay(request, userId);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    /**
     * Get a specifc day from date
     * @param userId The user to whom the dy belongs
     * @param date the date of the day
     * @return ResponseEntity<DayResponseDTO>
     */
    @GetMapping("/{date}")
    public ResponseEntity<DayResponseDTO> getDayByDate(
            @PathVariable Long userId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        DayResponseDTO response = dayService.getDayByDate(userId, date);
        return ResponseEntity.ok(response);
    }

    /**
     * Get all days belonging to a User
     * @param userId
     * @return
     */
    @GetMapping
    public ResponseEntity<List<DayResponseDTO>> getAllDays(
            @PathVariable Long userId
    ) {
        List<DayResponseDTO> days = dayService.getAllDaysForUser(userId);
        return ResponseEntity.ok(days);
    }


    /**
     * Get all days of a week
     * @param userId the User who owns this week
     * @param startDate the date on which the week starts
     * @return ResponseEntity containing the list of day-data for each day of the week
     */
    @GetMapping("/week")
    public ResponseEntity<List<DayResponseDTO>> getDaysForWeek(
            @PathVariable Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate
    ) {
        List<DayResponseDTO> days = dayService.getDaysForWeek(userId, startDate);
        return ResponseEntity.ok(days);
    }


    /**
     * Delete a day.
     * @param userId The user who owns the day
     * @param dayId The id of the day
     * @return Empty response entity
     */
    @DeleteMapping("/{dayId}")
    public ResponseEntity<Void> deleteDay(
            @PathVariable Long userId,
            @PathVariable Long dayId
    ) {
        dayService.deleteDay(dayId);
        return ResponseEntity.noContent().build();
    }
}
