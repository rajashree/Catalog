/**
 * 
 */
package com.dell.acs.persistence.repository.impl.hibernate;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.dell.acs.dataimport.model.DataImportError;
import com.dell.acs.dataimport.model.ValidatorError;
import com.dell.acs.persistence.domain.DataFileError;
import com.dell.acs.persistence.domain.DataFileStatistic;
import com.dell.acs.persistence.repository.DataFileErrorRepository;
import com.sourcen.core.persistence.repository.impl.hibernate.IdentifiableEntityRepositoryImpl;

/**
 * @author Shawn_Fisk
 * 
 */
@Repository
public class DataFileErrorRepositoryImpl extends
		IdentifiableEntityRepositoryImpl<Long, DataFileError> implements
		DataFileErrorRepository {
	/**
	 * Constructor
	 */
	public DataFileErrorRepositoryImpl() {
		super(DataFileError.class);
	}

	@Override
	public DataFileError create(Session session, DataFileStatistic stat,
			int column, Throwable t) {
		return new DataFileError(stat, column, this.dumpException(t));
	}

	@Override
	public DataFileError create(Session session, DataFileStatistic stat,
			DataImportError error) {
		StringBuilder msg = new StringBuilder();
		msg.append("<SERVERITY>");
		msg.append(error.getServerity().name());
		msg.append("</SERVERITY>");
		msg.append("<MESSAGE>");
		msg.append(error.getMsg());
		msg.append("</MESSAGE>");
		Throwable t = error.getThrowable();
		if (t != null) {
			msg.append(this.dumpException(t));
		}
		return new DataFileError(stat, error.getColumn(), msg.toString());
	}

	@Override
	public DataFileError create(Session session, DataFileStatistic stat,
			ValidatorError error) {
		StringBuilder msg = new StringBuilder();
		msg.append("<SERVERITY>");
		msg.append(error.getServerity().name());
		msg.append("</SERVERITY>");
		msg.append("<MESSAGE>");
		msg.append(error.getMsg());
		msg.append("</MESSAGE>");
		Throwable t = error.getThrowable();
		if (t != null) {
			msg.append(this.dumpException(t));
		}
		return new DataFileError(stat, -1, msg.toString());
	}

	private String dumpException(Throwable exception) {
		StringBuilder sb = new StringBuilder();
		sb.append("<STACK>");
		Throwable root = exception;
		sb.append("exception: ");
		sb.append(exception.getMessage());
		sb.append(";");

		while (root != null) {
			StackTraceElement[] stes = root.getStackTrace();

			for (StackTraceElement ste : stes) {
				sb.append(ste.getClassName());
				sb.append(":");
				sb.append(ste.getMethodName());
				sb.append(":");
				String fileName = ste.getFileName();
				sb.append(fileName != null ? fileName : "N/A");
				sb.append(":");
				int lineNumber = ste.getLineNumber();
				sb.append(lineNumber > -1 ? "" + lineNumber : "N/A");
				sb.append(";");
			}

			root = root.getCause();

			if (root != null) {
				sb.append("cause by: ");
				sb.append(exception.getMessage());
				sb.append(";");
			}
		}
		sb.append("</STACK>");

		return sb.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dell.acs.persistence.repository.DataFileErrorRepository#create(org
	 * .hibernate.Session, com.dell.acs.persistence.domain.DataFileStatistic,
	 * int, java.lang.String, java.lang.Object[])
	 */
	@Override
	public DataFileError create(Session session, DataFileStatistic stat,
			int column, String msg, Object... args) {
		return new DataFileError(stat, column, String.format(msg, args));
	}

    @Override
    public Long getErrorCountForStat(Session session, DataFileStatistic dataFileStat) {
        Criteria dataFileErrorCriteria = session.createCriteria(DataFileError.class);
        dataFileErrorCriteria.add(Restrictions.eq("dataFileStat_id", dataFileStat.getId()));
        dataFileErrorCriteria.setProjection(Projections.rowCount());

        return (Long)dataFileErrorCriteria.uniqueResult();
    }
}
