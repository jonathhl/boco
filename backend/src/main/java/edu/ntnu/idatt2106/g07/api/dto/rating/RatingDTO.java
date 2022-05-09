package edu.ntnu.idatt2106.g07.api.dto.rating;

import edu.ntnu.idatt2106.g07.api.misc.Attachable;
import edu.ntnu.idatt2106.g07.api.entity.Rating;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RatingDTO implements Attachable {

    private Long ratingId;
    private Integer rating;
    private String review;
    private Long requestId;
    private LocalDateTime time;
    private String from;

    public RatingDTO() {
    }

    public RatingDTO(Rating rating) {
        this.ratingId = rating.getRatingId();
        this.requestId = rating.getRequest().getRequestId();
        this.time = rating.getTime();
        this.from = rating.getFrom();

        if (rating.getRating() != null && rating.getReview() != null) {
            this.rating = rating.getRating();
            this.review = rating.getReview();
        }
    }

    public RatingDTO(Long requestId) {
        this.requestId = requestId;
    }

    public RatingDTO(int rating, String review, Long requestId) {
        this.rating = rating;
        this.review = review;
        this.requestId = requestId;
    }
}
