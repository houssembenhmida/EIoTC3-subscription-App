package com.eniso.iotapp.Util;

import com.eniso.iotapp.Entity.Student;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class ExcelWriter {
    private String[] columns = {"Rang", "Nom", "Prénom", "Tél", "Etablissement"};
    private List<Student> students;
    private String fileName;
    private String sheetName;

    public ExcelWriter(List<Student> students, String fileName, String sheetName) {
        students.sort(Comparator.comparingInt(Student::getId));
        this.students = students;
        this.fileName = fileName;
        this.sheetName = sheetName;
    }

    public void writeToFile() {
        Workbook workbook = new XSSFWorkbook();
        CreationHelper creationHelper = workbook.getCreationHelper();
        Sheet sheet = workbook.createSheet(sheetName);

        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 12);
        headerFont.setColor(IndexedColors.RED.getIndex());
        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);

        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerCellStyle);
        }

        int rowNum = 1;
        for (Student student : students) {
            Row row = sheet.createRow(rowNum);
            row.createCell(0).setCellValue(rowNum++);
            row.createCell(1).setCellValue(student.getLastName());
            row.createCell(2).setCellValue(student.getFirstName());
            row.createCell(3).setCellValue(student.getPhoneNumber());
            row.createCell(4).setCellValue(student.getEstablishment());
        }

        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }
        try {
            FileOutputStream fileOut = new FileOutputStream("files/" + fileName + ".xlsx");
            workbook.write(fileOut);
            fileOut.close();
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void backupFiles() {
        File resultDir = new File("files");
        if (!resultDir.exists()) {
            resultDir.mkdir();
        }

        File backupDir = new File("backup");
        if (!backupDir.exists()) {
            backupDir.mkdir();
        }

        if (resultDir.listFiles() != null && resultDir.listFiles().length > 0) {
            if (backupDir.listFiles() != null) {
                String newBackup = String.valueOf(backupDir.listFiles().length + 1);
                backupDir = new File(backupDir.getName() + "/" + newBackup);
                backupDir.mkdir();
            }
            for (File file : resultDir.listFiles()) {
                if (file.getName().endsWith(".xlsx")) {
                    try {
                        Files.move(Paths.get(file.toURI()),
                                Paths.get(new File(backupDir.getAbsolutePath() + "/" + file.getName()).toURI()),
                                StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }
}
