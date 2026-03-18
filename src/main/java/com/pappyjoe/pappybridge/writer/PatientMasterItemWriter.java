package com.pappyjoe.pappybridge.writer;

import com.pappyjoe.pappybridge.models.dtos.SaveRegPatientMasterDto;
import com.pappyjoe.pappybridge.repositories.PatientMasterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PatientMasterItemWriter implements ItemWriter<SaveRegPatientMasterDto> {

    private final PatientMasterRepository repository;

    @Value("${aesSecretKey}")
    private String secretKey;

    @Override
    public void write(Chunk<? extends SaveRegPatientMasterDto> chunk) {

        for (SaveRegPatientMasterDto dto : chunk) {
            repository.insertPatient(dto, secretKey);
        }
    }
}
