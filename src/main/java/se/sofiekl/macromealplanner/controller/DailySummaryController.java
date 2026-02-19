package se.sofiekl.macromealplanner.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.sofiekl.macromealplanner.dto.dailySummaryFlow.DailySummaryDTO;
import se.sofiekl.macromealplanner.service.DailySummaryService;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/daily-summary")
public class DailySummaryController {
    private final DailySummaryService dailySummaryService;

    public DailySummaryController(DailySummaryService dailySummaryService) {
        this.dailySummaryService = dailySummaryService;
    }

    /**
     * Gets daily summary for specific date and user.
     * @param date The date
     * @return a DailySummaryDTO containing date, macrototals, macrogoals and progress.
     */
    @GetMapping("/{date}")
    public ResponseEntity<DailySummaryDTO> getDailySummary(
            @PathVariable("date") LocalDate date){
        return ResponseEntity.ok(dailySummaryService.getDailySummary(date));
    }

    @GetMapping("/today")
    public ResponseEntity<DailySummaryDTO> getTodaySummary(){
        return ResponseEntity.ok(dailySummaryService.getDailySummary(LocalDate.now()));
    }
}
