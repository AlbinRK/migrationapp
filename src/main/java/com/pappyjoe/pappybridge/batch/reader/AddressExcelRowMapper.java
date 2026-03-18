package com.pappyjoe.pappybridge.batch.reader;

import com.pappyjoe.pappybridge.models.dtos.SaveAddressMasterDto;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AddressExcelRowMapper {


    public SaveAddressMasterDto mapRow(Row row) {

        SaveAddressMasterDto dto = new SaveAddressMasterDto();

        dto.setAddressMasterID(getIntegerValue(row,0));
        dto.setAddress1(getStringValue(row, 1));
        dto.setAddress2(getStringValue(row, 2));
        dto.setEmailId(getStringValue(row, 3));
        dto.setFaxNo(getStringValue(row, 4));
        dto.setIp(getStringValue(row, 5));
        dto.setMobileNo(getStringValue(row, 6));
        dto.setPhoneNo(getStringValue(row, 7));
        dto.setPlace(getStringValue(row, 8));
        dto.setCountryId(getIntegerValue(row, 9));
        dto.setAddPatientMasterId(getIntegerValue(row, 10));
        dto.setStateId(getIntegerValue(row, 11));
        dto.setHowKnow(getStringValue(row, 12));
        dto.setAddressType(getIntegerValue(row, 13));
        dto.setCountryCode(getIntegerValue(row, 14));
        dto.setClinicCountryId(getIntegerValue(row, 15));
        dto.setStatus(getIntegerValue(row, 16));
        dto.setClinicId(getIntegerValue(row, 17));
        dto.setCreatedById(getIntegerValue(row, 18));
        dto.setCreatedDate(getDateTimeValue(row, 19 ));


        return dto;
    }

    private String getStringValue(Row row, int index) {
        Cell cell = row.getCell(index);

        if (cell == null) return null;

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();

            case NUMERIC:
                double value = cell.getNumericCellValue();

                // remove .0 if whole number
                if (value == (long) value) {
                    return String.valueOf((long) value);
                } else {
                    return String.valueOf(value);
                }

            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());

            case FORMULA:
                return cell.getCellFormula();

            default:
                return null;
        }
    }


    private Integer getIntegerValue(Row row, int index) {
        Cell cell = row.getCell(index);

        if (cell == null) return null;

        try {
            switch (cell.getCellType()) {

                case NUMERIC:
                    return (int) cell.getNumericCellValue();

                case STRING:
                    String value = cell.getStringCellValue().trim();

                    if (value.isEmpty()) return null;

                    // remove decimal if present (like "30.0")
                    if (value.contains(".")) {
                        value = value.substring(0, value.indexOf("."));
                    }

                    return Integer.parseInt(value);

                default:
                    return null;
            }

        } catch (Exception e) {
            return null;
        }
    }

    private LocalDateTime getDateTimeValue(Row row, int index) {

        Cell cell = row.getCell(index);
        if (cell == null) return LocalDateTime.now();

        try {

            //Case 1: Excel date (best case)
            if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
                return cell.getLocalDateTimeCellValue()
                        .toLocalDate()
                        .atStartOfDay(); // 00:00:00
            }

            //Case 2: String "18/03/2026"
            if (cell.getCellType() == CellType.STRING) {

                String value = cell.getStringCellValue().trim();

                if (value.isEmpty()) return LocalDateTime.now();

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                LocalDate date = LocalDate.parse(value, formatter);

                return date.atStartOfDay(); // 00:00:00
            }

        } catch (Exception e) {
            throw new RuntimeException("Invalid date at column " + index + ": " + cell);
        }

        return LocalDateTime.now();
    }
}
