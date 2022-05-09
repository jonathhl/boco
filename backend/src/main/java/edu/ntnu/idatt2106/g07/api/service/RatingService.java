package edu.ntnu.idatt2106.g07.api.service;

import edu.ntnu.idatt2106.g07.api.dto.rating.RatingDTO;
import edu.ntnu.idatt2106.g07.api.entity.*;
import edu.ntnu.idatt2106.g07.api.repository.RatingRepository;
import edu.ntnu.idatt2106.g07.api.repository.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service for managing user ratings.
 *
 * @see Rating
 */
@Service
public class RatingService {
    private final RatingRepository ratingRepository;
    private final NotificationService notificationService;
    private final ChatService chatService;
    private final UserService userService;

    @Autowired
    public RatingService(RatingRepository ratingRepository, RequestRepository requestRepository,
            NotificationService notificationService, ChatService chatService, UserService userService) {
        this.ratingRepository = ratingRepository;
        this.notificationService = notificationService;
        this.chatService = chatService;
        this.userService = userService;
    }

    /**
     * Creates a rating for the given request.
     *
     * @param request
     *            Request to create rating for.
     */
    public void createRating(Request request) {
        ratingRepository.save(new Rating(request));
    }

    /**
     * Fills the rating with the existing id.
     * 
     * @param data
     *            Data to fill.
     * 
     * @return Updated rating.
     */
    public Optional<Rating> fillRating(Long id, RatingDTO data) {
        Optional<Rating> optionalRating = ratingRepository.findById(id);

        // If rating cannot be found.
        if (optionalRating.isEmpty()) {
            return Optional.empty();
        }

        Rating rating = optionalRating.get();

        // If rating already exists.
        if (rating.getRating() != null) {
            return Optional.empty();
        }

        Optional<User> optionalSender = userService.getUserByEmail(rating.getFrom());

        if (optionalSender.isEmpty()) {
            return Optional.empty();
        }

        User sender = optionalSender.get();
        User recipient = rating.getRequest().getListing().getUser();

        Chat chat = chatService.getChatByUsers(sender, recipient);

        rating.setRating(data.getRating());
        rating.setReview(data.getReview());

        notificationService.createNotification(rating.getRequest().getListing().getUser(), chat);

        return Optional.of(ratingRepository.save(rating));
    }

    /**
     * Gets all ratings for a given user.
     * 
     * @param user
     *            User to get ratings for.
     * 
     * @return User ratings.
     */
    public List<Rating> getRatings(User user) {
        ArrayList<Request> requests = new ArrayList<>();
        user.getListings().stream().map(Listing::getRequests).forEach(requests::addAll);

        ArrayList<Rating> ratings = new ArrayList<>();
        requests.stream().map(Request::getRating).filter(Objects::nonNull).filter(it -> it.getRating() != null)
                .forEach(ratings::add);

        return ratings;
    }

    /**
     * Gets average rating for a given user.
     * 
     * @param user
     *            User to get average rating for.
     * 
     * @return Average rating.
     */
    public Double getAverageRating(User user) {
        List<Rating> ratings = getRatings(user);

        return ratings.stream().mapToDouble(Rating::getRating).filter(Objects::nonNull).average().orElse(0);
    }

    public List<Rating> getAllRatingsRegardingUsers(User userA, User userB) {
        List<Rating> ratings = ratingRepository.findAll();

        return ratings.stream()
                .filter(rating -> rating.getFrom().equals(userA.getEmail())
                        && rating.getRequest().getListing().getUser().getEmail().equals(userB.getEmail())
                        || rating.getFrom().equals(userB.getEmail())
                                && rating.getRequest().getListing().getUser().getEmail().equals(userA.getEmail()))
                .collect(Collectors.toList());
    }
}
