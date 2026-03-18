package com.pappyjoe.pappybridge.batch.writer;

import com.pappyjoe.pappybridge.models.dtos.SaveAddressMasterDto;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import javax.sql.DataSource;

@Configuration
public class AddressWriterConfig {

    @Value("${aesSecretKey}")
    private String aesKey;

    @Bean
    public JdbcBatchItemWriter<SaveAddressMasterDto> addressWriter(DataSource dataSource) {

        JdbcBatchItemWriter<SaveAddressMasterDto> writer = new JdbcBatchItemWriter<>();
        writer.setDataSource(dataSource);

        writer.setSql("""
            INSERT INTO addressmaster (
                Address1,
                Address2,
                clinicID,
                CreatedById,
                CreatedDate,
                EmailID,
                FaxNo,
                IP,
                MobileNo,
                PhoneNo,
                Place,
                status,
                uploadfile,
                CountryID,
                add_patientMasterID,
                StateID,
                howKnow,
                addressType,
                country_code,
                clinic_country_id
            ) VALUES (
                AES_ENCRYPT(:address1, :aesKey),
                AES_ENCRYPT(:address2, :aesKey),
                :clinicId,
                :createdById,
                :createdDate,
                AES_ENCRYPT(:emailId, :aesKey),
                AES_ENCRYPT(:faxNo, :aesKey),
                :ip,
                AES_ENCRYPT(:mobileNo, :aesKey),
                AES_ENCRYPT(:phoneNo, :aesKey),
                AES_ENCRYPT(:place, :aesKey),
                :status,
                :uploadfile,
                :countryId,
                :addPatientMasterId,
                :stateId,
                AES_ENCRYPT(:howKnow, :aesKey),
                :addressType,
                :countryCode,
                :clinicCountryId
            )
        """);

        writer.setItemSqlParameterSourceProvider(item -> {
            MapSqlParameterSource params = new MapSqlParameterSource();

            params.addValue("address1", item.getAddress1());
            params.addValue("address2", item.getAddress2());
            params.addValue("clinicId", item.getClinicId());
            params.addValue("createdById", item.getCreatedById());
            params.addValue("createdDate", item.getCreatedDate());
            params.addValue("emailId", item.getEmailId());
            params.addValue("faxNo", item.getFaxNo());
            params.addValue("ip", item.getIp());
            params.addValue("mobileNo", item.getMobileNo());
            params.addValue("phoneNo", item.getPhoneNo());
            params.addValue("place", item.getPlace());
            params.addValue("status", item.getStatus());
            params.addValue("uploadfile", item.getUploadfile());
            params.addValue("countryId", item.getCountryId());
            params.addValue("addPatientMasterId", item.getAddPatientMasterId());
            params.addValue("stateId", item.getStateId());
            params.addValue("howKnow", item.getHowKnow());
            params.addValue("addressType", item.getAddressType());
            params.addValue("countryCode", item.getCountryCode());
            params.addValue("clinicCountryId", item.getClinicCountryId());

            params.addValue("aesKey", aesKey);

            return params;
        });

        writer.afterPropertiesSet();
        return writer;
    }
}
