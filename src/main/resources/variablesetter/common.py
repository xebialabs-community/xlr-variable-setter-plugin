#
# Copyright 2020 XEBIALABS
#
# Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
#
# The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
#
# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
#

import logging

from setvariables import DynamicVariables

from com.xebialabs.xlrelease.api.v1 import ReleaseApi
from com.xebialabs.xlrelease.api.v1.forms import Variable

logging.basicConfig(filename='log/varSetterPlugin.log',
                            filemode='a',
                            format='%(asctime)s,%(msecs)d %(name)s %(levelname)s %(message)s',
                            datefmt='%H:%M:%S',
                            level=logging.DEBUG)

# Update XLR variable values
def set_variables(releaseApi, newVars, allExistingVars, ignoreDataType=False):
    varLen = len(allExistingVars)
    logging.debug("processing %s variables" % varLen)

    for i in range(varLen): 
        val = allExistingVars[i]
        logging.debug("  key: %s" % val.key)

        # We don't create new vars, make sure this is an existing Release Variable
        newVar = newVars.get(val.key)

        # Values from property files and xml files will always be a string type so we will
        #     skip comparing the newVar type and the existing var type. If the 
        #     existing var type is not String, we will let XLR do the conversion.
        if newVar and (ignoreDataType or newVar.getType() == val.type):
            val.value = newVar.getValue()
            releaseApi.updateVariable(val)
            if val.type != DynamicVariables.TYPE_PASSWORD:
                logging.info("  setting: %s = %s" % (val.key, str(val.value)))
            else:
                logging.info("  setting: %s = *******" % val.key)
        else:
            if newVar and newVar.getType() != val.type:
                logging.debug("FAILURE: Type of new value does not match the existing release variable type so will NOT set: %s = %s"
                        % (val.key, str(val.value)))