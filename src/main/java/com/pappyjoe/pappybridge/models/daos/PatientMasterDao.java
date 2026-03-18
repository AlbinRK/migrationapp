package com.pappyjoe.pappybridge.models.daos;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "patientmaster")
@DynamicInsert
@DynamicUpdate
public class PatientMasterDao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "patientMasterID")
    private Integer patientMasterID;

    @Column(name = "age")
    private String age;

    @Column(name = "ageDays")
    private String ageDays;

    @Column(name = "ageMonths")
    private String ageMonths;

    @Column(name = "clinicID")
    private Integer clinicID = 1;

    @Column(name = "CreatedByUserId")
    private Integer createdByUserId = 1;

   // @CreationTimestamp
    @Column(name = "CreatedDate", nullable = false, updatable = false)
    private LocalDate createdDate;

    @Column(name = "DOB")
    private String dob;

    @Column(name = "EmiratesID")
    private String emiratesID;

    @Column(name = "FirstName")
    private String firstName;

    @Column(name = "FoDate")
    private LocalDate foDate;

    @Column(name = "Gender")
    private String gender;

    @Column(name = "IP")
    private String ip;

    @Column(name = "IsVip")
    private Integer isVip;

    @Column(name = "LastName")
    private String lastName;

    @Column(name = "MiddleName")
    private String middleName;

    @Column(name = "PatientID")
    private String patientID;

    @Column(name = "Salutation")
    private String salutation;

    @Column(name = "Status")
    private Integer status = 1;

    @Column(name = "`Grouping`")
    private Integer grouping;

    @Column(name = "pat_group")
    private Integer patGroup;

    @Column(name = "postoffice_id")
    private Integer postofficeId;

    @Column(name = "religionId")
    private Integer religionId;

    @Column(name = "languageId")
    private Integer languageId;

    @Column(name = "maritalId")
    private Integer maritalId;

    @Column(name = "raceId")
    private Integer raceId;

    @Column(name = "a28_msg_status")
    private Integer a28MsgStatus = 0;

    @Column(name = "passportNum")
    private String passportNum;

    @Column(name = "gccId")
    private String gccId;

    @Column(name = "priority")
    private Integer priority;

    @Column(name = "globalConsent")
    private Integer globalConsent = 0;

    @Column(name = "isEstablished")
    private Integer isEstablished = 0;

    @Column(name = "photo")
    private String photo;
}