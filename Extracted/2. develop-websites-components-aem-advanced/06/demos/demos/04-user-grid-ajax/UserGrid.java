package apps.training.components.content.user_grid;

import com.adobe.cq.sightly.WCMUsePojo;
import org.apache.sling.jcr.api.SlingRepository;
import com.day.cq.commons.TidyJSONWriter;

import org.apache.jackrabbit.api.JackrabbitSession;
import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.jackrabbit.api.security.user.User;
import org.apache.jackrabbit.api.security.user.Query;
import org.apache.jackrabbit.api.security.user.QueryBuilder;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

import javax.jcr.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserGrid extends WCMUsePojo {
	private final Logger logger = LoggerFactory.getLogger(getClass());
    @Override
    public void activate() throws Exception{
        
    	final SlingRepository repos = getSlingScriptHelper().getService(SlingRepository.class);

        JackrabbitSession jcrSession = null;
        Iterator<Authorizable> authorizableIterator = null;
        try
        {
            // Ensure that the currently logged on user has admin privileges.
        	jcrSession = (JackrabbitSession)repos.loginAdministrative(null);
         
            final UserManager um = jcrSession.getUserManager();
            final TidyJSONWriter writer = new TidyJSONWriter(getResponse().getWriter());
  
            authorizableIterator = um.findAuthorizables(new Query() {
                public  void build(QueryBuilder builder) {
                    builder.setSelector(User.class);
                }
            });
            
            List<Authorizable> users = new ArrayList<Authorizable>();

            // copy iterator into a List for additional manipulations.
            Authorizable tmpUser;
            while(authorizableIterator.hasNext())
            {
                tmpUser = authorizableIterator.next();
                users.add((User)tmpUser);
         
            }

            writer.setTidy("true".equals(getRequest().getParameter("tidy")));
            writer.object();
            writer.key("page").value(1);
            writer.key("total").value(users.size());
            writer.key("rows").array();
         
            for(int i=0; i < users.size(); i++)
            {
         
                Authorizable aUser = users.get(i);

                if(aUser.hasProperty("./profile/givenName") && 
                		aUser.hasProperty("./profile/familyName") && 
                		aUser.hasProperty("./profile/email")){
                	
                	writer.object();
                    writer.key("id").value(aUser.getID());
                    writer.key("cell").array();
                    
                    writer.value(aUser.getID());
                    writer.value(aUser.getProperty("./profile/givenName")[0].getString());
                    writer.value(aUser.getProperty("./profile/familyName")[0].getString());
                    writer.value(aUser.getProperty("./profile/email")[0].getString());
                    
                    writer.endArray();
                    writer.endObject();
                    
                }
                
            }
         
                writer.endArray();
                writer.endObject();
                jcrSession.logout();
        }
        catch (Exception e)
        {
            logger.error("myajaxsample Exception Occured: " + e.getMessage());
        }
        finally
        {
        	jcrSession.logout();
        	jcrSession = null;
        }
    }
}