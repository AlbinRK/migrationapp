package com.pappyjoe.pappybridge.services;

import org.springframework.web.multipart.MultipartFile;

public interface MigrationService {

    public String uploadPatientMaster(MultipartFile file) throws Exception;

    public String uploadAddressMaster(MultipartFile file) throws Exception;

    String uploadEmergencyContact(MultipartFile file) throws Exception;

    String storeFile(MultipartFile file)throws Exception;
}
