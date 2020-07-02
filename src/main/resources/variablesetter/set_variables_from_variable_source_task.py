#
# Copyright 2020 XEBIALABS
#
# Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
#
# The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
#
# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
#

import sys
import logging
import re

from variablesetter.common import set_variables

from setvariables.parsers import JsonParser
from setvariables.parsers import XmlParser

from com.xebialabs.xlrelease.api.v1 import ReleaseApi
from com.xebialabs.xlrelease.api.v1.forms import Variable



def main():
    # We will only update values for existing variables. We do not create new ones
    allExistingVars = releaseApi.getVariables(release.id)

    logging.info("allExistingVars = %s" % allExistingVars)
    logging.info("type(allExistingVars) = %s" % type(allExistingVars))

    newVars = []
    if dataFormat == 'XML':
        newVars = XmlParser.getVariablesList(namePrefix, source) # DynamicVariables
    elif dataFormat == 'JSON':
        newVars = JsonParser.getVariablesList(namePrefix, source) # DynamicVariables
    
    logging.debug("newVars = %s" % newVars)

    set_variables(releaseApi, newVars, allExistingVars)


if __name__ == '__main__' or __name__ == '__builtin__':
    main()
