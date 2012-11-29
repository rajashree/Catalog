package com.dell.dw.persistence.repository;

import com.dell.dw.persistence.domain.GAEventDimension;
import com.sourcen.core.persistence.repository.IdentifiableEntityRepository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: bhaskara
 * Date: 5/29/12
 * Time: 8:48 PM
 * To change this template use File | Settings | File Templates.
 */
public interface GAEventDimensionRepository extends IdentifiableEntityRepository<Long, GAEventDimension> {
    GAEventDimension getEventDimension(String eventCategory, String eventAction, String eventLabel, Long profileId);
    List<GAEventDimension> getEventDimensions(Long profileId);

}
