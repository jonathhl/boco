package edu.ntnu.idatt2106.g07.api.controller;

import edu.ntnu.idatt2106.g07.api.dto.MessageDTO;
import edu.ntnu.idatt2106.g07.api.dto.RequestDTO;
import edu.ntnu.idatt2106.g07.api.entity.Request;
import edu.ntnu.idatt2106.g07.api.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

/**
 * Controller for managing rental requests.
 *
 * @see Request
 */
@RestController
@CrossOrigin
@RequestMapping("/request")
public class RequestController {
    private final RequestService requestService;

    @Autowired
    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    /**
     * Creates a new request.
     *
     * @param data
     *            Request to create
     * 
     * @return New request.
     */
    @PostMapping
    public ResponseEntity<Object> createRequest(@RequestBody RequestDTO data, Principal principal) {
        data.setFrom(principal.getName());
        return ResponseEntity.ok(new RequestDTO(requestService.saveRequest(data)));
    }

    /**
     * Gets the request with the given id.
     *
     * @param id
     *            Request to get.
     * 
     * @return Request.
     */

    @GetMapping("/{id}")
    public ResponseEntity<Object> getRequestByRequestId(@PathVariable Long id) {
        Optional<Request> request = requestService.getRequestByRequestId(id);

        if (request.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageDTO("Request not found."));
        }

        return ResponseEntity.ok(new RequestDTO(request.get()));
    }

    /**
     * Updates the request with the given id.
     *
     * @param requestDTO
     *            Data to update and id.
     * @param principal
     *            Active user.
     *
     * @return Updated request.
     */
    @PutMapping
    public ResponseEntity<Object> updateRequest(@RequestBody RequestDTO requestDTO, Principal principal) {
        Optional<Request> optionalRequest = requestService.updateRequest(requestDTO, principal.getName());

        if (optionalRequest.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageDTO("Request not found"));
        }

        return ResponseEntity.ok(new RequestDTO(optionalRequest.get()));
    }

}
