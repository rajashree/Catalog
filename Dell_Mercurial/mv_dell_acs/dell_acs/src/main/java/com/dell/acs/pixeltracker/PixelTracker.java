package com.dell.acs.pixeltracker;

import java.util.Map;

/**
 * Any custom pixel tracking implementation should implement this interface
 * to construct the following URLs: Buy, Info, Reviews, etc
 *
 * @author : Vivek Kondur
 * Date: 6/20/12
 * Time: 3:03 PM
 */
public interface PixelTracker {

    Map getLinks(PixelTrackerContext context);
}
