package com.flashcard.service.excel;

import com.flashcard.constants.Constants;
import com.flashcard.exception.BusinessException;
import com.flashcard.model.FillBlankQuiz;
import com.flashcard.model.Topic;
import com.flashcard.model.enums.QuizType;
import com.flashcard.repository.FillBlankQuizRepository;
import com.flashcard.repository.TopicRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class FillBlankQuizExcelImporter {

    private final TopicRepository topicRepository;
    private final FillBlankQuizRepository fillBlankQuizRepository;


    public void saveExcel(Long topicId, MultipartFile file) throws IOException {

        if (file.isEmpty()) {
            throw new IllegalArgumentException("Dosya boş olamaz");
        }

        List<FillBlankQuizDTO> dtoList = getExcelDataFromExcel(file);
        Map<String, List<FillBlankQuizDTO>> groupedByTitle = dtoList.stream()
                .collect(Collectors.groupingBy(FillBlankQuizDTO::getTitle));

        groupedByTitle.forEach((title, quizzes) -> {
            if (quizzes.size() == 1) {
                throw new IllegalArgumentException( title + "birden fazla soru içermeli");
            }
        });

        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new NoSuchElementException(Constants.TOPIC_NOT_FOUND));

        List<FillBlankQuiz> fillBlankQuizList = new ArrayList<>();
        FillBlankQuiz fillBlankQuiz;

        for (FillBlankQuizDTO fillBlankQuizDTO : dtoList) {
            fillBlankQuiz = new FillBlankQuiz();

            fillBlankQuiz.setTopic(topic);
            fillBlankQuiz.setTitle(fillBlankQuizDTO.getTitle());
            fillBlankQuiz.setQuestion(fillBlankQuizDTO.getQuestion());
            fillBlankQuiz.setAnswer(fillBlankQuizDTO.getAnswer());
            fillBlankQuiz.setQuizType(QuizType.FILL_BLANK);

            fillBlankQuizList.add(fillBlankQuiz);
        }

        fillBlankQuizRepository.saveAll(fillBlankQuizList);
    }

    private List<FillBlankQuizDTO> getExcelDataFromExcel(MultipartFile file) throws IOException {

        XSSFWorkbook workbook;
        InputStream inputStream = file.getInputStream();

        workbook = new XSSFWorkbook(inputStream);

        Set<FillBlankQuizDTO> quizDTOSet = new HashSet<>();
        FillBlankQuizDTO dto;

        int numberOfSheets = workbook.getNumberOfSheets();
        for (int i = 0; i < numberOfSheets; i++) {
            Sheet sheet = workbook.getSheetAt(i);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue;
                }
                dto = new FillBlankQuizDTO();
                try {
                    for (Cell cell : row) {
                        if (cell.getRow().toString().isEmpty() || cell.getCellType() == CellType.BLANK /*|| getColumnName(cell.getColumnIndex()) == null*/) {
                            continue;
                        }
                        switch (cell.getColumnIndex()) {
                            case 0 -> // soru sütunu
                                    dto.setTitle(getStringCell(cell, "başlık"));
                            case 1 -> // soru sütunu
                                    dto.setQuestion(getStringCell(cell, "soru"));
                            case 2 -> // cevap sütunu
                                    dto.setAnswer(getStringCell(cell, "cevap"));
                            default -> {
                                return Collections.emptyList();
                            }
                        }
                    }
                    if (dto.getAnswer() != null) {
                        quizDTOSet.add(dto);
                    } else {
                        break;
                    }

                } catch (InvalidCellException e) {
                    log.debug("Hata oluştu. Satır: " + (row.getRowNum() + 1) + ", Hata: " + e.getMessage(), e);
                    throw new BusinessException(row.getRowNum() + 1 + " Satırda okunamadı " + e.getMessage());
                }
            }
        }
        return quizDTOSet.stream().toList();
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
