package edu.ntnu.idatt2106.g07.api.dto;

import edu.ntnu.idatt2106.g07.api.dto.listing.ListingDTO;
import edu.ntnu.idatt2106.g07.api.entity.Request;
import edu.ntnu.idatt2106.g07.api.misc.Attachable;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class RequestDTO implements Attachable {

    private Long requestId;
    private String from;
    private ListingDTO listing;
    private Date startDate;
    private Date endDate;
    private String message;
    private String accepted;
    private LocalDateTime time;

    public RequestDTO(Long requestId, String requestEmail, ListingDTO listing, Date startDate, Date endDate,
            String message, String accept) {
        this.requestId = requestId;
        this.from = requestEmail;
        this.listing = listing;
        this.startDate = startDate;
        this.endDate = endDate;
        this.message = message;
        this.accepted = accept;
    }

    public RequestDTO(Request request) {
        this.requestId = request.getRequestId();
        this.from = request.getEmail();
        this.listing = new ListingDTO(request.getListing());
        this.startDate = request.getStartDate();
        this.endDate = request.getEndDate();
        this.message = request.getMessage();
        this.accepted = request.getAccepted();
        this.time = request.getTime();
    }

}
