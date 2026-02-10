package se.sofiekl.macromealplanner.controller;

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
     * Gets daily summary for specific date nd user.
     * @param userId The user
     * @param date The date
     * @return a DailySummareDTO containing date, macrototals, macrogoals and progress.
     */
    @GetMapping("/{userId}/{date}")
    public DailySummaryDTO getDailySummary(
            @PathVariable("userId") Long userId,
            @PathVariable("date") LocalDate date){
        return dailySummaryService.getDailySummary(userId,date);
    }

    @GetMapping("/{userId}/today")
    public DailySummaryDTO getTodaySummary(@PathVariable Long userId){
        return dailySummaryService.getDailySummary(userId,LocalDate.now());
    }
}
