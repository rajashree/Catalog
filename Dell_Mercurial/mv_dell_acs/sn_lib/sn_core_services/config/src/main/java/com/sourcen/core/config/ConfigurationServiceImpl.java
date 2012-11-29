/*
 * Copyright(C) 2011, Sourcen Framework and the original authors.
 * All Rights Reserved. Use is subject to license terms and conditions.
 */

package com.sourcen.core.config;

import com.sourcen.core.config.providers.ApplicationPropertiesProvider;
import com.sourcen.core.config.providers.EnvironmentPropertiesProvider;
import com.sourcen.core.config.providers.FileSystemStartupPropertiesProvider;
import com.sourcen.core.config.providers.SystemPropertiesProvider;
import com.sourcen.core.event.EventDispatcher;
import com.sourcen.core.services.ServiceProviderRegistrar;
import com.sourcen.core.util.FileSystem;
import com.sourcen.core.util.FileUtils;
import com.sourcen.core.util.collections.AbstractPropertiesProvider;
import com.sourcen.core.util.collections.PropertiesProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: sandeep $
 * @version $Revision: 3676 $, $Date:: 2012-06-27 09:41:34#$
 */
@Service
public class ConfigurationServiceImpl extends AbstractPropertiesProvider implements ConfigurationService {

    // just load SystemPropertiesProvider in all spaces.
    private static ConfigurationServiceImpl instance;
    private ServiceProviderRegistrar<PropertiesProvider> registrar = new ServiceProviderRegistrar<PropertiesProvider>();
    
    private static final Logger logger = LoggerFactory.getLogger(ConfigurationServiceImpl.class);


    public static ConfigurationService getInstance() {
        if (instance == null) {
            instance = new ConfigurationServiceImpl();
            // this order is based on initialization, we always want system properties to load first,
            // the application, the environment and lastly profile, But the priority varies.
            // we are not adding a sync lock here as the property providers internally use ConfigurationService :)
//
//            // NOTE : all indexes are 1 as the ArrayList will shift the elements by 1 index.
//            // but in the order of least to highest.
            instance.registrar.addProvider(1, FileSystemStartupPropertiesProvider.getInstance());
            // add the providers in the order of priority
//            EventDispatcher.dispatchAsynchronous(new ConfigurationServiceEvent(ConfigurationServiceEvent.PROPERTIES_LOADED));
        }
        return instance;
    }
    
    // SFISK - Return point to mock configuration.
    public static void setInstance(ConfigurationServiceImpl cs) {
    	instance = cs;
    }

    private ConfigurationServiceImpl() {
        // only include property providers that dont have dependencies on ConfigurationService itself.
        registrar.addProvider(0, SystemPropertiesProvider.getInstance());
        registrar.addProvider(1, ApplicationPropertiesProvider.getInstance());
        registrar.addProvider(1, EnvironmentPropertiesProvider.getInstance());
    }


    @Override
    public boolean hasProperty(String key) throws NullPointerException {
        for (PropertiesProvider provider : registrar.getProviders()) {
            if (provider.hasProperty(key)) return true;
        }
        return false;
    }

    @Override
    public Set<String> keySet() {
        Set<String> combinedSet = new HashSet<String>();
        for (PropertiesProvider provider : registrar.getProviders()) {
            combinedSet.addAll(provider.keySet());
        }
        return combinedSet;
    }

    @Override
    public Object getObjectProperty(String key) {

        for (PropertiesProvider provider : registrar.getProviders()) {
            if (provider.hasProperty(key)) {
                return provider.getObjectProperty(key);
            }
        }
        return null;
    }

    @Override
    public void setProperty(String key, String value) {
        for (PropertiesProvider provider : registrar.getProviders()) {
            if (provider.supportsPersistence()) {
                provider.setProperty(key, value);
            }
        }

        // dispatch events.
        if (value == null) {
            final ConfigurationServiceEvent event = new ConfigurationServiceEvent(ConfigurationServiceEvent.PROPERTY_DELETED);
            event.setPropertyName(key);
            event.setPropertyValue(value);
            EventDispatcher.dispatchAsynchronous(event);
        } else {
            final ConfigurationServiceEvent event = new ConfigurationServiceEvent(ConfigurationServiceEvent.PROPERTY_UPDATED);
            event.setPropertyName(key);
            event.setPropertyValue(value);
            EventDispatcher.dispatchAsynchronous(event);
        }

    }


    // FILESYSTEM
    private ReentrantLock fileSystemLock = new ReentrantLock(true);
    private FileSystem fileSystem;

    public FileSystem getFileSystem() throws IOException {
        if (fileSystem == null) {
            fileSystemLock.lock();
            fileSystem = new FileSystem(getProperty("filesystem", "/app_work_dir"));
            if (isDevMode() && getBooleanProperty("app.devMode.filesystem.flushOnStartup", false)) {
                logger.info("flushing fileSystem empty :=" + fileSystem.getFileSystemAsString());
                File fileSystemFile = fileSystem.getFileSystem();
                File[] files = fileSystemFile.listFiles();
                for (File file : files) {
                    if (file.isFile() && !file.delete()) {
                        logger.warn("Unable to delete file :=" + file.getPath());
                    }
                    if (file.isDirectory()) {
                        try {
                            FileUtils.deleteDirectory(file);
                        } catch (IOException e) {
                            logger.error("Unable to delete directory :=" + file.getPath());
                        }
                    }
                }
            }
            // TODO - update so that it can copy the filesystem out of the JAR file.
            try {
                URL srcUrl = FileUtils.loadResource("/filesystem");
                if (srcUrl != null) {
                    if (ResourceUtils.isJarURL(srcUrl)) {
                        logger.warn("cannot copy /filesystem from JAR file.");
                    } else {
                        File sourceDir = new File(srcUrl.toURI());
                        File destinationFileSystem = fileSystem.getFileSystem();
                        copyFiles(sourceDir, destinationFileSystem, getBooleanProperty("filesystem.overwriteExistingFiles",true));
                    }
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
            FileSystem.setDefaultFileSystem(fileSystem);
            fileSystemLock.unlock();
        }
        return fileSystem;
    }

    private void copyFiles(File source, File destination, boolean overwriteExistingFiles) throws Exception {
        File[] sourceFiles = source.listFiles();
        for (File src : sourceFiles) {
            File destinationFile = new File(destination, src.getName());

            if (src.isDirectory()) {
                if (destinationFile.exists()) {
                    if (destinationFile.isDirectory()) {
                        copyFiles(src, destinationFile,overwriteExistingFiles);
                    }
                } else {
                    destinationFile.mkdir();
                    copyFiles(src, destinationFile,overwriteExistingFiles);
                }
            } else {

                if(destinationFile.exists() && overwriteExistingFiles){
                    logger.info("overwriting existing file :=" + destinationFile);
                    if(destinationFile.delete()){
                        logger.error("unable to delete file :=" + destinationFile);
                    }
                }

                if (!destinationFile.exists()) {
                    if(destinationFile.createNewFile()){
                        FileUtils.copyFile(src, destinationFile);
                    }else{
                        logger.error("unable to create new file :="+destinationFile);
                    }
                }else{
                    logger.info("destination file already exists, skipping copy :=" + destinationFile);
                }
            }
        }
    }


    //
    // Service provider.
    //

    @Override
    public void addProvider(Integer priority, PropertiesProvider provider) {
        registrar.addProvider(priority, provider);
        EventDispatcher.dispatchAsynchronous(new ConfigurationServiceEvent(ConfigurationServiceEvent.PROPERTIES_LOADED));
    }

    @Override
    public void removeProvider(PropertiesProvider provider) {
        registrar.removeProvider(provider);
    }

    @Override
    public void setProviders(Collection<PropertiesProvider> providers) {
        registrar.setProviders(providers);
    }

    @Override
    public Collection getProviders() {
        return registrar.getProviders();
    }

    //
    // helper properties
    //

    @Override
    public String getCharacterEncoding() {
        return getInstance().getProperty(Constants.CHARACTER_ENCODING, "UTF-8");
    }

    @Override
    public Boolean isDevMode() {
        return getInstance().getBooleanProperty(Constants.DEV_MODE, false);
    }

    @Override
    public void setProvider(PropertiesProvider provider) {
        throw new UnsupportedOperationException();
    }

    //
    // Service methods.
    //

    @Override
    public String getId() {
        return ServiceIdGenerator.get(getClass());
    }

    @Override
    public void refresh() {
        registrar.refresh();
    }

    @Override
    public void initialize() {
        registrar.initialize();
    }

    @Override
    public void destroy() {
        registrar.destroy();
    }

	@Override
	public String getApplicationUrl() {
		if (isCatalinaRunning()) {
			return new CatalinaInfoSource().getApplicationUrl();
		} else {
			return "unknown";
		}
	}
	
	private boolean isCatalinaRunning() {
		boolean running = false;
		
		try {
			Class.forName("org.apache.catalina.manager.host.HostManagerServlet");
			running = true;
		} catch (ClassNotFoundException e) {
			// Ignoring, should not fail if running on catalina.
		}
		
		return running;
	}
}
