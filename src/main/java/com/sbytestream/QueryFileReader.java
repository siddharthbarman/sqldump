package com.sbytestream;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import java.io.*;

public class QueryFileReader {
    public ArrayList<QueryWork> read(String filePath) throws IOException, SAXException, ParserConfigurationException, ApplicationException {
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
            String query = null;

            Node queryNode = node.getAttributes().getNamedItem(ATT_QUERY);
            if (queryNode != null) {
                query = queryNode.getNodeValue();
            }

            Node sqlfileNode = node.getAttributes().getNamedItem(ATT_SQLFILE);
            if (sqlfileNode != null) {
                if (query != null) {
                    logger.error("Found both sqlfile and query nodes in the same query node");
                    throw new ApplicationException("Found both sqlfile and query nodes in the same query node");
                }
                else {
                    String sqlfile = sqlfileNode.getNodeValue();
                    logger.info(String.format("Reading sql query from %s", sqlfile));
                    query = FileUtils.readTextFileContent(sqlfile);
                }
            }

            result.add(new QueryWork(query, file));
        }

        return result;
    }

    private final String ATT_FILE = "file";
    private final String ATT_QUERY = "query";
    private final String ATT_SQLFILE = "sqlfile";
    private final Logger logger = LoggerFactory.getLogger(QueryFileReader.class);
}
