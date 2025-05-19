package com.flashcard.service.excel;

import com.flashcard.constants.Constants;
import com.flashcard.exception.BadRequestException;
import com.flashcard.exception.BusinessException;
import com.flashcard.model.*;
import com.flashcard.model.enums.AWSDirectory;
import com.flashcard.model.enums.CardFace;
import com.flashcard.repository.CardRepository;
import com.flashcard.repository.FlashCardRepository;
import com.flashcard.repository.LessonRepository;
import com.flashcard.repository.TopicRepository;
import com.flashcard.service.S3StorageService;
import com.flashcard.service.UserCardPercentageService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.*;
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
    private static final Logger logger = LoggerFactory.getLogger(FlashcardExcelImporter.class);

    private final LessonRepository lessonRepository;
    private final TopicRepository topicRepository;
    private final FlashCardRepository flashCardRepository;
    private final CardRepository cardRepository;
    private static List<XSSFPictureData> pictures;
    private final UserCardPercentageService userCardPercentageService;
    private final S3StorageService s3StorageService;

    @Transactional
    public void saveExcel(Long lessonId, MultipartFile file) throws IOException {

        List<ExcelCardDTO> dtoList = getExcelDataFomExcel(file);

        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new NoSuchElementException(Constants.LESSON_NOT_FOUND));

        Map<String, List<ExcelCardDTO>> groupedBySubject = dtoList.stream()
                .collect(Collectors.groupingBy(ExcelCardDTO::getSubject));

        Map<String, Integer> topicIndexes = dtoList.stream()
                .collect(Collectors.toMap(
                        ExcelCardDTO::getSubject,
                        ExcelCardDTO::getTopicIndex,
                        (existing, replacement) -> existing // aynı subject varsa ilkini koru
                ));

        Map<String, Integer> flashcardIndexes = dtoList.stream()
                .collect(Collectors.toMap(
                        ExcelCardDTO::getSubject,
                        ExcelCardDTO::getFlashcardIndex,
                        (existing, replacement) -> existing // aynı subject varsa ilkini koru
                ));


        for (Map.Entry<String, List<ExcelCardDTO>> entry : groupedBySubject.entrySet()) {// konulara göre grupladık
            String subject = entry.getKey();

            Optional<Topic> optionalTYTTopic = topicRepository.findBySubjectAndLesson(subject, lesson);

            Topic topic;
            if (optionalTYTTopic.isPresent()) {
                topic = optionalTYTTopic.get();
            } else {
                topic = new Topic();
                topic.setLesson(lesson);
                topic.setSubject(subject);
                topic.setIndex(topicIndexes.get(subject));
                topic = topicRepository.save(topic);
            }

            Map<String, List<ExcelCardDTO>> groupedByFlashcard = groupedBySubject.get(topic.getSubject()).stream()
                    .collect(Collectors.groupingBy(ExcelCardDTO::getFlashcardName));

            for (Map.Entry<String, List<ExcelCardDTO>> entryFlash : groupedByFlashcard.entrySet()) {// flashcarda göre
                // grupladık
                String flashCardName = entryFlash.getKey();

                Optional<Flashcard> optionalFlashcard = flashCardRepository.findByCardNameAndTopic(flashCardName, topic);
                Flashcard flashcard;
                if (optionalFlashcard.isEmpty()) {
                    flashcard = new Flashcard();
                    flashcard.setCardName(flashCardName);
                    flashcard.setTopic(topic);
                    flashcard.setIndex(flashcardIndexes.get(flashCardName));

                    flashcard = flashCardRepository.save(flashcard);
                } else {
                    flashcard = optionalFlashcard.get();
                }

                Set<Card> cards = new HashSet<>();
                Card card;
                ImageData imageDataFront;
                ImageData imageDataBack;

                for (ExcelCardDTO dto : entryFlash.getValue()) {

                    card = new Card();
                    if (dto.getFrontImage() != null) {
                        imageDataFront = new ImageData();
                        imageDataFront.setFace(CardFace.FRONT);

                        String path = s3StorageService.uploadFile(dto.getFrontImage(), AWSDirectory.CARDS);
                        card.setFrontPhotoPath(path);
                    }

                    if (dto.getBackImage() != null) {
                        imageDataBack = new ImageData();
                        imageDataBack.setFace(CardFace.BACK);

                        String path = s3StorageService.uploadFile(dto.getBackImage(), AWSDirectory.CARDS);
                        card.setBackPhotoPath(path);
                    }

                    card.setFlashcard(flashcard);
                    card.setFrontFace(dto.getFrontFace());
                    card.setBackFace(dto.getBackFace());
                    card.setIndex(dto.getCardIndex());

                    cards.add(card);
                }

                if (cards.size() < 4) {
                    throw new BadRequestException("Bir flashcard taki kart sayısı 4 ten az olamaz");
                }

                cardRepository.saveAll(cards);

            }
        }
        userCardPercentageService.updateCardCount(lesson);
    }

    @Getter
    @Setter
    class ImageInfo {
        private int row;
        private int column;
        private byte[] imageData;
        private String imageFormat;
        private String cellReference;

        // Getters and setters
        // ... (implement as needed)
    }

    public HashMap<String, ImageInfo> readImagesWithDetails(Sheet sheet) {
        HashMap<String, ImageInfo> imageInfoMap = new HashMap<>();

        XSSFDrawing drawing = (XSSFDrawing) sheet.getDrawingPatriarch();

        if (drawing != null) {
            for (XSSFShape shape : drawing.getShapes()) {
                if (shape instanceof XSSFPicture) {
                    XSSFPicture picture = (XSSFPicture) shape;
                    XSSFClientAnchor anchor = (XSSFClientAnchor) picture.getAnchor();

                    ImageInfo info = new ImageInfo();
                    info.setRow(anchor.getRow1());
                    info.setColumn(anchor.getCol1());
                    info.setImageData(picture.getPictureData().getData());
                    info.setImageFormat(getImageFormat(picture.getPictureData()));
                    info.setCellReference(getCellReference(anchor.getRow1(), anchor.getCol1()));

                    imageInfoMap.put(getCellReference(anchor.getRow1(), anchor.getCol1()), info);
                }
            }
        }


        return imageInfoMap;
    }

    private String getCellReference(int row1, int col1) {
        return CellReference.convertNumToColString(col1) + (row1 + 1);
    }

    private String getImageFormat(XSSFPictureData pictureData) {
        String format = pictureData.suggestFileExtension();
        return format != null ? format : "unknown";
    }

    private List<ExcelCardDTO> getExcelDataFomExcel(MultipartFile file) throws IOException {

        XSSFWorkbook workbook;
        InputStream inputStream = file.getInputStream();

        workbook = new XSSFWorkbook(inputStream);


        Set<ExcelCardDTO> excelCardDTOS = new HashSet<>();
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
                            case 0 -> // konu
                                    cardDTO.setSubject(getStringCell(cell, "konu"));
                            case 1 -> // konu index
                                    cardDTO.setTopicIndex(getCellValueAsInteger(cell, "konuIndex"));
                            case 2 -> // flashcard
                                    cardDTO.setFlashcardName(getStringCell(cell, "flashcard"));
                            case 3 -> // flashcard
                                    cardDTO.setFlashcardIndex(getCellValueAsInteger(cell, "flashcardIndex"));
                            case 4 -> // flashcard
                                    cardDTO.setCardIndex(getCellValueAsInteger(cell, "cardIndex"));
                            case 5 -> // ön yüz sütunu
                                    cardDTO.setFrontFace(getStringCell(cell, "ön yüz"));
                            case 6 -> // arka yüz sütunu
                                    cardDTO.setBackFace(getStringCell(cell, "arka yüz"));
                            case 7 -> // ön resim sütunu
                                    cardDTO.setFrontImage(getImageFromCell(cell, cell.getRowIndex() + " ön resim", workbook));
                            case 8 -> // arka resim sütunu
                                    cardDTO.setBackImage(getImageFromCell(cell, cell.getRowIndex() + " arka resim", workbook));
                            default -> {
                                return Collections.emptyList();
                            }
                        }
                    }
                    if (cardDTO.getSubject() != null) {
                        excelCardDTOS.add(cardDTO);
                    } else {
                        break;
                    }

                } catch (InvalidCellException e) {
                    log.debug("Hata oluştu. Satır: " + (row.getRowNum() + 1) + ", Hata: " + e.getMessage(), e);
                    throw new BusinessException(row.getRowNum() + " Satırda okunamadı " + e.getMessage());
                }
            }
        }
        return excelCardDTOS.stream().toList();
    }

    public static MultipartFile getImageFromCell(Cell cell, String columnName, XSSFWorkbook workbook) {
        // Eğer hücre boşsa veya içerik yoksa, null döndür.
        if (cell == null || cell.toString().isEmpty()) {
            return null;
        }

        try {
            // Excel dosyasındaki tüm resimleri al
            pictures = workbook.getAllPictures();

            if (!pictures.isEmpty()) {
                byte[] picture = pictures.get(0).getData();
                //   CustomMultipartFile file = new CustomMultipartFile(picture,"filename","image/jpeg");

                pictures.remove(0);
                return new CustomMultipartFile(picture, " file.getOriginalFilename()", "image/jpeg");
            }
            return null;

            //  return picture;

        } catch (Exception e) {
            // Hata durumunda özel exception fırlat
            throw new InvalidCellException(columnName, "Resim alınamadı", e);
        }
    }

    private String getStringCell(Cell cell, String columnName) {
        if (cell == null || cell.toString().isEmpty()) {
            return null;
        }

        String cellValue = null;
        try {
            switch (cell.getCellType()) {
                case STRING:
                    cellValue = cell.getStringCellValue();
                    break;
                case NUMERIC:
                    // Numeric değeri String'e çevir
                    cellValue = String.valueOf(cell.getNumericCellValue());
                    // Eğer tam sayı istiyorsanız alternatif olarak:
                    // cellValue = String.format("%.0f", cell.getNumericCellValue());
                    break;
                case BOOLEAN:
                    cellValue = String.valueOf(cell.getBooleanCellValue());
                    break;
                case BLANK:
                    cellValue = "";
                    break;
                default:
                    throw new InvalidCellException(columnName, "Unsupported cell type", null);
            }
            return cellValue;
        } catch (Exception e) {
            throw new InvalidCellException(columnName, cell.toString(), e);
        }
    }

    public static Integer getCellValueAsInteger(Cell cell, String columnName) {
        try {
            if (cell == null) {
                return null;
            }

            switch (cell.getCellType()) {
                case STRING:
                    try {
                        return Integer.parseInt(cell.getStringCellValue().trim());
                    } catch (NumberFormatException e) {
                        System.err.println("String değeri Long'a çevrilemedi: " + cell.getStringCellValue());
                        return null;
                    }

                case NUMERIC:
                    return (int) cell.getNumericCellValue(); // ondalık sayı varsa kesilir

                case FORMULA:
                    // Formül sonucu sayı ise
                    if (cell.getCachedFormulaResultType() == CellType.NUMERIC) {
                        return (int) cell.getNumericCellValue();
                    } else if (cell.getCachedFormulaResultType() == CellType.STRING) {
                        try {
                            return Integer.parseInt(cell.getStringCellValue().trim());
                        } catch (NumberFormatException e) {
                            System.err.println("Formül sonucu Long'a çevrilemedi: " + cell.getStringCellValue());
                            return null;
                        }
                    }
                    break;

                default:
                    return null;
            }

            return null;
        } catch (Exception e) {
            throw new InvalidCellException(columnName, cell.toString(), e);
        }
    }
}