package com.dell.acs.curation;

import com.dell.acs.content.EntityConstants;
import com.dell.acs.persistence.domain.CurationSource;
import org.junit.runners.Parameterized;

import java.util.Collection;
import java.util.LinkedList;

/** @author Navin Raj Kumar G.S. */
public class DellWorldFeedsTest extends CurationSourceHandlerTest {

    public DellWorldFeedsTest(final CurationSource curationSource) {
        super(curationSource);
    }

    @Override
    protected CurationSourceDataHandler getHandler(final CurationSource source) {
        return new RSSFeedHandler();
    }

    @Parameterized.Parameters
    public static Collection<Object[]> getSources() {
        Collection<Object[]> sources = new LinkedList<Object[]>();

        sources.add(getArgs("http://feeds.feedburner.com/KrebsOnSecurity"));
        sources.add(getArgs("http://feeds.feedburner.com/schneier/fulltext"));
        sources.add(getArgs("http://blog.nrf.com/feed/"));
        sources.add(getArgs("http://feeds.feedburner.com/marismithrelationshipspecialist"));
        sources.add(getArgs("http://feeds.feedburner.com/BrianSolis"));
        sources.add(getArgs("http://feedproxy.google.com/chrisbrogandotcom"));
        sources.add(getArgs("http://blog.tweetsmarter.com/feed/"));
        sources.add(getArgs("http://feeds.feedburner.com/SocialMediaExplorer"));
        sources.add(getArgs("http://threatpost.com/en_us/rss.xml"));
        sources.add(getArgs("http://gcn.com/rss-feeds/security.aspx"));
        sources.add(getArgs("http://feeds.feedburner.com/Edudemic"));
        sources.add(getArgs("http://www.modernhealthcare.com/section/rss01&mime=xml"));
        sources.add(getArgs("http://www.modernhealthcare.com/section/rss08&mime=xml"));
        sources.add(getArgs("http://www.modernhealthcare.com/section/rss02&mime=xml"));
        sources.add(getArgs("http://www.modernhealthcare.com/section/rss03&mime=xml"));
        sources.add(getArgs("http://www.modernhealthcare.com/section/rss04&mime=xml"));
        sources.add(getArgs("http://www.meditech.com/meditech.xml"));
        sources.add(getArgs("http://en.community.dell.com/rss.aspx?Tags=Healthcare"));
        sources.add(getArgs("http://www.mbtmag.com/rss-feeds/all/rss.xml/all"));
        sources.add(getArgs("http://www.retail-week.com/XmlServers/navsectionRSS.aspx?navsectioncode=5963"));
        sources.add(getArgs("http://www.internetretailer.com/rss/news/"));
        sources.add(getArgs("http://www.internetretailer.com/rss/categories/technology/"));
        sources.add(getArgs("http://www.wwd.com/rss/2/news/retail"));
        sources.add(getArgs("http://www.wwd.com/rss/2/news/business"));
        sources.add(getArgs("http://feeds.mashable.com/Mashable"));
        sources.add(getArgs("http://www.socialmediaexaminer.com/feed/"));
        sources.add(getArgs("http://www.enterpriseefficiency.com/rss_simple.asp?f_n=2481 &f_ln=Security"));
        sources.add(getArgs("http://www.enterpriseefficiency.com/rss_simple.asp?f_n=2503&f_ln=Education"));
        sources.add(getArgs("http://www.enterpriseefficiency.com/rss_simple.asp?f_n=2506&f_ln=Financial+Services"));
        sources.add(getArgs("http://www.enterpriseefficiency.com/rss_simple.asp?f_n=2505&f_ln=Government"));
        sources.add(getArgs("http://fcw.com/rss-feeds/all.aspx"));
        sources.add(getArgs("http://gcn.com/rss-feeds/all.aspx"));
        sources.add(getArgs("http://www.enterpriseefficiency.com/rss_simple.asp?f_n=2508&f_ln=Healthcare"));
        sources.add(getArgs("http://www.enterpriseefficiency.com/rss_simple.asp?f_n=3153&f_ln=Manufacturing"));
        sources.add(getArgs("http://www.enterpriseefficiency.com/rss_simple.asp?f_n=2517&f_ln=Retail"));
        sources.add(getArgs("http://www.modernhealthcare.com/section/rss05&mime=xml"));
        sources.add(getArgs("http://www.modernhealthcare.com/section/rss06&mime=xml"));

        sources.add(getArgs("http://twitter.com/statuses/user_timeline/delltechcenter.rss"));
        sources.add(getArgs("http://twitter.com/statuses/user_timeline/dellevents.rss"));
        sources.add(getArgs("http://search.twitter.com/search.atom?q=%23DellWorld"));

        sources.add(getArgs("http://www.facebook.com/feeds/page.php?format=rss20&id=124161897646081"));

        return sources;
    }

    protected static Object[] getArgs(String url) {
        return new Object[]{createDummySource(EntityConstants.CurationSourceType.RSS_FEED, "rss.url", url)};
    }

}
