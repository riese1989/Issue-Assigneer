package ru.pestov.alexey.plugins.spring.service;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import ru.pestov.alexey.plugins.spring.model.User;

import javax.inject.Named;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;
import java.util.List;

@Named
public class XMLService {
    public Document createXML(List<User> users)  {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("activeusers");
            doc.appendChild(rootElement);
            for (User user : users) {
                Element userElem = doc.createElement("user");
                Element name = doc.createElement("name");
                name.setTextContent(user.getName());
                Element id = doc.createElement("id");
                id.setTextContent(String.valueOf(user.getID()));
                userElem.appendChild(name);
                userElem.appendChild(id);
                rootElement.appendChild(userElem);
            }
            return doc;
        }
        catch (ParserConfigurationException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
