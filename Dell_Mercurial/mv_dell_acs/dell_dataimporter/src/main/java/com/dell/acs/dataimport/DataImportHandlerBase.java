/**
 * 
 */
package com.dell.acs.dataimport;

import com.dell.acs.dataimport.DataImportService.Phases;
import com.dell.acs.dataimport.model.DataImportContext;
import com.dell.acs.dataimport.model.DataImportError;
import com.dell.acs.dataimport.model.Row;
import com.dell.acs.dataimport.transformers.DataImportColumnTransformer;
import com.dell.acs.persistence.domain.DataFile;
import com.dell.acs.persistence.domain.RetailerSite;
import com.dell.acs.persistence.domain.User;
import com.sourcen.core.config.ConfigurationService;
import com.sourcen.core.persistence.domain.PropertiesAwareEntity;
import com.sourcen.core.persistence.domain.impl.jpa.EntityModel;
import com.sourcen.core.persistence.repository.Repository;
import com.sourcen.core.util.collections.PropertiesProvider;
import org.apache.commons.lang.StringEscapeUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.persistence.Column;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * @author Shawn_Fisk
 * 
 */
@SuppressWarnings("unused")
public abstract class DataImportHandlerBase implements DataImportHandler {
	private DataImportContext _context;
    private String[] _header;
	private DataFile _dataFile;
	private int _currentRow;
	private int _currentColumn;
	private Logger _dataFileLogger;

	/**
	 * 
	 */
	public DataImportHandlerBase() {
	}

	public void afterProperties() {
		String loggerName = "com.dell.acs.dataimport.dataFile.logs."
				+ this._dataFile.getFilePath().replace("/feeds", "");
		this._dataFileLogger = LoggerFactory.getLogger(loggerName);
		_context = new DataImportContext(this._dataFile);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dell.acs.dataimport.DataImportHandler#setCurrentRow(int)
	 */
	@Override
	public void setCurrentRow(int row) {
		this._currentRow = row;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dell.acs.dataimport.DataImportHandler#setCurrentColumn(int)
	 */
	@Override
	public void setCurrentColumn(int Column) {
		this._currentColumn = Column;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dell.acs.dataimport.DataImportHandler#hasErrors()
	 */
	@Override
	public boolean hasErrors() {
		return this._context.hasErrors();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dell.acs.dataimport.DataImportHandler#getErrors()
	 */
	@Override
	public Iterable<DataImportError> getErrors() {
		return this._context.getErrors();
	}

    @Override
    public void setHeader(String[] header) {
        this._header = header;
    }

    @Override
    public void handleException(String msg, Object... args) {
        this.handleException(null, msg, args);
    }

    @Override
	public void handleException(Throwable t, String msg, Object... args) {
		this._context.handleException(t, this._currentRow, this._currentColumn,
				msg, args);

		_dataFileLogger.error(
				String.format("row:(%d), column:(%s), %s", this._currentRow,
					    this.getColumnName(this._currentColumn), String.format(msg, args)), t);
	}

    private String getColumnName(int colIdx) {
        return String.format("%d(%s,%s)", colIdx, this._header[colIdx], this.getSpreadSheetColumnName(colIdx));
    }

    private String getSpreadSheetColumnName(int colIdx) {
        StringBuilder sb = new StringBuilder();

        int firstChar = colIdx / 26;
        int secondChar = colIdx % 26;

        if (firstChar != 0) {
            sb.append((char)('A' + firstChar-1));
        }
        sb.append((char)('A' + secondChar));

        return sb.toString();
    }

	/**
	 * ConfigurationService bean injection.
	 */
	@Autowired
	protected ConfigurationService configurationService;

	public void setConfigurationService(
			final ConfigurationService configurationService) {
		this.configurationService = configurationService;
	}

	protected DataImportService _dataImportService;

    protected abstract Repository getRepository();

	@Override
	public void saveOrUpdate(EntityModel entity, Row row) {
		Session session = this.getSession();

		if (entity == null) {
			entity = this.createEntity(row);
		} else {
			session.refresh(entity);
		}

		this.transform(entity, row);

		this.transfer(entity, row);

		this.handleProperties(entity, row);

		this.precommit(entity);

        getRepository().put(entity);
	}

	public List<DataImportColumnTransformer> getTransformers(Phases phase,
			String sourceTable) {
		try {
			return this._dataImportService.getTransforms(phase, sourceTable);
		} catch (DataImportServiceException e) {
			// TODO Auto-generated catch block
			handleException(e,
					"Unable to get transforms for phase:%s, source:%s", phase,
					sourceTable);
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dell.acs.dataimport.DataImportHandler#init(com.dell.acs.dataimport
	 * .DataImportServiceImpl, com.dell.acs.persistence.domain.DataFile)
	 */
	@Override
	public void init(DataImportServiceImpl dataImportServiceImpl,
			DataFile dataFile) {
		this._dataImportService = dataImportServiceImpl;
		this._dataFile = dataFile;
		this.afterProperties();
		this.afterProperties();
	}

	/**
	 * Called to create the entity.
	 * 
	 * @param row
	 *            the row information to used to create the entity.
	 * 
	 * @return the newly created entity.
	 */
	protected abstract EntityModel createEntity(Row row);

	/**
	 * Called to transform any the values in the entity.
	 * 
	 * @param entity
	 *            the entity.
	 * @param row
	 *            the row containing values that need to transform.
	 */
	protected void transform(EntityModel entity, Row row) {
	}

	/**
	 * Called to transfer all the values from the row to the entity.
	 * 
	 * @param entity
	 *            the entity
	 * @param row
	 *            the row to full the entity.
	 */
	protected void transfer(EntityModel entity, Row row) {
		for (Map.Entry<String, Object> entry : row) {
			String key = entry.getKey();
			Object value = entry.getValue();

			if (value instanceof EntityModel) {
				this.getSession().refresh(value);
			}

			set(entity, key, value);
		}
	}

	/**
	 * Called to handle the properties if the entity implement PropertyAware.
	 * 
	 * @param entity
	 *            the entity to set the properties.
	 * @param row
	 *            the row containing the properties.
	 */
	protected void handleProperties(EntityModel entity, Row row) {
		if (entity instanceof PropertiesAwareEntity) {
			@SuppressWarnings("unchecked")
			Map<String, Object> properties = (Map<String, Object>) row
					.get("_properties");
			PropertiesProvider pa = this.get(PropertiesProvider.class, entity,
					"properties");
			for (Map.Entry<String, Object> propertyEntry : properties
					.entrySet()) {
				pa.setProperty(propertyEntry.getKey(), propertyEntry.getValue());
			}
		}
	}

	/**
	 * Called to just before the commit of the entity to make last minute
	 * changes.
	 * 
	 * @param entity
	 *            the entity.
	 */
	protected void precommit(EntityModel entity) {
	}

	protected <T> T get(Class<T> expectedType, EntityModel entity, String field) {
		Class<?> type = entity.getClass();
		try {
			Field fieldDef = type.getDeclaredField(field);
			Class<?> fieldType = fieldDef.getType();

			Method method = entity.getClass()
					.getMethod(getterMethodName(field));

			return expectedType.cast(method.invoke(entity));
		} catch (SecurityException e) {
			// Ignore
		} catch (NoSuchFieldException e) {
			// Ignore
		} catch (NoSuchMethodException e) {
			// Ignore
		} catch (IllegalArgumentException e) {
			// Ignore
		} catch (IllegalAccessException e) {
			// Ignore
		} catch (InvocationTargetException e) {
			// Ignore
		}

		return null;
	}

	protected String getterMethodName(String field) {
		return "get" + Character.toUpperCase(field.charAt(0))
				+ field.substring(1);
	}

	protected void set(EntityModel entity, String field, Object value) {
		Class<?> type = entity.getClass();
		try {
			Field fieldDef = type.getDeclaredField(field);
			Column columnInfo = fieldDef.getAnnotation(Column.class);
			Class<?> fieldType = fieldDef.getType();

			Method method = entity.getClass().getMethod(
					setterMethodName(field), fieldType);

			if (fieldType == String.class) {
				if (value != null) {
					int strLength = ((String) value).length();
					int maxLength = columnInfo != null ? columnInfo.length()
							: 255;
					maxLength = (maxLength >= 4000) ? Integer.MAX_VALUE
							: maxLength;

					if (strLength > maxLength) {
						throw new RuntimeException(String.format(
								"The value for field %s is too long, %d > %d",
								field, strLength, maxLength));
					}
				}
			}
			method.invoke(entity, value);
		} catch (SecurityException e) {
			// Ignore
		} catch (NoSuchFieldException e) {
			// Ignore
		} catch (NoSuchMethodException e) {
			// Ignore
		} catch (IllegalArgumentException e) {
			// Ignore
		} catch (IllegalAccessException e) {
			// Ignore
		} catch (InvocationTargetException e) {
			// Ignore
		}
	}

	protected String setterMethodName(String field) {
		return "set" + Character.toUpperCase(field.charAt(0))
				+ field.substring(1);
	}

	protected Object unEscapeUrl(Object object) {
		if (object != null) {
			return StringEscapeUtils.unescapeHtml(object.toString().trim());
		}
		return object;
	}

	protected RetailerSite getRetailerSite(String siteName) {
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(RetailerSite.class);
		criteria.add(Restrictions.eq("siteName", siteName));
		criteria.setMaxResults(1);

		return (RetailerSite) criteria.list().iterator().next();
	}

	protected User getUser(String username) {
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(User.class);
		criteria.add(Restrictions.eq("username", username));
		criteria.setMaxResults(1);

		return (User) criteria.list().iterator().next();
	}

	public void setDataImportService(DataImportService dataImportService) {
		this._dataImportService = dataImportService;
	}

	public void setDataFile(DataFile dataFile) {
		this._dataFile = dataFile;
	}

	protected Session getSession() {
		return this.sessionFactory.getCurrentSession();
	}

	@Autowired
	@Qualifier("hibernateSessionFactory")
	private SessionFactory sessionFactory;

	public void setSessionFactory(final SessionFactory pSessionFactory) {
		this.sessionFactory = pSessionFactory;
	}
}