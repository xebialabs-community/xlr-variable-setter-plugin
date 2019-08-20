package integration;
import static org.junit.Assert.assertTrue;

/**
 * Copyright 2019 XEBIALABS
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
import java.io.IOException;

import com.palantir.docker.compose.DockerComposeRule;
import com.palantir.docker.compose.configuration.ShutdownStrategy;

//import org.json.JSONObject;
import org.json.JSONObject;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import integration.util.VarSetterTestHelper;

public class VarSetterIntegrationTest {
    
    @ClassRule
    // This DockerComposeRule will clean up the docker containers after the tests have run
    public static DockerComposeRule docker = DockerComposeRule.builder()
                                                .file(VarSetterTestHelper.getResourceFilePath("docker/docker-compose.yml"))
                                                .pullOnStartup(true)
                                                .shutdownStrategy(getShutdownStrategy())
                                                .build();

    @BeforeClass
    public static void initialize() throws IOException, InterruptedException {
        VarSetterTestHelper.initializeXLR();
    }

    // Utility Methods

    private static ShutdownStrategy getShutdownStrategy()
    {
        ShutdownStrategy strategy = null;
        System.out.println("In get ShutdownStrategy");
        String skip = System.getProperty("test.skipShutDown");
        System.out.println("Property test.skipShutDown = "+skip);
        if(skip != null && skip.equalsIgnoreCase("true"))
        {
            System.out.println("Skip");
            strategy = ShutdownStrategy.SKIP;
        } else {
            System.out.println("Graceful");
            strategy = ShutdownStrategy.GRACEFUL;
        }
        return strategy;
    }


    // Tests

    @Test
    public void testSetVariables() throws Exception {
        JSONObject theResult = VarSetterTestHelper.getVarSetterReleaseResult();
        //System.out.println("The TEST RESULT - "+theResult);
        assertTrue(theResult != null);
        // The file, testSetVariables, contains the JSONObject we expect to be returned from XLR. Order of variables does not matter
        String expected = VarSetterTestHelper.readFile(VarSetterTestHelper.getResourceFilePath("testExpected/testSetVariables.txt"));
        try {
            // This will assert that all pre-exisiting variables are there, have been set to the correct and no variables were add. Order does not matter.
            JSONAssert.assertEquals(expected, theResult, JSONCompareMode.NON_EXTENSIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("testSetVariables passed ");
    }
}
