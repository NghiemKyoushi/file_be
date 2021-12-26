package com.assignment3_jsd.fileManagementSystem.controllers;


import com.assignment3_jsd.fileManagementSystem.message.ResponseMessage;
import com.assignment3_jsd.fileManagementSystem.models.File_Upload;
import com.assignment3_jsd.fileManagementSystem.models.Setting;
import com.assignment3_jsd.fileManagementSystem.services.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins ="http://localhost:3000/")
public class SettingController {
    private final SettingService settingService;

    @Autowired
    public SettingController(SettingService settingService) {
        this.settingService = settingService;
    }
    @PutMapping(value = "/setting")
    public ResponseEntity<ResponseMessage> updateSetting(@RequestBody Setting setting){
        String message = "";

        try {
            settingService.save(setting);
            message = " Save setting the file successfully: ";
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            System.out.println(e);
            message = "Save setting fail";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }

}
