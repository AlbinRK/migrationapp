package com.pappyjoe.pappybridge.models.daos;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "addressmaster", schema = "test_migration")
public class AddressMasterDao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "addressMasterID")
    private Integer addressMasterID;

    @Column(name = "Address1")
    private String address1;

    @Column(name = "Address2")
    private String address2;

    @Column(name = "clinicID")
    private Integer clinicID;

    @Column(name = "CreatedById")
    private Integer createdById;

   // @CreationTimestamp
    @Column(name = "CreatedDate", nullable = false, updatable = false)
    private String createdDate;  // consider using LocalDateTime

    @Column(name = "EmailID")
    private String emailID;

    @Column(name = "FaxNo")
    private String faxNo;

    @Column(name = "IP")
    private String ip;

    @Column(name = "MobileNo")
    private String mobileNo;

    @Column(name = "PhoneNo")
    private String phoneNo;

    @Column(name = "Place")
    private String place;

    @Column(name = "status")
    private Integer status = 1;

    @Column(name = "uploadfile")
    private String uploadfile;

    @Column(name = "CountryID")
    private Integer countryID;

    @Column(name = "add_patientMasterID")
    private Integer addPatientMasterID;

    @Column(name = "StateID")
    private Integer stateID;

    @Column(name = "howKnow")
    private String howKnow;

    @Column(name = "addressType")
    private Integer addressType;

    @Column(name = "country_code")
    private Integer countryCode;

    @Column(name = "clinic_country_id")
    private Integer clinicCountryId;
}