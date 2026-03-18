package com.pappyjoe.pappybridge.config;

import com.pappyjoe.pappybridge.batch.listener.BatchSkipListener;
import com.pappyjoe.pappybridge.batch.listener.BatchStepListener;
import com.pappyjoe.pappybridge.models.dtos.SaveAddressMasterDto;
import com.pappyjoe.pappybridge.batch.processor.AddressProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class AddressMigrationJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final ItemReader<SaveAddressMasterDto> reader;
    private final AddressProcessor processor;
    private final JdbcBatchItemWriter<SaveAddressMasterDto> addressWriter;

    private final BatchStepListener listener;
    private final BatchSkipListener skipListener;


    @Bean
    public Step addressStep() {
        return new StepBuilder("addressStep", jobRepository)
                .<SaveAddressMasterDto, SaveAddressMasterDto>chunk(50, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(addressWriter)

                //  FAULT TOLERANCE (same as patient)
                .faultTolerant()
                .skip(ValidationException.class)   // IMPORTANT: not generic Exception
                .skipLimit(1000)
                .retry(Exception.class)            // retry DB/network issues
                .retryLimit(3)

                .listener(listener)
                .listener(skipListener)
                .build();
    }

    @Bean
    public Job addressMigrationJob(Step addressStep) {
        return new JobBuilder("addressMigrationJob", jobRepository)
                .start(addressStep)
                .build();
    }


}
