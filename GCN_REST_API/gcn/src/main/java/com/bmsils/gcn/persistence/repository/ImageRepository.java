/*
 * Copyright (c) 2012 - BMS Innolabs Software Pvt Ltd.
 * All rights reserved
 */

package com.bmsils.gcn.persistence.repository;

import com.bmsils.gcn.persistence.domain.Image;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: rajashree
 * Date: 4/24/12
 * Time: 6:15 PM
 * ImageRepository contains data access methods pertaining to Image table related functions
 */
public interface ImageRepository extends Repository{

    void deleteImage(Long imageId);

    public List<Image> getImages(String userGCN);

    Long addImage(Image image);

    Image getImage(Long imageId);

    Image getDefaultImage(String userGCN);
}
