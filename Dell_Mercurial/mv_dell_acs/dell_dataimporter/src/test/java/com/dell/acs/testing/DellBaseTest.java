/**
 * 
 */
package com.dell.acs.testing;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Shawn_Fisk
 *
 */
public class DellBaseTest {

	/**
	 * 
	 */
	public DellBaseTest() {
		// TODO Auto-generated constructor stub
	}


	public static void compareFileSystem(String actualLocation,
			String expectedLocation) {

		File actualRoot = new File(actualLocation);
		File expectedRoot = new File(expectedLocation);
		Stack<File> actualStack = new Stack<File>();
		Stack<File> expectedStack = new Stack<File>();
		
		if ((actualRoot.exists()) && (!expectedRoot.exists())) {
			fail("The file " + actualRoot.getAbsolutePath() + " should not exist!"); 
		}

		if ((!actualRoot.exists()) && (expectedRoot.exists())) {
			fail("The file " + expectedRoot.getAbsolutePath() + " is missing from the actual!"); 
		}

		actualStack.push(actualRoot);
		expectedStack.push(expectedRoot);

		while (!actualStack.isEmpty()) {
			File actualFile = actualStack.pop();
			File expectedFile = expectedStack.pop();
			
			if ((actualFile != null) && (expectedFile == null)) {
				fail("The file " + actualFile.getAbsolutePath() + " should not exist!"); 
			}

			if ((actualFile == null) && (expectedFile != null)) {
				fail("The file " + expectedFile.getAbsolutePath() + " is missing from the actual!"); 
			}

			if (actualFile.isDirectory()) {
				Map<String, File> actualByName = new TreeMap<String, File>();
				boolean atLeastOneActual = false;

				for (File actualChild : actualFile.listFiles()) {
					atLeastOneActual = true;
					actualByName.put(actualChild.getName(), actualChild);
				}
				
				boolean atLeastOneExpected = false;
				//System.out.printf("The actualFile location is %s\n", actualFile.getAbsolutePath());
				for (File expectedChild : expectedFile.listFiles()) {
                    if (expectedChild.getName().endsWith("_emptyDir.txt")) {
                        continue;
                    }
					atLeastOneExpected = true;
					if (actualByName.containsKey(expectedChild.getName())) {
						actualStack.push(actualByName.get(expectedChild.getName()));
						expectedStack.push(expectedChild);
					} else {
						fail("The file " + expectedChild.getAbsolutePath() + " is missing from the actual!"); 
					}
				}
				
				if (atLeastOneActual && !atLeastOneExpected) {
					fail("The file " + actualFile.getAbsolutePath() + " should not have children!"); 
				}

				if (!atLeastOneActual && atLeastOneExpected) {
					fail("The file " + expectedFile.getAbsolutePath() + " is missing children from the actual!"); 
				}
			} else {
				fileCompare(actualFile, expectedFile);
			}
		}
	}

	public static void fileCompare(File actualFile, File expectedFile) {
		try {
			FileInputStream actualFIS = new FileInputStream(actualFile);
			FileInputStream expectedFIS = new FileInputStream(expectedFile);
			ByteBuffer actualBuffer = ByteBuffer.allocate(1024);
			ByteBuffer expectedBuffer = ByteBuffer.allocate(1024);
			boolean done = false;
			long offset = 0;
			
			assertEquals(String.format("The actual file '%s' is difference than expected file '%s'", actualFile.getAbsolutePath(), expectedFile.getAbsolutePath()), actualFile.getTotalSpace(), actualFile.getTotalSpace());
			
			while(!done) {
				int actualBytes = actualFIS.getChannel().read(actualBuffer);
				int expectedBytes = expectedFIS.getChannel().read(expectedBuffer);
				
				actualBuffer.flip();
				expectedBuffer.flip();
				
				assertEquals(String.format("at offset %d, the actual file '%s' is difference than expected file '%s'", offset, actualFile.getAbsolutePath(), expectedFile.getAbsolutePath()), actualBytes, expectedBytes);

				assertTrue(String.format("at offset %d, the actual file '%s' is difference than expected file '%s'", offset, actualFile.getAbsolutePath(), expectedFile.getAbsolutePath()), actualBuffer.compareTo(expectedBuffer) == 0);
				
				offset += actualBytes;

				actualBuffer.flip();
				expectedBuffer.flip();
				
				if ((actualBytes == 0) || (expectedBytes == 0)) {
					done = true;
				}
			}
			actualFIS.close();
			expectedFIS.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			fail(String.format("Unknown error while comparing actual file '%s' to expected file '%s'!", actualFile.getAbsolutePath(), expectedFile.getAbsolutePath()));
		} catch (IOException e) {
			e.printStackTrace();
			fail(String.format("Unknown error while comparing actual file '%s' to expected file '%s'!", actualFile.getAbsolutePath(), expectedFile.getAbsolutePath()));
		}
	}

    public static String getTestingRootDir() {
        try {
            String userDir = System.getProperty("user.dir");

            File testing1Dir = new File(String.format("%s/testing", userDir));
            if (testing1Dir.exists()) {
                return testing1Dir.getParentFile().getCanonicalPath();
            } else {
                File testing2Dir = new File(String.format("%s/../testing", userDir));
                if (testing2Dir.exists()) {
                    return testing2Dir.getParentFile().getCanonicalPath();
                } else {
                    throw new RuntimeException("Can not find the testing directory related to the user.dir system environment property!");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Can not find the testing directory related to the user.dir system environment property!",e);
        }
    }
}
