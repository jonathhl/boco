package edu.ntnu.idatt2106.g07.api.dto.listing;

import edu.ntnu.idatt2106.g07.api.entity.Listing;
import lombok.Data;

@Data
public class ListingDTO {
    private long id;
    private String title;
    private String description;
    private Double price;
    private String address;
    private Long image;
    private String phone;

    /**
     * Email of listing owner
     */
    private String email;
    private String firstname;

    public ListingDTO(String title, String description, Double price, String address, Long image, String phone,
            String email, String firstname) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.address = address;
        this.image = image;
        this.phone = phone;
        this.email = email;
        this.firstname = firstname;
    }

    public ListingDTO(Listing listing) {
        this.id = listing.getId();
        this.title = listing.getTitle();
        this.description = listing.getDescription();
        this.price = listing.getPrice();
        this.address = listing.getAddress();
        this.image = listing.getImageId();
        this.phone = listing.getPhone();
        this.email = listing.getUser() != null ? listing.getUser().getEmail() : null;
        this.firstname = listing.getUser() != null ? listing.getUser().getFirstname() : null;
    }
}
