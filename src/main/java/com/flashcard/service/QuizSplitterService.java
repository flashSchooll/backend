package com.flashcard.service;

import com.flashcard.controller.quiz.dto.QuizGroupDTO;
import com.flashcard.controller.quiz.dto.QuizRowDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class QuizSplitterService {

    // Quiz_adi sütunundaki sonundaki "_01", "_02" gibi sayısal suffix'i tespit eder
    // ve serinin prefix'ini çıkartır.
    // Örnek: "Fizik_Vektor_01" -> prefix = "Fizik_Vektor"
    // Hem "_01" hem de " - 1" formatlarını yakalar
    private static final Pattern SERIES_PATTERN =
            Pattern.compile("^(.+?)(?:_\\d+|\\s*-\\s*\\d+)$");

    /**
     * Yüklenen Excel dosyasını okur, Quiz_adi'na göre gruplar ve
     * her grubu ayrı bir Excel'e yazar, sonunda ZIP olarak döner.
     */
    public byte[] splitAndZip(MultipartFile file) throws IOException {
        List<QuizRowDTO> allRows = readExcel(file);
        List<QuizGroupDTO> groups = groupBySeries(allRows);
        return buildZip(groups);
    }

    // ------------------------------------------------------------------ //
    //  1. Excel'i oku
    // ------------------------------------------------------------------ //
    private List<QuizRowDTO> readExcel(MultipartFile file) throws IOException {
        List<QuizRowDTO> rows = new ArrayList<>();

        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            // Başlık satırını atla (satır 0)
            boolean firstRow = true;
            for (Row row : sheet) {
                if (firstRow) {
                    firstRow = false;
                    continue;
                }
                if (isRowEmpty(row)) continue;


                QuizRowDTO dto = new QuizRowDTO(
                        getCellValue(row, 0), // Soru
                        getCellValue(row, 1), // A
                        getCellValue(row, 2), // B
                        getCellValue(row, 3), // C
                        getCellValue(row, 4), // D
                        getCellValue(row, 5), // E
                        getCellValue(row, 6), // Cevap
                        getCellValue(row, 7), // Quiz_adi
                        getCellValue(row, 8)  // Quiz_Tipi
                );
                rows.add(dto);

            }
        }
        return rows;
    }

    // ------------------------------------------------------------------ //
    //  2. Satırları seriye göre grupla
    // ------------------------------------------------------------------ //
    private List<QuizGroupDTO> groupBySeries(List<QuizRowDTO> allRows) {
        Map<String, List<QuizRowDTO>> map = new LinkedHashMap<>();

        for (QuizRowDTO row : allRows) {
            String prefix = extractPrefix(row.getQuizAdi());
            map.computeIfAbsent(prefix, k -> new ArrayList<>()).add(row);
        }

        return map.entrySet().stream()
                .map(e -> {
                    redistributeAnswers(e.getValue()); // ← yeni satır
                    return new QuizGroupDTO(e.getKey(), e.getValue());
                })
                .collect(Collectors.toList());
    }

    /**
     * "Fizik_Vektor_01" -> "Fizik_Vektor"
     * Eğer eşleşme yoksa quiz adının kendisini döner (bozuk/tek kayıt).
     */
    /**
     * "Fizik_Vektor_01"      -> "Fizik_Vektor"
     * "Ahlak Felsefesi - 1"  -> "Ahlak Felsefesi"
     * "Ahlak Felsefesi - 12" -> "Ahlak Felsefesi"
     */
    private String extractPrefix(String quizAdi) {
        if (quizAdi == null || quizAdi.isBlank()) return "Bilinmeyen";
        Matcher m = SERIES_PATTERN.matcher(quizAdi.trim());
        return m.matches() ? m.group(1).trim() : quizAdi.trim();
    }

    // ------------------------------------------------------------------ //
    //  3. ZIP oluştur
    // ------------------------------------------------------------------ //
    private byte[] buildZip(List<QuizGroupDTO> groups) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try (ZipOutputStream zos = new ZipOutputStream(baos)) {
            for (QuizGroupDTO group : groups) {
                byte[] excelBytes = writeGroupToExcel(group);
                // Her grup için dosya adı: seriesPrefix.xlsx
                String fileName = sanitizeFileName(group.getSeriesPrefix()) + ".xlsx";
                ZipEntry entry = new ZipEntry(fileName);
                zos.putNextEntry(entry);
                zos.write(excelBytes);
                zos.closeEntry();
            }
        }

        return baos.toByteArray();
    }

    // ------------------------------------------------------------------ //
    //  4. Tek bir grubu Excel byte[]'a yaz
    // ------------------------------------------------------------------ //
    private byte[] writeGroupToExcel(QuizGroupDTO group) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Quiz");

            // Başlık stili
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setBorderBottom(BorderStyle.THIN);

            // Başlık satırı
            String[] headers = {"Soru", "A", "B", "C", "D", "E", "Cevap", "Quiz_adi", "Quiz_Tipi"};
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Veri satırları
            int rowNum = 1;
            for (QuizRowDTO dto : group.getRows()) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(nullSafe(dto.getSoru()));
                row.createCell(1).setCellValue(nullSafe(dto.getA()));
                row.createCell(2).setCellValue(nullSafe(dto.getB()));
                row.createCell(3).setCellValue(nullSafe(dto.getC()));
                row.createCell(4).setCellValue(nullSafe(dto.getD()));
                row.createCell(5).setCellValue(nullSafe(dto.getE()));
                row.createCell(6).setCellValue(nullSafe(dto.getCevap()));
                row.createCell(7).setCellValue(nullSafe(dto.getQuizAdi()));
                row.createCell(8).setCellValue(nullSafe(dto.getQuizTipi()));
            }

            // Sütun genişliklerini otomatik ayarla
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
                // Soru sütunu için maksimum genişlik sınırı (256 birim = 1 karakter)
                if (i == 0 && sheet.getColumnWidth(i) > 256 * 80) {
                    sheet.setColumnWidth(i, 256 * 80);
                }
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return out.toByteArray();
        }
    }

    // ------------------------------------------------------------------ //
    //  Yardımcı metodlar
    // ------------------------------------------------------------------ //
    private String getCellValue(Row row, int colIndex) {
        Cell cell = row.getCell(colIndex, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
        if (cell == null) return "";

        try {
            switch (cell.getCellType()) {
                case STRING:
                    return cell.getStringCellValue().trim();
                case NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        return cell.getLocalDateTimeCellValue().toString();
                    }
                    double val = cell.getNumericCellValue();
                    // Tam sayıyı "1.0" yerine "1" olarak döndür
                    if (val == Math.floor(val) && !Double.isInfinite(val)) {
                        return String.valueOf((long) val);
                    }
                    return String.valueOf(val);
                case BOOLEAN:
                    return String.valueOf(cell.getBooleanCellValue());
                case FORMULA:
                    try {
                        return cell.getStringCellValue().trim();
                    } catch (IllegalStateException e) {
                        return String.valueOf(cell.getNumericCellValue());
                    }
                case BLANK:
                default:
                    return "";
            }
        } catch (Exception e) {
            throw new RuntimeException(String.valueOf(row.getRowNum()));
        }
    }

    private boolean isRowEmpty(Row row) {
        if (row == null) return true;
        for (Cell cell : row) {
            if (cell != null && cell.getCellType() != CellType.BLANK
                    && !cell.toString().isBlank()) {
                return false;
            }
        }
        return true;
    }

    private String nullSafe(String s) {
        return s == null ? "" : s;
    }

    /**
     * Dosya adında kullanılamayan karakterleri temizler
     */
    private String sanitizeFileName(String name) {
        return name.replaceAll("[\\\\/:*?\"<>|]", "_");
    }

    // ------------------------------------------------------------------ //
//  2b. Grup içi cevap dağılımını dengele (shuffle ile rastgele dağılım)
// ------------------------------------------------------------------ //
    private void redistributeAnswers(List<QuizRowDTO> rows) {
        List<String> slots = new ArrayList<>(List.of("A", "B", "C", "D", "E"));
        int size = rows.size();
        Random rnd = new Random();

        // 1. Adım: A,B,C,D,E'nin her biri EN AZ 1 kez geçsin (shuffle ile garanti)
        Collections.shuffle(slots, rnd);
        List<String> targets = new ArrayList<>(slots); // ilk 5 slot garantili

        // 2. Adım: Kalan (size - 5) soru için tamamen rastgele şık ata
        for (int i = 5; i < size; i++) {
            targets.add(slots.get(rnd.nextInt(slots.size())));
        }

        // 3. Adım: Hedef sırayı karıştır (ilk 5'in pozisyonu da sabit kalmasın)
        Collections.shuffle(targets, rnd);

        // 4. Adım: Her soruya hedef şıkkı uygula
        for (int i = 0; i < size; i++) {
            QuizRowDTO row = rows.get(i);
            String targetSlot = targets.get(i);
            String currentSlot = row.getCevap().trim().toUpperCase();

            if (!currentSlot.equals(targetSlot)) {
                swapOptions(row, currentSlot, targetSlot);
                row.setCevap(targetSlot);
            }
        }
    }

    /**
     * İki şıkkın içeriğini birbiriyle takas eder.
     */
    private void swapOptions(QuizRowDTO row, String from, String to) {
        String fromVal = getOption(row, from);
        String toVal = getOption(row, to);
        setOption(row, from, toVal);
        setOption(row, to, fromVal);
    }

    private String getOption(QuizRowDTO row, String slot) {
        return switch (slot) {
            case "A" -> row.getA();
            case "B" -> row.getB();
            case "C" -> row.getC();
            case "D" -> row.getD();
            case "E" -> row.getE();
            default -> "";
        };
    }

    private void setOption(QuizRowDTO row, String slot, String value) {
        switch (slot) {
            case "A" -> row.setA(value);
            case "B" -> row.setB(value);
            case "C" -> row.setC(value);
            case "D" -> row.setD(value);
            case "E" -> row.setE(value);
        }
    }
}
