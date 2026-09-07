package com.sms.serviceImpl;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import com.sms.exception.BusinessException;
import com.sms.service.FileStorageService;


import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class FileStorageServiceImpl 
        implements FileStorageService {


    private final Path uploadPath;



    public FileStorageServiceImpl(

            @Value("${file.upload-dir}")
            String uploadDir

    ) {


        this.uploadPath =
                Paths.get(uploadDir)
                .toAbsolutePath()
                .normalize();


        try {

            Files.createDirectories(
                    uploadPath
            );

        } catch (IOException e) {

            log.error(
                    "Unable to create upload directory : {}",
                    uploadPath,
                    e
            );

            throw new BusinessException(
                    "Could not initialize file storage."
            );

        }

    }



    @Override
    public String storeFile(MultipartFile file) {

        // Validate file first
        if (file == null || file.isEmpty()) {
            throw new BusinessException(
                    "File cannot be empty"
            );
        }

        String originalName = file.getOriginalFilename();

        // Validate original file name
        if (originalName == null || originalName.isBlank()) {
            throw new BusinessException(
                    "Invalid file name."
            );
        }

        String extension = getExtension(originalName);

        String storedFileName =
                UUID.randomUUID().toString() + extension;

        try {

            Path targetLocation = uploadPath
                    .resolve(storedFileName)
                    .normalize()
                    .toAbsolutePath();

            Files.copy(
                    file.getInputStream(),
                    targetLocation,
                    java.nio.file.StandardCopyOption.REPLACE_EXISTING
            );

            log.info(
                    "File stored successfully : {}",
                    storedFileName
            );

            return storedFileName;

        } catch (IOException e) {

            log.error(
                    "Failed to store file : {}",
                    originalName,
                    e
            );

            throw new BusinessException(
                    "File storage failed."
            );
        }
    }




    private String getExtension(
            String fileName
    ) {


        if(fileName == null ||
                !fileName.contains(".")) {


            return "";

        }


        return fileName.substring(
                fileName.lastIndexOf(".")
        );

    }




    @Override
    public void deleteFile(
            String fileName
    ) {


        try {


            Files.deleteIfExists(
                    uploadPath.resolve(fileName)
            );


        } catch(IOException e){

            log.error(
                    "File deletion failed : {}",
                    fileName,
                    e
            );

            throw new BusinessException(
                    "Unable to delete file."
            );

        }

    }



    @Override
    public Resource loadFile(String fileName) {


        try {


        	Path filePath =

        	        uploadPath

        	        .resolve(fileName)

        	        .normalize()

        	        .toAbsolutePath();
        	
        	if(!filePath.startsWith(uploadPath)){

        	    throw new BusinessException(
        	            "Invalid file path."
        	    );

        	}


            Resource resource =
                    new UrlResource(
                            filePath.toUri()
                    );


            if(resource.exists() &&
                    resource.isReadable()) {


                return resource;

            }


            throw new BusinessException(
                    "File not found"
            );


        } catch (BusinessException e) {

            throw e;

        }

        catch (Exception e) {

            log.error(
                    "Unable to load file : {}",
                    fileName,
                    e
            );

            throw new BusinessException(
                    "File loading failed."
            );

        }

    }


}