package edu.ntnu.idatt2106.g07.api.entity;

import edu.ntnu.idatt2106.g07.api.misc.Attachable;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
public class Rating implements Attachable {
    @Id
    @GeneratedValue
    @Getter
    private long ratingId;

    @Getter
    @Setter
    private Integer rating;

    @Setter
    @Getter
    private String review;

    @OneToOne
    @JoinColumn(name = "request_id")
    @Getter
    private Request request;

    @NotNull
    @Getter
    private LocalDateTime time;

    @PrePersist
    void time() {
        this.time = LocalDateTime.now();
    }

    public String getFrom() {
        return getRequest().getEmail();
    }

    public Rating(int rating, String review, Request request) {
        this.rating = rating;
        this.review = review;
        this.request = request;
    }

    public Rating(long rating_id, int rating, String review, Request request) {
        this.ratingId = rating_id;
        this.rating = rating;
        this.review = review;
        this.request = request;
    }

    public Rating() {
    }

    public Rating(Request request) {
        this.request = request;
    }
}
