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

import java.io.StringReader;
import java.util.Set;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonString;
import javax.json.JsonNumber;
import javax.json.JsonValue;
import javax.json.JsonArray;

import setvariables.DynamicVariables;

public class JsonParser 
{

    public static DynamicVariables getVariablesList(String input)
    {
      return getVariablesList("", input);
    }

    public static DynamicVariables getVariablesList(String namePrefix, String input)
    {
        DynamicVariables dynamicVars = new DynamicVariables();

        JsonReader reader = Json.createReader(new StringReader(input));
        JsonObject jsonObj = reader.readObject();
        reader.close();

        processJsonValue(dynamicVars, namePrefix, jsonObj);

        return dynamicVars;
    }

    protected static void processJsonValue(DynamicVariables dynamicVars, String prefix, JsonValue jsonVal)
    {
        if ( jsonVal.getValueType().equals(JsonValue.ValueType.ARRAY) )
        {
            JsonArray jsonArray = (JsonArray) jsonVal;
            for ( int i=0; i<jsonArray.size(); i++ )
            {
                processJsonValue(dynamicVars, makeFullKey(prefix, Integer.toString(i)), jsonArray.get(i));
            }
        }
        else if ( jsonVal.getValueType().equals(JsonValue.ValueType.OBJECT) )
        {
            Set<String> keys = ((JsonObject) jsonVal).keySet();
            for ( String key : keys )
            {
                JsonValue val = ((JsonObject) jsonVal).get(key);
                processJsonValue(dynamicVars, makeFullKey(prefix, key), val);
            }
        }
        else if ( jsonVal.getValueType().equals(JsonValue.ValueType.NUMBER) )
        {
            dynamicVars.addVariable(prefix, toObject(jsonVal), DynamicVariables.TYPE_INTEGER);
        }
        else if ( jsonVal.getValueType().equals(JsonValue.ValueType.STRING) )
        {
            dynamicVars.addVariable(prefix, toObject(jsonVal), DynamicVariables.TYPE_STRING);
        }
        else if ( jsonVal.getValueType().equals(JsonValue.ValueType.TRUE) )
        {
            dynamicVars.addVariable(prefix, toObject(jsonVal), DynamicVariables.TYPE_BOOLEAN);
        }
        else if ( jsonVal.getValueType().equals(JsonValue.ValueType.FALSE) )
        {
            dynamicVars.addVariable(prefix, toObject(jsonVal), DynamicVariables.TYPE_BOOLEAN);
        }
        else
        {
            System.out.println("Unknown json type: " + jsonVal.getValueType() + ", value = "+toObject(jsonVal)+ " ,keyPath = "+prefix);
        }
    }

    protected static String makeFullKey(String prefix, String key)
    {
        if ( prefix == null ) prefix = "";
        if ( key == null || key.length() == 0 ) return prefix;
        if ( prefix.length() > 0 ) prefix = prefix + "_";
        return prefix + key;
    }

    protected static Object toObject(JsonValue jsonValue) {
        switch (jsonValue.getValueType()) {
        case ARRAY:
          return jsonValue.toString();
        case OBJECT:
          return jsonValue.toString();
        case STRING:
          return ((JsonString) jsonValue).getString();
        case NUMBER:
          return ((JsonNumber) jsonValue).numberValue();
        case TRUE:
          return true;
        case FALSE:
          return false;
        case NULL:
          return null;
        default:
          return jsonValue.toString();
        }
      }
}