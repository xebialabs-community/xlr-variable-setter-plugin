#
# Copyright 2019 XEBIALABS
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
from setvariables.parsers import YamlParser
from setvariables.parsers import PropertiesParser
from java.util import HashMap
from com.xebialabs.xlrelease.api.v1 import ReleaseApi
from com.xebialabs.xlrelease.api.v1.forms import Variable

logging.basicConfig(filename='log/plugin.log',
                            filemode='a',
                            format='%(asctime)s,%(msecs)d %(name)s %(levelname)s %(message)s',
                            datefmt='%H:%M:%S',
                            level=logging.DEBUG)

def main():  
      
    base64string = base64.b64encode('%s:%s' % (username, password)).replace('\n', '')
     
    fileName = ""
    fileType = ""
    # These are the file types we are prepared to process
    listOfYamlTypes = ['.yaml' , '.yml']
    listOfPropertiesTypes = ['.properties']

    newVars = []
    newVarsMap = HashMap()
    # All of the current release variables in the template or the release
    # We will only update values for existing variables. We do not create new ones
    allExistingVars = releaseApi.getVariables(release.id)

    for item in fileNameList:
        fileName = item
        fileType = os.path.splitext(fileName)[1]
        # If it is a file type we can process, get the contents and process according to type
        if fileType in listOfYamlTypes or fileType in listOfPropertiesTypes:
            url = targetURL.replace(":filename:", item)
            logging.debug("The new URL is "+url)
            data = getData(url, base64string)
            if len(data) > 0:
                if fileType in listOfYamlTypes:
                    newVars = YamlParser.getVariablesList(data)
                elif fileType in listOfPropertiesTypes:
                    newVars = PropertiesParser.getVariablesList(data)
                else:
                    # If no data was returned, skip this file
                    break
                # put the new vars in a map indexed by key(name)
                newVarsMap = HashMap()
                for dynamicVar in newVars.getVariables():
                    newVarsMap.put(dynamicVar.getKey(), dynamicVar)
                
                for var in allExistingVars:
                    newVar = newVarsMap.get(var.key)
                    # If this is from a properites file (where we have no type) or it is from a yaml file and type matches)
                    if newVar and ( fileType in listOfPropertiesTypes or newVar.getType() == var.type):
                    #if newVar:
                        var.value = newVar.getValue()
                        releaseApi.updateVariable(var)
  

def getData(url, authString): 
    request = urllib2.Request(url)
    request.add_header("Authorization", "Basic %s" % authString)
    data = ""

    try:
        response = urllib2.urlopen(request)
        logging.debug('Response status: '+ str(response.code))
    except urllib2.URLError as e:
        if hasattr(e, 'reason'):
            logging.debug('We failed to reach a server.')
            logging.debug('Reason: ', e.reason)
            logging.debug("For URL - "+url)
        if hasattr(e, 'code'):
            logging.debug('The server couldn\'t fulfill the request.')
            logging.debug('Error code: ', e.code)
            logging.debug("ForURL - "+url)
        if failIfFileNotFound:
            print ("File not found - "+url)
            sys.exit(1)
    else:
        data = response.read(20000) # read only 20 000 chars
    
    return data



if __name__ == '__main__' or __name__ == '__builtin__':
    main()   
        
