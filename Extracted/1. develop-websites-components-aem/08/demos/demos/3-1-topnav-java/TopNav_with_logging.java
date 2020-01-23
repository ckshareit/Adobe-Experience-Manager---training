package apps.training.components.structure.site_topnav;

import java.util.*;
import java.util.Iterator;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageFilter;

import com.adobe.cq.sightly.WCMUsePojo;

//Logging classes
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TopNav extends WCMUsePojo{
    private List<Page> items = new ArrayList<Page>();
    private Page rootPage;

    //Logger Object
    private static final Logger LOGGER = LoggerFactory.getLogger(TopNav.class);
    
    // Initializes the navigation
    @Override
    public void activate() throws Exception {
        rootPage = getCurrentPage().getAbsoluteParent(2);

        if (rootPage == null) {
        	rootPage = getCurrentPage();
        }
        
        //Logger Message
        LOGGER.info("########[JAVA] Root page is: " + rootPage.getTitle());
        
        Iterator<Page> childPages = rootPage.listChildren(new PageFilter(getRequest()));
	   	while (childPages.hasNext()) {
			items.add(childPages.next());
	   	}
    }

    // Returns the navigation items
    public List<Page> getItems() {
        return items;
    }
    // Returns the navigation root
    public Page getRoot() {
        return rootPage;
    }
}