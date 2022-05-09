package edu.ntnu.idatt2106.g07.api.service;

import edu.ntnu.idatt2106.g07.api.dto.RequestDTO;
import edu.ntnu.idatt2106.g07.api.entity.Chat;
import edu.ntnu.idatt2106.g07.api.entity.Listing;
import edu.ntnu.idatt2106.g07.api.entity.Request;
import edu.ntnu.idatt2106.g07.api.entity.User;
import edu.ntnu.idatt2106.g07.api.repository.ListingRepository;
import edu.ntnu.idatt2106.g07.api.repository.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service for managing requests.
 * 
 * @see Request
 */
@Service
public class RequestService {
    private final RequestRepository requestRepository;
    private final ListingRepository listingRepository;
    private final RatingService ratingService;
    private final ChatService chatService;
    private final UserService userService;
    private final NotificationService notificationService;

    @Autowired
    public RequestService(RequestRepository requestRepository, ListingRepository listingRepository,
            RatingService ratingService, ChatService chatService, UserService userService,
            NotificationService notificationService) {
        this.requestRepository = requestRepository;
        this.listingRepository = listingRepository;
        this.ratingService = ratingService;
        this.chatService = chatService;
        this.userService = userService;
        this.notificationService = notificationService;
    }

    /**
     * Gets the request with the given id.
     * 
     * @param requestId
     *            Request to retrieve.
     * 
     * @return Request.
     */
    public Optional<Request> getRequestByRequestId(Long requestId) {
        return requestRepository.findById(requestId);
    }

    /**
     * Updates the request with the given id.
     *
     * @param data
     *            Data to update and id.
     * 
     * @return Updated request.
     */
    public Optional<Request> updateRequest(RequestDTO data, String email) {
        Optional<Request> optionalRequest = requestRepository.findById(data.getRequestId());

        if (optionalRequest.isEmpty()) {
            return Optional.empty();
        }

        Request request = optionalRequest.get();
        Optional<User> optionalRecipient = userService.getUserByEmail(request.getFrom());

        User recipient = optionalRecipient.get();
        if (!Objects.equals(request.getListing().getUser().getEmail(), email)) {
            return Optional.empty();
        }

        Chat chat = chatService.getChatByUsers(recipient, request.getListing().getUser());

        // Notify owner of listing that their request has been updated
        notificationService.createNotification(recipient, chat);

        request.update(data);

        try {
            Request updatedRequest = requestRepository.save(request);
            return Optional.of(updatedRequest);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Creates a request with the given data.
     *
     * @param data
     *            Request data.
     * 
     * @return New request.
     */
    public Request saveRequest(RequestDTO data) {
        Optional<Listing> optionalListing = listingRepository.findById(data.getListing().getId());
        Optional<User> optionalSender = userService.getUserByEmail(data.getFrom());

        if (optionalListing.isEmpty() || optionalSender.isEmpty()) {
            return null;
        }

        Listing listing = optionalListing.get();
        Request request = new Request(data);
        Chat chat = chatService.getChatByUsers(listing.getUser(), optionalSender.get());

        request.setListing(listing);
        request = requestRepository.save(request);

        // Notify owner of listing that a new request has been received
        notificationService.createNotification(listing.getUser(), chat);

        // Add a rating with the given rating, so it can be filled later.
        ratingService.createRating(request);

        return request;
    }

    /**
     * Get all requests for the user with the given email.
     *
     * @param email
     *            User email.
     * 
     * @return Requests.
     */
    public List<Request> getAllRequests(String email) {
        return requestRepository.getAllByEmail(email);
    }

    /**
     * Gets all requests where both users are either sender or recipient.
     * 
     * @param userA
     *            First user.
     * @param userB
     *            Second user.
     * 
     * @return Requests.
     */
    public List<Request> getAllRequestsRegardingUsers(User userA, User userB) {
        List<Request> requests = requestRepository.findAll();

        return requests.stream().filter(
                request -> request.getEmail().equals(userA.getEmail()) && request.getListing().getUser().equals(userB)
                        || request.getEmail().equals(userB.getEmail()) && request.getListing().getUser().equals(userA))
                .collect(Collectors.toList());
    }
}
