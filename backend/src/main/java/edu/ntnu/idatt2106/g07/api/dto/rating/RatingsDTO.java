package edu.ntnu.idatt2106.g07.api.dto.rating;

import lombok.Data;

import java.util.List;

@Data
public class RatingsDTO {
    private Double average;
    private List<RatingDTO> ratings;

    public RatingsDTO() {
    }

    public RatingsDTO(Double average, List<RatingDTO> ratings) {
        this.average = average;
        this.ratings = ratings;
    }
}
