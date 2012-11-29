package com.dell.acs.managers;

import com.dell.acs.DellTestCase;
import com.dell.acs.UserNotFoundException;
import com.dell.acs.persistence.domain.Event;
import com.dell.acs.persistence.domain.Retailer;
import com.dell.acs.persistence.domain.RetailerSite;
import com.dell.acs.persistence.domain.User;
import com.dell.acs.persistence.repository.EventRepository;
import com.dell.acs.persistence.repository.impl.hibernate.EventRepositoryImpl;
import com.sourcen.core.util.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 @author Ashish
 @author $LastChangedBy: Ashish $
 @version $Revision: 1595 $, $Date:: 7/25/12 10:15 AM#$
 */
@RunWith(Parameterized.class)
public class EventManagerImplTest extends DellTestCase {

    private static Logger logger = LoggerFactory.getLogger(EventManagerImplTest.class);

    // Required parameter.

    private Long userId;

    private Long eventId;

    private Long retailerSiteId;

    private String eventName;

    /*    Constructor.
    The JUnit test runner will instantiate this class once for every
    element in the Collection returned by the method annotated with
    @Parameters.*/

    public EventManagerImplTest(Long userId, Long eventId, Long retailerSiteId, String eventName) {

        this.userId = userId;
        this.eventId = eventId;
        this.retailerSiteId = retailerSiteId;
        this.eventName = eventName;
    }

/*  Test data generator.
       This method is called the the JUnit parameterized test runner and
       returns a Collection of Arrays.  For each Array in the Collection,
       each array element corresponds to a parameter in the constructor.*/


    @Parameterized.Parameters
    public static Collection<Object[]> generateData() {

        // failure case: 0,0,0,"Test Event" , due to user not exist.
        // failure case: 1,1,0,"Test Event " , due to retailer site not found.
        // failure case: 1,1,1,"Testing Event 01", due to duplicate entry.
        // failure case: 1,3,1,"Testing Event Done", if event not found for retailer site id and event id.
        // success case: 1,3,1,"Testing Done".
        Object[][] objectArray = new Object[][]{

                {0L, 0L, 0L, "Test Event"},
                {1L, 1L, 0L, "Test Event"},
                {1L, 1L, 1L, "Testing Event 01"},
                {1L, 3L, 1L, "Testing Done"},
                {1L, 5L, 1L, "Testing Done"}

        };

        return Arrays.asList(objectArray);
    }

    @Test
    public void testSaveEvent() throws Exception {

        logger.info("--------------------testSaveEvent--------------------------");
        logger.info("User id. " + this.userId);
        logger.info("Event id. " + this.eventId);
        logger.info("RetailerSite Id " + this.retailerSiteId);
        logger.info("Event Name. " + this.eventName);
        logger.info("-----------------------------------------------------------");
        Event event = addEvent();
        Assert.notNull(event, "Unable to create the event");
        eventManager.saveEvent(event);
    }

    @Test
    public void testUpdateEvent() throws Exception {

        logger.info("--------------------testUpdateEvent--------------------------");
        logger.info("User id. " + this.userId);
        logger.info("Event id. " + this.eventId);
        logger.info("RetailerSite Id " + this.retailerSiteId);
        logger.info("Event Name. " + this.eventName);
        logger.info("-----------------------------------------------------------");

        Event event = eventManager.getEvent(this.eventId, this.retailerSiteId);
        Assert.notNull(event, "Event entity not found for event id==>" + this.eventId + " and retailer site id==>" +
                this.retailerSiteId);
        event.getProperties().setProperty(EventManager.EVENT_THUMBNAIL_PROPERTIES,
                "/cdn/1_dell/1_dell/event/" + event.getId() + "/thumbnailFile.png");
        eventManager.saveEvent(event);
        logger.info("Event is updated successfully");
    }

    @Test
    public void testGetEvent() throws Exception {

        logger.info("--------------------testGetEvent--------------------------");
        logger.info("User id. " + this.userId);
        logger.info("Event id. " + this.eventId);
        logger.info("RetailerSite Id " + this.retailerSiteId);
        logger.info("Event Name. " + this.eventName);
        logger.info("-----------------------------------------------------------");

        Event event = eventManager.getEvent(this.eventId, this.retailerSiteId);
        Assert.notNull(event, "Event entity not found for event id==>" + this.eventId + " and retailer site id==>" +
                this.retailerSiteId);
        logger.info("Event Found");
    }

    @Test
    public void testGetEventsByRetailerSiteId() throws Exception {

        logger.info("--------------------testGetEventsByRetailerSiteId-----------");
        logger.info("User id. " + this.userId);
        logger.info("Event id. " + this.eventId);
        logger.info("RetailerSite Id " + this.retailerSiteId);
        logger.info("Event Name. " + this.eventName);
        logger.info("-----------------------------------------------------------");

        List<Event> event = eventManager.getEventsByRetailerSiteId(this.retailerSiteId);
        Assert.notNull(event, "Unable to found the events of particular retailer site");
        logger.info("Find the events of particular site");

    }

    @Test
    public void testCheckNameAvailability() {

        logger.info("--------------------testGetEventsByRetailerSiteId----------");
        logger.info("User id. " + this.userId);
        logger.info("Event id. " + this.eventId);
        logger.info("RetailerSite Id " + this.retailerSiteId);
        logger.info("Event Name. " + this.eventName);
        logger.info("-----------------------------------------------------------");

        boolean eventNameAvailability = eventManager.checkNameAvailability(this.eventName);
        if (eventNameAvailability) {
            logger.info("Event " + this.eventName + " found");
        } else {
            logger.info("Event " + this.eventName + " not found");
        }

    }

    @Test
    public void testGetNumberOfOptionalImage() {

        logger.info("--------------------testGetNumberOfOptionalImage----------");
        logger.info("User id. " + this.userId);
        logger.info("Event id. " + this.eventId);
        logger.info("RetailerSite Id " + this.retailerSiteId);
        logger.info("Event Name. " + this.eventName);
        logger.info("-----------------------------------------------------------");

        Map<String, String> noOfOptionalImage = null;
        noOfOptionalImage = eventManager.getNumberOfOptionalImage(this.eventId);
        if (noOfOptionalImage != null) {
            if (noOfOptionalImage.size() > 0) {
                logger.info("Event [" + this.eventId + "] contain the optional images");
            } else {
                logger.info("Event [" + this.eventId + "] not contain the optional images");
            }
        }else{
           logger.info("Event [" + this.eventId + "] not contain the optional images");
        }
    }

    @Test
    public void testGetListAllOptionalKeyFromEventProperty() {

        logger.info("--------------------testGetListAllOptionalKeyFromEventProperty----------");
        logger.info("User id. " + this.userId);
        logger.info("Event id. " + this.eventId);
        logger.info("RetailerSite Id " + this.retailerSiteId);
        logger.info("Event Name. " + this.eventName);
        logger.info("-----------------------------------------------------------");

        String listAllOptionalKeyFromEventProperty[] = null;
        if(listAllOptionalKeyFromEventProperty!=null){
            listAllOptionalKeyFromEventProperty=eventManager.getListAllOptionalKeyFromEventProperty(this.eventId);
        }else{

        }
    }

    @Test
    public void testGetEventNameById(){

        logger.info("--------------------testGetEventNameById----------");
        logger.info("User id. " + this.userId);
        logger.info("Event id. " + this.eventId);
        logger.info("RetailerSite Id " + this.retailerSiteId);
        logger.info("Event Name. " + this.eventName);
        logger.info("-----------------------------------------------------------");

        String eventName=eventManager.getEventNameByID(this.eventId);
        Assert.notNull(eventName,"No Event occur for id==>"+this.eventId);
    }

    public Event addEvent() throws UserNotFoundException {

        User user = userManager.getUser(this.userId);
        Assert.notNull(user, "User Not Found for user id==>" + this.userId);

        RetailerSite retailerSite = retailerManager.getRetailerSite(this.retailerSiteId);
        Assert.notNull(retailerSite, "RetailerSite Not Found for id==>" + this.retailerSiteId);

        Event event = new Event();
        event.setName(this.eventName);
        event.setRetailerSite(retailerSite);
        event.setCreationDate(new Date());
        event.setModifiedDate(new Date());
        event.setCreatedBy(user);
        event.setModifiedBy(user);

        return event;

    }
}
