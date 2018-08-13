package de.kgrupp.inoksjavautils.transform;

import de.kgrupp.monads.result.Result;
import de.kgrupp.monads.result.ResultUtils;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Log
public final class XmlUtils {

    private XmlUtils() {
        // utility class
    }

    public static <T> Result<T> unmarshalFirst(String nodeName, String xmlString, Class<T> clazz) {
        return IOUtils.stringToInputStream(xmlString).flatMap(inputStream -> unmarshalFirst(nodeName, inputStream, clazz));
    }

    public static <T> Result<T> unmarshalFirst(String nodeName, InputStream xmlInputStream, Class<T> clazz) {
        return parse(xmlInputStream).flatMap(doc -> {
            Node node = doc.getElementsByTagName(nodeName).item(0);
            if (node == null) {
                return Result.fail("The node '" + nodeName + "' was not found.");
            }
            return unmarshalFirst(node, clazz);
        });
    }

    private static <T> Result<T> unmarshalFirst(Node node, Class<T> clazz) {
        try {
            JAXBContext jc = JAXBContext.newInstance(clazz);
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            JAXBElement<T> jb = unmarshaller.unmarshal(node, clazz);
            return Result.of(jb.getValue());
        } catch (JAXBException | IllegalArgumentException e) {
            return Result.fail(e);
        }
    }

    public static Result<Document> parse(InputStream xmlInputStream) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            return Result.of(builder.parse(xmlInputStream));
        } catch (IOException | SAXException | ParserConfigurationException e) {
            return Result.fail(e);
        }
    }

    public static <T> Result<List<T>> unmarshalEach(String nodeName, String xmlstring, Class<T> clazz) {
        return IOUtils.stringToInputStream(xmlstring).flatMap(inputStream -> unmarshalEach(nodeName, inputStream, clazz));
    }

    public static <T> Result<List<T>> unmarshalEach(String nodeName, InputStream xmlInputStream, Class<T> clazz) {
        return parse(xmlInputStream).flatMap(doc -> {
            NodeList nodeList = doc.getElementsByTagName(nodeName);
            Stream<Result<T>> streamResult = StreamSupport.stream(toIterable(nodeList).spliterator(), false)
                    .map(node -> unmarshalFirst(node, clazz));
            return ResultUtils.flatCombine(streamResult).map(resultStream -> resultStream.collect(Collectors.toList()));
        });
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
