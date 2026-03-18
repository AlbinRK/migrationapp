package com.pappyjoe.pappybridge.models.dtos;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class SaveAddressMasterDto {
    private Integer addressMasterID;
    private String address1;
    private String address2;
    private Integer clinicId;
    private Integer createdById;
    private String emailId;
    private String faxNo;
    private String ip;
    private String mobileNo;
    private String phoneNo;
    private String place;
    private String uploadfile;
    private Integer countryId;
    private Integer addPatientMasterId;
    private Integer stateId;
    private String howKnow;
    private Integer addressType;
    private Integer countryCode;
    private Integer clinicCountryId;
    private Integer status;
    private LocalDateTime createdDate;

}