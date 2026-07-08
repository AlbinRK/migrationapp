package com.pappyjoe.pappybridge.batch.processor;

import com.pappyjoe.pappybridge.models.dtos.SaveAddressMasterDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AddressProcessor implements ItemProcessor<SaveAddressMasterDto, SaveAddressMasterDto> {

    @Value("${default.clinicId:1}")
    private Integer defaultClinicId;

    @Override
    public SaveAddressMasterDto process(SaveAddressMasterDto dto) {
        dto.setAddress1(normalizeText(dto.getAddress1()));
        dto.setAddress2(normalizeText(dto.getAddress2()));
        dto.setEmailId(normalizeText(dto.getEmailId()));
        dto.setFaxNo(normalizeText(dto.getFaxNo()));
        dto.setIp(normalizeText(dto.getIp()));
        dto.setMobileNo(normalizeText(dto.getMobileNo()));
        dto.setPhoneNo(normalizeText(dto.getPhoneNo()));
        dto.setPlace(normalizeText(dto.getPlace()));
        dto.setHowKnow(normalizeText(dto.getHowKnow()));

        if (dto.getClinicId() == null || dto.getClinicId() <= 0) {
            log.warn("clinicId missing/invalid, using default clinicId={}", defaultClinicId);
            dto.setClinicId(defaultClinicId);
        }

        return dto;
    }

    private String normalizeText(String value) {
        if (value == null) {
            return null;
        }

        String normalized = value.replace('\u00A0', ' ').strip();
        return normalized.isEmpty() ? null : normalized;
    }
}