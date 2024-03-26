package com.mine.nosd.healthcheck.utils;

import com.mine.nosd.healthcheck.model.Excel;
import com.mine.nosd.healthcheck.model.Table;
import com.mine.nosd.healthcheck.repository.ExcelMongoRepository;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ExcelFileHandler {
    public static final String EXCEL_PATH = System.getProperty("user.dir");
    public static final String FILE_NAME = "excel.xlsx";

    public ExcelMongoRepository excelMongoRepository;

    public ExcelFileHandler(ExcelMongoRepository excelMongoRepository) {
        this.excelMongoRepository = excelMongoRepository;
    }

    public boolean isExcelExists() throws IOException {
        if (Path.of(EXCEL_PATH).resolve(FILE_NAME).toFile().exists()) {
            Files.deleteIfExists(Paths.get(Path.of(EXCEL_PATH).resolve(FILE_NAME).toString()));
        }
        retrieveExcelFromDb();
        return Path.of(EXCEL_PATH).resolve(FILE_NAME).toFile().exists();
    }

    public Table readExcelFile() {
        List<String> thead = new ArrayList<>();
        List<List<String>> tbody = new ArrayList<>();
        Table table = new Table();
        try (InputStream inputStream = new FileInputStream(Path.of(EXCEL_PATH).resolve(FILE_NAME).toFile());
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);
            int i = 1;
            for (Row currentRow : sheet) {

                Iterator<Cell> cellIterator = currentRow.iterator();

                List<String> rowData = new ArrayList<>();
                while (cellIterator.hasNext()) {
                    Cell currentCell = cellIterator.next();
                    if (i != 1) {
                        rowData.add(currentCell.toString());
                    } else {
                        thead.add(currentCell.toString());
                    }
                }
                if (i != 1) {
                    tbody.add(rowData);
                }
                i++;
            }
            table.setThead(thead);
            table.setTbody(tbody);
            table.setColCount(thead.size());
            table.setRowCount(tbody.size() + 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return table;
    }

    public void updateExcelFile(Table updatedTable) {
        Path path = Path.of(EXCEL_PATH);
        try (InputStream inputStream = new FileInputStream(path.resolve(FILE_NAME).toFile());
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);

            // Clear existing data
            int lastRowNum = sheet.getLastRowNum();
            for (int rowIndex = 1; rowIndex <= lastRowNum; rowIndex++) {
                sheet.removeRow(sheet.getRow(rowIndex));
            }

            // Write updated data
            List<String> thead = updatedTable.getThead();
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < thead.size(); i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(thead.get(i));
            }

            List<List<String>> tbody = updatedTable.getTbody();
            for (int i = 0; i < tbody.size(); i++) {
                Row dataRow = sheet.createRow(i + 1);
                List<String> rowData = tbody.get(i);
                for (int j = 0; j < rowData.size(); j++) {
                    Cell cell = dataRow.createCell(j);
                    cell.setCellValue(rowData.get(j));
                }
            }

            try (OutputStream outputStream = new FileOutputStream(path.resolve(FILE_NAME).toFile())) {
                workbook.write(outputStream);
            }
            saveExcelToDb();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void saveExcelFile(MultipartFile file) {
        try {
            Path uploadPath = Path.of(EXCEL_PATH);
            Path filePath = uploadPath.resolve(FILE_NAME);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            saveExcelToDb();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void retrieveExcelFromDb() {
        byte[] fileData = excelMongoRepository.findById("excel").get().getExcel();
        try (FileOutputStream fileOutputStream = new FileOutputStream(Path.of(EXCEL_PATH).resolve(FILE_NAME).toString())) {
            fileOutputStream.write(fileData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveExcelToDb() {
        File file = Path.of(EXCEL_PATH).resolve(FILE_NAME).toFile();
        byte[] fileData = new byte[(int) file.length()];
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            fileInputStream.read(fileData);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Excel excel;
        try {
            excel = excelMongoRepository.findById("excel").get();
            excel.setExcel(fileData);
            excelMongoRepository.save(excel);
        } catch (NoSuchElementException e) {
            excel = new Excel("excel", fileData);
        }
        excelMongoRepository.save(excel);
    }
}
