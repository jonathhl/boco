package edu.ntnu.idatt2106.g07.api.controller;

import edu.ntnu.idatt2106.g07.api.dto.MessageDTO;
import edu.ntnu.idatt2106.g07.api.entity.Image;
import edu.ntnu.idatt2106.g07.api.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

/**
 * Controller for managing images.
 * 
 * @see Image
 */
@RestController
@RequestMapping("/image")
public class ImageController {
    private final ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    /**
     * Creates a new image.
     * 
     * @param file
     *            Image file to post.
     * 
     * @return New image.
     */
    @PostMapping
    public ResponseEntity<Object> postImage(@RequestParam("image") MultipartFile file) throws IOException {
        Optional<Image> optionalImage = imageService.createImage(file);

        if (optionalImage.isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageDTO("Unable to create image."));
        }

        return ResponseEntity.ok(Map.of("id", optionalImage.get().getId()));
    }

    /**
     * Gets the image with the given id.
     * 
     * @param id
     *            The image to get.
     * 
     * @return Image.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Object> getImage(@PathVariable Long id) {
        Optional<Image> optionalImage = imageService.getImage(id);

        if (optionalImage.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageDTO("Unable to find image."));
        }

        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(optionalImage.get().getData());
    }

    /**
     * Deletes the image with the given id.
     * 
     * @param id
     *            Id of image to delete.
     * 
     * @return Confirmation message.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteImage(@PathVariable Long id) {
        imageService.deleteImage(id);

        return ResponseEntity.ok(new MessageDTO("Image deleted successfully"));
    }
}
