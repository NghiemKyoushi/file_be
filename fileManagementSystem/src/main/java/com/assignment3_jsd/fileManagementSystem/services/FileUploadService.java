package com.assignment3_jsd.fileManagementSystem.services;

import com.assignment3_jsd.fileManagementSystem.models.File_Upload;
import com.assignment3_jsd.fileManagementSystem.models.Setting;
import com.assignment3_jsd.fileManagementSystem.repositories.FileRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.activation.MimetypesFileTypeMap;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class FileUploadService {


    private final FileRepositories  fileRepositories;
    private final SettingService settingService;

    @Autowired
    public FileUploadService(FileRepositories fileRepositories1, SettingService settingService){

        this.fileRepositories = fileRepositories1;
        this.settingService = settingService;
    }
    public String store(MultipartFile imageFile) throws IOException {

        MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
        String mimeType = mimeTypesMap.getContentType(imageFile.getOriginalFilename());
        Setting setting = settingService.findSetting();
        boolean check_mime = setting.getMimeTypeAllowed().equals(mimeType.split("/")[1]);
        if(check_mime == false){
            return "Invalid type of file";
        }
        if(imageFile.getBytes().length /1024 > setting.getMaxFileSize()){
            return "The size of file bigger than requirement";
        }
        File_Upload FileDB = new File_Upload();
        if (!imageFile.isEmpty()) {
            try {
                byte[] bytes1 = imageFile.getBytes();

                // Creating the directory to store file
                String rootPath = System.getProperty("catalina.home");
                File dir = new File(rootPath + File.separator + "tmpFiles");
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                // Create the file on server
                File serverFile = new File(dir.getAbsolutePath() + File.separator + "name");
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
                stream.write(bytes1);
                stream.close();

                System.out.println("Server File Location=" + serverFile.getAbsolutePath());
                FileDB.setPath(serverFile.getAbsolutePath());
                FileDB.setFileSize(bytes1.length/1024);
                FileDB.setName(imageFile.getOriginalFilename());

                //Check mime to save Database

                FileDB.setMime(mimeType.split("/")[1]);
                //Check time to save Database
                LocalDateTime dayCreate = LocalDateTime.now();
                FileDB.setCreateDateTime(dayCreate);
                FileDB.setStatus("OPEN");

                List<File_Upload> versionSaved = fileRepositories.findByNameEqualsAndStatus(imageFile.getOriginalFilename(), "OPEN");
//                System.out.println("old version"+versionSaved);
                if(!versionSaved.isEmpty()){
                    FileDB.setVersion(versionSaved.get(versionSaved.size()-1).getVersion() +1);
                }else {
                    FileDB.setVersion(1);
                }
                FileDB.setVersionIds("");
                FileDB.setNumberOfDownload(0);
                fileRepositories.save(FileDB);
            } catch (Exception e) {
                System.out.println(e);
            }
        }

        return "save successfully!";
    }

    public List<File_Upload> getAllFiles() {
        return fileRepositories.findByStatusNot("DELETE");
    }

    public boolean deleteById (long id){
        try{
            File_Upload fileDelete = fileRepositories.findById(id).get();
            fileDelete.setStatus("DELETE");
            fileRepositories.save(fileDelete);
            return true;
        }catch (Exception e){
            System.out.println(e);
        }
        return false;
    }
    public void downloadService(Long id, HttpServletResponse response) throws IOException {
       File_Upload fileDownload = fileRepositories.findById(id).get();
        response.setContentType("application/octet-stream");
        String header_Key = "Content-Disposition";
        String header_Value = "attachment;filename=" + fileDownload.getName();
        response.setHeader(header_Key, header_Value);
        ServletOutputStream servletOutputStream = response.getOutputStream();
        File serverFile = new File(fileDownload.getPath());
        byte[] fileContent = Files.readAllBytes(serverFile.toPath());
        servletOutputStream.write(fileContent);
        fileDownload.setNumberOfDownload(fileDownload.getNumberOfDownload() + 1);
        fileRepositories.save(fileDownload);
        servletOutputStream.close();
    }
    public Setting getSetting(){
        return settingService.findSetting();
    }
    public Page<File_Upload> filterSetting( int pageNumber) {
        Setting setting = settingService.findSetting();
            System.out.println("item" + setting.getItemPerPage());
            Pageable page = PageRequest.of(pageNumber,setting.getItemPerPage(),
                    Sort.by("name").ascending().and(Sort.by("version").ascending()));
//            List<File_Upload> recordForSetting = fileRepositories.findByFileSizeLessThanEqualAndStatusNot(setting.getMaxFileSize(),"delete",page);
            return fileRepositories.findByStatusContaining("OPEN",page);
//        }

    }

}
