/*
 * Copyright (c) 2012 - BMS Innolabs Software Pvt Ltd.
 * All rights reserved
 */

package com.bmsils.gcn.managers;

import com.bmsils.gcn.persistence.domain.Image;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: rajashree
 * Date: 4/24/12
 * Time: 6:17 PM
 * Service to handle Images related function calls
 */
public interface ImageManager {

    /**
     * delete an image
     * @param imageId
     */
    public void deleteImage(Long imageId);

    /**
     * get images associated with a GCN
     * @param userGCN
     * @return
     */
    public List<Image> getImages(String userGCN);

    /**
     * get image based on imageId
     * @param imageId
     * @return
     */
    public Image getImage(Long imageId);

    /**
     * get default avatar of an user
     * @param userGCN
     * @return
     */
    public Image getDefaultImage(String userGCN );

    /**
     * set the default image for an user
     * @param userGCN
     * @param imageId
     */
    public void setDefaultImage(String userGCN,Long imageId );

    /**
     * add an image
     * @param image
     * @return
     */
    public Long addImage(Image image);

    /**
     * update an image
     * @param image
     * @return
     */
    public Long updateImage(Image image);
}
