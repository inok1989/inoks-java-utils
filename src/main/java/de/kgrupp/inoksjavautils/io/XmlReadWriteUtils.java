package de.kgrupp.inoksjavautils.io;

import de.kgrupp.monads.result.Result;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * @author Konstantin
 */
public final class XmlReadWriteUtils {

    private XmlReadWriteUtils() {
        // utility class
    }

    public static Result<Document> read(File file) {
        Objects.requireNonNull(file);
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            byte[] data = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
            InputStream stream = new ByteArrayInputStream(data);
            return Result.of(builder.parse(stream));
        } catch (ParserConfigurationException | SAXException | IOException e) {
            return Result.fail(String.format("Reading file '%s'", file.getName()), e);
        }
    }

    public static Result<Document> readSafe(File file) {
        Objects.requireNonNull(file);
        if (file.exists()) {
            return read(file);
        } else {
            return createEmptyDocument();
        }
    }

    public static Result<Document> createEmptyDocument() {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            return Result.of(docBuilder.newDocument());
        } catch (ParserConfigurationException e) {
            return Result.fail("Creating empty document failed.", e);
        }
    }

    public static Result<Void> write(Document document, File file) {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(file);
            transformer.transform(source, result);
            return Result.emptySuccess();
        } catch (TransformerException e) {
            return Result.fail("Writing document failed.", e);
        }
    }

    public static Result<Void> writeSafe(Document document, File file) {
        File parent = new File(file.getParent());
        parent.mkdirs();
        return write(document, file);
    }
}
