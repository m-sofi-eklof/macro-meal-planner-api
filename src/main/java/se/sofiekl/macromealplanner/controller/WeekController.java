package se.sofiekl.macromealplanner.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.sofiekl.macromealplanner.dto.WeekRequestDTO;
import se.sofiekl.macromealplanner.dto.WeekResponseDTO;
import se.sofiekl.macromealplanner.service.WeekService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users/{userId}/weeks")
public class WeekController {

    private final WeekService weekService;

    public WeekController(WeekService weekService) {
        this.weekService = weekService;
    }


    /**
     * Create or get week for a specific date
     * @param weekRequestDTO The week request data
     * @param userId The id of the User
     * @return The week response dto + 201CREATED
     */
    @PostMapping()
    public ResponseEntity<WeekResponseDTO> createOrGetWeek(
            @Valid @RequestBody WeekRequestDTO weekRequestDTO,
            @PathVariable Long userId
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(weekService.createOrGetWeek(weekRequestDTO, userId));
    }

    /**
     * Get a specific week by ID
     * @param userId The ID of the user
     * @param weekId The ID of the Week
     * @return The WeekResponseDTO
     */
    @GetMapping("/{weekId}")
    public ResponseEntity<WeekResponseDTO> getWeekById(
            @PathVariable Long userId,
            @PathVariable Long weekId
    ) {
        WeekResponseDTO week = weekService.getWeekById(userId, weekId);
        return ResponseEntity.ok(week);
    }

    @GetMapping()
    public ResponseEntity<List<WeekResponseDTO>> findAllByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(weekService.getAllWeeksForUser(userId));
    }

    @GetMapping("/current")
    public ResponseEntity<WeekResponseDTO> getCurrentWeek(
            @PathVariable Long userId)
    {
        return ResponseEntity.ok(weekService.getOrCreateCurrentWeek(userId));
    }

    @DeleteMapping("/{weekId}")
    public ResponseEntity<Void> deleteWeekById(
            @PathVariable Long userId,
            @PathVariable Long weekId
    ){
        weekService.deleteWeek(weekId, userId);
        return ResponseEntity.noContent().build();
    }
}
