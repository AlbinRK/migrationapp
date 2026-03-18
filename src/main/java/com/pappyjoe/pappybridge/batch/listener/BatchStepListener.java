package com.pappyjoe.pappybridge.batch.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BatchStepListener implements StepExecutionListener {

    @Override
    public void beforeStep(StepExecution stepExecution) {
        log.info("Step started: {}", stepExecution.getStepName());
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {

        log.info("Step completed: {}", stepExecution.getStepName());
        log.info("Read: {}", stepExecution.getReadCount());
        log.info("Written: {}", stepExecution.getWriteCount());
        log.info("Skipped: {}", stepExecution.getSkipCount());

        return stepExecution.getExitStatus();
    }
}