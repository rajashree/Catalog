/*
 * Copyright (c) 2012 - BMS Innolabs Software Pvt Ltd.
 * All rights reserved
 */

package com.bmsils.gcn.persistence.repository.impl;

import com.bmsils.gcn.persistence.domain.Image;
import com.bmsils.gcn.persistence.repository.ImageRepository;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: rajashree
 * Date: 4/24/12
 * Time: 6:16 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("imageRepository")
@Transactional
public class ImageRepositoryImpl  extends RepositoryImpl implements ImageRepository {
    public ImageRepositoryImpl() {
        super(Image.class);
    }

    
    public void deleteImage(Long imageId) {
       Image image = (Image)getSession().get(getEntityClass(), imageId);
        remove(image);
    }

    
    public List<Image> getImages(String userGCN) {
        try {
            List<Image> o = getSession().createCriteria(Image.class)
                    //.add(Restrictions.eq("user.userGCN", userGCN))
                    .createAlias("user", "u")
                    .add(Restrictions.or(Restrictions.eq("user.userGCN", userGCN),Restrictions.eq("u.alias", userGCN)))
                    .list();
            if (o != null) {
                return o;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    
    public Long addImage(Image image) {
        getSession().saveOrUpdate(image);
        return image.getId();
    }

    
    public Image getImage(Long imageId) {
        return (Image) getSession().get(getEntityClass(), imageId);
    }

    
    public Image getDefaultImage(String userGCN) {
        try {
            Object img = getSession().createCriteria(Image.class)
                    //.add(Restrictions.eq("user.userGCN", userGCN))
                    .createAlias("user", "u")
                    .add(Restrictions.or(Restrictions.eq("user.userGCN", userGCN),Restrictions.eq("u.alias", userGCN)))
                    .add(Restrictions.eq("isDefault",true))
                    .uniqueResult();
            if (img != null) {
                return (Image)img;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
