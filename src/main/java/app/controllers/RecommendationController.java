package app.controllers;

import app.dtos.responding.ResponseRecommendationDTO;
import app.services.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}