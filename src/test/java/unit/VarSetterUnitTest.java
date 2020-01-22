/**
 * Copyright 2020 XEBIALABS
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */


package unit;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import setvariables.DynamicVariable;
import setvariables.DynamicVariables;
import setvariables.parsers.XmlParser;
import setvariables.parsers.JsonParser;

public class VarSetterUnitTest {


    // Tests

    final static String testXML = "<project>"
    +"<modelVersion>4.0.0</modelVersion><groupId>com.xebialabs</groupId>"
    +"<artifactId>OrderProcessing</artifactId>"
    +"<packaging>jar</packaging>"  
    +"<version>1.0.0</version>"
    +"<name>OrderProcessing</name>"
    +"<url>http://maven.apache.org</url>"
    +"<dependencies>"
    +"<dependency>" 
    +"<groupId>junit</groupId>"  
    +"<artifactId>junit</artifactId>"  
    +"<version>3.8.1</version>"
    +"<scope>test</scope>"  
    +"</dependency>"  
    +"</dependencies>"  
    +"</project>";

    final static String testJSON = "{\"size\":2,\"limit\":25,\"isLastPage\":true,\"values\":"
    +"[{\"id\":\"01f9c8680e9db9888463b61e423b7b1d18a5c2c1\",\"displayId\":\"01f9c86\",\"author\":"
    +"{\"name\":\"Seb Ruiz\",\"emailAddress\":\"sruiz@atlassian.com\"}"
    +",\"authorTimestamp\":1334730200000,\"message\":\"NONE: Add groovy as java synhi\\n+review @aahmed\",\"parents\":"
    +"[{\"id\":\"06a499d51107533a4f24a3620280edbb342d89b7\",\"displayId\":\"06a499d\"}],\"attributes\":{}},"
    +"{\"id\":\"c9d6630b88143dab6a922c5cffe931dae68a612a\",\"displayId\":\"c9d6630\",\"author\":"
    +"{\"name\":\"Pierre-Etienne Poirot\",\"emailAddress\":\"pepoirot@atlassian.com\"}}]"
    +",\"start\":0,\"filter\":null,\"nextPageStart\":2}";

    @Test
    public void testXMLParser() throws Exception {
        DynamicVariables dv = XmlParser.getVariablesList(testXML);

        System.out.println("The DynamicVariable list size = "+dv.getVariables().size());
        System.out.println(dv.toString());

        assertTrue(dv.getVariables().size() == 11);
        System.out.println("testXMLParser passed");
    }

    
    @Test
    public void testJSONParser() throws Exception {
        DynamicVariables dv = JsonParser.getVariablesList("", testJSON);
        
        System.out.println("The DynamicVariable list size = "+dv.getVariables().size());
        System.out.println(dv.toString());

        assertTrue(dv.getVariables().size() == 17);
        System.out.println("testJsonParser passed");
    }
    

}