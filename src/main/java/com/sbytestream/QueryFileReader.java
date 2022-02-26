package com.sbytestream;

import java.util.ArrayList;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import java.io.*;

public class QueryFileReader {
    public ArrayList<QueryWork> read(String filePath) throws IOException, SAXException, ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new File(filePath));

        ArrayList<QueryWork> result = new ArrayList<QueryWork>();
        NodeList nodes = doc.getElementsByTagName("query");

        for (int n=0; n < nodes.getLength(); n++) {
            Node node = nodes.item(n);
            if (node instanceof Element == false)
                continue;

            String file = node.getAttributes().getNamedItem(ATT_FILE).getNodeValue();
            String query = node.getAttributes().getNamedItem(ATT_QUERY).getNodeValue();
            result.add(new QueryWork(query, file));
        }

        return result;
    }

    private final String ATT_FILE = "file";
    private final String ATT_QUERY = "query";
}
