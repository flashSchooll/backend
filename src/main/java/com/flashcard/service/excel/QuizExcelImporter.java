package com.flashcard.service.excel;

import com.flashcard.constants.Constants;
import com.flashcard.exception.BadRequestException;
import com.flashcard.exception.BusinessException;
import com.flashcard.model.Quiz;
import com.flashcard.model.Topic;
import com.flashcard.model.enums.QuizOption;
import com.flashcard.model.enums.QuizType;
import com.flashcard.repository.QuizRepository;
import com.flashcard.repository.TopicRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class QuizExcelImporter {

    private final TopicRepository topicRepository;
    private final QuizRepository quizRepository;

    // ─── Tekli Import (topicId parametre olarak gelir) ───────────────────────
    public void saveExcel(Long topicId, MultipartFile file) throws IOException {
        if (file.isEmpty()) throw new IllegalArgumentException("Dosya boş olamaz");

        List<ExcelQuizDTO> dtoList = getExcelDataFromExcel(file, false);

        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new NoSuchElementException(Constants.TOPIC_NOT_FOUND));

        List<Quiz> quizList = dtoList.stream()
                .map(dto -> mapToQuiz(dto, topic))
                .collect(Collectors.toList());

        quizRepository.saveAll(quizList);
        log.info("Tekli import tamamlandı. {} quiz kaydedildi.", quizList.size());
    }

    // ─── Toplu Import (topicId Excel'den okunur) ─────────────────────────────
    public void saveExcelBulk(MultipartFile file) throws IOException {
        if (file.isEmpty()) throw new IllegalArgumentException("Dosya boş olamaz");

        List<ExcelQuizDTO> dtoList = getExcelDataFromExcel(file, true);

        Map<Long, List<ExcelQuizDTO>> groupedByTopic = dtoList.stream()
                .filter(dto -> dto.getTopicId() != null)
                .collect(Collectors.groupingBy(ExcelQuizDTO::getTopicId));

        if (groupedByTopic.isEmpty()) {
            throw new IllegalArgumentException("Excel'de geçerli topicId bulunamadı");
        }

        // Tüm topic'leri tek sorguda çek
        Map<Long, Topic> topicMap = topicRepository.findAllById(groupedByTopic.keySet())
                .stream()
                .collect(Collectors.toMap(Topic::getId, Function.identity()));

        // Eksik topic kontrolü
        groupedByTopic.keySet().forEach(id -> {
            if (!topicMap.containsKey(id)) {
                throw new NoSuchElementException(Constants.TOPIC_NOT_FOUND + ": " + id);
            }
        });

        // ✦ YENİ: Her topic grubuna cevap dağılımını dengele
        groupedByTopic.values().forEach(this::redistributeAnswers);

        List<Quiz> quizList = groupedByTopic.entrySet().stream()
                .flatMap(entry -> entry.getValue().stream()
                        .map(dto -> mapToQuiz(dto, topicMap.get(entry.getKey()))))
                .collect(Collectors.toList());

        quizRepository.saveAll(quizList);
        log.info("Bulk import tamamlandı. {} quiz kaydedildi.", quizList.size());
    }

    // ─── Cevap Dağılımını Dengele ─────────────────────────────────────────────
    private void redistributeAnswers(List<ExcelQuizDTO> rows) {
        List<String> slots = new ArrayList<>(List.of("A", "B", "C", "D", "E"));
        int size = rows.size();
        Random rnd = new Random();

        // İlk 5 slot: A,B,C,D,E her biri en az 1 kez geçsin (shuffle ile garanti)
        Collections.shuffle(slots, rnd);
        List<String> targets = new ArrayList<>(slots);

        // Kalan (size - 5) soru için tamamen rastgele şık ata
        for (int i = 5; i < size; i++) {
            targets.add(slots.get(rnd.nextInt(slots.size())));
        }

        // Hedef sırayı karıştır (ilk 5'in pozisyonu da sabit kalmasın)
        Collections.shuffle(targets, rnd);

        // Her soruya hedef şıkkı uygula
        for (int i = 0; i < size; i++) {
            ExcelQuizDTO dto = rows.get(i);
            String targetSlot = targets.get(i);
            String currentSlot = dto.getAnswer().getLabel().trim().toUpperCase();

            if (!currentSlot.equals(targetSlot)) {
                swapOptions(dto, currentSlot, targetSlot);
                dto.setAnswer(QuizOption.byLabel(targetSlot));
            }
        }
    }

    private void swapOptions(ExcelQuizDTO dto, String from, String to) {
        String fromVal = getOption(dto, from);
        String toVal = getOption(dto, to);
        setOption(dto, from, toVal);
        setOption(dto, to, fromVal);
    }

    private String getOption(ExcelQuizDTO dto, String slot) {
        return switch (slot) {
            case "A" -> dto.getA();
            case "B" -> dto.getB();
            case "C" -> dto.getC();
            case "D" -> dto.getD();
            case "E" -> dto.getE();
            default  -> "";
        };
    }

    private void setOption(ExcelQuizDTO dto, String slot, String value) {
        switch (slot) {
            case "A" -> dto.setA(value);
            case "B" -> dto.setB(value);
            case "C" -> dto.setC(value);
            case "D" -> dto.setD(value);
            case "E" -> dto.setE(value);
        }
    }

    // ─── Excel Okuma (withTopicId=true ise kolon 9 = topicId) ────────────────
    private List<ExcelQuizDTO> getExcelDataFromExcel(MultipartFile file, boolean withTopicId) throws IOException {
        try (XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream())) {

            Set<ExcelQuizDTO> result = new HashSet<>();

            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                Sheet sheet = workbook.getSheetAt(i);

                for (Row row : sheet) {
                    if (row.getRowNum() == 0) continue; // başlık satırını atla

                    ExcelQuizDTO dto = new ExcelQuizDTO();
                    try {
                        for (Cell cell : row) {
                            if (cell.getCellType() == CellType.BLANK) continue;

                            switch (cell.getColumnIndex()) {
                                case 0 -> dto.setQuestion(getStringCell(cell, "Soru"));
                                case 1 -> dto.setA(getStringCell(cell, "A"));
                                case 2 -> dto.setB(getStringCell(cell, "B"));
                                case 3 -> dto.setC(getStringCell(cell, "C"));
                                case 4 -> dto.setD(getStringCell(cell, "D"));
                                case 5 -> dto.setE(getStringCell(cell, "E"));
                                case 6 -> dto.setAnswer(setEnumCell(cell, "Cevap"));
                                case 7 -> dto.setName(getStringCell(cell, "Quiz Adı"));
                                case 8 -> dto.setType(getQuizType(cell, "Quiz Tipi"));
                                // Kolon 9: withTopicId'ye göre farklı anlam
                                case 9 -> {
                                    if (withTopicId) {
                                        dto.setTopicId(getLongCell(cell, "topicId"));
                                    }
                                    // withTopicId=false ise bu kolonu yoksay
                                }
                            }
                        }

                        if (dto.getAnswer() != null) {
                            result.add(dto);
                        } else {
                            break; // cevap yoksa satır boş, dur
                        }

                    } catch (InvalidCellException e) {
                        log.debug("Satır {} okunamadı: {}", row.getRowNum() + 1, e.getMessage());
                        throw new BusinessException((row.getRowNum() + 1) + ". satırda hata: " + e.getMessage());
                    }
                }
            }
            return result.stream().toList();
        }
    }

    // ─── Quiz Mapping ─────────────────────────────────────────────────────────
    private Quiz mapToQuiz(ExcelQuizDTO dto, Topic topic) {
        Quiz quiz = new Quiz();
        quiz.setQuestion(dto.getQuestion());
        quiz.setAnswer(dto.getAnswer());
        quiz.setA(dto.getA());
        quiz.setB(dto.getB());
        quiz.setC(dto.getC());
        quiz.setD(dto.getD());
        quiz.setE(dto.getE());
        quiz.setTopic(topic);
        quiz.setName(dto.getName());
        quiz.setType(dto.getType());
        quiz.setDeleted(false);
        quiz.setDescription(dto.getDescription());
        quiz.setLevel(null);
        return quiz;
    }

    // ─── Yardımcı Metodlar ───────────────────────────────────────────────────
    private QuizType getQuizType(Cell cell, String columnName) {
        if (cell.getCellType() == CellType.BLANK) {
            throw new IllegalArgumentException("Soru tipi girilmelidir");
        }
        try {
            return QuizType.by(cell.getStringCellValue());
        } catch (Exception e) {
            throw new InvalidCellException(columnName, cell.getStringCellValue(), e);
        }
    }

    private QuizOption setEnumCell(Cell cell, String columnName) {
        if (cell.getCellType() == CellType.BLANK) {
            throw new BadRequestException("Cevap şıkkı boş olamaz");
        }
        String cellValue = null;
        try {
            cellValue = cell.getStringCellValue().trim();
            return QuizOption.byLabel(cellValue);
        } catch (Exception e) {
            throw new InvalidCellException(columnName, cellValue, e);
        }
    }

    private Integer getIntegerCell(Cell cell, String columnName) {
        if (cell == null || cell.getCellType() == CellType.BLANK) return null;
        try {
            if (cell.getCellType() == CellType.NUMERIC) {
                return (int) cell.getNumericCellValue();
            } else if (cell.getCellType() == CellType.STRING) {
                String val = cell.getStringCellValue().trim();
                try {
                    return Integer.parseInt(val);
                } catch (NumberFormatException e) {
                    return (int) Double.parseDouble(val);
                }
            }
            throw new InvalidCellException(columnName, "Beklenmeyen hücre tipi: " + cell.getCellType(), null);
        } catch (InvalidCellException e) {
            throw e;
        } catch (Exception e) {
            throw new InvalidCellException(columnName, "Sayısal değer okunamadı", e);
        }
    }

    private Long getLongCell(Cell cell, String columnName) {
        Integer val = getIntegerCell(cell, columnName);
        return val != null ? val.longValue() : null;
    }

    private String getStringCell(Cell cell, String columnName) {
        if (cell == null || cell.getCellType() == CellType.BLANK) return null;
        String cellValue = null;
        try {
            switch (cell.getCellType()) {
                case STRING -> cellValue = cell.getStringCellValue().trim();
                case NUMERIC -> {
                    if (DateUtil.isCellDateFormatted(cell)) {
                        cellValue = cell.getLocalDateTimeCellValue().toString();
                    } else {
                        double val = cell.getNumericCellValue();
                        cellValue = (val == (long) val)
                                ? String.format("%d", (long) val)
                                : String.valueOf(val);
                    }
                }
                default -> throw new InvalidCellException(columnName,
                        "Beklenmeyen hücre tipi: " + cell.getCellType(), null);
            }
            return (cellValue == null || cellValue.isEmpty()) ? null : cellValue;
        } catch (InvalidCellException e) {
            throw e;
        } catch (Exception e) {
            throw new InvalidCellException(columnName, cellValue, e);
        }
    }
}