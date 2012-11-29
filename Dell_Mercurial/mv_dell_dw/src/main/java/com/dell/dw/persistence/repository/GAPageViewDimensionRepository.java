package com.dell.dw.persistence.repository;

import com.dell.dw.persistence.domain.GAPageViewDimension;
import com.sourcen.core.persistence.repository.IdentifiableEntityRepository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: bhaskara
 * Date: 5/30/12
 * Time: 1:43 PM
 * To change this template use File | Settings | File Templates.
 */
public interface GAPageViewDimensionRepository extends IdentifiableEntityRepository<Long, GAPageViewDimension>  {

    GAPageViewDimension getPageViewDimension(String pageTitle, String pagePath, Integer pageDepth, Long profileId);
    List<GAPageViewDimension> getPageViewDimensions(Long profileId);
}
