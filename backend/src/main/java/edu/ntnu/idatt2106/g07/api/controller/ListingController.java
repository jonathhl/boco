package edu.ntnu.idatt2106.g07.api.controller;

import edu.ntnu.idatt2106.g07.api.dto.listing.ListingsDTO;
import edu.ntnu.idatt2106.g07.api.dto.listing.ListingDTO;
import edu.ntnu.idatt2106.g07.api.dto.MessageDTO;
import edu.ntnu.idatt2106.g07.api.entity.Listing;
import edu.ntnu.idatt2106.g07.api.service.ListingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Controller for managing listings.
 * 
 * @see Listing
 */
@RestController
@RequestMapping("/listing")
public class ListingController {
    private final ListingService listingService;

    @Autowired
    public ListingController(ListingService listingService) {
        this.listingService = listingService;
    }

    /**
     * Creates a new listing.
     * 
     * @param data
     *            Listing to create.
     * @param principal
     *            Active user.
     * 
     * @return New listing.
     */
    @PostMapping
    public ResponseEntity<Object> postListing(@RequestBody ListingDTO data, Principal principal) {
        Optional<Listing> listing = listingService.createListing(data.getTitle(), data.getDescription(),
                data.getPrice(), data.getAddress(), data.getImage(), data.getPhone(), principal.getName());

        if (listing.isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unable to save listing.");
        }

        return ResponseEntity.ok(new ListingDTO(listing.get()));
    }

    /**
     * Gets the listing with the given id.
     * 
     * @param id
     *            Listing to retrieve.
     * 
     * @return Listing.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Object> getListingById(@PathVariable Long id) {
        Optional<Listing> listing = listingService.getListingById(id);

        if (listing.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageDTO("Listing not found."));
        }

        return ResponseEntity.ok(new ListingDTO(listing.get()));
    }

    /**
     * Updates the listing with the given id.
     * 
     * @param id
     *            Listing to update.
     * @param data
     *            Data to update.
     * @param principal
     *            Active user.
     * 
     * @return Updated listing.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateListingById(@PathVariable Long id, @RequestBody ListingDTO data,
            Principal principal) {
        Listing updatedListing = listingService.updateListingById(id, data, principal.getName());

        if (updatedListing == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageDTO("Listing not found."));
        }

        if (!principal.getName().equals(updatedListing.getUser().getEmail())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new MessageDTO("No permission to edit this listing."));
        }

        return ResponseEntity.ok(new ListingDTO(updatedListing));
    }

    /**
     * Gets all listings.
     * 
     * @return All listings.
     */
    @GetMapping
    public ResponseEntity<Object> getListings() {
        List<ListingDTO> list = listingService.getListings().stream().map(ListingDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    /**
     * Gets all listings the active user.
     * 
     * @param principal
     *            Active user.
     * 
     * @return Personal listings.
     */
    @GetMapping("/user")
    public ResponseEntity<Object> getPersonalListings(Principal principal) {
        List<ListingsDTO> list = listingService.getListingsByEmail(principal.getName()).stream().map(ListingsDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    /**
     * Gets all listings matching the given query.
     * 
     * @param query
     *            Search query.
     * 
     * @return All matching listings.
     */
    @GetMapping("search/{query}")
    public ResponseEntity<Object> getListingsByQuery(@PathVariable String query) {
        List<ListingsDTO> list = listingService.getListingsByQuery(query).stream().map(ListingsDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    /**
     * Deletes the listing with the given id.
     * 
     * @param id
     *            Listing to delete.
     * 
     * @return Response OK.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteListingById(@PathVariable Long id) {
        return ResponseEntity.ok(listingService.deleteListing(id));
    }
}