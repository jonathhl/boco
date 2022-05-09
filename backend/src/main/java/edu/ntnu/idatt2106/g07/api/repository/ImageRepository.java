package edu.ntnu.idatt2106.g07.api.repository;

import edu.ntnu.idatt2106.g07.api.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
