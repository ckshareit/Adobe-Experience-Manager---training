package com.adobe.training.core;

import javax.jcr.security.AccessControlList;
import javax.jcr.security.AccessControlManager;
import javax.jcr.security.Privilege;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.apache.jackrabbit.api.JackrabbitSession;
import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.jackrabbit.commons.jackrabbit.authorization.AccessControlUtils;
import org.osgi.framework.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.workflow.WorkflowException;
import com.day.cq.workflow.WorkflowSession;
import com.day.cq.workflow.exec.WorkItem;
import com.day.cq.workflow.exec.WorkflowData;
import com.day.cq.workflow.exec.WorkflowProcess;
import com.day.cq.workflow.metadata.MetaDataMap;

/**
 * This is a project workflow process step. When starting a workflow from a project, you can specify the:
 * 
 * Payload: The page that will have 'modify' permissions added/removed from its ACL
 * User: The user attached to the ACL
 * 
 * This process will add/remove jcr privileges that correlate to 'Modify, Create, Delete' in the /useradmin 
 * for the given user on the payload. An argument is used to specify to add or remove 'modify' permissions. 
 * The argument must follow permission=<add || remove>
 *
 * @author Kevin Nennig (nennig@adobe.com)
 */
@Component
@Service
@Properties({ @Property(name = Constants.SERVICE_DESCRIPTION, value = "A process to auto assign permissions to edit a given page."),
		@Property(name = Constants.SERVICE_VENDOR, value = "Adobe Digital Learning Services"),
		@Property(name = "process.label", value = "Auto Assign Edit Permissions") })
public class AutoAssignACL implements WorkflowProcess{
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 * @param item - Holds the payload and user
	 * @param workflowSession - Session with service user (workflow-process-service) that will be modifying the permissions
	 * @param workflowArgs - permission=add will add 'modify' permissions. permission=remove will remove 'modify' permissions
	 */
	@Override
	public void execute(WorkItem item, WorkflowSession workflowSession, MetaDataMap workflowArgs) throws WorkflowException {	
		
		WorkflowData workflowData = item.getWorkflowData(); //get the workflow properties
		String userID = workflowData.getMetaDataMap().get("assignee", String.class);
		logger.info("User to add permissions: " + userID);
		String payload = workflowData.getPayload().toString();
		logger.info("Content path to add permissions: " + payload);
		
		UserManager uM;
		try {
			JackrabbitSession jcrSession = (JackrabbitSession)workflowSession.getSession();
			//The user that actually modifies the permissions is the workflow-process-service
			//The workflow-process-service must have Read ACL and Write ACL permissions for the payload
			logger.info("Service user to change permissions: " + jcrSession.getUserID());
			
			uM = jcrSession.getUserManager();
			//Get the Authorizable object for the new user
			Authorizable authorizable = uM.getAuthorizable(userID); //this might be a user or a group
			
			AccessControlManager accessControlManager = jcrSession.getAccessControlManager();
			
			//JCR privileges that encompass AEM Permissions: "Modify, Create, Delete" in the /useradmin
			Privilege[] privileges = {accessControlManager.privilegeFromName(Privilege.JCR_MODIFY_PROPERTIES),
					accessControlManager.privilegeFromName(Privilege.JCR_LOCK_MANAGEMENT),
					accessControlManager.privilegeFromName(Privilege.JCR_VERSION_MANAGEMENT),
					accessControlManager.privilegeFromName(Privilege.JCR_REMOVE_CHILD_NODES),
					accessControlManager.privilegeFromName(Privilege.JCR_REMOVE_NODE),
					accessControlManager.privilegeFromName(Privilege.JCR_ADD_CHILD_NODES),
					accessControlManager.privilegeFromName(Privilege.JCR_NODE_TYPE_MANAGEMENT)};
			
			//Get the ACL for the payload
			AccessControlList acl = AccessControlUtils.getAccessControlList(jcrSession, payload);
			//Add the user/privileges to the payload ACL
			acl.addAccessControlEntry(authorizable.getPrincipal(), privileges);
			
			
			//Based on the process step arguments, permissions are either added or removed
			//Assumes arguments are given as: permission=add
			String arguments = workflowArgs.get("PROCESS_ARGS", "");
			if(arguments.contains("permission")){
				String permission = arguments.split("=")[1];
				if(permission.equals("add")){ //Adds permissions to edit the payload
					logger.info("Adding permissions");
					accessControlManager.setPolicy(payload, acl);
					logger.info("Added modify permissions to " + payload + " for " + userID);
				}else if(permission.equals("remove")){ //Removes permissions to edit the payload
					logger.info("Removing permissions");
					accessControlManager.removePolicy(payload, acl);
					logger.info("Removed modify permissions to " + payload + " for " + userID);
				}
			}else{
				//do nothing if the workitem argument is not set
				logger.info("No arguments given for workitem");
			}
			
			jcrSession.save();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
		}		
	}
}

