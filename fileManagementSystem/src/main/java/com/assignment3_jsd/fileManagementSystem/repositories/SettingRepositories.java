package com.assignment3_jsd.fileManagementSystem.repositories;

import com.assignment3_jsd.fileManagementSystem.models.Setting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SettingRepositories extends JpaRepository<Setting,Long> {
}
