package com.assignment3_jsd.fileManagementSystem.controllers;


import com.assignment3_jsd.fileManagementSystem.message.ResponseMessage;
import com.assignment3_jsd.fileManagementSystem.models.File_Upload;
import com.assignment3_jsd.fileManagementSystem.models.Setting;
import com.assignment3_jsd.fileManagementSystem.services.FileUploadService;
import com.assignment3_jsd.fileManagementSystem.services.SettingService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


@RestController
@CrossOrigin(origins ="http://localhost:3000/")
public class FileUploadController {


    private FileUploadService fileUploadService;
    public FileUploadController(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;

    }

    @PostMapping("/upload")
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile imageFile) throws IOException {
        String message = "";
        try {
            message = fileUploadService.store(imageFile);
//            message = "Uploaded the file successfully: " + imageFile.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            System.out.println(e);
            message = "Could not upload the file: " + imageFile.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }
    @GetMapping("/getFiles")
    public ResponseEntity<List<File_Upload>> getListFiles() {
        List<File_Upload> files = fileUploadService.getAllFiles();
        return ResponseEntity.status(HttpStatus.OK).body(files);
    }
    @PostMapping("/deleteById")
    public ResponseEntity<ResponseMessage> deletleFile(@RequestParam(name ="id") long id){
        String message = "";
        try {
            fileUploadService.deleteById(id);
            message = "Delete the file successfully: ";
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            System.out.println(e);
            message = "Delete fail";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }
    @GetMapping("/download")
    public  ResponseEntity<ResponseMessage> downloadFile(@RequestParam(name = "id") Long id, HttpServletResponse response) throws Exception{
        String message = "";

        try {
            fileUploadService.downloadService(id, response);
            message = "Download the file successfully: ";
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            System.out.println(e);
            message = "Download fail";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }

    @GetMapping(value = "/getSetting")
    public ResponseEntity<Setting> getSetting(){
        return ResponseEntity.status(HttpStatus.OK).body(fileUploadService.getSetting());
    }


    @GetMapping(value = "/pagination")
    public Page<File_Upload> paging(@RequestParam(name = "pageNumber") int pageNumber) {
//            List<File_Upload> file_uploads = fileUploadService.filterSetting(pageNumber);
            return fileUploadService.filterSetting(pageNumber);
    }

}
