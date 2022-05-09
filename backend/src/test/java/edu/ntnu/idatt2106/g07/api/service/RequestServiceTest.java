package edu.ntnu.idatt2106.g07.api.service;

import edu.ntnu.idatt2106.g07.api.entity.Listing;
import edu.ntnu.idatt2106.g07.api.entity.Request;
import edu.ntnu.idatt2106.g07.api.repository.ListingRepository;
import edu.ntnu.idatt2106.g07.api.repository.RequestRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestComponent;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@TestComponent
@ExtendWith(MockitoExtension.class)
public class RequestServiceTest {
    @Autowired
    @InjectMocks
    RequestService requestService;
    @Mock
    private RequestRepository requestRepository;

    @Test
    @DisplayName("Request service finds request by requestId.")
    void testGetRequestByRequestId() {
        Long requestId = 0x7L;
        Date startDate = new GregorianCalendar(2014, Calendar.FEBRUARY, 11).getTime();
        Date endDate = new GregorianCalendar(2014, Calendar.FEBRUARY, 11).getTime();
        Listing listing = new Listing("test", "test", 1.00, "test", null, null, null);

        Request expected = new Request(requestId, "Test", listing, startDate, endDate, "Test");
        when(requestRepository.findById(requestId)).thenReturn(Optional.of(expected));

        Optional<Request> actual = requestService.getRequestByRequestId(expected.getRequestId());

        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
    }

    @Test
    @DisplayName("Get all requests successfully.")
    void testGetAllRequests() {
        Long requestId1 = 0x7L;
        Listing listing = new Listing();
        Date startDate = new GregorianCalendar(2014, Calendar.FEBRUARY, 11).getTime();
        Date endDate = new GregorianCalendar(2014, Calendar.FEBRUARY, 11).getTime();

        Request request1 = new Request(requestId1, "Test", listing, startDate, endDate, "Test");
        // Do not execute method in "when",return the value in "then".
        // The method in when(getAllByEmail) is the method called in the method getAllRequest which is to be tested.
        List<Request> exceptedRequests = new ArrayList<>();
        exceptedRequests.add(request1);
        when(requestRepository.getAllByEmail(request1.getEmail())).thenReturn(exceptedRequests);
        List<Request> actual = requestService.getAllRequests(request1.getEmail());
        assertEquals(exceptedRequests, actual);

    }

}
