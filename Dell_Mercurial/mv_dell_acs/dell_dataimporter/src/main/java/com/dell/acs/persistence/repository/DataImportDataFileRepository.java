/**
 * 
 */
package com.dell.acs.persistence.repository;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dell.acs.managers.DataImportManager.ImportType;
import com.dell.acs.persistence.domain.DataFile;
import com.dell.acs.persistence.domain.RetailerSite;
import com.sourcen.core.persistence.repository.IdentifiableEntityRepository;

/**
 * @author Shawn_Fisk
 *
 */
public interface DataImportDataFileRepository extends IdentifiableEntityRepository<Long, DataFile> {

	@Transactional(propagation = Propagation.MANDATORY)
	boolean isRetailerSiteStillProcessing(RetailerSite retailerSite, ImportType importType);

	@Transactional(propagation = Propagation.MANDATORY)
	boolean hasProcessed(String srcFile);
}
