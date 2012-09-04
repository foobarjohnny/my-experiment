package com.telenav.cserver.dsr.util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.telenav.cserver.dsr.framework.ProcessConfiguration;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class XMLUtil {
	
	private static Logger logger = Logger.getLogger(XMLUtil.class.getName()) ;
	
    public static Node findNamedNode(Node parent, String name) {
        if (parent.getNodeType()
                == Node.ELEMENT_NODE) {
            Element parentElem = (Element) parent;

            NodeList children = parentElem.getChildNodes();

            for (int i = 0; i < children.getLength(); i++) {
                if (children.item(i).getNodeType()
                        == Node.ELEMENT_NODE) {
                    Element n = (Element) children.item(i);

                    if (n.getNodeName().equals(name)) {
                        return (Node) n;
                    }
                }
            }
        }


        return null;
    }

    public static Node getOnlyChild(Node parent, String tagName) {
        NodeList list = ((Element) parent).getElementsByTagName(tagName);
        return list.getLength() == 1 ? list.item(0) : null;
    }

    public static String getAttributeValue(Node node, String attriName) {
        Node attriNode = node.getAttributes().getNamedItem(attriName);
        return attriNode == null ? "" : attriNode.getNodeValue();
    }

    public static Document getDoc(String fileName) {
        InputStream is = ProcessConfiguration.class
                .getResourceAsStream(fileName);
        Document doc = null;
        try {
            DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder();
            doc = docBuilder.parse(is);
        }
        catch (Exception e) {
            logger.log(Level.SEVERE, "error", e);
        }
        return doc;
    }

}
