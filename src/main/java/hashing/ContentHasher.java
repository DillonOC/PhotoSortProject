package hashing;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.imageio.ImageIO;

import org.apache.commons.imaging.ImageFormats;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.ImagingException;

import java.awt.image.BufferedImage;

public class ContentHasher {
    
    public String createContentHash(File photo) throws ContentHasherException
    {
        // Read the image, ignoring metadata
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BufferedImage image;
        try {
            image = ImageIO.read(photo);
        }
        catch(IOException err) {
            throw new ContentHasherException("IO Exception for: " + photo, err);
        }

        if(image == null) {throw new ContentHasherException("Image processing failed for: " + photo);
        }

        try {
            Imaging.writeImage(image, byteArrayOutputStream, ImageFormats.PNG);
        }
        catch(ImagingException err) {
            throw new ContentHasherException("Image processing failed for: " + photo, err);
        }
        catch(IOException err) {
            throw new ContentHasherException("IO Exception for: " + photo, err);
        }
        byte[] imageBytes = byteArrayOutputStream.toByteArray();

        MessageDigest digest;
        try{
            // Create a MessageDigest instance for SHA-256
            digest = MessageDigest.getInstance("SHA-256");
        }
        catch(NoSuchAlgorithmException err) {
            throw new ContentHasherException("No such Algorithm Exceptiom.", err);
        }

        // Generate the hash of the image data
        byte[] hashBytes = digest.digest(imageBytes);

        // Convert the hash bytes to a hexadecimal string
        StringBuilder sb = new StringBuilder();
        for (byte b : hashBytes) {
            sb.append(String.format("%02x", b));
        }

        // Return the hexadecimal string representation of the hash
        return sb.toString();
    }
}
