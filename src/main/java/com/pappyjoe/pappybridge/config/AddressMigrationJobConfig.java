package com.pappyjoe.pappybridge.config;

import com.pappyjoe.pappybridge.models.dtos.SaveAddressMasterDto;
import com.pappyjoe.pappybridge.models.dtos.SaveRegPatientMasterDto;
import com.pappyjoe.pappybridge.writer.AddressMasterItemWriter;
import com.pappyjoe.pappybridge.writer.PatientMasterItemWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class AddressMigrationJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final AddressMasterItemWriter writer;

    @Bean
    public Step addressStep(ItemReader<SaveAddressMasterDto> reader) {
        return new StepBuilder("addressStep", jobRepository)
                .<SaveAddressMasterDto, SaveAddressMasterDto>chunk(50, transactionManager)
                .reader(reader)
                .writer(writer)
                .build();
    }

    @Bean
    public Job addressMigrationJob(Step addressStep) {
        return new JobBuilder("addressMigrationJob", jobRepository)
                .start(addressStep)
                .build();
    }


}
