package com.assignment3_jsd.fileManagementSystem.models;


import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name ="files")
@Data
public class File_Upload {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long Id;
    private String name;
    private String path;
    @Column(name ="file_size")
    private float fileSize;
    private String mime;
    @Column(name = "file_of_download")
    private int numberOfDownload;
    private int version;
    private String status;
    @Column(name="create_date_time")
    private LocalDateTime createDateTime;
    @Column(name ="version_ids")
    private String versionIds;
}
