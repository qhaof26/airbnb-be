package com.project.airbnb.services.Cloudinary;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.project.airbnb.constants.AppConst;
import com.project.airbnb.dtos.response.CloudinaryResponse;
import com.project.airbnb.enums.ImageType;
import com.project.airbnb.exceptions.AppException;
import com.project.airbnb.exceptions.ErrorCode;
import com.project.airbnb.exceptions.FileUploadException;
import com.project.airbnb.models.Listing;
import com.project.airbnb.models.User;
import com.project.airbnb.repositories.ImageRepository;
import com.project.airbnb.repositories.ListingRepository;
import com.project.airbnb.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class CloudinaryService {
    private final Cloudinary cloudinary;
    private final ImageRepository imageRepository;
    private final ListingRepository listingRepository;
    private final UserRepository userRepository;

    @Transactional
    public CloudinaryResponse uploadImage(String objectId, MultipartFile file) throws IOException{
//        if(objectType.equals(ObjectType.LISTING) && !listingRepository.existsById(objectId)){
//            throw new AppException(ErrorCode.LISTING_NOT_EXISTED);
//        } else if(objectType.equals(ObjectType.USER) && !userRepository.existsById(objectId)){
//            throw new AppException(ErrorCode.USER_NOT_EXISTED);
//        }

        //assert file.getOriginalFilename() != null;
        if(file.getOriginalFilename() == null) {
            throw new FileUploadException(ErrorCode.FILE_ERROR);
        }
        boolean isValid = isAllowedFileType(file.getOriginalFilename());
        if(!isValid){
            throw new FileUploadException(ErrorCode.FILE_ERROR);
        }
        String publicValue = generatePublicValue(file.getOriginalFilename());
        log.info("publicValue is: {}", publicValue);
        String extension = getFileName(file.getOriginalFilename())[1];
        log.info("extension is: {}", extension);
        File fileUpload = convert(file);
        log.info("fileUpload is: {}", fileUpload);
        cloudinary.uploader().upload(fileUpload, ObjectUtils.asMap("public_id", publicValue));
        cleanDisk(fileUpload);
        String url = cloudinary.url().generate(publicValue + "." + extension);

//        Image image = Image.builder()
//                .objectId(objectId)
//                .isAvatar(isAvatar.getValue())
//                .url(url)
//                .build();
//        imageRepository.save(image);
        if(listingRepository.existsById(objectId)){
            Listing listing = listingRepository.findById(objectId).orElseThrow(() -> new AppException(ErrorCode.LISTING_NOT_EXISTED));
            listing.getImages().add(url);
            listingRepository.save(listing);
        } else if(userRepository.existsById(Long.valueOf(objectId))){
            User user = userRepository.findById(Long.valueOf(objectId)).orElseThrow(()->new AppException(ErrorCode.USER_NOT_EXISTED));
            user.setAvatar(url);
            userRepository.save(user);
        }
        return CloudinaryResponse.builder()
                .publicId(publicValue)
                .url(url)
                .build();
    }

    private File convert(MultipartFile file) throws IOException {
        assert file.getOriginalFilename() != null;
        String filePath = generatePublicValue(file.getOriginalFilename() + getFileName(file.getOriginalFilename())[1]);
        File convFile = new File(filePath);
        try(InputStream is = file.getInputStream()) {
            Files.copy(is, convFile.toPath());
        }
        return convFile;
    }

    private void cleanDisk(File file) {
        try {
            log.info("file.toPath(): {}", file.toPath());
            Path filePath = file.toPath();
            Files.delete(filePath);
        } catch (IOException e) {
            log.error("Error");
        }
    }

    public boolean isAllowedFileType(String originalName) {
        return AppConst.ALLOWED_IMAGE_EXTENSIONS.stream()
                .anyMatch(extension -> originalName.toLowerCase().endsWith(extension));
    }

    public String generatePublicValue(String originalName){
        String fileName = getFileName(originalName)[0];
        return UUID.randomUUID().toString() + "_" + fileName;
    }

    public String[] getFileName(String originalName) {
        return originalName.split("\\.");
    }
}
