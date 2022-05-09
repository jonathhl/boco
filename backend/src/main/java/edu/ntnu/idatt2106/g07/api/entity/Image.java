package edu.ntnu.idatt2106.g07.api.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@Entity
public class Image {
    @Id
    @GeneratedValue
    @Getter
    private long id;

    @Column(length = 2_000_000)
    @Setter
    private byte[] data;

    public Image() {
    }

    /**
     * Compresses and saves image data
     *
     * @param data
     *            Image data to be saved.
     *
     * @throws IOException
     *             If image cannot be compressed.
     */
    public Image(byte[] data) throws IOException {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length)) {
            byte[] buffer = new byte[1024];
            while (!deflater.finished()) {
                int count = deflater.deflate(buffer);
                outputStream.write(buffer, 0, count);
            }

            this.data = outputStream.toByteArray();
        }
    }

    /**
     * Gets, inflates and returns the image data.
     *
     * @return Inflated image data.
     */
    public byte[] getData() {
        Inflater inflater = new Inflater();
        inflater.setInput(data);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length)) {
            byte[] buffer = new byte[1024];
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }

            return outputStream.toByteArray();
        } catch (IOException | DataFormatException e) {
            return new byte[0];
        }
    }
}
