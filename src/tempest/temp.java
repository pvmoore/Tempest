package tempest;

import org.w3c.dom.*;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.File;

public class temp {

    public temp() {
        try {
            log("temp");

            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = db.parse(new File("file.xml"));
            Element el = doc.getDocumentElement();
            log("document element = " + el);
            Node n = doc.getFirstChild();
            log("first child = " + n);
            DocumentType docType = doc.getDoctype();
            log("document type = " + docType);
            //
            XPathFactory xpathfact = XPathFactory.newInstance();
            XPath xpath = xpathfact.newXPath();
            InputSource src = new InputSource("file.xml");
            Node node = (Node)xpath.evaluate("theroot/para/sentence", src, XPathConstants.NODE);
            Node node2 = (Node)xpath.evaluate("theroot/para/sentence", doc, XPathConstants.NODE);
            log("node  = " + node);
            log("node2 = " + node2);
            Attr b = (Attr)xpath.evaluate("theroot/para/sentence/@pants-colour", doc, XPathConstants.NODE);
            log("attr = " + b);
            Attr b2 = (Attr)xpath.evaluate("theroot/para/sentence/@pants-colour2", doc, XPathConstants.NODE);
            log("attr = " + b2);
            //
            //
            //
            SAXParser sax = SAXParserFactory.newInstance().newSAXParser();
            //sax.parse(,);
        } catch(Throwable t) {
            log("error: " + t);
        }
    }

    public static void log(String s) { System.out.println(s); }

    public static void main(String[] args) {
        new temp();
    }
}
