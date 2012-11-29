/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.util.text;

import org.apache.log4j.Logger;
import org.apache.log4j.lf5.util.StreamUtils;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.StringTokenizer;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author Adarsh Kumar.
 * @author $LastChangedBy: adarsh $
 * @version $Revision: 1098 $, $Date:: 2012-03-30 11:06:13#$
 */
public class ProfanityUtil {

    private static final Logger logger = Logger.getLogger(ProfanityUtil.class);
    private final Collection<String> badWords = new CopyOnWriteArraySet<String>();


    public ProfanityUtil() {
        try {
            InputStream stream = SpellCheckUtil.class.getResourceAsStream("/textmodels/badwords.txt");
            if (stream == null) {
                throw new RuntimeException("Unable to read file /textmodels/dictionary.txt");
            }
            try {

                InputStreamReader inputStreamReader = new InputStreamReader(stream);
                String badWordsString = new String(StreamUtils.getBytes(stream));
                String[] words = badWordsString.split("\n");
                for (String word : words) {
                    badWords.add(word.replaceAll("[\'\r\n\t]", ""));
                }
            } catch (Exception exceptionObject) {
                logger.warn(exceptionObject.getMessage());
            }
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
    }

    final public int getMatchWordCount(String data) {
        int count = 0;
        final StringTokenizer stringTokenizer = new StringTokenizer(data," ");
        String word = null;
        while (stringTokenizer.hasMoreTokens()) {
            word = stringTokenizer.nextToken();
            if (badWords.contains(word)) {
                count += 1;
            }
        }
        return count;
    }
}
