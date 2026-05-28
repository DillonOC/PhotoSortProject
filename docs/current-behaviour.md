# Current Behaviour

# Classes
Currently just one class, PhotoSort.

##      PhotoSort Attributes:
        Just one attribute holding the directory that contains the images to be sorted.

##      PhotoSort Methods:

###      PhotoSort Constructor:
####              Method purpose:
                    Creates the PhotoSort object and sets the File attribute through the input.
####              Inputs:
                    The directory path as a string.

###      exifDateTaken:
####              Method purpose: 
                    Finding the date taken of the file (in this context an image) from the Exif metadata.
####              Inputs:
                    The file whose date is needed.
####              Outputs:
                    A Calender object with the date and time set to the image's Exif date time original metadata if present.
####              Main workflow:
                    Create a calender object variable, read the metadata from the file given as input, check ExifSubIFDDDirectory.
                    If the directory is present then get the date from the DATETIME_ORIGINAL Exif Metadata. Then if this is not null
                    covert the date into a calender object and put it in the previously created variable and print the date time.
                    If the directory/datetime original are not there then print an error. Finally return the calender, it will be null
                    if the method did not find the metadata and will have a calender object if it does.
####              Date extraction:
                    Performed by retrieving the metadata from the file using the ImageMetadataReader static method readMetadata(file). 
                    The ExifSubIFDDDirectory is then extracted using the metadata methid getFirstDirectoryofType. This object's method getDate is then called
                    with the tag of TAG_DATETIME_ORIGINAL to retrieve the date.
####              Error handling:
                    Errors are handled by returning a null calender which can then be caught by the calling method.
                    Method can throw exceptions as well which must be handled by the calling method. 
####              Known bugs / limitations:
                    Bugs: Unknown
                    Limitations: Currently only checks for EXIF metadata for the date, and only the date time original, should be extended
                    to check:
                        1. EXIF DateTimeOriginal
                        Best meaning: when the photo was taken.

                        2. EXIF DateTimeDigitized / CreateDate
                        Often close to the taken date, especially for phone/camera photos.

                        3. EXIF DateTime / ModifyDate
                        Less ideal, because it may mean when the image metadata/file was modified.

                        4. XMP creation date
                        Useful for edited/exported images.

                        5. IPTC date created
                        Less common for normal family photos, but worth supporting later.

                        6. HEIC/QuickTime-style creation date
                        Useful for iPhone HEIC/Live Photo-style files.

                        7. Filename date
                        Useful for files like:
                        IMG_20210614_153022.jpg
                        Screenshot_20220305-184455.png

                        8. File system created/modified date
                        Last-resort fallback.

                        9. Unknown date folder
                        If nothing reliable exists.
                        
                        And return the source of the date.

                        If ImageIO cannot handle the image type it will fail.
####              Refactor ideas:
                        Could be moved into it's own class for date extraction, with the metadata object being an attribute which it extracts when
                        called and a calender object set to null. Then each different date location can be a method which attempts to extract the date
                        and then if unsuccesful (the calender object is still null) the next one can be called. Add the above other location checks for the date. 

###     stripMetaDataCreateHash:
####              Method purpose:
                    Normalises the image by decoding it and re-encoding it without metadata, then hashes the normalised bytes.
####              Inputs:
                    The file (photo) that is being used to create the hash.
####              Outputs:
                    The hash byte string converted to a hexadecimal string.
####              Main workflow:
                    Creates a ByteArrayOutputStream to write the image data to. Creates a BufferedImage object to read the image data to,
                    using ImageIO.read which will ignore the metadata. This is then written to the ByteArrayOutputStream object using Image.writeImage
                    in a PNG format, the stream is then convereted to a byte array. A MessageDigest object is created with a SHA-256 hash function, 
                    this object is fed the byte array and returns a byte array of the hash. This is converted into a hexadecimal string representation
                    using a string builder and looping over the bytes and converting them into string format. This string is then returned.
####              Duplicate detection:
                    While this method does not detect duplicates it is the method that creates the hash used to compare images and catch duplicates.
####              Error handling:
                    Currently there is no error handling within this function, it will throw all errors to be handled by the calling method. This is okay 
                    if the method is internal but then should be labelled as private or protected to stop uintended use.
####              Known bugs / limitations:
                    Bugs: unknown
                    Limitations: Currently uses SHA-256 as hash, this means it will only capture images exactly the same. 
                                 Does not currently check the rotation of the image, could cause it to miss duplicates.
                                 Normalising the image may take lots of time especially for large images.
####              Refactor ideas:
                    Could again be refactored into it's own class. When refactored should switch to using both SHA-256 and a perceptual hash (need to do more research), to catch similar photos.

###     main:
####              Method purpose:
                    Handles calling the other methods, creating date directories, sorting images within the target directory and also comparing hashes to find the duplicates.
####              Inputs:
                    Currently does not take an input as the test folder was hardcoded into the main method.
####              Outputs:
                    No outputs as the method makes changes in the hardcoded directory.
####              Main workflow:
                    The directory path is hardcoded into a string variable. Then a PhotoSort object is created with this path as input. The directory attribute in the object
                    is then retrieved into a variable and a HashMap is created to use to compare hashes and catch duplicates. The files in the directory as then looped over
                    the PhotoSort exifDateTaken method is called and also teh stripMetaDataCreateHash method. If the date is found then the relevant directories are created if needed.
                    Then the hash is compared with the keys in the HashMap and if the key is found duplicate directories are created in the relevant date directory and both images are placed within.
                    If there is no keys that are the same present the program saves the image in the relevant folders and creates a key,value pair for the hashmap. If the date is not found currently the photo
                    is left in its current place.
####              Date extraction:
                    Done through exifDateTaken.
####              Duplicate detection:
                    Done using the hash from stripMetaDataCreateHash stored in a dictionary to allow for comparison of keys.
####              Folder structure created:
                    Folders created in the structure year/month/day. Duplicate folders created within the specific day if found.
####              Error handling:
                    Currently no error handling, not good.
####              Known bugs / limitations:
                    Bugs: unknown
                    Limitations: Currently has the limitations assosciated with the methods called. Also the fact the comparison logic is not seperated. This main function controlling the class should
                    be seperate too.
####              Refactor ideas:
                    Seperate main into a controlling class. Seperate out the comparison logic so it can be called from the main function in a seperate class. HashMap can be stored in the class it is seperated into. Need to add handling for photos where date is not found and also handling of HEIC images from apple.

# Possible Future Modules

## DateExtractor
Responsibility:
- Extract date taken from metadata
- Fall back to file date if metadata missing

## ExactImageHasher
Responsibility:
- Strip metadata by decoding image
- Create SHA-256 hash from image content

## DuplicateDetector
Responsibility:
- Track hashes already seen
- Decide whether current image is a duplicate

## PhotoSortService
Responsibility:
- Coordinate the sorting process
- Decide where files should be moved

## FileMover
Responsibility:
- Create folders
- Move/copy files safely