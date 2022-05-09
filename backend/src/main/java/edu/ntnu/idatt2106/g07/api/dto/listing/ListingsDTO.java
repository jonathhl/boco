package edu.ntnu.idatt2106.g07.api.dto.listing;

import edu.ntnu.idatt2106.g07.api.entity.Listing;
import lombok.Data;

@Data
public class ListingsDTO {
    private Long id;
    private String title;
    private Long image;
    private double price;
    private String address;

    public ListingsDTO(Listing listing) {
        this.id = listing.getId();
        this.title = listing.getTitle();
        this.image = listing.getImageId();
        this.price = listing.getPrice();
        this.address = listing.getAddress();
    }
}
