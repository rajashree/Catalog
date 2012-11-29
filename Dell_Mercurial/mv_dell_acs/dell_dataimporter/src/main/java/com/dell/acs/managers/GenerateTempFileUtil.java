package com.dell.acs.managers;

import java.io.File;
import java.io.IOException;

import com.dell.acs.managers.DataImportManager.ImportType;

public interface GenerateTempFileUtil {
    /**
     * Generates the temp file with the given filename with an option to generate timestamp.
     *
     * @param originalFilename
     * @return generated temp file
     * @throws IOException
     */
    File generateTempFile(String originalFilename) throws IOException;

	String getTimestamp(String srcFile);

	String getTimestamp(String tempFileName, ImportType importType);
}
