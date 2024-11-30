package com.flashcard.fileutils;

import lombok.experimental.UtilityClass;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@UtilityClass
public class FileUtils {
    private static final Set<String> PNG_EXTENSIONS = Set.of("png", "PNG");
    private static final Set<String> JPEG_EXTENSIONS = Set.of("jpeg", "JPEG");
    private static final Set<String> JPG_EXTENSIONS = Set.of("jpg", "JPG");
    private static final Set<String> PDF_EXTENSIONS = Set.of("pdf", "PDF");
    private static final Set<String> DOC_EXTENSIONS = Set.of("doc", "DOC");
    private static final Set<String> DOCX_EXTENSIONS = Set.of("docx", "DOCX");
    private static final Set<String> XLS_EXTENSIONS = Set.of("xls", "XLS");
    private static final Set<String> XLSX_EXTENSIONS = Set.of("xlsx", "XLSX");
    private static final Set<String> IMAGE_EXTENSIONS = concat(
            PNG_EXTENSIONS,
            JPEG_EXTENSIONS,
            JPG_EXTENSIONS
    );
    private static final Set<String> DOCUMENT_EXTENSIONS = concat(
            PDF_EXTENSIONS,
            DOC_EXTENSIONS,
            DOCX_EXTENSIONS,
            XLS_EXTENSIONS,
            XLSX_EXTENSIONS
    );

    public String getExtension(MultipartFile file) {
        return FilenameUtils.getExtension(file.getOriginalFilename());
    }

    public boolean isExceptedExtension(Set<String> exceptedExtensions, String extension) {
        return exceptedExtensions.contains(extension);
    }

    public boolean isImageExtension(String extension) {
        return isExceptedExtension(IMAGE_EXTENSIONS, extension);
    }

    public boolean isDocumentExtension(String extension) {
        return isExceptedExtension(DOCUMENT_EXTENSIONS, extension);
    }

    public boolean isImage(MultipartFile file) {
        return isImageExtension(getExtension(file));
    }

    public boolean isPdf(MultipartFile file) {
        return isPdfExtension(getExtension(file));
    }

    public boolean isDocument(MultipartFile file) {
        return isDocumentExtension(getExtension(file));
    }

    public boolean isExcelExtensionXLS(MultipartFile file) {
        return isExceptedExtension(XLS_EXTENSIONS, getExtension(file));
    }

    public boolean isExcelExtensionXLSX(MultipartFile file) {
        return isExceptedExtension(XLSX_EXTENSIONS, getExtension(file));
    }

    public boolean isPdfExtension(String extension) {
        return isExceptedExtension(PDF_EXTENSIONS, extension);
    }

    public String generateFileName(MultipartFile multipartFile) {
        long key = GetRandomPass.getRandomNum();
        String randomString = GetRandomPass.getRandomString() + "_" + key;

        return randomString + "." + FilenameUtils.getExtension(multipartFile.getOriginalFilename());
    }

    @SafeVarargs
    private <T> Set<T> concat(Set<T>... sets) {
        return Stream.of(sets)
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
    }
}
