package ru.kudryashov.moexservice.parser;

import ru.kudryashov.moexservice.dto.BondDto;
import ru.kudryashov.moexservice.exception.BondParsingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class MoexXmlParser implements Parser {

    @Override
    public List<BondDto> parse(String ratesAsString) {
        try {
            Document document = parseXmlString(ratesAsString);
            NodeList rows = extractRows(document);
            return parseBonds(rows);
        } catch (Exception ex) {
            log.error("XML parsing error, xml: {}", ratesAsString, ex);
            throw new BondParsingException(ex);
        }
    }

    /**
     * Parses the XML string into a Document object.
     */
    private Document parseXmlString(String xmlString) throws Exception {
        DocumentBuilderFactory dbf = configureDocumentBuilderFactory();
        DocumentBuilder documentBuilder = dbf.newDocumentBuilder();

        try (var reader = new StringReader(xmlString)) {
            return documentBuilder.parse(new InputSource(reader));
        }
    }

    /**
     * Configures and returns a secure instance of DocumentBuilderFactory.
     */
    private DocumentBuilderFactory configureDocumentBuilderFactory() throws ParserConfigurationException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        dbf.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
        dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        return dbf;
    }

    /**
     * Extracts rows (NodeList) from the Document object.
     */
    private NodeList extractRows(Document document) {
        document.getDocumentElement().normalize();
        return document.getElementsByTagName("row");
    }

    /**
     * Parses bonds from a NodeList of "row" elements.
     */
    private List<BondDto> parseBonds(NodeList rows) {
        List<BondDto> bonds = new ArrayList<>();

        for (int i = 0; i < rows.getLength(); i++) {
            Node node = rows.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                BondDto bond = parseBond((Element) node);
                if (bond != null) {
                    bonds.add(bond);
                }
            }
        }
        return bonds;
    }

    /**
     * Parses a single bond from an Element.
     */
    private BondDto parseBond(Element element) {
        String ticker = element.getAttribute("SECID");
        String price = element.getAttribute("PREVPRICE");
        String name = element.getAttribute("SHORTNAME");

        if (!ticker.isEmpty() && !price.isEmpty() && !name.isEmpty()) {
            try {
                double priceValue = Double.parseDouble(price);
                return new BondDto(ticker, name, priceValue);
            } catch (NumberFormatException ex) {
                log.warn("Failed to parse bond price: {}", price, ex);
            }
        }
        return null;
    }
}

