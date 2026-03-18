package com.pappyjoe.pappybridge.batch.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.SkipListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BatchSkipListener implements SkipListener<Object, Object> {

    @Override
    public void onSkipInRead(Throwable t) {
        log.error("Skipped during READ: {}", t.getMessage());
    }

    @Override
    public void onSkipInWrite(Object item, Throwable t) {
        log.error("Skipped during WRITE: {}, Error: {}", item, t.getMessage());
    }

    @Override
    public void onSkipInProcess(Object item, Throwable t) {
        log.error("Skipped during PROCESS: {}, Error: {}", item, t.getMessage());
    }
}