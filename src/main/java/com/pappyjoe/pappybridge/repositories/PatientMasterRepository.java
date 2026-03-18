package com.pappyjoe.pappybridge.repositories;

import com.pappyjoe.pappybridge.models.daos.PatientMasterDao;
import com.pappyjoe.pappybridge.models.dtos.SaveRegPatientMasterDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PatientMasterRepository extends JpaRepository<PatientMasterDao, Integer> {

    @Transactional
    @Modifying
    @Query(value = """
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
        :#{#dto.patientMasterId},
        AES_ENCRYPT(:#{#dto.ageYears}, :key),
        AES_ENCRYPT(:#{#dto.ageDays}, :key),
        AES_ENCRYPT(:#{#dto.ageMonths}, :key),
        CURRENT_DATE,
        :#{#dto.clinicId},
        AES_ENCRYPT(:#{#dto.dob}, :key),
        CASE WHEN :#{#dto.emiratesId} IS NULL OR :#{#dto.emiratesId} = '' 
            THEN NULL ELSE AES_ENCRYPT(:#{#dto.emiratesId}, :key) END,
        AES_ENCRYPT(:#{#dto.firstName}, :key),
        :#{#dto.foDate},
        AES_ENCRYPT(:#{#dto.gender}, :key),
        :#{#dto.ip},
        :#{#dto.isVip},
        AES_ENCRYPT(:#{#dto.lastName}, :key),
        CASE WHEN :#{#dto.middleName} IS NULL OR :#{#dto.middleName} = '' 
            THEN NULL ELSE AES_ENCRYPT(:#{#dto.middleName}, :key) END,
        :#{#dto.patientId},
        :#{#dto.salutation},
        :#{#dto.status},
        :#{#dto.religionId},
        :#{#dto.languageId},
        :#{#dto.maritalId},
        :#{#dto.raceId},
        :#{#dto.a28MsgStatus},
        CASE WHEN :#{#dto.passportId} IS NULL OR :#{#dto.passportId} = '' 
            THEN NULL ELSE AES_ENCRYPT(:#{#dto.passportId}, :key) END,
        CASE WHEN :#{#dto.gccId} IS NULL OR :#{#dto.gccId} = '' 
            THEN NULL ELSE AES_ENCRYPT(:#{#dto.gccId}, :key) END,
        :#{#dto.priority},
        :#{#dto.globalConsent},
        :#{#dto.isEstablished},
        :#{#dto.photo},
        :#{#dto.createdByUserId}
       
    )
""", nativeQuery = true)
    void insertPatient(@Param("dto") SaveRegPatientMasterDto dto,
                       @Param("key") String key);
}
