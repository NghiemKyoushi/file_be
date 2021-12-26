package com.assignment3_jsd.fileManagementSystem.repositories;

import com.assignment3_jsd.fileManagementSystem.models.File_Upload;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepositories extends JpaRepository<File_Upload,Long> {
    List<File_Upload> findByFileSizeLessThanEqualAndStatusNot(float file_Size,String status, Pageable pageable);
    List<File_Upload> findByStatusNot(String status);
    List<File_Upload> findByNameEqualsAndStatus(String name, String status);
    Page<File_Upload> findByStatusContaining(String status,Pageable page);

}
