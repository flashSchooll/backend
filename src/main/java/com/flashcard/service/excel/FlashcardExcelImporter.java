package com.flashcard.service.excel;

import com.flashcard.constants.Constants;
import com.flashcard.exception.BusinessException;
import com.flashcard.model.*;
import com.flashcard.model.enums.CardFace;
import com.flashcard.repository.CardRepository;
import com.flashcard.repository.FlashCardRepository;
import com.flashcard.repository.LessonRepository;
import com.flashcard.repository.TopicRepository;
import com.flashcard.service.UserCardPercentageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.sl.usermodel.ObjectMetaData;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFPictureData;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class FlashcardExcelImporter {
    private static final Logger logger = LoggerFactory.getLogger(ObjectMetaData.Application.class);

    private final LessonRepository tytLessonRepository;
    private final TopicRepository tytTopicRepository;
    private final FlashCardRepository flashCardRepository;
    private final CardRepository tytCardRepository;
    private static List<XSSFPictureData> pictures;
    private final UserCardPercentageService userCardPercentageService;


    @Transactional
    public void saveExcel(Long lessonId, MultipartFile file) throws IOException {

        List<ExcelCardDTO> dtoList = getExcelDataFomExcel(file);

        Lesson lesson = tytLessonRepository.findById(lessonId)
                .orElseThrow(() -> new NoSuchElementException(Constants.TYT_LESSON_NOT_FOUND));

        Map<String, List<ExcelCardDTO>> groupedBySubject = dtoList.stream()
                .collect(Collectors.groupingBy(ExcelCardDTO::getSubject));

        for (Map.Entry<String, List<ExcelCardDTO>> entry : groupedBySubject.entrySet()) {// konulara göre grupladık
            String subject = entry.getKey();

            Optional<Topic> optionalTYTTopic = tytTopicRepository.findBySubject(subject);

            Topic topic;
            if (optionalTYTTopic.isPresent()) {
                topic = optionalTYTTopic.get();
            } else {
                topic = new Topic();
                topic.setLesson(lesson);
                topic.setSubject(subject);
            }


            topic = tytTopicRepository.save(topic);

            Map<String, List<ExcelCardDTO>> groupedByFlashcard = dtoList.stream()
                    .collect(Collectors.groupingBy(ExcelCardDTO::getFlashcardName));

            for (Map.Entry<String, List<ExcelCardDTO>> entryFlash : groupedByFlashcard.entrySet()) {// flashcarda göre grupladık
                String flashCardName = entryFlash.getKey();

                Flashcard flashcard = new Flashcard();
                flashcard.setCardName(flashCardName);
                flashcard.setTopic(topic);

                flashcard = flashCardRepository.save(flashcard);

                List<Card> cards = new ArrayList<>();
                Card card;
                ImageData imageDataFront;
                ImageData imageDataBack;

                for (ExcelCardDTO dto : entryFlash.getValue()) {
                    List<ImageData> imageData = new ArrayList<>();
                    if (dto.getFrontImage() != null) {
                        imageDataFront = new ImageData();
                        imageDataFront.setFace(CardFace.FRONT);
                        imageDataFront.setData(dto.getFrontImage());
                        imageData.add(imageDataFront);
                    }

                    if (dto.getBackImage() != null) {
                        imageDataBack = new ImageData();
                        imageDataBack.setFace(CardFace.BACK);
                        imageDataBack.setData(dto.getFrontImage());
                        imageData.add(imageDataBack);
                    }

                    card = new Card();
                    card.setFlashcard(flashcard);
                    card.setFrontFace(dto.getFrontFace());
                    card.setBackFace(dto.getBackFace());
                    card.setImageData(imageData.isEmpty() ? null : imageData);

                    cards.add(card);
                }

                tytCardRepository.saveAll(cards);

            }
        }
        userCardPercentageService.updateCardCount(lesson, dtoList.size());
    }

    private List<ExcelCardDTO> getExcelDataFomExcel(MultipartFile file) throws IOException {

        XSSFWorkbook workbook;
        InputStream inputStream = file.getInputStream();

        workbook = new XSSFWorkbook(inputStream);

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
                                    cardDTO.setBackFace(getStringCell(cell, "arka yüz"));
                            case 4 -> // gsm sütunu
                                    cardDTO.setFrontImage(getImageFromCell(cell, "ön resim", workbook));
                            case 5 -> // doğum tarihi sütunu
                                    cardDTO.setBackImage(getImageFromCell(cell, "ön resim", workbook));
                            default -> {

                            }
                        }
                    }
                    excelCardDTOS.add(cardDTO);

                } catch (InvalidCellException e) {
                    log.debug("Hata oluştu. Satır: " + (row.getRowNum() + 1) + ", Hata: " + e.getMessage(), e);
                    throw new BusinessException(row.getRowNum() + 1 + " Satırda okunamadı " + e.getMessage());
                }
            }
        }
        return excelCardDTOS;
    }

    private String getStringCell(Cell cell, String columnName) {
        if (cell.toString().isEmpty()) {
            return null;
        }
        String cellValue = null;
        StringBuilder htmlContent = new StringBuilder();
        try {
            cellValue = cell.getStringCellValue();
            XSSFRichTextString richText = (XSSFRichTextString) cell.getRichStringCellValue();

            // HTML başlangıcı
            htmlContent.append("<html><body>");

            // Cümleleri ayırt etmek için metni bölüyoruz
            String[] sentences = cellValue.split("(?<=\\.)");  // Noktadan sonra cümleyi ayır
            for (String sentence : sentences) {
                // Cümledeki her kelimeyi kontrol et
                String[] words = sentence.split(" ");
                for (String word : words) {
                    // Her kelimenin stilini kontrol et
                    int start = sentence.indexOf(word);
                    int end = start + word.length();

                    XSSFFont font = richText.getFontAtIndex(start);
                    boolean isBold = font != null && font.getBold();
                    boolean isItalic = font != null && font.getItalic();
                    boolean isUnderlined = font != null && (font.getUnderline() != XSSFFont.U_NONE);
                    Short fontColor = font != null ? font.getColor() : null;

                    // Biçim etiketini başlat
                    htmlContent.append("<span style=\"");

                    if (isBold) {
                        htmlContent.append("font-weight: bold; ");
                    }
                    if (isItalic) {
                        htmlContent.append("font-style: italic; ");
                    }
                    if (isUnderlined) {
                        htmlContent.append("text-decoration: underline; ");
                    }
                    if (fontColor != null && fontColor != XSSFFont.DEFAULT_FONT_COLOR) {
                        String hexColor = String.format("#%02x%02x%02x", (fontColor >> 16) & 0xFF, (fontColor >> 8) & 0xFF, fontColor & 0xFF);
                        htmlContent.append("color: " + hexColor + "; ");
                    }

                    htmlContent.append("\">");

                    // Kelimeyi yaz
                    htmlContent.append(word);

                    // Biçim etiketini kapat
                    htmlContent.append("</span> ");
                }
                // Yeni cümle başladı, buna göre de uygun boşlukları ekleyebilirsiniz.
                htmlContent.append(". ");
            }

            htmlContent.append("</body></html>");

            return htmlContent.toString();
        } catch (Exception e) {
            throw new InvalidCellException(columnName, cellValue, e);
        }
    }

    public static byte[] getImageFromCell(Cell cell, String columnName, XSSFWorkbook workbook) {
        // Eğer hücre boşsa veya içerik yoksa, null döndür.
        if (cell == null || cell.toString().isEmpty()) {
            return null;
        }

        try {
            // Excel dosyasındaki tüm resimleri al
            pictures = workbook.getAllPictures();

            byte[] picture = pictures.get(0).getData();

            pictures.remove(0);

            return picture;

        } catch (Exception e) {
            // Hata durumunda özel exception fırlat
            throw new InvalidCellException(columnName, "Resim alınamadı", e);
        }
    }
}
