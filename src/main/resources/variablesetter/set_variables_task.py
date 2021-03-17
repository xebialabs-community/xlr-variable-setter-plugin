#
# Copyright 2021 XEBIALABS
#
# Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
#
# The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
#
# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
#

import os.path
import urllib2, base64
import sys
import logging
import re

from variablesetter.common import set_variables

from setvariables.parsers import XmlParser
from setvariables.parsers import YamlParser
from setvariables.parsers import PropertiesParser
from setvariables.parsers import JsonParser

from com.xebialabs.xlrelease.api.v1 import ReleaseApi
from com.xebialabs.xlrelease.api.v1.forms import Variable


# These are the file types we are prepared to process
listOfYamlTypes = ['.yaml' , '.yml']
listOfPropertiesTypes = ['.properties']
listOfXmlTypes = ['.xml']
listOfJsonTypes = ['.json']

def main():
    fileType = ""
    newVars = []
    # We will only update values for existing variables. We do not create new ones
    allExistingVars = releaseApi.getVariables(release.id)
    # If it is a file type we can process, place in interator
    filesToProcessItr = filter(lambda x: os.path.splitext(x)[1] in (listOfPropertiesTypes + listOfYamlTypes + listOfXmlTypes +listOfJsonTypes), fileNameList )
    logging.info("The filtered list = %s" % list(filesToProcessItr))

    # TODO - fail if no valid file found
    
    for fileName in filesToProcessItr:
        ignoreDataType = False
        fileType = os.path.splitext(fileName)[1]
        url = targetURL.replace(":filename:", fileName)
        logging.info("The new URL is "+url)
        data = getData(url)

        if len(data) == 0:
            continue

        if fileType in listOfYamlTypes:
            newVars = YamlParser.getVariablesList(data)
        elif fileType in listOfPropertiesTypes:
            newVars = PropertiesParser.getVariablesList(data)
            ignoreDataType = True
        elif fileType in listOfXmlTypes:
            newVars = XmlParser.getVariablesList(data)
            ignoreDataType = True
        elif fileType in listOfJsonTypes:
            newVars = JsonParser.getVariablesList(data)
        else:
            # If no data was returned, skip this file
            logging.error("Unknown file type: "+fileType)
            sys.exit(1)

        logging.debug("newVars = %s" % newVars)
        set_variables(releaseApi, newVars, allExistingVars, ignoreDataType)


def getData(url):
    base64string = base64.b64encode('%s:%s' % (username, password)).replace('\n', '')
    request = urllib2.Request(url)
    request.add_header("Authorization", "Basic %s" % base64string)
    data = ""
    try:
        response = urllib2.urlopen(request)
    except urllib2.URLError as e:
        if hasattr(e, 'reason'):
            logging.info('Failed to reach server - Reason: %s'% e.reason)
        if hasattr(e, 'code'):
            logging.info('The server did not fulfill the request - Code: %s'% str(e.code))

        # If user has configured for failure if a file is not retrieved
        if failIfFileNotFound:
            logging.info('File Not Found: %s'% url)
            print ("File not found - %s" % url)
            sys.exit(1)
        else:
            return data
    else:
        data = response.read(20000) # read only 20 000 chars
        logging.debug('data = %s, code = %s' % (data, response.getcode())) 
    return data



if __name__ == '__main__' or __name__ == '__builtin__':
    main()
