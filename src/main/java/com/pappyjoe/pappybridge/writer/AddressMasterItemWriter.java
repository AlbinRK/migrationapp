package com.pappyjoe.pappybridge.writer;

import com.pappyjoe.pappybridge.models.dtos.SaveAddressMasterDto;
import com.pappyjoe.pappybridge.repositories.AddressMasterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AddressMasterItemWriter implements ItemWriter<SaveAddressMasterDto> {

    private final AddressMasterRepository repository;


    @Value("${aesSecretKey}")
    private String secretKey;

    @Override
    public void write(Chunk<? extends SaveAddressMasterDto> chunk) {

        for (SaveAddressMasterDto dto : chunk) {
            repository.insertAddress(dto, secretKey);
        }
    }


}


