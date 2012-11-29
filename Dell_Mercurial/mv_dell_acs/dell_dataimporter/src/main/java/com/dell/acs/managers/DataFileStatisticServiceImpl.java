package com.dell.acs.managers;

import com.dell.acs.dataimport.model.DataImportError;
import com.dell.acs.dataimport.model.ValidatorContext;
import com.dell.acs.dataimport.model.ValidatorError;
import com.dell.acs.managers.DataImportManager.FileStatus;
import com.dell.acs.managers.DataImportManager.ImportType;
import com.dell.acs.managers.model.ProductValidationStatus;
import com.dell.acs.persistence.domain.*;
import com.dell.acs.persistence.repository.*;
import com.sourcen.core.config.ConfigurationService;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Service
public class DataFileStatisticServiceImpl implements DataFileStatisticService {

	public DataFileStatisticServiceImpl() {
	}

	@Override
	public void initializeStatistics(DataFile dataFile) {
		Session session = this.getSession();

		int numRows = dataFile.getNumRows();

		for (int row = 0; row < numRows; row++) {
			DataFileStatistic dfs = this.dataFileStatisticRepository.create(
					session, dataFile, row + 1); // Base 1!
			session.save(dfs);
		}
	}

	@Override
	public void startImport(DataFile dataFile) {
		Date now = new Date();
		Session session = this.getSession();

		Collection<DataFileStatistic> stats = this.dataFileStatisticRepository
				.getByDataFile(session, dataFile);

		for (DataFileStatistic stat : stats) {
			stat.setImportStartTime(now);
			session.update(stat);
		}
	}

	@Override
	public void processImport(DataFile dataFile, int row) {
		Session session = this.getSession();

		DataFileStatistic stat = this.dataFileStatisticRepository
				.getByDataFileAndRow(session, dataFile, row);

		stat.setImportHost(this.configurationService.getApplicationUrl());
		session.update(stat);
	}

	@Override
	public void importHandleError(DataFile dataFile, int row, Throwable t) {
		Date now = new Date();
		Session session = this.getSession();

		DataFileStatistic stat = this.dataFileStatisticRepository
				.getByDataFileAndRow(session, dataFile, row);

		stat.setHasImportErrors(true);
		stat.setImagesEndTime(now);
		session.update(stat);

		DataFileError error = this.dataFileErrorRepository.create(session,
				stat, -1, t);
		session.save(error);
	}

	@Override
	public void endImport(DataFile dataFile, int row, boolean hasErrors,
			Iterable<DataImportError> diErrors) {
		Date now = new Date();
		Session session = this.getSession();

		DataFileStatistic stat = this.dataFileStatisticRepository
				.getByDataFileAndRow(session, dataFile, row);

		stat.setHasImportErrors(hasErrors);
		stat.setImportEndTime(now);
		session.update(stat);

		for (DataImportError diError : diErrors) {
			DataFileError error = this.dataFileErrorRepository.create(session,
					stat, diError);
			session.save(error);
		}
	}

	private static final Logger logger = LoggerFactory
			.getLogger(DataFileStatisticServiceImpl.class);

	@Override
	public void startValidation(DataFile dataFile) {
		Date now = new Date();
		Session session = this.getSession();

		Collection<DataFileStatistic> stats = this.dataFileStatisticRepository
				.getByDataFile(session, dataFile);

		logger.error(Thread.currentThread().getId() + ":" + dataFile.getId()
				+ " has " + stats.size() + " stats?");

		for (DataFileStatistic stat : stats) {
			UnvalidatedProduct product = this
					.getUnvalidateProductByStatistic(stat);

			if (product == null) {
				throw new RuntimeException("No unvaliated product for status:"
						+ stat.getDataFile_id() + " row " + stat.getRow());
			}

			if (stat.getValidationEndTime() != null) {
				throw new RuntimeException(
						"Validation end time should not be set!");
			}

			stat.setValidationStartTime(now);
			session.update(stat);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dell.acs.managers.DataFileStatisticService#checkStartValidation(com
	 * .dell.acs.persistence.domain.DataFile)
	 */
	@Override
	public void checkStartValidation(DataFile doneDataFile) {
		Session session = this.getSession();
		Criteria relatedDataFilesQuery = session.createCriteria(DataFile.class);
		relatedDataFilesQuery.add(Restrictions.eq("srcFile",
				doneDataFile.getSrcFile()));
		@SuppressWarnings("unchecked")
		Collection<DataFile> dataFiles = (Collection<DataFile>) relatedDataFilesQuery
				.list();

		int numStillProcessing = 0;
		List<DataFile> productDataFiles = new ArrayList<DataFile>();

		for (DataFile dataFile : dataFiles) {
			if (ImportType.getTypeFromTableName(dataFile.getImportType()) == ImportType.products) {
				productDataFiles.add(dataFile);
			}

			if (dataFile.getStatus() == FileStatus.DONE) {
				continue;
			} else if (dataFile.getStatus() == FileStatus.ERROR_READ) {
				continue;
			} else if (dataFile.getStatus() == FileStatus.ERROR_EXTRACTING) {
				continue;
			} else if (dataFile.getStatus() == FileStatus.ERROR_PARSING) {
				continue;
			} else if (dataFile.getStatus() == FileStatus.ERROR_RESIZING) {
				continue;
			} else if (dataFile.getStatus() == FileStatus.ERROR_WRITE) {
				continue;
			} else if (dataFile.getStatus() == FileStatus.PREPROCESS_ERROR) {
				continue;
			} else if (dataFile.getStatus() == FileStatus.PREPROCESS_SPLITTING_UP_DONE) {
				continue;
            } else if (dataFile.getStatus() == FileStatus.PREPROCESS_CONVERT_TO_FICSTAR_DONE) {
                continue;
            } else if (dataFile.getStatus() == FileStatus.ERROR_UNKNOWN) {
				continue;
			} else if (dataFile.getStatus() == FileStatus.TRANSFER_DONE) {
				continue;
			} else {
				numStillProcessing++;
			}
		}

		if (numStillProcessing == 0) {
			for(DataFile dataFile : dataFiles) {
                if (dataFile.getStatus() == FileStatus.PREPROCESS_SPLITTING_UP_DONE) {
                    continue;
                } else if (dataFile.getStatus() == FileStatus.PREPROCESS_CONVERT_TO_FICSTAR_DONE) {
                    continue;
                }

                dataFile.setStatus(FileStatus.READY_TO_VALIDATE);
				this.dataFileRepository.update(dataFile);
			}
			
			Collection<UnvalidatedProduct> products = this.unvalidatedProductRepository
					.getUnvalidateProductByDataFiles(productDataFiles);
			for (UnvalidatedProduct product : products) {
				product.setStatus(ProductValidationStatus.IN_QUEUE.getDbValue());
				this.unvalidatedProductRepository.update(product);
			}
		}
	}

	private UnvalidatedProduct getUnvalidateProductByStatistic(
			DataFileStatistic stat) {
		Session session = this.getSession();
		Criteria critieria = session.createCriteria(UnvalidatedProduct.class);
		critieria.add(Restrictions.eq("dataFile.id", stat.getDataFile_id()));
		critieria.add(Restrictions.eq("dataFileRow", stat.getRow()));

		@SuppressWarnings("rawtypes")
		List result = critieria.list();

		if (result == null) {
			return null;
		} else if (result.size() == 0) {
			return null;
		} else {
			return (UnvalidatedProduct) result.iterator().next();
		}
	}

	@Override
	public void processValidation(UnvalidatedProduct product) {
		Session session = this.getSession();

		DataFileStatistic stat = this.dataFileStatisticRepository
				.getByDataFileAndRow(session, product.getDataFile(),
						product.getDataFileRow());

		stat.setValidationHost(this.configurationService.getApplicationUrl());
		session.update(stat);
	}

	@Override
	public void endValidation(UnvalidatedProduct product,
			ValidatorContext context) {
		Date now = new Date();
		Session session = this.getSession();

		DataFileStatistic stat = this.dataFileStatisticRepository
				.getByDataFileAndRow(session, product.getDataFile(),
						product.getDataFileRow());
		if (stat.getValidationStartTime() == null) {
			throw new RuntimeException("Validation start time should be set!");
		}
		stat.setValidationEndTime(now);
		stat.setHasValidationErrors(!context.isValid());
		session.update(stat);

		for (ValidatorError vError : context.getErrors()) {
			DataFileError error = this.dataFileErrorRepository.create(session,
					stat, vError);
			session.save(error);
		}
	}

	@Override
	public void validationHandleException(UnvalidatedProduct product,
			Throwable t) {
		Date now = new Date();
		Session session = this.getSession();

		DataFileStatistic stat = this.dataFileStatisticRepository
				.getByDataFileAndRow(session, product.getDataFile(),
						product.getDataFileRow());

		stat.setHasImportErrors(true);
		stat.setImagesEndTime(now);
		session.update(stat);

		DataFileError error = this.dataFileErrorRepository.create(session,
				stat, -1, t);
		session.save(error);
	}

	@Override
	public void startImages(UnvalidatedProduct product) {
		Date now = new Date();
		Session session = this.getSession();
		product = (UnvalidatedProduct) session.get(product.getClass(),
				product.getId());

		DataFileStatistic stat = this.dataFileStatisticRepository
				.getByDataFileAndRow(session, product.getDataFile(),
						product.getDataFileRow());

		stat.setImagesStartTime(now);
		stat.setNumImages(product.getImages().size());
		session.update(stat);
	}

	@Override
	public void processImages(UnvalidatedProduct product) {
		Session session = this.getSession();

		DataFileStatistic stat = this.dataFileStatisticRepository
				.getByDataFileAndRow(session, product.getDataFile(),
						product.getDataFileRow());

		stat.setImageHost(this.configurationService.getApplicationUrl());
		session.update(stat);
	}

	@Override
	public void imagesHandleError(UnvalidatedProduct product,
			UnvalidatedProductImage upi) {
		Session session = this.getSession();
		product = (UnvalidatedProduct) session.get(product.getClass(),
				product.getId());

		DataFileStatistic stat = this.dataFileStatisticRepository
				.getByDataFileAndRow(session, product.getDataFile(),
						product.getDataFileRow());

		stat.setNumImageErrors(stat.getNumImageErrors() + 1);
		session.update(stat);
	}

	@Override
	public void endImages(UnvalidatedProduct product) {
		Date now = new Date();
		Session session = this.getSession();

		DataFileStatistic stat = this.dataFileStatisticRepository
				.getByDataFileAndRow(session, product.getDataFile(),
						product.getDataFileRow());

		stat.setImagesEndTime(now);
		session.update(stat);
	}

	@Override
	public void startTransfer(UnvalidatedProduct product) {
		Date now = new Date();
		Session session = this.getSession();

		DataFileStatistic stat = this.dataFileStatisticRepository
				.getByDataFileAndRow(session, product.getDataFile(),
						product.getDataFileRow());

		stat.setTransferStartTime(now);
		session.update(stat);
	}

	@Override
	public void processTransfer(UnvalidatedProduct product) {
		Session session = this.getSession();

		DataFileStatistic stat = this.dataFileStatisticRepository
				.getByDataFileAndRow(session, product.getDataFile(),
						product.getDataFileRow());

		stat.setTransferHost(this.configurationService.getApplicationUrl());
		stat.setTransferProductDone(true);
		stat.setTransferProductImagesDone(true);
		stat.setTransferProductReviewsDone(true);
		stat.setNumTransferProductImages(product.getImages().size());
		stat.setNumTransferProductReviews(product.getProductReviews().size());
		session.update(stat);
	}

	@Override
	public void processTransferSlider(UnvalidatedProduct product) {
		Session session = this.getSession();

		DataFileStatistic stat = this.dataFileStatisticRepository
				.getByDataFileAndRow(session, product.getDataFile(),
						product.getDataFileRow());

		stat.setTransferSliderHost(this.configurationService
				.getApplicationUrl());
		stat.setTransferProductSlidersDone(true);
		stat.setNumTransferProductSliders(product.getSliders().size());
		session.update(stat);
	}

	@Override
	public void endTransfer(DataFile dataFile, int row) {
		Date now = new Date();
		Session session = this.getSession();
		dataFile = (DataFile) session
				.get(dataFile.getClass(), dataFile.getId());

		DataFileStatistic stat = this.dataFileStatisticRepository
				.getByDataFileAndRow(session, dataFile, row);

		stat.setTransferEndTime(now);
		session.update(stat);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dell.acs.managers.DataFileStatisticService#getByRetailerSite(com.
	 * dell.acs.persistence.domain.RetailerSite)
	 */
	@Override
	public Collection<DataFileStatistic> getByRetailerSite(RetailerSite rs, List<Long> skipStatsDataFileIds) {
		Session session = this.getSession();

		return this.dataFileStatisticRepository.getByRetailerSite(session, rs, skipStatsDataFileIds);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dell.acs.managers.DataFileStatisticService#getByRetailerSiteAndHost
	 * (com.dell.acs.persistence.domain.RetailerSite, java.lang.String)
	 */
	@Override
	public Collection<DataFileStatistic> getByRetailerSiteAndHost(
            RetailerSite rs, String host, List<Long> skipStatsDataFileIds) {
		Session session = this.getSession();

		return this.dataFileStatisticRepository.getByRetailerSiteAndHost(
				session, rs, host, skipStatsDataFileIds);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dell.acs.managers.DataFileStatisticService#downloadImageFailed(com
	 * .dell.acs.persistence.domain.UnvalidatedProduct,
	 * com.dell.acs.persistence.domain.UnvalidatedProductImage,
	 * com.dell.acs.dataimport.model.ValidatorContext)
	 */
	@Override
	public void downloadImageFailed(UnvalidatedProduct product,
			UnvalidatedProductImage productImage, ValidatorContext context) {
		Session session = this.getSession();
		DataFileStatistic stat = this.dataFileStatisticRepository
				.getByDataFileAndRow(session, product.getDataFile(),
						product.getDataFileRow());

		DataFileError error = this.dataFileErrorRepository.create(session,
				stat, -1, "Failed to download image %s",
				productImage.getSrcImageURL());
		session.save(error);
	}

	@Override
	public void transportHandleError(DataFile dataFile, int row, Throwable t,
			String msg) {
		Session session = this.getSession();
		DataFileStatistic stat = this.dataFileStatisticRepository
				.getByDataFileAndRow(session, dataFile, row);

		DataFileError error = this.dataFileErrorRepository.create(session,
				stat, -1, msg);
		session.save(error);
	}

    @Override
    public void updatingImport(DataFile dataFile, int row, boolean updating) {
        Session session = this.getSession();

        DataFileStatistic stat = this.dataFileStatisticRepository
                .getByDataFileAndRow(session, dataFile, row);

        stat.setUpdating(updating);

        session.save(stat);
    }

    @Override
    public Collection<DataFileStatisticSummary> getSummaryByRetailerSite(RetailerSite rs) {
        Session session = this.getSession();

        return this.dataFileStatisticSummaryRepository.getSummaryByRetailerSite(session, rs);
    }

    @Override
    public Long getErrorCountForStat(DataFileStatistic dataFileStat) {
        Session session = this.getSession();

        return this.dataFileErrorRepository.getErrorCountForStat(session, dataFileStat);
    }

    /**
	 * ConfigurationService bean injection.
	 */
	@Autowired
	private ConfigurationService configurationService;

	/**
	 * Setter for configurationService
	 */
	public void setConfigurationService(
			final ConfigurationService pConfigurationService) {
		this.configurationService = pConfigurationService;
	}

	/**
	 * DataFileStatisticRepository bean injection.
	 */
	@Autowired
	private DataFileStatisticRepository dataFileStatisticRepository;

	/**
	 * Setter for dataFileStatisticRepository
	 */
	public void setDataFileStatisticRepository(
			final DataFileStatisticRepository pDataFileStatisticRepository) {
		this.dataFileStatisticRepository = pDataFileStatisticRepository;
	}

    /**
     * DataFileStatisticSummaryRepository bean injection.
     */
    @Autowired
    private DataFileStatisticSummaryRepository dataFileStatisticSummaryRepository;

    /**
     * Setter for dataFileStatisticRepository
     */
    public void setDataFileStatisticSummaryRepository(
            final DataFileStatisticSummaryRepository pDataFileStatisticSummaryRepository) {
        this.dataFileStatisticSummaryRepository = pDataFileStatisticSummaryRepository;
    }

    /**
	 * DataFileErrorRepository bean injection.
	 */
	@Autowired
	private DataFileErrorRepository dataFileErrorRepository;

	/**
	 * Setter for dataFileErrorRepository
	 */
	public void setDataFileErrorRepository(
			final DataFileErrorRepository pDataFileErrorRepository) {
		this.dataFileErrorRepository = pDataFileErrorRepository;
	}

	/**
	 * DataFileRepository bean injection.
	 */
	@Autowired
	private DataFileRepository dataFileRepository;

	/**
	 * Setter for dataFileRepository
	 */
	public void setDataFileRepository(
			final DataFileRepository pDataFileRepository) {
		this.dataFileRepository = pDataFileRepository;
	}

	/**
	 * DataFileErrorRepository bean injection.
	 */
	@Autowired
	private UnvalidatedProductRepository unvalidatedProductRepository;

	/**
	 * Setter for unvalidatedProductRepository
	 */
	public void setUnvalidatedProductRepository(
			final UnvalidatedProductRepository pUnvalidatedProductRepository) {
		this.unvalidatedProductRepository = pUnvalidatedProductRepository;
	}

	private Session getSession() {
		return this.sessionFactory.getCurrentSession();
	}

	@Autowired
	@Qualifier("hibernateSessionFactory")
	private SessionFactory sessionFactory;

	public void setSessionFactory(final SessionFactory pSessionFactory) {
		this.sessionFactory = pSessionFactory;
	}
}
