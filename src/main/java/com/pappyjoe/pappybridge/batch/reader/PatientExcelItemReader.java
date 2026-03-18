package com.pappyjoe.pappybridge.batch.reader;

import com.pappyjoe.pappybridge.models.dtos.SaveRegPatientMasterDto;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;

@Component
@StepScope
public class PatientExcelItemReader implements ItemReader<SaveRegPatientMasterDto> {

    @Value("#{jobParameters['filePath']}")
    private String filePath;

    private Iterator<Row> rowIterator;

    private final PatientExcelRowMapper rowMapper = new PatientExcelRowMapper();

    private Workbook workbook;
    private FileInputStream fis;

    @PostConstruct
    public void init() throws Exception {

        fis = new FileInputStream(new File(filePath));
        workbook = WorkbookFactory.create(fis);
        Sheet sheet = workbook.getSheetAt(0);

        rowIterator = sheet.iterator();

        // Skip header row
        if (rowIterator.hasNext()) {
            rowIterator.next();
        }

    }


    @PreDestroy
    public void cleanup() throws Exception {
        if (workbook != null) workbook.close();
        if (fis != null) fis.close();
    }


    @Override
    public SaveRegPatientMasterDto read() {

        if (rowIterator != null && rowIterator.hasNext()) {
            Row row = rowIterator.next();
            return rowMapper.mapRow(row);
        }

        return null; // signals end of file
    }
}