package jeffaschenk.commons.frameworks.cnxidx.utility.xml;

import java.io.InputStream;

import jeffaschenk.commons.exceptions.XmlException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * This full static class is there to provide helper methods for dealing with xml.
 */
public class XmlUtilities {

    //---------------------------
    // Constructors
    //---------------------------

    /**
     * Don't construct one of these, just use the static methods
     */
    protected XmlUtilities() {
    }

    //---------------------------
    // Public Methods
    //---------------------------

    /**
     * Returns a org.w3c.dom.Document given an input stream.
     *
     * @param inStream an input stream for an xml string/file/etc
     * @return a org.w3c.dom.Document
     */
    public static Document getDocument(InputStream inStream)
            throws XmlException {

        Document doc = null;
        try {
            DocumentBuilder parser;
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            // Turn off the conversion validation and namespace-awareness
            // Namespace-awareness is turned off now so that we can process
            // XML that has namespacing, but no corresponding schema (such as
            // with Junipers 'junos' name
            factory.setNamespaceAware(true);
            factory.setValidating(false);
            factory.setIgnoringComments(false);
            parser = factory.newDocumentBuilder();
            doc = parser.parse(inStream);
        } catch (Exception e) {
            //CNXLogger.logStatic(CNXLogger.WARNING, CNAME, "getDOM", e);
            throw new XmlException("Could not convert stream to an xml document.", e);
        }
        return (doc);
    }

    /**
     * Get the element text node.  Locate the text node and return it.  For example;
     * <element>this is the text node</element>
     *
     * @param element The element to get the text node for
     * @return The text node
     */
    public static Node getElementTextNode(Element element) {

        Node textNode = null;

        // go through each child element
        Node node;
        short nodeType;
        NodeList children = element.getChildNodes();
        for (int ii = 0; ii < children.getLength(); ii++) {
            node = children.item(ii);
            nodeType = node.getNodeType();
            if (nodeType == Node.TEXT_NODE) {
                textNode = node;
                break;
            } else if (nodeType == Node.CDATA_SECTION_NODE) {
                textNode = node;
                break;
            }
        }
        return (textNode);
    }

    /**
     * Get the element text value.  Locate the text node and return
     * its contents.
     *
     * @param element The element to get the name for
     * @return The text value
     */
    public static String getElementTextValue(Element element) {
        String value = "";

        // get the text node
        Node node = getElementTextNode(element);
        if (node != null) {
            value = node.getNodeValue();
        }

        // if the entire string is whitespace ignore this
        // but don't trim the whitespace if it is there
        String testValue = value.trim();
        if (testValue.length() <= 0) {
            value = "";
        }
        return (value);
    }
}
