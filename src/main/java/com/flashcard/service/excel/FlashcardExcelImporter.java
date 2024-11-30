package com.flashcard.service.excel;

import com.flashcard.exception.BusinessException;
import com.flashcard.fileutils.FileUtils;
import com.flashcard.model.enums.TYT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFPictureData;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class FlashcardExcelImporter {

    public void saveExcel(TYT tyt, MultipartFile file) throws IOException {

        List<ExcelCardDTO> dtoList = getExcelDataFomExcel(file);
    }

    private List<ExcelCardDTO> getExcelDataFomExcel(MultipartFile file) throws IOException {

        Workbook workbook;
        InputStream inputStream = file.getInputStream();
        if (FileUtils.isExcelExtensionXLSX(file)) {
            workbook = new XSSFWorkbook(inputStream);
        } else if (FileUtils.isExcelExtensionXLS(file)) {
            workbook = new HSSFWorkbook(inputStream);
        } else {
            throw new BusinessException("yanlış dosya formatı");
        }

        List<ExcelCardDTO> excelCardDTOS = new ArrayList<>();
        ExcelCardDTO cardDTO;
        int numberOfSheets = workbook.getNumberOfSheets();
        for (int i = 0; i < numberOfSheets; i++) {
            Sheet sheet = workbook.getSheetAt(i);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue;
                }
                cardDTO = new ExcelCardDTO();
                try {
                    for (Cell cell : row) {
                        if (cell.getRow().toString().isEmpty() || cell.getCellType() == CellType.BLANK /*|| getColumnName(cell.getColumnIndex()) == null*/) {
                            continue;
                        }
                        switch (cell.getColumnIndex()) {
                            case 0 -> // tc sütunu
                                    cardDTO.setSubject(getStringCell(cell, "konu"));
                            case 1 -> // isim sütunu
                                    cardDTO.setFlashcardName(getStringCell(cell, "flashcard"));
                            case 2 -> // soyisim sütunu
                                    cardDTO.setFrontFace(getStringCell(cell, "ön yüz"));
                            case 3 -> // E-posta sütunu
                                    cardDTO.setFrontFace(getStringCell(cell, "arka yüz"));
                            case 4 -> // gsm sütunu
                                    cardDTO.setFrontImage(null);
                            case 5 -> // doğum tarihi sütunu
                                    cardDTO.setBackImage(null);

                        }
                    }

                } catch (InvalidCellException e) {
                    log.debug("Hata oluştu. Satır: " + (row.getRowNum() + 1) + ", Hata: " + e.getMessage(), e);
                    throw new BusinessException(row.getRowNum() + 1 + " Satırda okunamadı " + e.getMessage());
                }
            }
        }
        return null;
    }

    private String getStringCell(Cell cell, String columnName) {
        if (cell.toString().isEmpty()) {
            return null;
        }
        String cellValue = null;
        try {
            cellValue = cell.getStringCellValue();
            return cellValue;
        } catch (Exception e) {
            throw new InvalidCellException(columnName, cellValue, e);
        }
    }
}


/*public byte[] getImageFromCell(Cell cell, String columnName, XSSFWorkbook workbook) throws IOException {
    // Eğer hücre boşsa veya içerik yoksa, null döndür.
    if (cell == null || cell.toString().isEmpty()) {
        return null;
    }

    try {
        // Excel dosyasındaki tüm resimleri al
        List<XSSFPictureData> pictures = workbook.getAllPictures();

        // Resimleri kontrol et ve hücreyle ilişkilendirilmiş resmi bul
        Optional<XSSFPictureData> matchingPicture = pictures.stream()
                .filter(pictureData -> pictureData.getPackagePart().equals(cell.toString()))
                .findFirst();

        // Eğer eşleşen resim varsa, byte[] olarak döndür
        return matchingPicture.map(XSSFPictureData::getData).orElse(null);

    } catch (Exception e) {
        // Hata durumunda özel exception fırlat
        throw new InvalidCellException(columnName, "Resim alınamadı", e);
    }
}
*/