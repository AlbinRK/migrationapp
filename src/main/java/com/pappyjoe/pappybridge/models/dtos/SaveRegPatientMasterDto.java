package com.pappyjoe.pappybridge.models.dtos;

import lombok.Data;

import java.time.LocalDate;
import java.util.Date;
@Data
public class SaveRegPatientMasterDto {


    private Integer patientMasterId;
    private String ageYears;
    private String ageDays;
    private String ageMonths;
    private Integer clinicId;
    private Integer createdByUserId;
    private String dob;
    private String emiratesId;
    private String firstName;
    private String foDate;
    private String gender;
    private String ip;
    private Integer isVip;
    private String lastName;
    private String middleName;
    private String patientId;
    private String salutation;
    private Integer status;
    private Integer religionId;
    private Integer languageId;
    private Integer maritalId;
    private Integer raceId;
    private Integer a28MsgStatus;
    private String passportId;
    private String gccId;
    private Integer priority;
    private Integer globalConsent;
    private Integer isEstablished;
    private String photo;
    private Integer Grouping;
    private Integer pat_group;
    private Integer postoffice_id;
    private String passportNum;
}
