package app.controllers;

import app.dtos.responding.ResponseRecommendationDTO;
import app.models.Cafe;
import app.services.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/recommendations")
public class RecommendationController {
    private final RecommendationService recommendationService;

    @GetMapping
    public ResponseEntity<List<ResponseRecommendationDTO>> getRecommendationsByUserId(@PathVariable Long userId) {
        return new ResponseEntity<>(recommendationService.getRecommendationsByUserId(userId), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecommendationById(@PathVariable Long userId, @PathVariable Long id) {
        recommendationService.deleteRecommendationById(id, userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<app.dtos.responding.ResponseCafeDTO>> getPersonalizedRecommendations(
        @PathVariable Long userId,
        @RequestParam(defaultValue = "5") int limit) {
    
    List<Cafe> recommendedCafes = recommendationService.getRecommendationsForUser(userId, limit);
    
    List<app.dtos.responding.ResponseCafeDTO> dtos = recommendedCafes.stream()
            .map(cafe -> {
                return new app.dtos.responding.ResponseCafeDTO(
                    cafe.getId(), cafe.getName(), cafe.getAddress(), cafe.getHasWifi(),
                    cafe.getAllowsPets(), cafe.getSellsFood(), cafe.getAllowsStudentsDiscounts(),
                    cafe.getIsLgbtqFriendly(), cafe.getHasTerrace(), cafe.getAllowsIntake(),
                    cafe.getAllowsTakeaway(), cafe.getSupportsCardPayments()
                );
            })
            .collect(Collectors.toList());
            
    return ResponseEntity.ok(dtos);
}
}


