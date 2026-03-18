package com.pappyjoe.pappybridge.batch.writer;

import com.pappyjoe.pappybridge.models.dtos.SaveRegPatientMasterDto;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import javax.sql.DataSource;

@Configuration
public class PatientWriterConfig {

    @Value("${aesSecretKey}")
    private String aesKey;

    @Bean
    public JdbcBatchItemWriter<SaveRegPatientMasterDto> patientWriter(DataSource dataSource) {

        JdbcBatchItemWriter<SaveRegPatientMasterDto> writer = new JdbcBatchItemWriter<>();
        writer.setDataSource(dataSource);

        writer.setSql("""
            INSERT INTO patientmaster (
                patientMasterID,
                age,
                ageDays,
                ageMonths,
                CreatedDate,
                clinicID,
                DOB,
                EmiratesID,
                FirstName,
                FoDate,
                Gender,
                IP,
                IsVip,
                LastName,
                MiddleName,
                PatientID,
                Salutation,
                Status,
                religionId,
                languageId,
                maritalId,
                raceId,
                a28_msg_status,
                passportNum,
                gccId,
                priority,
                globalConsent,
                isEstablished,
                photo,
                CreatedByUserId
            ) VALUES (
                :patientMasterId,
                AES_ENCRYPT(:ageYears, :aesKey),
                AES_ENCRYPT(:ageDays, :aesKey),
                AES_ENCRYPT(:ageMonths, :aesKey),
                :createdDate,
                :clinicId,
                AES_ENCRYPT(:dob, :aesKey),
                CASE WHEN :emiratesId IS NULL OR :emiratesId = '' 
                    THEN NULL ELSE AES_ENCRYPT(:emiratesId, :aesKey) END,
                AES_ENCRYPT(:firstName, :aesKey),
                :foDate,
                AES_ENCRYPT(:gender, :aesKey),
                :ip,
                :isVip,
                AES_ENCRYPT(:lastName, :aesKey),
                CASE WHEN :middleName IS NULL OR :middleName = '' 
                    THEN NULL ELSE AES_ENCRYPT(:middleName, :aesKey) END,
                :patientId,
                :salutation,
                :status,
                :religionId,
                :languageId,
                :maritalId,
                :raceId,
                :a28MsgStatus,
                CASE WHEN :passportId IS NULL OR :passportId = '' 
                    THEN NULL ELSE AES_ENCRYPT(:passportId, :aesKey) END,
                CASE WHEN :gccId IS NULL OR :gccId = '' 
                    THEN NULL ELSE AES_ENCRYPT(:gccId, :aesKey) END,
                :priority,
                :globalConsent,
                :isEstablished,
                :photo,
                :createdByUserId
            )
        """);

        writer.setItemSqlParameterSourceProvider(item -> {
            MapSqlParameterSource params = new MapSqlParameterSource();

            params.addValue("patientMasterId", item.getPatientMasterId());
            params.addValue("ageYears", item.getAgeYears());
            params.addValue("ageDays", item.getAgeDays());
            params.addValue("ageMonths", item.getAgeMonths());
            params.addValue("createdDate", item.getCreatedDate());
            params.addValue("clinicId", item.getClinicId());
            params.addValue("dob", item.getDob());
            params.addValue("emiratesId", item.getEmiratesId());
            params.addValue("firstName", item.getFirstName());
            params.addValue("foDate", item.getFoDate());
            params.addValue("gender", item.getGender());
            params.addValue("ip", item.getIp());
            params.addValue("isVip", item.getIsVip());
            params.addValue("lastName", item.getLastName());
            params.addValue("middleName", item.getMiddleName());
            params.addValue("patientId", item.getPatientId());
            params.addValue("salutation", item.getSalutation());
            params.addValue("status", item.getStatus());
            params.addValue("religionId", item.getReligionId());
            params.addValue("languageId", item.getLanguageId());
            params.addValue("maritalId", item.getMaritalId());
            params.addValue("raceId", item.getRaceId());
            params.addValue("a28MsgStatus", item.getA28MsgStatus());
            params.addValue("passportId", item.getPassportId());
            params.addValue("gccId", item.getGccId());
            params.addValue("priority", item.getPriority());
            params.addValue("globalConsent", item.getGlobalConsent());
            params.addValue("isEstablished", item.getIsEstablished());
            params.addValue("photo", item.getPhoto());
            params.addValue("createdByUserId", item.getCreatedByUserId());

            params.addValue("aesKey", aesKey);

            return params;
        });

        writer.afterPropertiesSet();
        return writer;
    }
}