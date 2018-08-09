package de.kgrupp.inoksjavautils.transform;

import de.kgrupp.inoksjavautils.exception.UnCheckedException;
import lombok.extern.java.Log;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Log
public final class XmlUtils {

    private XmlUtils() {
        // utility class
    }

    public static <T> Optional<T> unmarshalFirst(String nodeName, String xmlString, Class<T> clazz) {
        return unmarshalFirst(nodeName, IOUtils.stringToInputStream(xmlString), clazz);
    }

    public static <T> Optional<T> unmarshalFirst(String nodeName, InputStream xmlInputStream, Class<T> clazz) {
        Document doc = parse(xmlInputStream);
        Node node = doc.getElementsByTagName(nodeName).item(0);
        if (node == null) {
            log.log(Level.INFO, () -> "The node '" + nodeName + "' was not found.");
            return Optional.empty();
        }
        return unmarshalFirst(node, clazz);
    }

    private static <T> Optional<T> unmarshalFirst(Node node, Class<T> clazz) {
        try {
            JAXBContext jc = JAXBContext.newInstance(clazz);
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            JAXBElement<T> jb = unmarshaller.unmarshal(node, clazz);
            return Optional.of(jb.getValue());
        } catch (JAXBException | IllegalArgumentException e) {
            throw new UnCheckedException(e);
        }
    }

    public static Document parse(InputStream xmlInputStream) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.parse(xmlInputStream);
        } catch (IOException | SAXException | ParserConfigurationException e) {
            throw new UnCheckedException(e);
        }
    }

    public static Document createEmptyDocument() {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            return docBuilder.newDocument();
        } catch (ParserConfigurationException e) {
            throw new UnCheckedException(e);
        }
    }

    public static <T> List<T> unmarshalEach(String nodeName, String xmlstring, Class<T> clazz) {
        return unmarshalEach(nodeName, IOUtils.stringToInputStream(xmlstring), clazz);
    }

    public static <T> List<T> unmarshalEach(String nodeName, InputStream xmlInputStream, Class<T> clazz) {
        Document doc = parse(xmlInputStream);
        NodeList nodeList = doc.getElementsByTagName(nodeName);
        return StreamSupport.stream(toIterable(nodeList).spliterator(), false)
                .map(node -> unmarshalFirst(node, clazz))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(
                        Collectors.toList());
    }

    public static Iterable<Node> toIterable(NodeList nodeList) {
        return () -> new Iterator<Node>() {
            private int i = 0;

            @Override
            public boolean hasNext() {
                return i < nodeList.getLength();
            }

            @Override
            public Node next() {
                if (!hasNext()) {
                    throw new NoSuchElementException("NodeList has no more elements");
                }
                Node nextItem = nodeList.item(i);
                i++;
                return nextItem;
            }
        };
    }
}
