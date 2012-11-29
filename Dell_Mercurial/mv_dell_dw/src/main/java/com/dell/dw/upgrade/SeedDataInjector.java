package com.dell.dw.upgrade;

import com.dell.dw.DWHConstants;
import com.dell.dw.managers.GAManager;
import com.dell.dw.persistence.domain.*;
import com.dell.dw.persistence.repository.*;
import com.sourcen.core.config.ConfigurationService;
import com.sourcen.core.upgrade.UpgradeTask;
import com.sourcen.core.util.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashSet;

/**
 * Created by IntelliJ IDEA.
 * User: Rajashree
 * Date: 6/12/12
 * Time: 2:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class SeedDataInjector implements UpgradeTask {

    /**
     * {@inheritDoc}
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        // ignored.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void run() {

        // first create user and authorize as admin.

        if (userRoleRepository.size() == 0) {
            userRoleRepository.insert(new UserRole("ROLE_ADMIN"));
            userRoleRepository.insert(new UserRole("ROLE_USER"));
        }
        User admin = null;
        if (userRepository.size() == 0) {

            User anonymousUser = new User();
            anonymousUser.setFirstName("anonymous");
            anonymousUser.setLastName("");
            anonymousUser.setEmail("anonymous@localhost.com");
            anonymousUser.setCreatedDate(new Date());
            anonymousUser.setModifiedDate(new Date());
            anonymousUser.setUsername("anonymous");
            anonymousUser.setPassword(StringUtils.MD5Hash("anonymous") + Math.random());
            anonymousUser.setEnabled(true);
            userRepository.insert(anonymousUser);

            admin = new User();
            admin.setFirstName("admin");
            admin.setLastName("");
            admin.setEmail("admin@localhost.com");
            admin.setCreatedDate(new Date());
            admin.setModifiedDate(new Date());
            admin.setUsername("admin");
            admin.setPassword(StringUtils.MD5Hash("admin"));
            admin.setEnabled(true);
            admin.setRoles(new HashSet<UserRole>(userRoleRepository.getAll()));
            userRepository.insert(admin);
        }



        /*
        INSERT INTO `data_source` (`id`,`version`,`importType`,`name`) VALUES
        (1,1,'api','ga'),
        (2,1,'csv','d3');
        (3,1,'csv','order');
        */
        dataSourceRepository.insert(new DataSource("api","ga"));
        dataSourceRepository.insert(new DataSource("csv","d3"));
        dataSourceRepository.insert(new DataSource("csv","sf_order"));
        dataSourceRepository.insert(new DataSource("csv","otg"));
        dataSourceRepository.insert(new DataSource("api","order"));

        /*
        INSERT INTO `campaign` (`id`,`version`,`name`) VALUES
         (246902,1,'Facebook Store to Dell.com'),
         (248839,1,'Direct Ads');
         */
        if(campaignRepository.get(246902L) == null) {
            campaignRepository.insert(new Campaign(246902L,"Facebook Store to Dell.com"));
        }
        if(campaignRepository.get(248839L) == null) {
            campaignRepository.insert(new Campaign(248839L,"Direct Ads"));
        }

        /*
        INSERT INTO `retailers` (`id`,`version`,`description`,`name`) VALUES
        (1,1,'Dell','dell');
        */
        Retailer retailer = gaManager.createRetailer(new Retailer("Dell","1",configurationService.getProperty("app.retailer.name","dell")));

        sysMonEndPointTypeRepository.insert(new SysMonEndPointType(DWHConstants.EndpointType.CS.getValue(),"CS"));
        sysMonEndPointTypeRepository.insert(new SysMonEndPointType(DWHConstants.EndpointType.EXTERNAL.getValue(),"External"));

        /*
        INSERT INTO `ga_account` (`id`,`version`,`name`,`retailer_id`) VALUES
        (30933868,1,'Pixelfix Games',1),
        (31487503,1,'MarketvineDev',1),
        (31845868,1,'Dell SMB',1),
        (31851177,1,'Dell Consumer',1);
        */
        /*INSERT INTO `ga_webproperty` (`id`,`version`,`active`,`propertyName`,`gaAccount_id`) VALUES
         ('UA-30933868-2',1,1,'Pixelfix Games',30933868),
         ('UA-31487503-1',1,1,'Marketvine Dev',31487503),
         ('UA-31487503-2',1,1,'DellConsumer',31487503),
         ('UA-31845868-1',1,1,'OTG Whitepapers',31845868),
         ('UA-31845868-3',1,1,'Hot Deals',31845868),
         ('UA-31851177-1',1,1,'Reviews Ad',31851177);
        */
        /*INSERT INTO `ga_webproperty_profile` (`id`,`version`,`initializationDate`,`lastDownloadedDate`,`lockedThread`,`name`,`status`,`gaWebProperty_id`) VALUES
         (58807163,3645,'2012-04-01 00:00:00','2012-04-01 00:00:00','1338967483875-19','Pixelfix Games',2,'UA-30933868-2'),
         (59562655,3623,'2012-04-01 00:00:00','2012-04-01 00:00:008','1338967483875-19','DellConsumer',2,'UA-31487503-2'),
         (59966887,3623,'2012-04-01 00:00:00','2012-04-01 00:00:00','1338967483875-19','OTG Whitepapers',2,'UA-31845868-1'),
         (59973090,3613,'2012-04-01 00:00:00','2012-04-01 00:00:00','1338967483875-19','Reviews Ad',2,'UA-31851177-1'),
         (60700927,57,'2012-04-01 00:00:00','2012-04-01 00:00:00','1338967343328-19','Hot Deals',2,'UA-31845868-3');
        */
        /*GAAccount ga1 = gaManager.createGAAccount(new GAAccount(30933868L,"Pixelfix Games",retailer));
        GAWebProperty gaProperty1 = gaManager.createGAWebProperty(new GAWebProperty("UA-30933868-2",ga1,"Pixelfix Games",true));
        gaManager.createGAWebPropertyProfile(new GAWebPropertyProfile(58807163L,gaProperty1,"Pixelfix Games", DateUtils.getDate("2012-04-01 00:00:00"),DateUtils.getDate("2012-04-01 00:00:00"),2));

        GAAccount ga2 = gaManager.createGAAccount(new GAAccount(31487503L,"MarketvineDev",retailer));
        GAWebProperty gaProperty2 = gaManager.createGAWebProperty(new GAWebProperty("UA-31487503-2",ga2,"DellConsumer",true));
        gaManager.createGAWebPropertyProfile(new GAWebPropertyProfile(59562655L,gaProperty2,"DellConsumers", DateUtils.getDate("2012-04-01 00:00:00"),DateUtils.getDate("2012-04-01 00:00:00"),2));

        GAAccount ga3 = gaManager.createGAAccount(new GAAccount(31845868L,"Dell SMB",retailer));
        GAWebProperty gaProperty3a = gaManager.createGAWebProperty(new GAWebProperty("UA-31845868-1",ga3,"OTG Whitepapers",true));
        gaManager.createGAWebPropertyProfile(new GAWebPropertyProfile(59966887L,gaProperty3a,"OTG Whitepapers", DateUtils.getDate("2012-04-01 00:00:00"),DateUtils.getDate("2012-04-01 00:00:00"),2));
        GAWebProperty gaProperty3b =gaManager.createGAWebProperty(new GAWebProperty("UA-31845868-3",ga3,"Hot Deals",true));
        gaManager.createGAWebPropertyProfile(new GAWebPropertyProfile(60700927L,gaProperty3b,"Hot Deals", DateUtils.getDate("2012-04-01 00:00:00"),DateUtils.getDate("2012-04-01 00:00:00"),2));


        GAAccount ga4 = gaManager.createGAAccount(new GAAccount(31851177L,"Dell Consumer",retailer));
        GAWebProperty gaProperty4 = gaManager.createGAWebProperty(new GAWebProperty("UA-31851177-1",ga4,"Reviews Ad",true));
        gaManager.createGAWebPropertyProfile(new GAWebPropertyProfile(59973090L,gaProperty4,"Reviews Ad", DateUtils.getDate("2012-04-01 00:00:00"),DateUtils.getDate("2012-04-01 00:00:00"),2));*/
    }

    @Autowired
    GAManager gaManager;

    public GAManager getGaManager() {
        return gaManager;
    }

    public void setGaManager(GAManager gaManager) {
        this.gaManager = gaManager;
    }

    @Autowired
    ConfigurationService configurationService;

    public ConfigurationService getConfigurationService() {
        return configurationService;
    }

    public void setConfigurationService(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    @Autowired
    DataSourceRepository dataSourceRepository;

    public DataSourceRepository getDataSourceRepository() {
        return dataSourceRepository;
    }

    public void setDataSourceRepository(DataSourceRepository dataSourceRepository) {
        this.dataSourceRepository = dataSourceRepository;
    }

    @Autowired
    CampaignRepository campaignRepository;

    public CampaignRepository getCampaignRepository() {
        return campaignRepository;
    }

    public void setCampaignRepository(CampaignRepository campaignRepository) {
        this.campaignRepository = campaignRepository;
    }

    @Autowired
    RetailerRepository retailerRepository;

    public RetailerRepository getRetailerRepository() {
        return retailerRepository;
    }

    public void setRetailerRepository(RetailerRepository retailerRepository) {
        this.retailerRepository = retailerRepository;
    }

    @Autowired
    GAAccountRepository gaAccountRepository;

    public GAAccountRepository getGaAccountRepository() {
        return gaAccountRepository;
    }

    public void setGaAccountRepository(GAAccountRepository gaAccountRepository) {
        this.gaAccountRepository = gaAccountRepository;
    }

    @Autowired
    GAWebPropertyRepository gaWebPropertyRepository;

    public GAWebPropertyRepository getGaWebPropertyRepository() {
        return gaWebPropertyRepository;
    }

    public void setGaWebPropertyRepository(GAWebPropertyRepository gaWebPropertyRepository) {
        this.gaWebPropertyRepository = gaWebPropertyRepository;
    }



    @Autowired
    GAWebPropertyProfileRepository gaWebPropertyProfileRepository;

    public GAWebPropertyProfileRepository getGaWebPropertyProfileRepository() {
        return gaWebPropertyProfileRepository;
    }

    public void setGaWebPropertyProfileRepository(GAWebPropertyProfileRepository gaWebPropertyProfileRepository) {
        this.gaWebPropertyProfileRepository = gaWebPropertyProfileRepository;
    }

    @Autowired
    private UserRepository userRepository;

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    private UserRoleRepository userRoleRepository;

    public UserRoleRepository getUserRoleRepository() {
        return userRoleRepository;
    }

    public void setUserRoleRepository(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    @Autowired
    private SysMonEndPointRepository sysMonEndPointRepository;

    public SysMonEndPointRepository getSysMonEndPointRepository() {
        return sysMonEndPointRepository;
    }

    public void setSysMonEndPointRepository(SysMonEndPointRepository sysMonEndPointRepository) {
        this.sysMonEndPointRepository = sysMonEndPointRepository;
    }

    @Autowired
    private SysMonEndPointTypeRepository sysMonEndPointTypeRepository;

    public SysMonEndPointTypeRepository getSysMonEndPointTypeRepository() {
        return sysMonEndPointTypeRepository;
    }

    public void setSysMonEndPointTypeRepository(SysMonEndPointTypeRepository sysMonEndPointTypeRepository) {
        this.sysMonEndPointTypeRepository = sysMonEndPointTypeRepository;
    }
}
