package com.pappyjoe.pappybridge.batch.processor;

import com.pappyjoe.pappybridge.exceptions.ValidationException;
import com.pappyjoe.pappybridge.models.dtos.SaveAddressMasterDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AddressProcessor implements ItemProcessor<SaveAddressMasterDto, SaveAddressMasterDto> {

    @Override
    public SaveAddressMasterDto process(SaveAddressMasterDto dto) {

//        if (dto.getAddress1() == null || dto.getAddress1().isBlank()) {
//            log.warn("Skipping record: Address1 is null");
//            throw new ValidationException("Invalid Address1");
//        }

        if (dto.getClinicId() == null) {
            log.warn("Skipping record: clinicId missing for address");
            throw new ValidationException("Invalid clinicId");
        }

        return dto;
    }
}