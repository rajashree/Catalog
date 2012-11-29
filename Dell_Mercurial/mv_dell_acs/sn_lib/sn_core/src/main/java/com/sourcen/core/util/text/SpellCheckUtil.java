/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.util.text;

import com.swabunga.spell.engine.SpellDictionaryHashMap;
import com.swabunga.spell.event.DefaultWordFinder;
import com.swabunga.spell.event.SpellChecker;
import com.swabunga.spell.event.StringWordTokenizer;
import org.apache.log4j.Logger;

import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author Adarsh Kumar.
 * @author $LastChangedBy: adarsh $
 * @version $Revision: 1099 $, $Date:: 2012-03-30 11:06:29#$
 */

public final class SpellCheckUtil {

    private static final Logger logger = Logger.getLogger(SpellCheckUtil.class);
    private SpellChecker spellChecker;

    public SpellCheckUtil() {
        try {
            InputStream stream = SpellCheckUtil.class.getResourceAsStream("/textmodels/dictionary.txt");
            if (stream == null) {
                throw new RuntimeException("Unable to read file /textmodels/dictionary.txt");
            }
            try {
                spellChecker = new SpellChecker(new SpellDictionaryHashMap(new InputStreamReader(stream)));
            } catch (Exception exceptionObject) {
                logger.warn(exceptionObject.getMessage());
            }
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
    }

    public final int countMisspelledWords(String textData) {
        StringWordTokenizer texTok = new StringWordTokenizer(textData, new DefaultWordFinder());
        int count = spellChecker.checkSpelling(texTok);
        if (count == -1) {
            return 0;
        }
        return count;
    }
}
