/**
 * Copyright 2020 XEBIALABS
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
import java.util.Enumeration;
import java.util.Properties;

import setvariables.DynamicVariables;

public class PropertiesParser 
{
    public static DynamicVariables getVariablesList(String input) throws IOException 
    {
        DynamicVariables dynamicVars = new DynamicVariables();
        
        Properties prop = new Properties();
        Reader reader = new StringReader(input);
        try 
        {
            prop.load(reader);

            Enumeration<?> e = prop.propertyNames();
            while (e.hasMoreElements()) 
            {
                String key = (String) e.nextElement();
                String value = prop.getProperty(key);

                boolean isSet = false;

                // try to convert to integer
                try
                {
                    Integer i = Integer.parseInt(value);
                    dynamicVars.addVariable(key, i, DynamicVariables.TYPE_INTEGER);
                    isSet = true;
                } catch (Exception ex) {}

                // try to convert to boolean
                if ( isSet == false )
                {
                    try
                    {
                        Boolean b = Boolean.parseBoolean(value);
                        dynamicVars.addVariable(key, b, DynamicVariables.TYPE_BOOLEAN);
                        isSet = true;
                    } catch (Exception ex) {}
                }

                // assume string
                if ( isSet == false )
                {
                    dynamicVars.addVariable(key, value, DynamicVariables.TYPE_STRING);
                }
            }
           
        } 
        finally
        {
            if (reader != null)
            {
                try {reader.close();} catch (IOException ex){}
            }
        }

        return dynamicVars;
    }
}