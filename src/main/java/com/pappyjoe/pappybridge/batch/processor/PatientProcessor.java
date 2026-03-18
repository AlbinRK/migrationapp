package com.pappyjoe.pappybridge.batch.processor;

import com.pappyjoe.pappybridge.exceptions.ValidationException;
import com.pappyjoe.pappybridge.models.dtos.SaveRegPatientMasterDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PatientProcessor implements ItemProcessor<SaveRegPatientMasterDto, SaveRegPatientMasterDto> {

    @Override
    public SaveRegPatientMasterDto process(SaveRegPatientMasterDto dto) {

        if (dto.getPatientId() == null || dto.getPatientId().isBlank()) {
            log.warn("Skipping record: PatientId is null");
            throw new ValidationException("Invalid PatientId");
        }

        if (dto.getFirstName() == null || dto.getFirstName().isBlank()) {
            log.warn("Skipping record: FirstName missing for patientId={}", dto.getPatientId());
            throw new ValidationException("Invalid FirstName");
        }

        // You can normalize data here if needed

        return dto;
    }
}