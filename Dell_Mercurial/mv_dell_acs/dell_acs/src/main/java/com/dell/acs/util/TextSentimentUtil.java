/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.wlv.sentistrength.Corpus;
import uk.ac.wlv.sentistrength.Paragraph;

import java.io.File;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2699 $, $Date:: 2012-05-29 10:00:40#$
 */
public final class TextSentimentUtil {

    private static final Corpus c = new Corpus();

    private static final Logger logger = LoggerFactory.getLogger(TextSentimentUtil.class);

    public TextSentimentUtil() {
        try {
            c.resources.sgSentiStrengthFolder = new File(TextSentimentUtil.class.getResource("/textmodels/sentstrength/").toURI()).getAbsolutePath() + "/";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (c.initialise()) {
        } else {
            logger.error("Failed to initialise!");
            try {
                File f = new File(c.resources.sgSentiStrengthFolder);
                if (!f.exists())
                    logger.error("Folder does not exist! " + c.resources.sgSentiStrengthFolder);
            } catch (Exception e) {
                logger.error("Folder doesn't exist! " + c.resources.sgSentiStrengthFolder);
            }
        }
    }

    public int getStrength(String text) {
        if (text != null && !text.isEmpty()) {
            Paragraph paragraph = new Paragraph();
            paragraph.setParagraph(text, c.resources, c.options);
            return paragraph.getParagraphScaleSentiment();
        }
        return 0;
    }

}
