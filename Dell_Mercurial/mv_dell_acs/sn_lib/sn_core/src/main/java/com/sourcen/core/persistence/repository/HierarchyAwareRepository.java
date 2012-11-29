/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.persistence.repository;

import com.sourcen.core.persistence.domain.HierarchyAwareEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 */
@Transactional(readOnly = true)
public interface HierarchyAwareRepository<R extends HierarchyAwareEntity> extends IdentifiableEntityRepository<Long, R> {

    List<R> getTree(Long recordId);

    List<R> getPath(R record);

    List<R> getChildren(Long recordId);

    @Transactional
    void moveTo(R record, R newParent);

}
