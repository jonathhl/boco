package edu.ntnu.idatt2106.g07.api.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import edu.ntnu.idatt2106.g07.api.entity.User;

/**
 * Repository dedicated to users.
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {

}
