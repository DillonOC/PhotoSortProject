package com.example;
import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;

import org.apache.commons.imaging.ImageFormats;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.ImagingException;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Calendar;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.HashMap;
import javax.imageio.ImageIO;


public class PhotoSort {
    // Attributes
    File directory;

    // Constructor
    public PhotoSort(String directoryPath) {
        directory = new File(directoryPath);
    }

    // Method for finding EXIF Metadata Date Taken
    public Calendar exifDateTaken(File file) throws Exception {
    // Initialize the calendar object
        Calendar calendar = null;
        
        // Read metadata
        Metadata metadata = ImageMetadataReader.readMetadata(file);
        
        // Checking for EXIF Metadata
        ExifSubIFDDirectory directoryExif = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
        
        if (directoryExif != null) {
            // Get the Date Taken (DateTimeOriginal) from EXIF
            Date dateTaken = directoryExif.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL); // Should be getDateOriginal()
            
            if (dateTaken != null) {
                // Convert Date to Calendar
                calendar = Calendar.getInstance();
                calendar.setTime(dateTaken);
                System.out.println("Date Taken: " + calendar.getTime());
            } 
            else {
                System.out.println("No EXIF DateTimeOriginal tag found.");
            }
        } 
        else {
            System.out.println("No EXIF metadata found.");
        }
        
        return calendar;
    }

    // Method for removing metadata from image and creating Hash
    public static String stripMetaDataCreateHash(File photo) throws IOException, ImagingException, NoSuchAlgorithmException {
        // Read the image, ignoring metadata
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BufferedImage image = ImageIO.read(photo);
        Imaging.writeImage(image, byteArrayOutputStream, ImageFormats.PNG);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();

        // Create a MessageDigest instance for SHA-256
        MessageDigest digest = MessageDigest.getInstance("SHA-256");

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

    public static void main(String[] args) throws Exception {
        // Example usage
        String directoryPath = "C:/Users/Dillo/Documents/University Work/ECM1410 - Object-Oriented Programming/California";
        PhotoSort photoSort = new PhotoSort(directoryPath);
        File directory = photoSort.directory;
        Map<String, File> hashAndPhotos = new HashMap<String, File>();
        for(File photo : directory.listFiles()) {
            Calendar dateTaken = photoSort.exifDateTaken(photo);
            String hashOfPhoto = stripMetaDataCreateHash(photo);
            if (dateTaken != null) {
                //System.out.println("Photo date: " + dateTaken.getTime());
                // Create directories for year and month
                String year = Integer.toString(dateTaken.get(Calendar.YEAR));
                String month = String.format("%02d", dateTaken.get(Calendar.MONTH) + 1);  // Zero-padded month
                //String week = String.format("%02d", dateTaken.get(Calendar.WEEK_OF_MONTH) + 1);
                String day = String.format("%02d", dateTaken.get(Calendar.DAY_OF_MONTH));
                
                // Combine the directory paths
                File baseDir = new File(directory, "TestOutput");
                File yearDir = new File(baseDir, year);
                File monthDir = new File(yearDir, month);
                //File weekDir = new File(monthDir, week);
                File dayDir = new File(monthDir, day);

                // Create the directories
                dayDir.mkdirs();

                if(hashAndPhotos.containsKey(hashOfPhoto)) {
                    // Finding parent directory and creating duplicate folder inside
                    File parentDirectory = hashAndPhotos.get(hashOfPhoto).getParentFile();
                    File duplicateFolder = new File(parentDirectory, "Duplicates");
                    
                    // Create the directories
                    duplicateFolder.mkdirs();

                    // Path to save the photo
                    File destinationFile = new File(duplicateFolder, photo.getName());

                    // Copy the current photo to the duplicate directory
                    try {
                        Files.copy(photo.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                        System.out.println(photo.getName());
                        //System.out.println("Photo copied to: " + destinationFile.getAbsolutePath());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // Path to save the other photo
                    File destinationFile2 = new File(duplicateFolder, hashAndPhotos.get(hashOfPhoto).getName());

                    // Move the old photo to the duplicate directory
                    try {
                        Files.move(hashAndPhotos.get(hashOfPhoto).toPath(), destinationFile2.toPath(), StandardCopyOption.ATOMIC_MOVE);
                        System.out.println(hashAndPhotos.get(hashOfPhoto).getName());
                        //System.out.println("Photo copied to: " + destinationFile.getAbsolutePath());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                else {
                    // Path to save the photo
                    File destinationFile = new File(dayDir, photo.getName());

                    // Save the hash and destination
                    hashAndPhotos.put(hashOfPhoto, destinationFile);

                    // Copy the photo to the new directory
                    try {
                        Files.copy(photo.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                        System.out.println(photo.getName());
                        //System.out.println("Photo copied to: " + destinationFile.getAbsolutePath());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } 
            else {
                System.out.println("Date not found.");
            }
        }
    }
}