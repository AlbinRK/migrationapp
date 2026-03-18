package com.pappyjoe.pappybridge.services.serviceImpl;

import com.pappyjoe.pappybridge.models.daos.AddressMasterDao;
import com.pappyjoe.pappybridge.models.daos.EmergencyContactDao;
import com.pappyjoe.pappybridge.models.daos.PatientMasterDao;
import com.pappyjoe.pappybridge.repositories.AddressMasterRepository;
import com.pappyjoe.pappybridge.repositories.EmergencyContactRepository;
import com.pappyjoe.pappybridge.repositories.PatientMasterRepository;
import com.pappyjoe.pappybridge.services.MigrationService;
import org.apache.poi.ss.usermodel.*;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import static org.apache.poi.ss.util.DateParser.parseLocalDate;

@Service
public class MigrationServiceImpl implements MigrationService {

    private final PatientMasterRepository patientMasterRepository;
    private final AddressMasterRepository addressMasterRepository;
    private final EmergencyContactRepository emergencyContactRepository;
    private final JobLauncher jobLauncher;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    @Value("${default.clinicId:1}")
    private Integer defaultClinicId;

    @Value("${default.createdById:1}")
    private Integer defaultCreatedById;

    private static final String UPLOAD_DIR = "C:/migration-temp/"; // change if needed


    public MigrationServiceImpl(PatientMasterRepository patientMasterRepository, AddressMasterRepository addressMasterRepository, EmergencyContactRepository emergencyContactRepository, JobLauncher jobLauncher, JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        this.patientMasterRepository = patientMasterRepository;
        this.addressMasterRepository = addressMasterRepository;
        this.emergencyContactRepository = emergencyContactRepository;
        this.jobLauncher = jobLauncher;
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
    }


    @Override
    public String uploadPatientMaster(MultipartFile file) throws Exception {

        try {


            // Define Step
            Step step = new StepBuilder("patientMasterMigrationStep", jobRepository)
                    .<Row, PatientMasterDao>chunk(100, transactionManager)
                    .reader(patientMasterItemReader(file))
                    .processor(patientMasterItemProcessor())
                    .writer(patientMasterItemWriter())
                    .build();

            // Define Job
            Job job = new JobBuilder("patientMasterMigrationJob", jobRepository)
                    .start(step)
                    .build();

            // Launch Job
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();

            JobExecution execution = jobLauncher.run(job, jobParameters);

            // Check job status
            if (execution.getStatus() == BatchStatus.COMPLETED) {
                return "PatientMaster data migration job completed successfully";
            } else {
                throw new Exception("PatientMaster data migration job failed: " + execution.getFailureExceptions());
            }
        } catch (Exception e) {
            e.printStackTrace(); // temporary debugging
            throw new RuntimeException("PatientMaster data migration job failed", e);
        }
    }

    @Override
    public String uploadAddressMaster(MultipartFile file) throws Exception {

        Step step = new StepBuilder("addressMasterMigrationStep", jobRepository)
                .<Row, AddressMasterDao>chunk(100, transactionManager)
                .reader(addressMasterItemReader(file))
                .processor(addressMasterItemProcessor())
                .writer(addressMasterItemWriter())
                .build();

        Job job = new JobBuilder("addressMasterMigrationJob", jobRepository)
                .start(step)
                .build();

        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();

        JobExecution execution = jobLauncher.run(job, jobParameters);

        if (execution.getStatus() == BatchStatus.COMPLETED) {
            return "AddressMaster data migration job completed successfully";
        } else {
            throw new Exception("AddressMaster data migration job failed: " + execution.getFailureExceptions());
        }

    }

    @Override
    public String uploadEmergencyContact(MultipartFile file) throws Exception {
        Step step = new StepBuilder("emergencyContactMigrationStep", jobRepository)
                .<Row, EmergencyContactDao>chunk(100, transactionManager)
                .reader(emergencyContactItemReader(file))
                .processor(emergencyContactItemProcessor())
                .writer(emergencyContactItemWriter())
                .build();
        Job job = new JobBuilder("emergencyContactMigrationJob", jobRepository)
                .start(step)
                .build();

        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();

        JobExecution execution = jobLauncher.run(job, jobParameters);

        if (execution.getStatus() == BatchStatus.COMPLETED) {
            return "EmergencyContact data migration job completed successfully";
        } else {
            throw new Exception("EmergencyContact data migration job failed: " + execution.getFailureExceptions());
        }

    }


    @Override
    public String storeFile(MultipartFile file) throws Exception {

        File directory = new File(UPLOAD_DIR);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String filePath = UPLOAD_DIR + System.currentTimeMillis() + "_" + file.getOriginalFilename();

        File dest = new File(filePath);
        file.transferTo(dest);

        return filePath;
    }


    private ItemReader<Row> patientMasterItemReader(MultipartFile file) {
        return new ItemReader<>() {
            private Iterator<Row> rowIterator;
            private Workbook workbook;

            @Override
            public Row read() throws Exception {
                if (rowIterator == null) {
                    InputStream inputStream = file.getInputStream();
                    try {
                        workbook = WorkbookFactory.create(inputStream);
                        Sheet sheet = workbook.getSheetAt(0);
                        rowIterator = sheet.iterator();
                        if (rowIterator.hasNext()) {
                            rowIterator.next(); // Skip header row
                        }
                    } catch (IOException e) {
                        if (workbook != null) {
                            workbook.close();
                        }
                        throw e;
                    }
                }
                Row row = rowIterator.hasNext() ? rowIterator.next() : null;
                if (row == null && workbook != null) {
                    workbook.close();
                    workbook = null;
                }
                return row;
            }
        };
    }

    private ItemProcessor<Row, PatientMasterDao> patientMasterItemProcessor() {
        return row -> {
            PatientMasterDao patient = new PatientMasterDao();
            patient.setAge(getCellValueAsString(row.getCell(0)));
            patient.setAgeDays(getCellValueAsString(row.getCell(1)));
            patient.setAgeMonths(getCellValueAsString(row.getCell(2)));
            patient.setDob(getCellDateAsString(row.getCell(3)));
            patient.setEmiratesID(getCellValueAsString(row.getCell(4))); // Changed to emiratesID
            patient.setFirstName(getCellValueAsString(row.getCell(5)));
            patient.setFoDate(parseLocalDate(getCellValueAsString(row.getCell(6))));
            patient.setGender(getCellValueAsString(row.getCell(7)));
            patient.setIp(getCellValueAsString(row.getCell(8)));
            patient.setIsVip(parseInteger(getCellValueAsString(row.getCell(9))));
            patient.setLastName(getCellValueAsString(row.getCell(10)));
            patient.setMiddleName(getCellValueAsString(row.getCell(11)));
            patient.setPatientID(getCellValueAsString(row.getCell(12)));
            patient.setSalutation(getCellValueAsString(row.getCell(13)));
            patient.setGrouping(parseInteger(getCellValueAsString(row.getCell(14))));
            patient.setPatGroup(parseInteger(getCellValueAsString(row.getCell(15))));
            patient.setPostofficeId(parseInteger(getCellValueAsString(row.getCell(16))));
            patient.setReligionId(parseInteger(getCellValueAsString(row.getCell(17))));
            patient.setLanguageId(parseInteger(getCellValueAsString(row.getCell(18))));
            patient.setMaritalId(parseInteger(getCellValueAsString(row.getCell(19))));
            patient.setRaceId(parseInteger(getCellValueAsString(row.getCell(20))));
            patient.setPassportNum(getCellValueAsString(row.getCell(21)));
            patient.setGccId(getCellValueAsString(row.getCell(22)));
            patient.setPriority(parseInteger(getCellValueAsString(row.getCell(23))));
            patient.setPhoto(getCellValueAsString(row.getCell(24)));
            patient.setCreatedDate(LocalDate.now()); // Use LocalDate
            patient.setClinicID(defaultClinicId);
            patient.setCreatedByUserId(defaultCreatedById); // Changed to createdByUserId
            patient.setStatus(1);
            patient.setGlobalConsent(0);
            patient.setIsEstablished(0);
            patient.setA28MsgStatus(0);
            return patient;
        };
    }

    private ItemWriter<PatientMasterDao> patientMasterItemWriter() {
        return items -> patientMasterRepository.saveAll(items);
    }

    // Placeholder for AddressMaster (implement based on your AddressMasterDao structure)
    private ItemReader<Row> addressMasterItemReader(MultipartFile file) {
        return new ItemReader<>() {
            private Iterator<Row> rowIterator;
            private Workbook workbook;

            @Override
            public Row read() throws Exception {
                if (rowIterator == null) {
                    InputStream inputStream = file.getInputStream();
                    try {
                        workbook = WorkbookFactory.create(inputStream);
                        Sheet sheet = workbook.getSheetAt(0);
                        rowIterator = sheet.iterator();
                        if (rowIterator.hasNext()) {
                            rowIterator.next(); // Skip header row
                        }
                    } catch (IOException e) {
                        if (workbook != null) {
                            workbook.close();
                        }
                        throw e;
                    }
                }
                Row row = rowIterator.hasNext() ? rowIterator.next() : null;
                if (row == null && workbook != null) {
                    workbook.close();
                    workbook = null;
                }
                return row;
            }
        };
    }

    private ItemProcessor<Row, AddressMasterDao> addressMasterItemProcessor() {
        return row -> {
            AddressMasterDao address = new AddressMasterDao();
            address.setAddress1(getCellValueAsString(row.getCell(0)));
            address.setAddress2(getCellValueAsString(row.getCell(1)));
            String createdDateStr = getCellValueAsString(row.getCell(2));
            address.setCreatedDate((createdDateStr != null && !createdDateStr.isEmpty()) ? createdDateStr : new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            address.setEmailID(getCellValueAsString(row.getCell(3)));
            address.setFaxNo(getCellValueAsString(row.getCell(4)));
            address.setIp(getCellValueAsString(row.getCell(5)));
            address.setMobileNo(getCellValueAsString(row.getCell(6)));
            address.setPhoneNo(getCellValueAsString(row.getCell(7)));
            address.setPlace(getCellValueAsString(row.getCell(8)));
            address.setUploadfile(getCellValueAsString(row.getCell(9)));
            address.setCountryID(parseInteger(getCellValueAsString(row.getCell(10))));
            address.setAddPatientMasterID(parseInteger(getCellValueAsString(row.getCell(11))));
            address.setStateID(parseInteger(getCellValueAsString(row.getCell(12))));
            address.setHowKnow(getCellValueAsString(row.getCell(13)));
            address.setAddressType(parseInteger(getCellValueAsString(row.getCell(14))));
            address.setCountryCode(parseInteger(getCellValueAsString(row.getCell(15))));
            address.setClinicCountryId(parseInteger(getCellValueAsString(row.getCell(16))));
            // Set defaults (overridable if needed)
            address.setClinicID(defaultClinicId);
            address.setCreatedById(defaultCreatedById);
            address.setStatus(1);
            return address;
        };
    }

    private ItemWriter<AddressMasterDao> addressMasterItemWriter() {
        return items -> addressMasterRepository.saveAll(items);
    }

    private ItemReader<? extends Row> emergencyContactItemReader(MultipartFile file) {
        return new ItemReader<>() {
            private Iterator<Row> rowIterator;
            private Workbook workbook;

            @Override
            public Row read() throws Exception {
                if (rowIterator == null) {
                    InputStream inputStream = file.getInputStream();
                    try {
                        workbook = WorkbookFactory.create(inputStream);
                        Sheet sheet = workbook.getSheetAt(0);
                        rowIterator = sheet.iterator();
                        if (rowIterator.hasNext()) {
                            rowIterator.next(); // Skip header row
                        }
                    } catch (IOException e) {
                        if (workbook != null) {
                            workbook.close();
                        }
                        throw e;
                    }
                }
                Row row = rowIterator.hasNext() ? rowIterator.next() : null;
                if (row == null && workbook != null) {
                    workbook.close();
                    workbook = null;
                }
                return row;
            }
        };
    }


    private ItemProcessor<? super Row,? extends EmergencyContactDao> emergencyContactItemProcessor() {
        return row -> {
            EmergencyContactDao contact = new EmergencyContactDao();
            contact.setAddress1(getCellValueAsString(row.getCell(0)));
            contact.setAddress2(getCellValueAsString(row.getCell(1)));
            contact.setECNO(parseInteger(getCellValueAsString(row.getCell(2))));
            contact.setFullName(getCellValueAsString(row.getCell(3)));
            contact.setIp(getCellValueAsString(row.getCell(4)));
            contact.setMobile(getCellValueAsString(row.getCell(5)));
            contact.setStreetAddress(getCellValueAsString(row.getCell(6)));
            contact.setEquipmentType(getCellValueAsString(row.getCell(7)));
            contact.setLastName(getCellValueAsString(row.getCell(8)));
            contact.setEmgPatientMasterID(parseInteger(getCellValueAsString(row.getCell(9))));
            contact.setAddressType(parseInteger(getCellValueAsString(row.getCell(10))));
            contact.setRelationshipId(parseInteger(getCellValueAsString(row.getCell(11))));
            contact.setContactType(parseInteger(getCellValueAsString(row.getCell(12))));
            contact.setCreatedDate(LocalDate.now());
            contact.setClinicID(defaultClinicId);
            contact.setCreatedByUserId(defaultCreatedById);
            contact.setStatus(1);
            contact.setHl7MsgStatus(0);
            return contact;
        };
    }



    private ItemWriter<? super EmergencyContactDao> emergencyContactItemWriter() {
        return items -> emergencyContactRepository.saveAll(items);

    }





    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return null;
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return new SimpleDateFormat("yyyy-MM-dd").format(cell.getDateCellValue());
                }
                double numericValue = cell.getNumericCellValue();
                if (numericValue == Math.floor(numericValue)) {
                    return String.valueOf((int) numericValue);
                }
                return String.valueOf(numericValue);
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return null;
        }
    }

    private String getCellDateAsString(Cell cell) {
        if (cell == null) {
            return null;
        }
        if (cell.getCellType() == CellType.STRING) {
            return cell.getStringCellValue();
        } else if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
            return new SimpleDateFormat("dd/MM/yyyy").format(cell.getDateCellValue());
        }
        return null;
    }

    private Integer parseInteger(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    // Implement the previously empty methods
    public JobRepository getJobRepository() {
        return jobRepository;
    }

    public PlatformTransactionManager getTransactionManager() {
        return transactionManager;
    }

    public ItemReader<? extends Row> getPatientMasterItemReader(MultipartFile file) {
        return patientMasterItemReader(file);
    }

    public ItemProcessor<? super Row, ? extends PatientMasterDao> getPatientMasterItemProcessor() {
        return patientMasterItemProcessor();
    }

    public ItemWriter<? super PatientMasterDao> getPatientMasterItemWriter() {
        return patientMasterItemWriter();
    }



}




//    @Bean
//    public ItemProcessor<Row, PatientMasterDao> patientMasterItemProcessor() {
//        return row -> {
//            PatientMasterDao patient = new PatientMasterDao();
//            patient.setAge(getCellValueAsString(row.getCell(0)));
//            patient.setAgeDays(getCellValueAsString(row.getCell(1)));
//            patient.setAgeMonths(getCellValueAsString(row.getCell(2)));
//            patient.setDob(getCellDateAsString(row.getCell(3)));
//            patient.setEmiratesId(getCellValueAsString(row.getCell(4)));
//            patient.setFirstName(getCellValueAsString(row.getCell(5)));
//            patient.setFoDate(getCellValueAsString(row.getCell(6)));
//            patient.setGender(getCellValueAsString(row.getCell(7)));
//            patient.setIp(getCellValueAsString(row.getCell(8)));
//            patient.setIsVip(parseInteger(getCellValueAsString(row.getCell(9))));
//            patient.setLastName(getCellValueAsString(row.getCell(10)));
//            patient.setMiddleName(getCellValueAsString(row.getCell(11)));
//            patient.setPatientID(getCellValueAsString(row.getCell(12)));
//            patient.setSalutation(getCellValueAsString(row.getCell(13)));
//            patient.setGrouping(parseInteger(getCellValueAsString(row.getCell(14))));
//            patient.setPatGroup(parseInteger(getCellValueAsString(row.getCell(15))));
//            patient.setPostofficeId(parseInteger(getCellValueAsString(row.getCell(16))));
//            patient.setReligionId(parseInteger(getCellValueAsString(row.getCell(17))));
//            patient.setLanguageId(parseInteger(getCellValueAsString(row.getCell(18))));
//            patient.setMaritalId(parseInteger(getCellValueAsString(row.getCell(19))));
//            patient.setRaceId(parseInteger(getCellValueAsString(row.getCell(20))));
//            patient.setPassportNum(getCellValueAsString(row.getCell(21)));
//            patient.setGccId(getCellValueAsString(row.getCell(22)));
//            patient.setPriority(parseInteger(getCellValueAsString(row.getCell(23))));
//            patient.setPhoto(getCellValueAsString(row.getCell(24)));
//            patient.setCreatedDate(new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()));
//            patient.setClinicID(defaultClinicId);
//            patient.setCreatedBy(defaultCreatedById);
//            patient.setStatus(1);
//            patient.setGlobalConsent(0);
//            patient.setIsEstablished(0);
//            return patient;
//        };
//    }
//    @Bean
//    public ItemWriter<PatientMasterDao> patientMasterItemWriter() {
//        return items -> patientMasterRepository.saveAll(items);
//    }
//
//
//    @Override
//    public String uploadAddressMaster(MultipartFile file) {
//        try (InputStream inputStream = file.getInputStream();
//             Workbook workbook = WorkbookFactory.create(inputStream)) {
//
//            Sheet sheet = workbook.getSheetAt(0);
//            Iterator<Row> rowIterator = sheet.iterator();
//            List<AddressMasterDao> addressList = new ArrayList<>();
//
//            // Skip header row
//            if (rowIterator.hasNext()) {
//                rowIterator.next();
//            }
//
//            while (rowIterator.hasNext()) {
//                Row row = rowIterator.next();
//                AddressMasterDao address = new AddressMasterDao();
//
//                address.setAddress1(getCellValueAsString(row.getCell(0)));
//                address.setAddress2(getCellValueAsString(row.getCell(1)));
//                address.setCreatedDate(getCellValueAsString(row.getCell(2)));
//                address.setEmailID(getCellValueAsString(row.getCell(3)));
//                address.setFaxNo(getCellValueAsString(row.getCell(4)));
//                address.setIp(getCellValueAsString(row.getCell(5)));
//                address.setMobileNo(getCellValueAsString(row.getCell(6)));
//                address.setPhoneNo(getCellValueAsString(row.getCell(7)));
//                address.setPlace(getCellValueAsString(row.getCell(8)));
//                address.setUploadfile(getCellValueAsString(row.getCell(9)));
//                address.setCountryID(parseInteger(getCellValueAsString(row.getCell(10))));
//                address.setAddPatientMasterID(parseInteger(getCellValueAsString(row.getCell(11))));
//                address.setStateID(parseInteger(getCellValueAsString(row.getCell(12))));
//                address.setHowKnow(getCellValueAsString(row.getCell(13)));
//                address.setAddressType(parseInteger(getCellValueAsString(row.getCell(14))));
//                address.setCountryCode(parseInteger(getCellValueAsString(row.getCell(15))));
//                address.setClinicCountryId(parseInteger(getCellValueAsString(row.getCell(16))));
//
//                addressList.add(address);
//            }
//
//            addressMasterRepository.saveAll(addressList);
//            return "AddressMaster data uploaded successfully";
//
//        } catch (IOException e) {
//            return "Error reading file: " + e.getMessage();
//        } catch (Exception e) {
//            return "Error populating addressmaster: " + e.getMessage();
//        }
//    }
//
//
//
//


