package cz.cas.lib.arclib.service;

import cz.cas.lib.arclib.exception.general.BadArgument;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;

import java.util.List;

@Slf4j
public class XmlBuilder {
    /**
     * Recursive method to create a node and, if necessary, its parents and siblings
     *
     * @param doc         document
     * @param targetXPath to single node
     * @param value       if null an empty node will be created
     * @return the created Node
     */

    public static Node addNode(Document doc, String targetXPath, String value) {
        log.info("adding Node: " + targetXPath + " -> " + value);

        String elementName = XPathUtils.getChildElementName(targetXPath);
        String parentXPath = XPathUtils.getParentXPath(targetXPath);

        //add value as text to the root element and return
        if (("/").equals(parentXPath)) {
            Element rootElement = doc.getRootElement();
            //the root on the xpath is different from the root of the document
            if (!rootElement.getName().equals(elementName)) throw new BadArgument(targetXPath);

            if (value != null) {
                rootElement.addText(value);
            }
            return rootElement;
        }

        Node parentNode = doc.selectSingleNode(parentXPath);
        if (parentNode == null) {
            parentNode = addNode(doc, parentXPath, null);
        }

        //add value as attribute to the parent node and return
        if (elementName.startsWith("@")) {
            return ((Element) parentNode).addAttribute(elementName.substring(1), value);
        }

        // create younger siblings if needed
        Integer childIndex = XPathUtils.getChildElementIndex(targetXPath);
        if (childIndex > 1) {
            List<?> nodelist = doc.selectNodes(XPathUtils.createPositionXpath(targetXPath, childIndex));
            // how many to create = (index wanted - existing - 1 to account for the new element we will create)
            int nodesToCreate = childIndex - nodelist.size() - 1;
            for (int i = 0; i < nodesToCreate; i++) {
                ((Element) parentNode).addElement(elementName);
            }
        }

        //add new element to the parent node
        Element created = ((Element) parentNode).addElement(elementName);
        if (null != value) {
            created.addText(value);
        }

        return created;
    }
}