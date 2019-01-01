package de.kgrupp.inoksjavautils.io;

import de.kgrupp.monads.result.Result;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXParseException;

import javax.xml.transform.TransformerException;
import java.io.File;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class XmlReadWriteUtilsTest {

    @Test
    void read() {
        Result<Document> result = XmlReadWriteUtils.read(getFileInPackage("example.xml"));
        result.consumeOrThrow(this::assertExampleXml);
    }

    @Test
    void readInvalidXml() {
        Result<Document> result = XmlReadWriteUtils.read(getFileInPackage("invalid-xml.xml"));
        assertTrue(result.isInternalError());
        assertEquals(SAXParseException.class, result.getException().getClass());
    }

    @Test
    void readSafe() {
        Result<Document> result = XmlReadWriteUtils.readSafe(getFileInPackage("example.xml"));
        result.consumeOrThrow(this::assertExampleXml);
    }

    @Test
    void readSafeFileMissing() {
        Result<Document> result = XmlReadWriteUtils.readSafe(new File(getFileInPackage("").getAbsoluteFile() + "/file-missing.xml"));
        result.consumeOrThrow(document -> assertEquals(0, document.getChildNodes().getLength()));
    }

    @Test
    void createEmptyDocument() {
        Result<Document> result = XmlReadWriteUtils.createEmptyDocument();
        assertTrue(result.isSuccess());
        assertEquals(0, result.getObject().getChildNodes().getLength());
    }

    @Test
    void write() {
        XmlReadWriteUtils.read(getFileInPackage("example.xml")).consumeOrThrow(document -> {
            assertExampleXml(document);
            final File newFile = new File(getFileInPackage("").getAbsoluteFile() + "/new-file-" + Math.random() + ".xml");
            Result<Void> result = XmlReadWriteUtils.write(document, newFile);
            assertTrue(result.isSuccess());
            XmlReadWriteUtils.read(newFile).consumeOrThrow(this::assertExampleXml);
        });
    }

    @Test
    void writeFails() {
        XmlReadWriteUtils.read(getFileInPackage("example.xml")).consumeOrThrow(document -> {
            assertExampleXml(document);
            final File newFile = new File(getFileInPackage("").getAbsoluteFile() + "/new-folder-" + Math.random() + "/new-example.xml");
            Result<Void> result = XmlReadWriteUtils.write(document, newFile);
            assertTrue(result.isInternalError());
            assertEquals(TransformerException.class, result.getException().getClass());
        });
    }

    @Test
    void writeSafe() {
        XmlReadWriteUtils.read(getFileInPackage("example.xml")).consumeOrThrow(document -> {
            assertExampleXml(document);
            final File newFile = new File(getFileInPackage("").getAbsoluteFile() + "/new-folder-" + Math.random() + "/new-example.xml");
            Result<Void> result = XmlReadWriteUtils.writeSafe(document, newFile);
            assertTrue(result.isSuccess());
            XmlReadWriteUtils.read(newFile).consumeOrThrow(this::assertExampleXml);
        });
    }

    @NotNull
    private File getFileInPackage(String name) {
        URL packagePath = XmlReadWriteUtilsTest.class.getResource(name);
        return new File(packagePath.getFile());
    }

    private void assertExampleXml(Document document) {
        assertEquals(1, document.getChildNodes().getLength());
    }
}