package com.pappyjoe.pappybridge.services.serviceImpl;

import com.pappyjoe.pappybridge.models.daos.AddressMasterDao;
import com.pappyjoe.pappybridge.models.daos.EmergencyContactDao;
import com.pappyjoe.pappybridge.models.daos.PatientMasterDao;
import com.pappyjoe.pappybridge.repositories.EmergencyContactRepository;

import com.pappyjoe.pappybridge.services.MigrationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;

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
@Slf4j
@Service
public class MigrationServiceImpl implements MigrationService {



    @Value("${default.clinicId:1}")
    private Integer defaultClinicId;

    @Value("${default.createdById:1}")
    private Integer defaultCreatedById;

    private static final String UPLOAD_DIR = "C:/migration-temp/"; // change if needed



    @Override
    public String storeFile(MultipartFile file) throws IOException {

        File directory = new File(UPLOAD_DIR);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String filePath = UPLOAD_DIR + System.currentTimeMillis() + "_" + file.getOriginalFilename();
        log.info("File stored at: {}", filePath);
        File dest = new File(filePath);
        file.transferTo(dest);

        return filePath;
    }


//
//    private ItemProcessor<Row, PatientMasterDao> patientMasterItemProcessor() {
//        return row -> {
//            PatientMasterDao patient = new PatientMasterDao();
//            patient.setAge(getCellValueAsString(row.getCell(0)));
//            patient.setAgeDays(getCellValueAsString(row.getCell(1)));
//            patient.setAgeMonths(getCellValueAsString(row.getCell(2)));
//            patient.setDob(getCellDateAsString(row.getCell(3)));
//            patient.setEmiratesID(getCellValueAsString(row.getCell(4))); // Changed to emiratesID
//            patient.setFirstName(getCellValueAsString(row.getCell(5)));
//            patient.setFoDate(parseLocalDate(getCellValueAsString(row.getCell(6))));
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
//            patient.setCreatedDate(LocalDate.now()); // Use LocalDate
//            patient.setClinicID(defaultClinicId);
//            patient.setCreatedByUserId(defaultCreatedById); // Changed to createdByUserId
//            patient.setStatus(1);
//            patient.setGlobalConsent(0);
//            patient.setIsEstablished(0);
//            patient.setA28MsgStatus(0);
//            return patient;
//        };
//    }







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


