package com.assignment3_jsd.fileManagementSystem.services;


import com.assignment3_jsd.fileManagementSystem.models.Setting;
import com.assignment3_jsd.fileManagementSystem.repositories.SettingRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SettingService {

    private final SettingRepositories settingRepositories;

    @Autowired
    public SettingService(SettingRepositories settingRepositories) {
        this.settingRepositories = settingRepositories;
    }

    public boolean File_Exist(){
        List<Setting> listForSetting = settingRepositories.findAll();
        if(!listForSetting.isEmpty()){
            return  true;
        }
        return false;
    }
    public Setting findSetting() {
        List<Setting> settings = settingRepositories.findAll();
        if(settings.isEmpty()){
            return null;
        }
        return settings.get(0);
    }
    public void save(Setting setting) {
        Setting record = findSetting();
        if(record ==  null){
            record = new Setting();
        }
        record.setItemPerPage(setting.getItemPerPage());
        record.setMaxFileSize(setting.getMaxFileSize());
        record.setMimeTypeAllowed(setting.getMimeTypeAllowed());
        record.setLastUpdatedTime(LocalDateTime.now());
        settingRepositories.save(record);
    }

}
