package com.ctu.lab5.exporter;

import java.util.ArrayList;
import java.util.List;

// ==========================================
// 1. FACTORY METHOD PATTERN
// ==========================================

interface DocumentReader {
    String readContent(String filePath);
}

class PDFReader implements DocumentReader {
    @Override
    public String readContent(String filePath) {
        return "Content from PDF file: " + filePath;
    }
}

class DocxReader implements DocumentReader {
    @Override
    public String readContent(String filePath) {
        return "Content from DOCX file: " + filePath;
    }
}

// Bo doc CSV moi
class CsvReader implements DocumentReader {
    @Override
    public String readContent(String filePath) {
        return "Content from CSV file: " + filePath;
    }
}

class DocumentReaderFactory {

    public static DocumentReader getReader(String fileType) {

        if (fileType == null) {
            throw new IllegalArgumentException("File type null");
        }

        return switch (fileType.toUpperCase()) {
            case "PDF" -> new PDFReader();
            case "DOCX" -> new DocxReader();
            case "CSV" -> new CsvReader();
            default -> throw new IllegalArgumentException(
                    "Unsupported file type: " + fileType
            );
        };
    }
}

// ==========================================
// 2. STRATEGY PATTERN
// ==========================================

interface CompressionStrategy {
    byte[] compress(String rawData);
}

class ZipCompressionStrategy implements CompressionStrategy {

    @Override
    public byte[] compress(String rawData) {
        System.out.println("Nen du lieu theo chuan ZIP...");
        return ("ZIP[" + rawData + "]").getBytes();
    }
}

class RarCompressionStrategy implements CompressionStrategy {

    @Override
    public byte[] compress(String rawData) {
        System.out.println("Nen du lieu theo chuan RAR...");
        return ("RAR[" + rawData + "]").getBytes();
    }
}

// Thuat toan nen TAR moi
class TarCompressionStrategy implements CompressionStrategy {

    @Override
    public byte[] compress(String rawData) {
        System.out.println("Nen du lieu theo chuan TAR...");
        return ("TAR[" + rawData + "]").getBytes();
    }
}

// ==========================================
// 3. OBSERVER PATTERN
// ==========================================

interface ExportObserver {
    void onExportSuccess(String fileName, int compressedSize);
}

class AuditLogObserver implements ExportObserver {

    @Override
    public void onExportSuccess(String fileName, int compressedSize) {
        System.out.println(
                "[Audit Log] Da ghi nhat ky xuat file: "
                        + fileName
                        + " (Kich thuoc: "
                        + compressedSize
                        + " bytes)"
        );
    }
}

class ConsoleNotificationObserver implements ExportObserver {

    @Override
    public void onExportSuccess(String fileName, int compressedSize) {
        System.out.println(
                "[Thong bao] Xuat bao cao thanh cong file "
                        + fileName
        );
    }
}

// ==========================================
// 4. DOCUMENT EXPORTER SERVICE
// ==========================================

public class DocumentExporterService {

    private CompressionStrategy compressionStrategy;

    private final List<ExportObserver> observers =
            new ArrayList<>();

    public void setCompressionStrategy(CompressionStrategy strategy) {
        this.compressionStrategy = strategy;
    }

    public void attachObserver(ExportObserver observer) {
        observers.add(observer);
    }

    public byte[] exportDocument(String filePath, String fileType) {

        if (compressionStrategy == null) {
            throw new IllegalStateException(
                    "Chua thiet lap thuat toan nen!"
            );
        }

        // Factory: Doc noi dung tai lieu
        DocumentReader reader =
                DocumentReaderFactory.getReader(fileType);

        String rawContent = reader.readContent(filePath);

        // Strategy: Nen du lieu
        byte[] compressedData =
                compressionStrategy.compress(rawContent);

        // Observer: Gui thong bao
        for (ExportObserver obs : observers) {
            obs.onExportSuccess(filePath, compressedData.length);
        }

        return compressedData;
    }

    // ==========================================
    // 5. MAIN - KIEM THU CHUONG TRINH
    // ==========================================

    public static void main(String[] args) {

        DocumentExporterService exporter =
                new DocumentExporterService();

        // Dang ky Observer
        exporter.attachObserver(new AuditLogObserver());
        exporter.attachObserver(new ConsoleNotificationObserver());

        // Su dung ZIP
        exporter.setCompressionStrategy(
                new ZipCompressionStrategy()
        );

        System.out.println("Dang xuat file PDF...");
        exporter.exportDocument(
                "BaoCaoTaiChinh2026.pdf",
                "PDF"
        );

        System.out.println("\nDang xuat file CSV voi TAR...");

        // Thay doi Strategy sang TAR
        exporter.setCompressionStrategy(
                new TarCompressionStrategy()
        );

        exporter.exportDocument(
                "SinhVien.csv",
                "CSV"
        );

        System.out.println("\nHoan thanh chuong trinh!");
    }
}

