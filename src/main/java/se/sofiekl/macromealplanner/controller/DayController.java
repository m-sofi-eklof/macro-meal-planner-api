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
@RequestMapping("/api/weeks/{weekId}/days")
public class DayController {

    private final DayService dayService;

    public DayController(DayService dayService) {
        this.dayService = dayService;
    }

    /**
     * Create or get a day
     * @see DayRequestDTO
     */
    @PostMapping
    public ResponseEntity<DayResponseDTO> addOrGetDay(
            @Valid @RequestBody DayRequestDTO request,
            @PathVariable Long weekId
    ) {
        DayResponseDTO response = dayService.createOrGetDay(request, weekId);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{dayId}")
    public ResponseEntity<DayResponseDTO> getDay(
            @PathVariable Long dayId,
            @PathVariable Long weekId
    ){
        DayResponseDTO response = dayService.getDayById(dayId, weekId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * Get all days belonging to a User
     * @return List of Day response data
     */
    @GetMapping("/all")
    public ResponseEntity<List<DayResponseDTO>> getAllDays() {
        List<DayResponseDTO> days = dayService.getAllDaysForUser();
        return ResponseEntity.ok(days);
    }


    /**
     * Get all days of a week
     * @param weekId The ID of the Week
     * @return ResponseEntity containing the list of day-data for each day of the week
     */
    @GetMapping()
    public ResponseEntity<List<DayResponseDTO>> getDaysForWeek(
            @PathVariable Long weekId
    ) {
        List<DayResponseDTO> days = dayService.getDaysForWeek(weekId);
        return ResponseEntity.ok(days);
    }


    /**
     * Delete a day.
     * @param dayId The id of the day
     * @return Empty response entity
     */
    @DeleteMapping("/{dayId}")
    public ResponseEntity<Void> deleteDay(
            @PathVariable Long dayId
    ) {
        dayService.deleteDay(dayId);
        return ResponseEntity.noContent().build();
    }
}
