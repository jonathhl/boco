package edu.ntnu.idatt2106.g07.api.controller;

import edu.ntnu.idatt2106.g07.api.dto.MessageDTO;
import edu.ntnu.idatt2106.g07.api.dto.rating.RatingDTO;
import edu.ntnu.idatt2106.g07.api.dto.rating.RatingsDTO;
import edu.ntnu.idatt2106.g07.api.entity.Rating;
import edu.ntnu.idatt2106.g07.api.entity.Request;
import edu.ntnu.idatt2106.g07.api.entity.User;
import edu.ntnu.idatt2106.g07.api.service.RatingService;
import edu.ntnu.idatt2106.g07.api.service.RequestService;
import edu.ntnu.idatt2106.g07.api.service.UserService;
import edu.ntnu.idatt2106.g07.api.websocket.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Controller for managing user ratings.
 * 
 * @see Rating
 * @see User
 */
@RestController
@RequestMapping("/rating")
public class RatingController {
    private final RatingService ratingService;
    private final UserService userService;
    private final WebSocketService webSocketService;
    private final RequestService requestService;

    @Autowired
    public RatingController(RatingService ratingService, UserService userService, WebSocketService webSocketService,
            RequestService requestService) {
        this.ratingService = ratingService;
        this.userService = userService;
        this.webSocketService = webSocketService;
        this.requestService = requestService;
    }

    /**
     * Fills a user rating.
     *
     * Note: Empty ratings are created automatically when requests are made.
     *
     * @param id
     *            Rating to fill.
     * 
     * @param data
     *            Rating data.
     * 
     * @return Updated rating.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Object> fillRating(@PathVariable Long id, @RequestBody RatingDTO data) {
        Optional<Rating> rating = ratingService.fillRating(id, data);

        if (rating.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageDTO("Could not add rating"));
        }

        return ResponseEntity.ok(new RatingDTO(rating.get()));
    }

    /**
     * Gets average rating and rating list.
     * 
     * @param email
     *            User whose ratings to get.
     * 
     * @return Ratings.
     */
    @GetMapping("/{email}")
    public ResponseEntity<Object> getRatings(@PathVariable String email) {
        Optional<User> userOptional = userService.getUserByEmail(email);

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageDTO("User not found"));
        }

        User user = userOptional.get();
        Double average = ratingService.getAverageRating(user);
        List<Rating> ratings = ratingService.getRatings(user);

        return ResponseEntity.ok()
                .body(new RatingsDTO(average, ratings.stream().map(RatingDTO::new).collect(Collectors.toList())));
    }
}
