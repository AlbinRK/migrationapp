package com.pappyjoe.pappybridge.controllers;

import com.pappyjoe.pappybridge.services.MigrationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
@Slf4j
@RestController
@RequestMapping("/api/migration")
public class MigrationController {

    private final MigrationService migrationService;

    private final JobLauncher jobLauncher;
    private final Job patientMigrationJob;
    private final Job addressMigrationJob;

    public MigrationController(MigrationService migrationService, JobLauncher jobLauncher, Job patientMigrationJob, Job addressMigrationJob) {
        this.migrationService = migrationService;
        this.jobLauncher = jobLauncher;
        this.patientMigrationJob = patientMigrationJob;
        this.addressMigrationJob = addressMigrationJob;
    }
    @PostMapping("/patientmaster")
    public ResponseEntity<String> uploadPatientMaster(@RequestParam("file") MultipartFile file) {

        try {

            if (file == null || file.isEmpty()) {
                return ResponseEntity.badRequest().body("No file uploaded");
            }

            // Save file to disk
            String filePath = migrationService.storeFile(file);

            // Pass filePath to Job
            JobParameters params = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .addString("filePath", filePath)
                    .toJobParameters();

            jobLauncher.run(patientMigrationJob, params);

            return ResponseEntity.ok("Migration Started");

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

//    public ResponseEntity<String> uploadPatientMaster(@RequestParam("file") MultipartFile file) throws Exception {
//        if (file == null || file.isEmpty()) {
//            log.error("No file uploaded or file is empty");
//            return ResponseEntity.badRequest().body("No file uploaded or file is empty");
//        }
//        String contentType = file.getContentType();
//        if (!"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet".equals(contentType) &&
//                !"application/vnd.ms-excel".equals(contentType)) {
//            log.error("Invalid file type: {}", contentType);
//            return ResponseEntity.badRequest().body("Only Excel files (.xlsx, .xls) are allowed");
//        }
//        log.info("Received file upload request: {}", file.getOriginalFilename());
//        String result = migrationService.uploadPatientMaster(file);
//        log.info("Upload result: {}", result);
//        return ResponseEntity.ok(result);
//    }
//        try {
//            String result = migrationService.uploadPatientMaster(file);
//            return ResponseEntity.ok(result);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body("Error populating patientmaster: " + e.getMessage());
//        }


    @PostMapping("/addressmaster")
    public ResponseEntity<String> uploadAddressMaster(@RequestParam("file") MultipartFile file) {

        try {

            if (file == null || file.isEmpty()) {
                return ResponseEntity.badRequest().body("No file uploaded");
            }
            String filePath = migrationService.storeFile(file);
            JobParameters params = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .addString("filePath", filePath)
                    .toJobParameters();

            jobLauncher.run(addressMigrationJob, params);

            return ResponseEntity.ok("Address Migration Started");

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    @PostMapping("/emergencycontact")
    public ResponseEntity<String> uploadEmergencyContact(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("No file uploaded");
            }
            if (!file.getContentType().equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
                return ResponseEntity.badRequest().body("Invalid file type. Please upload an Excel file (.xlsx)");
            }
            String result = migrationService.uploadEmergencyContact(file);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Error uploading emergency contact file", e);
            return ResponseEntity.internalServerError().body("Failed to process file: " + e.getMessage());
        }
    }

    @GetMapping("/columns")
    public ResponseEntity<String> getColumns() {
        return ResponseEntity.ok("Columns: patientId, name, insuranceProvider");
    }

    @PostMapping("/start")
    public ResponseEntity<String> startMigration() {
        return ResponseEntity.ok("Migration started");
    }
}
