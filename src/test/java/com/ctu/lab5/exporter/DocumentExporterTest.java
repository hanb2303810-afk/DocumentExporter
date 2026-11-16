package com.ctu.lab5.exporter;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class DocumentExporterTest {

    @Test
    void testExportWithoutCompressionStrategy() {

        // Tao doi tuong DocumentExporterService
        DocumentExporterService exporter =
                new DocumentExporterService();

        // Kiem tra ngoai le khi chua thiet lap Strategy
        assertThrows(
                IllegalStateException.class,
                () -> exporter.exportDocument(
                        "BaoCao.pdf",
                        "PDF"
                )
        );
    }
}

