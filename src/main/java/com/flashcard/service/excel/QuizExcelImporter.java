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
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class QuizExcelImporter {
    private static final Logger logger = LoggerFactory.getLogger(QuizExcelImporter.class);
    private final TopicRepository topicRepository;
    private final QuizRepository quizRepository;


    public void saveExcel(Long topicId, MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Dosya boş olamaz");
        }
        List<ExcelQuizDTO> dtoList = getExcelDataFromExcel(file);

        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new NoSuchElementException(Constants.TOPIC_NOT_FOUND));

        Set<Quiz> quizSet = new HashSet<>();
        Quiz quiz;

        for (ExcelQuizDTO e : dtoList) {
            quiz = new Quiz();

            quiz.setQuestion(e.getQuestion());
            quiz.setAnswer(e.getAnswer());
            quiz.setA(e.getA());
            quiz.setB(e.getB());
            quiz.setC(e.getC());
            quiz.setD(e.getD());
            quiz.setTopic(topic);
            quiz.setName(e.getName());
            quiz.setType(e.getType());

            quizSet.add(quiz);

        }
        quizRepository.saveAll(quizSet);

    }

    private List<ExcelQuizDTO> getExcelDataFromExcel(MultipartFile file) throws IOException {

        XSSFWorkbook workbook;
        InputStream inputStream = file.getInputStream();

        workbook = new XSSFWorkbook(inputStream);

        Set<ExcelQuizDTO> excelCardDTOS = new HashSet<>();
        ExcelQuizDTO cardDTO;
        int numberOfSheets = workbook.getNumberOfSheets();
        for (int i = 0; i < numberOfSheets; i++) {
            Sheet sheet = workbook.getSheetAt(i);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue;
                }
                cardDTO = new ExcelQuizDTO();
                try {
                    for (Cell cell : row) {
                        if (cell.getRow().toString().isEmpty() || cell.getCellType() == CellType.BLANK /*|| getColumnName(cell.getColumnIndex()) == null*/) {
                            continue;
                        }
                        switch (cell.getColumnIndex()) {
                            case 0 -> // soru sütunu
                                    cardDTO.setQuestion(getStringCell(cell, "soru"));
                            case 1 -> // a sütunu
                                    cardDTO.setA(getStringCell(cell, "a"));
                            case 2 -> // b sütunu
                                    cardDTO.setB(getStringCell(cell, "b"));
                            case 3 -> // c sütunu
                                    cardDTO.setC(getStringCell(cell, "c"));
                            case 4 -> // d sütunu
                                    cardDTO.setD(getStringCell(cell, "d"));
                            case 5 -> // şık sütunu
                                    cardDTO.setAnswer(setEnumCell(cell, "şık"));
                            case 6 -> // quiz adı sütunu
                                    cardDTO.setName(getStringCell(cell, "quiz adı"));
                            case 7 -> // quiz tipi sütunu
                                    cardDTO.setType(getQuizType(cell, "quiz tipi"));

                        }
                    }
                    if (cardDTO.getAnswer() != null) {
                        excelCardDTOS.add(cardDTO);
                    } else {
                        break;
                    }

                } catch (InvalidCellException e) {
                    log.debug("Hata oluştu. Satır: " + (row.getRowNum() + 1) + ", Hata: " + e.getMessage(), e);
                    throw new BusinessException(row.getRowNum() + 1 + " Satırda okunamadı " + e.getMessage());
                }
            }
        }
        return excelCardDTOS.stream().toList();
    }

    private QuizType getQuizType(Cell cell, String quizType) {
        if (cell.getCellType().equals(CellType.BLANK)) {
            throw new IllegalArgumentException("Soru tipi girilmelidir");
        }

        String cellValue = null;

        try {
            cellValue = cell.getStringCellValue();

        } catch (Exception e) {
            throw new InvalidCellException(quizType, String.valueOf(cell.getStringCellValue()), e);
        }
        return QuizType.by(cellValue);
    }

    private QuizOption setEnumCell(Cell cell, String alternative) {
        if (cell.getCellType().equals(CellType.BLANK)) {
            throw new BadRequestException("Cevap şıkkı boş olamaz");
        }

        String cellValue = null;

        try {
            cellValue = cell.getStringCellValue().trim();

            return QuizOption.byLabel(cellValue);
        } catch (Exception e) {
            throw new InvalidCellException(alternative, String.valueOf(cellValue), e);
        }

    }


    private String getStringCell(Cell cell, String columnName) {
        if (cell.toString().isEmpty()) {
            return null;
        }
        String cellValue = null;
        try {
            cellValue = cell.getStringCellValue();
            return cellValue.trim();
        } catch (Exception e) {
            throw new InvalidCellException(columnName, cellValue, e);
        }
    }
}
