package app.controllers;

import app.dtos.creating.CreateRecommendationDTO;
import app.dtos.responding.ResponseCafeDTO;
import app.models.Cafe;
import app.models.Recommendation;
import app.services.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

    @PostMapping
    public ResponseEntity<Recommendation> createRecommendation(@RequestBody CreateRecommendationDTO dto) {
        Recommendation createdRecommendation = recommendationService.createRecommendation(dto);
        return new ResponseEntity<>(createdRecommendation, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Recommendation> getRecommendationById(@PathVariable Long id) {
        return ResponseEntity.ok(recommendationService.getRecommendationById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecommendation(@PathVariable Long id) {
        recommendationService.deleteRecommendationById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ResponseCafeDTO>> getPersonalizedRecommendations(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "5") int limit) {

        List<Cafe> recommendedCafes = recommendationService.getRecommendationsForUser(userId, limit);
      
        List<ResponseCafeDTO> dtos = recommendedCafes.stream()
                .map(cafe -> new ResponseCafeDTO(
                    cafe.getId(),
                    cafe.getName(),
                    cafe.getAddress(),
                    cafe.getHasWifi(),
                    cafe.getAllowsPets(),
                    cafe.getSellsFood(),
                    cafe.getAllowsStudentsDiscounts(),
                    cafe.getIsLgbtqFriendly(),
                    cafe.getHasTerrace(),
                    cafe.getAllowsIntake(),
                    cafe.getAllowsTakeaway(),
                    cafe.getSupportsCardPayments()
                ))
                .collect(Collectors.toList());
                
        return ResponseEntity.ok(dtos);
    }
}