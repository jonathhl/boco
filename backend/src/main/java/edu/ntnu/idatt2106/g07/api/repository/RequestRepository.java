package edu.ntnu.idatt2106.g07.api.repository;

import edu.ntnu.idatt2106.g07.api.entity.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository dedicated to requests.
 */
@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> getAllByEmail(String email);
    /*
     * @Query(value = "SELECT * FROM Request req, Listing li, User us\n" + "WHERE req.listing_Id = li.id \n" +
     * "AND li.user_email = us.email") List<Request> findAllRequests();
     */
}