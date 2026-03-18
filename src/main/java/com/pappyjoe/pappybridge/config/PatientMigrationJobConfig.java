package com.pappyjoe.pappybridge.config;

import com.pappyjoe.pappybridge.models.daos.AddressMasterDao;
import com.pappyjoe.pappybridge.models.dtos.SaveAddressMasterDto;
import com.pappyjoe.pappybridge.models.dtos.SaveRegPatientMasterDto;
import com.pappyjoe.pappybridge.writer.PatientMasterItemWriter;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class PatientMigrationJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final PatientMasterItemWriter writer;

    @Bean
    public Step patientStep(ItemReader<SaveRegPatientMasterDto> reader) {
        return new StepBuilder("patientStep", jobRepository)
                .<SaveRegPatientMasterDto, SaveRegPatientMasterDto>chunk(50, transactionManager)
                .reader(reader)
                .writer(writer)
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