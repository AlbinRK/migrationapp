package com.pappyjoe.pappybridge.repositories;

import com.pappyjoe.pappybridge.models.daos.AddressMasterDao;
import com.pappyjoe.pappybridge.models.dtos.SaveAddressMasterDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface AddressMasterRepository extends JpaRepository<AddressMasterDao, Integer> {

        @Transactional
        @Modifying
        @Query(value = """
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
            AES_ENCRYPT(:#{#dto.address1}, :key),
            AES_ENCRYPT(:#{#dto.address2}, :key),
            :#{#dto.clinicId},
            :#{#dto.createdById},
            CURRENT_TIMESTAMP,
            AES_ENCRYPT(:#{#dto.emailId}, :key),
            AES_ENCRYPT(:#{#dto.faxNo}, :key),
            :#{#dto.ip},
            AES_ENCRYPT(:#{#dto.mobileNo}, :key),
            AES_ENCRYPT(:#{#dto.phoneNo}, :key),
            AES_ENCRYPT(:#{#dto.place}, :key),
            1,
            :#{#dto.uploadfile},
            :#{#dto.countryId},
            :#{#dto.addPatientMasterId},
            :#{#dto.stateId},
            AES_ENCRYPT(:#{#dto.howKnow}, :key),
            :#{#dto.addressType},
            :#{#dto.countryCode},
            :#{#dto.clinicCountryId}
        )
    """, nativeQuery = true)
        void insertAddress(@Param("dto") SaveAddressMasterDto dto,
                           @Param("key") String key);
    }

