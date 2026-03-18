package com.pappyjoe.pappybridge.batch.reader;

import com.pappyjoe.pappybridge.models.dtos.SaveRegPatientMasterDto;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class PatientExcelRowMapper {

    public SaveRegPatientMasterDto mapRow(Row row) {

        SaveRegPatientMasterDto dto = new SaveRegPatientMasterDto();

        String dobString = getStringValue(row, 0);
        dto.setDob(dobString);

        dto.setEmiratesId(getStringValue(row, 1));
        dto.setFirstName(getStringValue(row, 2));
        dto.setFoDate(getStringValue(row, 3));
        dto.setGender(getStringValue(row, 4));
        dto.setIp(getStringValue(row, 5));
        dto.setIsVip(getIntegerValue(row, 6));
        dto.setLastName(getStringValue(row, 7));
        dto.setMiddleName(getStringValue(row, 8));
        dto.setPatientId(getStringValue(row, 9));
        dto.setSalutation(getStringValue(row, 10));
        dto.setGrouping(getIntegerValue(row, 11));
        dto.setPat_group(getIntegerValue(row, 12));
        dto.setPostoffice_id(getIntegerValue(row, 13));
        dto.setReligionId(getIntegerValue(row, 14));
        dto.setLanguageId(getIntegerValue(row, 15));
        dto.setMaritalId(getIntegerValue(row, 16));
        dto.setRaceId(getIntegerValue(row, 17));
        dto.setPassportNum(getStringValue(row, 18));
        dto.setGccId(getStringValue(row, 19));
        dto.setPriority(getIntegerValue(row, 20));
        dto.setClinicId(getIntegerValue(row, 21));
        dto.setCreatedByUserId(getIntegerValue(row, 22));
        dto.setStatus(getIntegerValue(row, 23));
        dto.setA28MsgStatus(getIntegerValue(row, 24));
        dto.setGlobalConsent(getIntegerValue(row, 25));
        dto.setIsEstablished(getIntegerValue(row, 26));
        dto.setCreatedDate(getDateValue(row,27 ));

        // =========================
        // AGE CALCULATION FROM DOB
        // =========================

        calculateAge(dto, dobString);

        return dto;
    }


    private void calculateAge(SaveRegPatientMasterDto dto, String dobString) {

        if (dobString == null || dobString.isBlank()) {
            dto.setAgeYears("0");
            dto.setAgeMonths("0");
            dto.setAgeDays("0");
            return;
        }

        try {
            LocalDate dob = LocalDate.parse(dobString);
            LocalDate today = LocalDate.now();

            if (dob.isAfter(today)) {
                dto.setAgeYears("0");
                dto.setAgeMonths("0");
                dto.setAgeDays("0");
                return;
            }

            Period period = Period.between(dob, today);

            dto.setAgeYears(String.valueOf(period.getYears()));
            dto.setAgeMonths(String.valueOf(period.getMonths()));
            dto.setAgeDays(String.valueOf(period.getDays()));

        } catch (Exception e) {
            dto.setAgeYears("0");
            dto.setAgeMonths("0");
            dto.setAgeDays("0");
        }
    }

    // ==========================================
    // SAFE STRING READER
    // ==========================================

    private String getStringValue(Row row, int index) {

        Cell cell = row.getCell(index);
        if (cell == null) return null;

        switch (cell.getCellType()) {

            case STRING:
                return cell.getStringCellValue().trim();

            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getLocalDateTimeCellValue()
                            .toLocalDate()
                            .toString(); // yyyy-MM-dd
                }

                double value = cell.getNumericCellValue();
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

    // ==========================================
    // SAFE INTEGER READER
    // ==========================================

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

                    // Remove decimal if exists (30.0 -> 30)
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


    private LocalDate getDateValue(Row row, int index) {

        Cell cell = row.getCell(index);
        if (cell == null) return LocalDate.now(); // fallback

        try {

            //Case 1: Proper Excel Date Cell
            if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
                return cell.getLocalDateTimeCellValue().toLocalDate();
            }

            //Case 2: String Date (e.g. "18/03/2026")
            if (cell.getCellType() == CellType.STRING) {
                String value = cell.getStringCellValue().trim();

                if (value.isEmpty()) return LocalDate.now();

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                return LocalDate.parse(value, formatter);
            }

        } catch (Exception e) {
            throw new RuntimeException("Invalid date at column " + index + " value: " + cell);
        }

        // fallback
        return LocalDate.now();
    }

}
//    private String getCellValue(Row row, int cellIndex) {
//        if (row.getCell(cellIndex) == null) {
//            return null;
//        }
//
//        switch (row.getCell(cellIndex).getCellType()) {
//            case STRING:
//                return row.getCell(cellIndex).getStringCellValue().trim();
//            case NUMERIC:
//                return String.valueOf((long) row.getCell(cellIndex).getNumericCellValue());
//            case BOOLEAN:
//                return String.valueOf(row.getCell(cellIndex).getBooleanCellValue());
//            default:
//                return row.getCell(cellIndex).toString().trim();
//        }
//    }
