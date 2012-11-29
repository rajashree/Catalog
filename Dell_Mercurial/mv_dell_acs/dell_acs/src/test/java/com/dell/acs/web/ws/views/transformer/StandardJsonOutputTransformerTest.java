package com.dell.acs.web.ws.views.transformer;

import com.dell.acs.persistence.domain.Campaign;
import com.dell.acs.persistence.domain.CampaignCategory;
import com.dell.acs.persistence.domain.CampaignItem;
import com.sourcen.core.persistence.domain.constructs.jpa.PropertiesAware;
import com.sourcen.core.util.collections.PropertiesProvider;
import com.sourcen.core.web.ws.views.transformer.StandardJsonOutputTransformer;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;
import com.thoughtworks.xstream.io.json.JsonWriter;
import net.sf.json.JsonConfig;
import org.apache.commons.lang.StringEscapeUtils;
import org.codehaus.jettison.mapped.Configuration;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletRequest;

import java.io.Writer;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/** @author Navin Raj Kumar G.S. */
public class StandardJsonOutputTransformerTest {

    private static final Logger logger = LoggerFactory.getLogger(StandardJsonOutputTransformerTest.class);

    @Test
    public void testCyclicConversion() {
        StandardJsonOutputTransformer transformer = new StandardJsonOutputTransformer();
        MockHttpServletRequest request =  new MockHttpServletRequest("GET","/api/v1/rest/CampaignService/getCampaign.json");
        request.setParameter("id","1");
        request.setParameter("scope","minimal");
        transformer.setHttpServletRequest(request);
        JsonConfig jsonConfig = new JsonConfig();
        transformer.setJsonConfig(jsonConfig);

        Campaign campaign = new Campaign();
        campaign.setId(1L);
        campaign.setName("test-campaign");
        campaign.setCreationDate(new Date());
        campaign.setEnabled(true);
        campaign.setVersion(1L);
        campaign.setEndDate(new Date(System.currentTimeMillis() + 1000000));
        campaign.setType(Campaign.Type.HOTDEALS);

        Set<CampaignItem> campaignItems = new HashSet<CampaignItem>();
        for (int i = 0; i < 1; i++) {
            CampaignItem item = new CampaignItem(i * 1L, CampaignItem.Type.PRODUCT);
            item.setCampaign(campaign);
            item.setVersion(1L);
            item.setCreationDate(new Date());
            item.setPriority(5);
            campaignItems.add(item);
        }

        LinkedHashSet<CampaignCategory> categories = new LinkedHashSet<CampaignCategory>();
        CampaignCategory lastAddedCategory = null;
        // build a CRAZY relationship which is recursive...
        for (int i = 0; i < 10; i++) {
            CampaignCategory item = new CampaignCategory();
            item.setId(i * 1L);
            item.setName("Category - " + i);
            item.setCampaign(campaign);
            item.setParent(lastAddedCategory);

            item.setCampaign(campaign);
            item.setVersion(1L);
            item.setItems(campaignItems);

            if(lastAddedCategory != null){
                HashSet child = new HashSet<CampaignCategory>();
                child.add(item);
                lastAddedCategory.setChildren(child);
            }
            lastAddedCategory = item;
            categories.add(item);
        }


        campaign.setCategories(categories);
        campaign.setItems(campaignItems);
        campaign.getProperties().setProperty("test.property", "myValue");
        Configuration configuration = new Configuration();
        JsonHierarchicalStreamDriver driver = new JsonHierarchicalStreamDriver() {
            @Override
            public HierarchicalStreamWriter createWriter(final Writer writer) {
                return new JsonWriter(writer, JsonWriter.DROP_ROOT_MODE);
            }
        };

        XStream xstream = new XStream(driver);
        xstream.setMode(XStream.XPATH_ABSOLUTE_REFERENCES);


        xstream.aliasSystemAttribute(null, "class");

        xstream.registerConverter(new Converter() {
            @Override
            public void marshal(final Object source, final HierarchicalStreamWriter writer,
                    final MarshallingContext context) {
                PropertiesProvider properties = ((PropertiesAware) source).getProperties();
                for (String key : properties.keySet()) {
                    writer.startNode(key);
                    writer.setValue("'"+ StringEscapeUtils.escapeJavaScript(properties.getProperty(key))+"'");
                    writer.endNode();

                }
            }

            @Override
            public Object unmarshal(final HierarchicalStreamReader reader, final UnmarshallingContext context) {
                throw new UnsupportedOperationException();
            }

            @Override
            public boolean canConvert(final Class type) {
                return type.equals(PropertiesAware.class);
            }
        });
        logger.debug(transformer.transform(campaign).toString());
//        logger.debug(xstream.toXML(campaign));
    }

}
