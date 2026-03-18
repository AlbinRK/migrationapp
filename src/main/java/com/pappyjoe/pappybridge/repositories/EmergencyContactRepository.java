package com.pappyjoe.pappybridge.repositories;

import com.pappyjoe.pappybridge.models.daos.EmergencyContactDao;
import com.pappyjoe.pappybridge.models.daos.PatientMasterDao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmergencyContactRepository extends JpaRepository<EmergencyContactDao, Integer> {
}
