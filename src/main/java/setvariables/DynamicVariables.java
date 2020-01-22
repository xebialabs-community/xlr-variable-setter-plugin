/**
 * Copyright 2020 XEBIALABS
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package setvariables;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class DynamicVariables 
{
    public static final String TYPE_STRING   = "xlrelease.StringVariable";
    public static final String TYPE_INTEGER  = "xlrelease.IntegerVariable";
    public static final String TYPE_BOOLEAN  = "xlrelease.BooleanVariable";
    public static final String TYPE_PASSWORD = "xlrelease.PasswordStringVariable";

    private Map<String,DynamicVariable> variables = new HashMap<String,DynamicVariable>();

    public Collection<DynamicVariable> getVariables()
    {
        return this.variables.values();
    }

    public void setVariables(Collection<DynamicVariable> variables){
        this.variables.clear();
        for (DynamicVariable dv : variables){
            this.variables.put(dv.getKey(), dv);
        }
    }
    
    public void addVariable(String key, Object value, String type)
    {
        DynamicVariable dv = new DynamicVariable();
        dv.setKey(key);
        dv.setValue(value);
        dv.setType(type);
        this.variables.put(dv.getKey(), dv);
    }

    public DynamicVariable get(String key)
    {
        return this.variables.get(key);
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        if ( this.variables.size() > 0 )
        {
            boolean first = true;
            for (DynamicVariable dv : this.variables.values() )
            {
                if ( !first ) sb.append(", ");
                first = false;

                sb.append(dv);
            }
        }
        else
        {
            sb.append("empty");
        }
        sb.append("]");
        return sb.toString();
    }
}

