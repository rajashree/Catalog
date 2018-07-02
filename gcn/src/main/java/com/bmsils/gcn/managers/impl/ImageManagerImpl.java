/*
 * Copyright (c) 2012 - BMS Innolabs Software Pvt Ltd.
 * All rights reserved
 */

package com.bmsils.gcn.managers.impl;

import com.bmsils.gcn.managers.ImageManager;
import com.bmsils.gcn.persistence.domain.Image;
import com.bmsils.gcn.persistence.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: rajashree
 * Date: 4/24/12
 * Time: 6:18 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class ImageManagerImpl implements ImageManager{
    
    @Transactional
    public List<Image> getImages(String userGCN) {
       return imageRepository.getImages(userGCN);
    }

    
    @Transactional
    public Image getImage(Long imageId) {
        return imageRepository.getImage(imageId);
    }

    
    @Transactional
    public Image getDefaultImage(String userGCN) {
        return imageRepository.getDefaultImage(userGCN);
    }

    
    @Transactional
    public void setDefaultImage(String userGCN, Long imageId) {
        List<Image> images = imageRepository.getImages(userGCN);
        for(Image img:images){
            if(img.getId().equals(imageId)){
               img.setDefault(true);
               imageRepository.update(img);
            }else{
                img.setDefault(false);
                imageRepository.update(img);
            }
        }
    }

    
    @Transactional
    public Long addImage(Image image) {
        return imageRepository.addImage(image);
    }

    
    @Transactional
    public Long updateImage(Image image) {
        imageRepository.update(image);
        return image.getId();
    }

    
    @Transactional
    public void deleteImage(Long imageId) {
        imageRepository.deleteImage(imageId);
    }

    /**
     * Dependency Injection of various Spring beans follows
     */
    @Autowired
    ImageRepository imageRepository;

    public ImageRepository getImageRepository() {
        return imageRepository;
    }

    public void setImageRepository(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }
}
