package edu.ntnu.idatt2106.g07.api.service;

import edu.ntnu.idatt2106.g07.api.entity.Listing;
import edu.ntnu.idatt2106.g07.api.repository.ListingRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestComponent;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@TestComponent
@ExtendWith(MockitoExtension.class)
class ListingServiceTest {
    @Mock
    ListingRepository listingRepository;

    @Autowired
    @InjectMocks
    ListingService listingService;

    @Test
    @DisplayName("Listing service finds listing by id.")
    void testFindListingById() {
        Listing expected = new Listing("test", "test", 1.00, "test", null, null, null);
        when(listingRepository.findById(expected.getId())).thenReturn(Optional.of(expected));

        Optional<Listing> actual = listingService.getListingById(expected.getId());

        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
    }
}
