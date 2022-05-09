package edu.ntnu.idatt2106.g07.api.repository;

import edu.ntnu.idatt2106.g07.api.entity.Listing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository dedicated to listings.
 */
@Repository
public interface ListingRepository extends JpaRepository<Listing, Long> {
    public List<Listing> findAllByUser_Email(String email);
}
