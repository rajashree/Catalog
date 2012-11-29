/**
 * 
 */
package com.dell.acs.managers;

import com.dell.acs.dataimport.model.DataImportError;
import com.dell.acs.dataimport.model.ValidatorContext;
import com.dell.acs.persistence.domain.*;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

/**
 * @author Shawn_Fisk
 *
 */
public interface DataFileStatisticService {
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	void initializeStatistics(DataFile dataFile);

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	void startImport(DataFile dataFile);

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	void processImport(DataFile dataFile, int row);

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	void importHandleError(DataFile dataFile, int row, Throwable t);

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	void endImport(DataFile dataFile, int row, boolean hasErrors, Iterable<DataImportError> errors);

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	void startValidation(DataFile dataFile);

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	void processValidation(UnvalidatedProduct product);

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	void endValidation(UnvalidatedProduct product, ValidatorContext context);

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	void validationHandleException(UnvalidatedProduct product, Throwable t);

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	void startImages(UnvalidatedProduct product);

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	void processImages(UnvalidatedProduct product);

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	void imagesHandleError(UnvalidatedProduct product,
			UnvalidatedProductImage upi);

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	void endImages(UnvalidatedProduct product);

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	void startTransfer(UnvalidatedProduct product);

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	void processTransfer(UnvalidatedProduct unvalidateProduct);

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	void processTransferSlider(UnvalidatedProduct unvalidateProduct);

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	void endTransfer(DataFile dataFile, int row);

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	Collection<DataFileStatistic> getByRetailerSite(RetailerSite rs, List<Long> skipStatsDataFileIds);

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	Collection<DataFileStatistic> getByRetailerSiteAndHost(RetailerSite rs,
                                                           String host, List<Long> skipStatsDataFileIds);

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	void downloadImageFailed(UnvalidatedProduct product,
			UnvalidatedProductImage productImage, ValidatorContext context);

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	void checkStartValidation(DataFile dataFile);

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	void transportHandleError(DataFile dataFile, int row, Throwable t, String msg);

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void updatingImport(DataFile dataFile, int i, boolean updating);

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    Collection<DataFileStatisticSummary> getSummaryByRetailerSite(RetailerSite rs);

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    Long getErrorCountForStat(DataFileStatistic dataFileStat);
}
