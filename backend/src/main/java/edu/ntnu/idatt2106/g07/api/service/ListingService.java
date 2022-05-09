package edu.ntnu.idatt2106.g07.api.service;

import edu.ntnu.idatt2106.g07.api.dto.listing.ListingDTO;
import edu.ntnu.idatt2106.g07.api.dto.MessageDTO;
import edu.ntnu.idatt2106.g07.api.entity.Listing;
import edu.ntnu.idatt2106.g07.api.entity.User;
import edu.ntnu.idatt2106.g07.api.repository.ListingRepository;
import edu.ntnu.idatt2106.g07.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service for managing listings.
 * 
 * @see Listing
 */
@Service
public class ListingService {
    private final ListingRepository listingRepository;
    private final UserRepository userRepository;

    @Autowired
    public ListingService(ListingRepository listingRepository, UserRepository userRepository) {
        this.listingRepository = listingRepository;
        this.userRepository = userRepository;
    }

    /**
     * Create a new listing
     * 
     * @param title
     *            Listing title.
     * @param description
     *            Listing description.
     * @param price
     *            Listing price.
     * @param address
     *            Listing address.
     * @param phone
     *            Listing phone number.
     * @param email
     *            Listing email.
     * 
     * @return New listing.
     */
    public Optional<Listing> createListing(String title, String description, double price, String address, Long image,
            String phone, String email) {
        Optional<User> user = userRepository.findById(email);
        if (user.isEmpty()) {
            return Optional.empty();
        }

        Listing listing = new Listing(title, description, price, address, phone, image, user.get());

        return Optional.of(listingRepository.save(listing));
    }

    /**
     * Updates the specified listing.
     * 
     * @param id
     *            Listing to update.
     * @param data
     *            Data to update.
     * @param email
     *            Email of user updating listing.
     * 
     * @return Updated listing.
     */
    public Listing updateListingById(Long id, ListingDTO data, String email) {
        Optional<Listing> optionalListing = listingRepository.findById(id);
        if (optionalListing.isEmpty())
            return null;
        Listing listing = optionalListing.get();

        if (!email.equals(listing.getUser().getEmail()))
            return listing;
        listing.update(data);
        try {
            return listingRepository.save(listing);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Gets the listing with the given id.
     * 
     * @param id
     *            Listing to get.
     * 
     * @return Listing.
     */
    public Optional<Listing> getListingById(Long id) {
        return listingRepository.findById(id);
    }

    /**
     * Gets all listings.
     * 
     * @return Listings.
     */
    public List<Listing> getListings() {
        return listingRepository.findAll();
    }

    /**
     * Gets all listings belonging to a user.
     * 
     * @param email
     *            User email.
     * 
     * @return User listings.
     */
    public List<Listing> getListingsByEmail(String email) {
        return listingRepository.findAllByUser_Email(email);
    }

    /**
     * Retrieves all listings matching the search query.
     * 
     * @param query
     *            Search query.
     * 
     * @return Matching listings.
     */
    public List<Listing> getListingsByQuery(String query) {
        ExampleMatcher matcher = ExampleMatcher.matchingAny()
                .withMatcher("title", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("description", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("address", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());

        Example<Listing> example = Example.of(new Listing(query), matcher);

        return listingRepository.findAll(example);
    }

    /**
     * Deletes the listing with the given id.
     * 
     * @param id
     *            Listing to delete.
     * 
     * @return Confirmation message.
     */
    public MessageDTO deleteListing(Long id) {
        Optional<Listing> optionalListing = listingRepository.findById(id);

        if (optionalListing.isEmpty())
            return new MessageDTO("Could not find listing with id: " + id);

        listingRepository.delete(optionalListing.get());
        return new MessageDTO("listing: " + id + " deleted");
    }
}