package com.assignment3_jsd.fileManagementSystem.models;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name="setting")
@Data
public class Setting {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int Id;
    @Column(name="max_file_size")
    private int maxFileSize;
    @Column(name="item_per_page")
    private int itemPerPage;
    @Column(name="mime_type_allowed")
    private String mimeTypeAllowed;
    @Column(name ="last_update_time")
    private LocalDateTime lastUpdatedTime;
}
