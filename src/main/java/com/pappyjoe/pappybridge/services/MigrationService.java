package com.pappyjoe.pappybridge.services;

import org.springframework.web.multipart.MultipartFile;

public interface MigrationService {

    String storeFile(MultipartFile file)throws Exception;
}
