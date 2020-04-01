/**
 * Package local.box for
 *
 * @author Maksim Tiunchik
 */
package box.local;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;

/**
 * Class XMLParser -
 *
 * @author Maksim Tiunchik (senebh@gmail.com)
 * @version 0.1
 * @since 31.03.2020
 */
public class XMLParser {

    private static final DBStore BASE = DBStore.getInstance();

    private static final Logger LOG = LogManager.getLogger(XMLParser.class.getName());

    private static String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
            + "<Storage>\n"
            + " <Box id=\"1\">\n"
            + "   <Item id=\"1\"/>\n"
            + "   <Item color=\"red\" id=\"2\"/>\n"
            + "   <Box id=\"3\">\n"
            + "       <Item id=\"3\" color=\"red\" />\n"
            + "       <Item id=\"4\" color=\"black\" />    \n"
            + "   </Box>\n"
            + "   <Box id=\"6\"/>\n"
            + "   <Item id=\"5\"/>\n"
            + " </Box>\n"
            + " <Item id=\"6\"/>\n"
            + "</Storage>";

    private LinkedList<Node> addNodes(NodeList nodes) {
        LinkedList<Node> temp = new LinkedList<>();
        int lenght = nodes.getLength();
        for (int index = 0; index < lenght; index++) {
            temp.push(nodes.item(index));
        }
        return temp;
    }

    private void addBox(Node node) {
        Box answer = new Box();
        if (node.hasAttributes()) {
            NamedNodeMap nodeMap = node.getAttributes();
            for (int i = 0; i < nodeMap.getLength(); i++) {
                Node attrNode = nodeMap.item(i);
                String name = attrNode.getNodeName();
                if (name.equalsIgnoreCase("id")) {
                    answer.setId(attrNode.getNodeValue());
                    answer.setParent(getParentID(node.getParentNode()));
                }
            }
        }
        BASE.addBox(answer);
    }

    private void addImage(Node node) {
        Item answer = new Item();
        if (node.hasAttributes()) {
            NamedNodeMap nodeMap = node.getAttributes();
            for (int i = 0; i < nodeMap.getLength(); i++) {
                Node attrNode = nodeMap.item(i);
                String name = attrNode.getNodeName();
                if (name.equalsIgnoreCase("id")) {
                    answer.setId(attrNode.getNodeValue());
                    answer.setParent(getParentID(node.getParentNode()));
                }
                if (name.equalsIgnoreCase("color")) {
                    answer.setColor(attrNode.getNodeValue());
                }
            }
        }
        BASE.addImage(answer);
    }

    private String getParentID(Node node) {
        String answer = "0";
        if (node != null && node.hasAttributes()) {
            NamedNodeMap nodeMap = node.getAttributes();
            for (int i = 0; i < nodeMap.getLength(); i++) {
                Node attrNode = nodeMap.item(i);
                if (attrNode.getNodeName().equalsIgnoreCase("id")) {
                    answer = attrNode.getNodeValue();
                }
            }
        }
        return answer;
    }

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
        XMLParser parse = new XMLParser();
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = null;
        if (args.length > 0) {
            Path path = Paths.get(args[0]);
            if (Files.exists(path)) {
                doc = dBuilder.parse(path.toFile());
            }
        } else {
            InputSource is = new InputSource(new StringReader(xml));
            doc = dBuilder.parse(is);
        }
        if (doc != null) {
            doc.getDocumentElement().normalize();
            LinkedList<Node> allnodes = parse.addNodes(doc.getChildNodes());
            while (!allnodes.isEmpty()) {
                Node temp = allnodes.pollFirst();
                if (temp.getNodeType() == Node.ELEMENT_NODE) {
                    NodeList tnList = temp.getChildNodes();
                    allnodes.addAll(parse.addNodes(tnList));
                    if (temp.getNodeName().equalsIgnoreCase("box")) {
                        parse.addBox(temp);
                    } else if (temp.getNodeName().equalsIgnoreCase("item")) {
                        parse.addImage(temp);
                    }
                }
            }
            System.out.println("Programm finished import to psqlDB");
        }
    }
}
