package com.chinapalms.kwobox.utils;

import java.io.StringReader;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.xml.sax.InputSource;

import com.chinapalms.kwobox.test.TestClass;

public class XmlXXEUtil {

    static Log log = LogFactory.getLog(TestClass.class);

    // XML解析防止实体注入
    public static Document getXXEDocument(String xmlStr) {

        Document doc = null;

        SAXReader reader = new SAXReader();

        try {
            // This is the PRIMARY defense. If DTDs (doctypes) are disallowed,
            // almost all XML entity attacks are prevented
            // Xerces 2 only -
            // http://xerces.apache.org/xerces2-j/features.html#disallow-doctype-decl
            reader.setFeature(
                    "http://apache.org/xml/features/disallow-doctype-decl",
                    true);
        } catch (Exception e) {
            // On Apache, this should be thrown when disallowing DOCTYPE
            log.error("A DOCTYPE was passed into the XML document" + e);
        }
        String encoding = getEncoding(xmlStr);
        InputSource source = new InputSource(new StringReader(xmlStr));
        source.setEncoding(encoding);
        try {
            doc = reader.read(source);
        } catch (Exception e) {
            log.error("try->catch exception:", e);
        }
        if (doc.getXMLEncoding() == null) {
            doc.setXMLEncoding(encoding);
        }
        return doc;
    }

    private static String getEncoding(String text) {
        String result = null;
        String xml = text.trim();
        if (xml.startsWith("<?xml")) {
            int end = xml.indexOf("?>");
            String sub = xml.substring(0, end);
            StringTokenizer tokens = new StringTokenizer(sub, " =\"'");
            while (tokens.hasMoreTokens()) {
                String token = tokens.nextToken();
                if ("encoding".equals(token)) {
                    if (!tokens.hasMoreTokens()) {
                        break;
                    }
                    result = tokens.nextToken();
                    break;
                }
            }
        }
        return result;
    }

}
