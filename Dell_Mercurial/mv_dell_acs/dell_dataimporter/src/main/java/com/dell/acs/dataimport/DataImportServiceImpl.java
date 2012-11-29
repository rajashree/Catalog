/**
 * 
 */
package com.dell.acs.dataimport;

import com.dell.acs.dataimport.preprocessor.PreprocessorHandler;
import com.dell.acs.dataimport.transformers.DataImportColumnTransformer;
import com.dell.acs.dataimport.validators.DataImportValidator;
import com.dell.acs.persistence.domain.DataFile;
import com.dell.acs.persistence.domain.Product;
import com.sourcen.core.App;
import com.sourcen.core.config.ConfigurationService;
import com.sourcen.core.util.FileUtils;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * @author Shawn_Fisk
 * 
 */
public class DataImportServiceImpl implements DataImportService,
		ApplicationListener<ContextRefreshedEvent> {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	private static DataImportService instance;

	public static DataImportService getInstance() {
		if (instance == null) {
			instance = new DataImportServiceImpl();
		}
		return instance;
	}

	@SuppressWarnings("unused")
	private ConfigurationService configurationService;

	private DataImportData data;

	private ApplicationContext _applicationContext;

	/**
	 * Constructor
	 */
	private DataImportServiceImpl() {
		configurationService = App.getService(ConfigurationService.class);
		initialize();
	}

	@Override
	public <T extends DataImportHandler> T get(Class<T> type, DataFile dataFile, String source,
			Phases phase) {
		for (DataImportMapData map : this.data.getMaps()) {
			if (Phases.lookupText(map.phase) == phase) {
				if (map.getSource().equals(source)) {
					DataImportHandler handler = this._applicationContext
							.getBean(map.handler, DataImportHandler.class);
					handler.init(this, dataFile);
					return type.cast(handler);
				}
			}
		}

		return null;
	}

	@Override
	public List<DataImportColumnTransformer> getTransforms(Phases phase,
			String source) throws DataImportServiceException {
		List<DataImportColumnTransformer> result = new ArrayList<DataImportColumnTransformer>();
		ArrayList<DataImportTransformerData> transformerDatas = this.data
				.getTransformers();

		for (DataImportTransformerData transformerData : transformerDatas) {
			if ((Phases.lookupText(transformerData.phase) == phase)
					&& (transformerData.source.equals(source))) {
				try {
					Class<?> transformerType = Class
							.forName(transformerData.className);
					Constructor<?> cons = transformerType
							.getConstructor(DataImportService.class, String.class);
                    DataImportColumnTransformer transformer = (DataImportColumnTransformer) cons
                            .newInstance(this, transformerData.column);
					result.add(transformer);
				} catch (ClassNotFoundException e) {
					handleException(
							e,
							"Unable to get transformer of type %s, for phase %s",
							transformerData.className, phase);
				} catch (SecurityException e) {
					handleException(
							e,
							"Unable to get transformer of type %s, for phase %s",
							transformerData.className, phase);
				} catch (NoSuchMethodException e) {
					handleException(
							e,
							"Unable to get transformer of type %s, for phase %s",
							transformerData.className, phase);
				} catch (IllegalArgumentException e) {
					handleException(
							e,
							"Unable to get transformer of type %s, for phase %s",
							transformerData.className, phase);
				} catch (InstantiationException e) {
					handleException(
							e,
							"Unable to get transformer of type %s, for phase %s",
							transformerData.className, phase);
				} catch (IllegalAccessException e) {
					handleException(
							e,
							"Unable to get transformer of type %s, for phase %s",
							transformerData.className, phase);
				} catch (InvocationTargetException e) {
					handleException(
							e,
							"Unable to get transformer of type %s, for phase %s",
							transformerData.className, phase);
				}
			}
		}

		return result;
	}

	@Override
	public Object transform(String type, String value, Object defaultValue) {
		if (TRANSFORM_AVAILABILITY.equals(type)) {
			if (value == null) {
				return defaultValue;
			}
			for (DataImportAvailabilityData diad : this.data
					.getAvailabilities()) {
				if (diad.getText().equals(value)) {
					return Product.Availability.valueOf(diad.getValue()).getDbValue();
				}
			}
		}

		return null;
	}

	public static final class DataImportValidatorComparator implements
			Comparator<DataImportValidatorData> {
		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		@Override
		public int compare(DataImportValidatorData o1,
				DataImportValidatorData o2) {
			return o2.getOrder() - o1.getOrder();
		}
	}

	@Override
	public List<DataImportValidator> getValidators()
			throws DataImportServiceException {
		List<DataImportValidator> result = new ArrayList<DataImportValidator>();
		ArrayList<DataImportValidatorData> validatorDatas = this.data
				.getValidators();
		Collections.sort(validatorDatas, new DataImportValidatorComparator());

		for (DataImportValidatorData validatorData : validatorDatas) {
			try {
				Class<?> validatorType = Class
						.forName(validatorData.className);
				Constructor<?> cons = validatorType
						.getConstructor(DataImportService.class);

				result.add((DataImportValidator) cons.newInstance(this));
			} catch (ClassNotFoundException e) {
				handleException(
						e,
						"Unable to get validator of type %s",
						validatorData.className);
			} catch (SecurityException e) {
				handleException(
						e,
						"Unable to get validator of type %s",
						validatorData.className);
			} catch (NoSuchMethodException e) {
				handleException(
						e,
						"Unable to get validator of type %s",
						validatorData.className);
			} catch (IllegalArgumentException e) {
				handleException(
						e,
						"Unable to get validator of type %s",
						validatorData.className);
			} catch (InstantiationException e) {
				handleException(
						e,
						"Unable to get validator of type %s",
						validatorData.className);
			} catch (IllegalAccessException e) {
				handleException(
						e,
						"Unable to get validator of type %s",
						validatorData.className);
			} catch (InvocationTargetException e) {
				handleException(
						e,
						"Unable to get validator of type %s",
						validatorData.className);
			}
		}

		return result;
	}
	

	@Override
	public PreprocessorHandler getPreprocessorHandler(String provider, String retailerSite) {
		for (DataImportPreprocessorData pp : this.data.getPreprocessors()) {
			if (pp.provider.equals(provider)) {
                if (pp.retailerSite != null) {
                    if (pp.retailerSite.equals(retailerSite)) {
                        return this._applicationContext
                                .getBean(pp.beanName, PreprocessorHandler.class);
                    }
                }  else {
                    return this._applicationContext
                            .getBean(pp.beanName, PreprocessorHandler.class);
                }
			}
		}
		
		return null;
	}
	
	private void initialize() {
		try {
			if (DataImportServiceImpl.class.getResource("/dataimport.xml") == null) {
				logger.error("Unable to find dataimport.xml in classpath.");
				return;
			}

			final XStream xstream = new XStream(new DomDriver());
			xstream.setMode(XStream.NO_REFERENCES);
			xstream.alias("dataimport",
					DataImportServiceImpl.DataImportData.class);
			xstream.alias("map", DataImportMapData.class);
			xstream.aliasAttribute(DataImportMapData.class, "source", "source");
			xstream.aliasAttribute(DataImportMapData.class, "phase", "phase");
			xstream.aliasAttribute(DataImportMapData.class, "type", "type");
			xstream.aliasAttribute(DataImportMapData.class, "handler",
					"handler");

			xstream.alias("availability", DataImportAvailabilityData.class);
			xstream.aliasAttribute(DataImportAvailabilityData.class, "text",
					"text");
			xstream.aliasAttribute(DataImportAvailabilityData.class, "value",
					"value");

			xstream.alias("transformer", DataImportTransformerData.class);
			xstream.aliasAttribute(DataImportTransformerData.class, "column",
					"column");
			xstream.aliasAttribute(DataImportTransformerData.class,
					"className", "className");
			xstream.aliasAttribute(DataImportTransformerData.class, "phase",
					"phase");
			xstream.aliasAttribute(DataImportTransformerData.class, "source",
					"source");

			xstream.alias("validator", DataImportValidatorData.class);
			xstream.aliasAttribute(DataImportValidatorData.class, "order",
					"order");
			xstream.aliasAttribute(DataImportValidatorData.class, "className",
					"className");

			xstream.alias("preprocessor", DataImportPreprocessorData.class);
			xstream.aliasAttribute(DataImportPreprocessorData.class, "provider",
                    "provider");
            xstream.aliasAttribute(DataImportPreprocessorData.class, "retailerSite",
                    "retailerSite");
            xstream.aliasAttribute(DataImportPreprocessorData.class, "beanName",
					"beanName");

			this.data = (DataImportServiceImpl.DataImportData) xstream
					.fromXML(FileUtils.loadStream("/dataimport.xml"));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.context.ApplicationListener#onApplicationEvent(org
	 * .springframework.context.ApplicationEvent)
	 */
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		this._applicationContext = event.getApplicationContext();
	}

	public static final class DataImportData {

		private ArrayList<DataImportMapData> maps = new ArrayList<DataImportMapData>();
		private ArrayList<DataImportAvailabilityData> availabilities = new ArrayList<DataImportAvailabilityData>();
		private ArrayList<DataImportTransformerData> transformers = new ArrayList<DataImportTransformerData>();
		private ArrayList<DataImportValidatorData> validators = new ArrayList<DataImportValidatorData>();
		private ArrayList<DataImportPreprocessorData> preprocessors = new ArrayList<DataImportPreprocessorData>();

		private DataImportData() {
		}

		public Collection<DataImportMapData> getMaps() {
			return maps;
		}

		public void setMaps(ArrayList<DataImportMapData> maps) {
			this.maps = maps;
		}

		public ArrayList<DataImportAvailabilityData> getAvailabilities() {
			return availabilities;
		}

		public void setAvailabilities(
				ArrayList<DataImportAvailabilityData> availabilities) {
			this.availabilities = availabilities;
		}

		public ArrayList<DataImportTransformerData> getTransformers() {
			return transformers;
		}

		public void setTransformers(
				ArrayList<DataImportTransformerData> transforms) {
			this.transformers = transforms;
		}

		public ArrayList<DataImportValidatorData> getValidators() {
			return validators;
		}

		public void setValidators(ArrayList<DataImportValidatorData> validators) {
			this.validators = validators;
		}

		public ArrayList<DataImportPreprocessorData> getPreprocessors() {
			return preprocessors;
		}

		public void setPreprocessors(ArrayList<DataImportPreprocessorData> preprocessors) {
			this.preprocessors = preprocessors;
		}
	}

	public static final class DataImportMapData {

		private String source;
		private String phase;
		private String type;
		private String handler;

		private DataImportMapData() {
		}

		public String getSource() {
			return source;
		}

		public void setSource(String source) {
			this.source = source;
		}

		public String getPhase() {
			return phase;
		}

		public void setPhase(String phase) {
			this.phase = phase;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getHandler() {
			return handler;
		}

		public void setHandler(String handler) {
			this.handler = handler;
		}

		@Override
		public String toString() {
			return "DataImportMapData{" + ", source='" + source + '\''
					+ ", phase='" + phase + '\'' + ", type='" + type + '\''
					+ ", handler='" + handler + '\'' + '}';
		}

	}

	public static final class DataImportAvailabilityData {
		private String text;
		private String value;

		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}
	}

	public static final class DataImportTransformerData {
		private String column;
		private String className;
		private String phase;
		private String source;

		public String getColumn() {
			return column;
		}

		public void setColumn(String column) {
			this.column = column;
		}

		public String getClassName() {
			return className;
		}

		public void setClassName(String className) {
			this.className = className;
		}

		public String getPhase() {
			return phase;
		}

		public void setPhase(String phase) {
			this.phase = phase;
		}

		public String getSource() {
			return source;
		}

		public void setSource(String source) {
			this.source = source;
		}
	}

	public static final class DataImportValidatorData {
		private Integer order;
		private String className;

		public Integer getOrder() {
			return order;
		}

		public void setOrder(Integer order) {
			this.order = order;
		}

		public String getClassName() {
			return className;
		}

		public void setClassName(String className) {
			this.className = className;
		}
	}

	public static final class DataImportPreprocessorData {
		private String provider;
        private String retailerSite;
        private String beanName;

		public String getBeanName() {
			return beanName;
		}

		public void setBeanName(String beanName) {
			this.beanName = beanName;
		}

		public String getProvider() {
			return provider;
		}

		public void setProvider(String provider) {
			this.provider = provider;
		}

        public String getRetailerSite() {
            return retailerSite;
        }

        public void setRetailerSite(String retailerSite) {
            this.retailerSite = retailerSite;
        }
    }

	private void handleException(Throwable t, String msg, Object... args)
			throws DataImportServiceException {
		throw new DataImportServiceException(String.format(msg, args), t);

	}

	@Autowired
	@Qualifier("hibernateSessionFactory")
	private SessionFactory sessionFactory;

	public void setSessionFactory(final SessionFactory pSessionFactory) {
		this.sessionFactory = pSessionFactory;
	}
}
