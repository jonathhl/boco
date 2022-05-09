package edu.ntnu.idatt2106.g07.api.dto;

import edu.ntnu.idatt2106.g07.api.entity.Listing;
import edu.ntnu.idatt2106.g07.api.entity.User;
import lombok.Data;

@Data
public class AllListingDTO {
    private Long id;
    private String title;
    private Long image;
    private double price;

    public AllListingDTO(Listing listing) {
        this.id = listing.getId();
        this.title = listing.getTitle();
        this.image = listing.getImageId();
        this.price = listing.getPrice();
    }
}
