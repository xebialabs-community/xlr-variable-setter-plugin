/**
 * Copyright 2019 XEBIALABS
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package setvariables.parsers;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

import setvariables.DynamicVariable;
import setvariables.DynamicVariables;

public class XmlParser {

    public static DynamicVariables getVariablesList(String input)
            throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(input);
        doc.getDocumentElement().normalize();

        DynamicVariables dynamicVars = null;
        return dynamicVars;
    }

    static DynamicVariables getElementAttributes(Document doc)
    {
       NodeList nl = doc.getElementsByTagName("*");
     
       List<DynamicVariable> theList = new ArrayList<DynamicVariable>();

       int len = nl.getLength();
    
       for (int j=0; j < len; j++)
       {
          Element e = (Element) nl.item(j);
          System.out.println(e.getTagName() + ":");

          addChildren(theList, "", e);
       }

       DynamicVariables dynamicVars = new DynamicVariables();
       dynamicVars.setVariables(theList);
       return dynamicVars;
    }

    private static void addChildren(List<DynamicVariable> theList, String prefix, Element e) 
    {
        String nodename = e.getNodeValue();
        if ( e.hasChildNodes() ) 
        {
            String p = ( nodename.length() > 0 ? prefix + "_" + nodename : nodename );

            NodeList nList = e.getChildNodes();

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                System.out.println("\nCurrent Element :" + nNode.getNodeName());
                
                if (nNode.getNodeType() == Node.ELEMENT_NODE) 
                {
                    Element eElement = (Element) nNode;
                    addChildren(theList, p, eElement);
                }
            }
        }
        else
        {
            String nodeval  = e.getNodeValue();

            DynamicVariable dynVar = new DynamicVariable();
            dynVar.setKey(nodename);
            dynVar.setValue(nodeval);
            dynVar.setType("xlrelease.StringVariable");
            theList.add(dynVar);

            System.out.print(nodename + " : " + nodeval);
        }
    }
}