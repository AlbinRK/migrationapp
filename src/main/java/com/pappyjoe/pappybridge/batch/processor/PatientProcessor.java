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
        dto.setPatientId(normalizeText(dto.getPatientId()));
        dto.setFirstName(normalizeText(dto.getFirstName()));
        dto.setLastName(normalizeText(dto.getLastName()));
        dto.setMiddleName(normalizeText(dto.getMiddleName()));
        dto.setEmiratesId(normalizeText(dto.getEmiratesId()));
        dto.setPassportNum(normalizeText(dto.getPassportNum()));
        dto.setGccId(normalizeText(dto.getGccId()));
        dto.setGender(normalizeText(dto.getGender()));
        dto.setSalutation(normalizeText(dto.getSalutation()));

        if (dto.getPatientId() == null || dto.getPatientId().isBlank()) {
            log.warn("Skipping record: PatientId is null");
            throw new ValidationException("Invalid PatientId");
        }

        if (dto.getFirstName() == null || dto.getFirstName().isBlank()) {
            // Keep the row migratable when source firstName is blank.
            dto.setFirstName(dto.getPatientId());
            log.warn("FirstName missing for patientId={}, defaulting firstName to patientId", dto.getPatientId());
        }

        return dto;
    }

    private String normalizeText(String value) {
        if (value == null) {
            return null;
        }

        // Normalize non-breaking spaces often seen in Excel exports.
        String normalized = value.replace('\u00A0', ' ').strip();
        return normalized.isEmpty() ? null : normalized;
    }
}