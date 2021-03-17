/**
 * Copyright 2021 XEBIALABS
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package setvariables.parsers;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Properties;
import java.util.List;

import setvariables.DynamicVariable;
import setvariables.DynamicVariables;

public class PropertiesParser {

    public static DynamicVariables getVariablesList(String input) {
        
        Properties prop = new Properties();
        DynamicVariables dynamicVars = new DynamicVariables();
        List<DynamicVariable> theList = new ArrayList<DynamicVariable>();
        
        Reader reader = new StringReader(input);
        try {
            prop.load(reader);

            Enumeration<?> e = prop.propertyNames();
            while (e.hasMoreElements()) {
                String key = (String) e.nextElement();
                String value = prop.getProperty(key);
                DynamicVariable dynVar = new DynamicVariable();
                dynVar.setKey(key);
                dynVar.setValue(value);
                dynVar.setType("xlrelease.StringVariable");
                theList.add(dynVar);
            }
            
        } catch (IOException e) {
            
        } finally{
            if (reader != null){
                try {reader.close();} catch (IOException ex){}
            }
        }
        dynamicVars.setVariables(theList);
        return dynamicVars;
    }

}