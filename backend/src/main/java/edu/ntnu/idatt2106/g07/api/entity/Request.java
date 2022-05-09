package edu.ntnu.idatt2106.g07.api.entity;

import edu.ntnu.idatt2106.g07.api.dto.RequestDTO;
import edu.ntnu.idatt2106.g07.api.misc.Attachable;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
public class Request implements Attachable {
    @Id
    @GeneratedValue
    @Getter
    private Long requestId;

    @Getter
    @Setter
    private String email;

    @Getter
    @Setter
    private Date startDate;

    @Getter
    @Setter
    private Date endDate;

    @Getter
    @Setter
    private String message;

    @Getter
    @Setter
    private String accepted = "NOT_SEEN";

    @ManyToOne
    @JoinColumn(name = "listing_id")
    @Getter
    @Setter
    private Listing listing;

    @OneToOne(mappedBy = "request")
    @Getter
    private Rating rating;

    @NotNull
    @Getter
    private LocalDateTime time;

    @PrePersist
    void time() {
        this.time = LocalDateTime.now();
    }

    public String getFrom() {
        return getEmail();
    }

    public Request() {
    }

    public Request(RequestDTO data) {
        this.email = data.getFrom();
        this.startDate = data.getStartDate();
        this.endDate = data.getEndDate();
        this.message = data.getMessage();
    }

    public void update(RequestDTO dto) {
        if (dto.getFrom() != null)
            setEmail(dto.getFrom());
        if (dto.getAccepted() != null)
            setAccepted(dto.getAccepted());
    }

    public Request(Long requestId, String email, Listing listing, Date startDate, Date endDate, String message) {
        this.requestId = requestId;
        this.email = email;
        this.listing = listing;
        this.startDate = startDate;
        this.endDate = endDate;
        this.message = message;
    }
}
