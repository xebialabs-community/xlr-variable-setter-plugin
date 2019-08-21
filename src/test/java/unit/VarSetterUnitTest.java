/**
 * Copyright 2019 XEBIALABS
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

    @Test
    public void testXMLParser() throws Exception {
        DynamicVariables dv = XmlParser.getVariablesList(testXML);
        System.out.println("The DynamicVariable list size = "+dv.getVariables().size());
        for(int i = 0 ; i < dv.getVariables().size(); i++) 
        {
            DynamicVariable dynVar = (DynamicVariable) dv.getVariables().get(i);
            System.out.println("Key = "+dynVar.getKey()+", Value = "+dynVar.getValue());   
        }
        assertTrue(dv.getVariables().size() == 11);
        System.out.println("testXMLParser passed");
    }

}