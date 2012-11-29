package com.dell.dw.managers;

import com.dell.dw.persistence.domain.GAAccount;
import com.dell.dw.persistence.domain.GAWebProperty;
import com.dell.dw.persistence.domain.GAWebPropertyProfile;
import com.dell.dw.persistence.domain.Retailer;
import com.dell.dw.web.controller.formbeans.AppHealthBean;
import com.dell.dw.web.controller.formbeans.GAWebPropertyBean;
import com.sourcen.core.managers.Manager;

import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Devashree
 * Date: 6/13/12
 * Time: 1:24 PM
 * To change this template use File | Settings | File Templates.
 */
public interface GAManager  extends Manager {

    GAAccount createGAAccount(GAAccount gaAccount);

    GAWebProperty createGAWebProperty(GAWebProperty gaWebProperty);

    GAWebPropertyProfile createGAWebPropertyProfile(GAWebPropertyProfile gaWebPropertyProfile);

    Retailer createRetailer(Retailer retailer);

    GAWebPropertyProfile getGAWebPropertyProfile(Long webPropertyProfileId, String webPropertyId, Long accountId);

    GAWebPropertyProfile getGAWebPropertyProfile(Long webPropertyProfileId);

    List<GAWebPropertyProfile> getAllWebPropertyProfiles();

    void updateWebPropertyStatus(String webPropertyId, boolean active);

    void synchronize(GAWebPropertyBean webProperty);

    List<GAAccount> getGAAccounts();

    void updateEmail(Long accountId, String email);

    List<GAWebProperty> getAllWebProperties();

    void updateInitializationDate(Long profileId, Date newDate, boolean delPreviouData);

    void removeEventsTillDate(Long profileId, Date date);

    void removePageViewsTillDate(Long profileId, Date date);

    void removeDataSchedulerBatchesTillDate(Long profileId, Date newDate, Date previousDate);

    List<AppHealthBean> getAppHealthDetails();
}
