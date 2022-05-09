package edu.ntnu.idatt2106.g07.api.entity;

import edu.ntnu.idatt2106.g07.api.dto.listing.ListingDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
public class Listing {
    @Id
    @GeneratedValue
    @Getter
    private Long id;

    @NotNull
    @Getter
    @Setter
    private String title;

    @Getter
    @Setter
    private String description;

    @Getter
    @Setter
    private double price;

    @Getter
    @Setter
    private String address;

    @Getter
    @Setter
    private String phone;

    @Getter
    @Setter
    private Long imageId;

    @ManyToOne
    @JoinColumn(name = "email")
    @Getter
    @Setter
    private User user;

    @OneToMany(mappedBy = "listing")
    @Getter
    private List<Request> requests;

    public Listing() {
    }

    public Listing(String title, String description, double price, String address, String phone, Long imageId,
            User user) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.address = address;
        this.phone = phone;
        this.imageId = imageId;
        this.user = user;
    }

    public Listing(Long id, ListingDTO listing) {
        this.id = id;
        this.title = listing.getTitle();
        this.description = listing.getDescription();
        this.price = listing.getPrice();
        this.address = listing.getAddress();
        this.phone = listing.getPhone();
        this.imageId = listing.getImage();
    }

    public Listing(String query) {
        this.title = query;
        this.description = query;
        this.address = query;
    }

    /**
     * Method to update an existing listing.
     * 
     * @param dto
     *            retrieves the updated information from the UserDTO class.
     * 
     * @return the updated details.
     */
    public Listing update(ListingDTO dto) {
        if (dto.getTitle() != null) {
            this.title = dto.getTitle();
        }
        if (dto.getDescription() != null) {
            this.description = dto.getDescription();
        }
        if (dto.getPrice() != null) {
            this.price = dto.getPrice();
        }
        if (dto.getAddress() != null) {
            this.address = dto.getAddress();
        }
        if (dto.getPhone() != null) {
            this.phone = dto.getPhone();
        }
        if (dto.getImage() != null) {
            this.imageId = dto.getImage();
        }

        return this;
    }
}
