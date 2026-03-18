package com.pappyjoe.pappybridge.config;

import com.pappyjoe.pappybridge.batch.listener.BatchSkipListener;
import com.pappyjoe.pappybridge.batch.listener.BatchStepListener;
import com.pappyjoe.pappybridge.models.dtos.SaveRegPatientMasterDto;
import com.pappyjoe.pappybridge.batch.processor.PatientProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class PatientMigrationJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final ItemReader<SaveRegPatientMasterDto> reader;
    private final PatientProcessor processor;
    private final JdbcBatchItemWriter<SaveRegPatientMasterDto> patientWriter;
    private final BatchStepListener listener;
    private final BatchSkipListener skipListener;

    @Bean
    public Step patientStep() {
        return new StepBuilder("patientStep", jobRepository)
                .<SaveRegPatientMasterDto, SaveRegPatientMasterDto>chunk(100, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(patientWriter)

                //FAULT TOLERANCE
                .faultTolerant()
                .skip(ValidationException.class)
                .skipLimit(1000)
                .retry(Exception.class)
                .retryLimit(3)

                .listener(listener)
                .listener(skipListener)
                .build();
    }

    @Bean
    public Job patientMigrationJob(Step patientStep) {
        return new JobBuilder("patientMigrationJob", jobRepository)
                .start(patientStep)
                .build();
    }

//
//    @Bean
//    public Step addressMigrationStep(JobRepository jobRepository,
//                                     PlatformTransactionManager transactionManager,
//                                     ItemReader<Row> addressMasterItemReader,
//                                     ItemProcessor<Row, AddressMasterDao> addressMasterItemProcessor,
//                                     ItemWriter<AddressMasterDao> addressMasterItemWriter) {
//
//        return new StepBuilder("addressMigrationStep", jobRepository)
//                .<Row, AddressMasterDao>chunk(100, transactionManager)
//                .reader(addressMasterItemReader)
//                .processor(addressMasterItemProcessor)
//                .writer(addressMasterItemWriter)
//                .build();
//    }




}