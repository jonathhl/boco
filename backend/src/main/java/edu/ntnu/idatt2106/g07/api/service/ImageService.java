package edu.ntnu.idatt2106.g07.api.service;

import edu.ntnu.idatt2106.g07.api.entity.Image;
import edu.ntnu.idatt2106.g07.api.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

/**
 * Service for managing images.
 * 
 * @see Image
 */
@Service
public class ImageService {
    private final ImageRepository imageRepository;

    @Autowired
    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    /**
     * Creates a new image.
     * 
     * @param file
     *            Image attached to the request.
     * 
     * @return Image.
     * 
     * @throws IOException
     *             If bytes cannot be retrieved from file.
     */
    public Optional<Image> createImage(MultipartFile file) throws IOException {
        Image image = new Image(file.getBytes());

        return Optional.of(imageRepository.save(image));
    }

    /**
     * Gets the image with the given id.
     * 
     * @param id
     *            Image to get.
     * 
     * @return The image entity if it exists, empty if not.
     */
    public Optional<Image> getImage(Long id) {
        if (!imageRepository.existsById(id)) {
            return Optional.empty();
        }

        return Optional.of(imageRepository.getById(id));
    }

    /**
     * Deletes the image with the given id.
     * 
     * @param id
     *            Image to delete.
     */
    public void deleteImage(Long id) {
        imageRepository.deleteById(id);
    }
}
