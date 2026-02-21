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
@RequestMapping("/api/weeks")
public class WeekController {

    private final WeekService weekService;

    public WeekController(WeekService weekService) {
        this.weekService = weekService;
    }


    /**
     * Create or get week for a specific date.
     * @param weekRequestDTO The week request data
     * @return The week response dto + 201CREATED
     */
    @PostMapping()
    public ResponseEntity<WeekResponseDTO> createOrGetWeek(
            @Valid @RequestBody WeekRequestDTO weekRequestDTO
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(weekService.createOrGetWeek(weekRequestDTO));
    }

    /**
     * Get a specific week by ID
     * @param weekId The ID of the Week
     * @return The WeekResponseDTO
     */
    @GetMapping("/{weekId}")
    public ResponseEntity<WeekResponseDTO> getWeekById(
            @PathVariable Long weekId
    ) {
        WeekResponseDTO week = weekService.getWeekById(weekId);
        return ResponseEntity.ok(week);
    }

    @GetMapping()
    public ResponseEntity<List<WeekResponseDTO>> findAllByUserId() {
        return ResponseEntity.ok(weekService.getAllWeeksForUser());
    }

    @GetMapping("/current")
    public ResponseEntity<WeekResponseDTO> getCurrentWeek(){
        return ResponseEntity.ok(weekService.getOrCreateCurrentWeek());
    }

    /**
     * Get the following week from week ID
     * @param weekId The id of a week
     * @return 200ok + weekresponsedto for the next week
     */
    @GetMapping("/{weekId}/next")
    public ResponseEntity<WeekResponseDTO> getNextWeek(@PathVariable Long weekId){
        return ResponseEntity.ok(weekService.getNext(weekId));
    }

    /**
     * Get the previous week
     * @param weekId The id of a week
     * @return 200OK + the response dto for the previous week
     */
    @GetMapping("/{weekId}/prev")
    public ResponseEntity<WeekResponseDTO> getPreviousWeek(@PathVariable Long weekId){
        return ResponseEntity.ok(weekService.getPrev(weekId));
    }

    @DeleteMapping("/{weekId}")
    public ResponseEntity<Void> deleteWeekById(
            @PathVariable Long weekId
    ){
        weekService.deleteWeek(weekId);
        return ResponseEntity.noContent().build();
    }
}
