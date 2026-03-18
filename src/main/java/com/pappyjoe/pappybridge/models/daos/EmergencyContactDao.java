package com.pappyjoe.pappybridge.models.daos;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "emergencycontact", schema = "test_migration")
@DynamicInsert
@DynamicUpdate
public class EmergencyContactDao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "emgContactID")
    private Integer emgContactID;

    @Column(name = "address1")
    private String address1;

    @Column(name = "address2")
    private String address2;

    @Column(name = "clinicID")
    private Integer clinicID = 1;

    @Column(name = "CreatedByUserId")
    private Integer createdByUserId = 1;

    @Column(name = "CreatedDate")
    private LocalDate createdDate;

    @Column(name = "eCNO")
    private Integer eCNO;

    @Column(name = "fullName")
    private String fullName;

    @Column(name = "IP")
    private String ip;

    @Column(name = "mobile")
    private String mobile;

    @Column(name = "Status")
    private Integer status = 1;

    @Column(name = "emg_patientMasterID")
    private Integer emgPatientMasterID;

    @Column(name = "addressType")
    private Integer addressType;

    @Column(name = "streetAddress")
    private String streetAddress;

    @Column(name = "equipmentType")
    private String equipmentType;

    @Column(name = "relationshipId")
    private Integer relationshipId;

    @Column(name = "contactType")
    private Integer contactType;

    @Column(name = "hl7_msg_status")
    private Integer hl7MsgStatus = 0;

    @Column(name = "lastName")
    private String lastName;
}