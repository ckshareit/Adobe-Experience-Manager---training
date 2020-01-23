package com.adobe.training.core.impl;

import java.util.Map;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Modified;
import org.apache.felix.scr.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.training.core.DeveloperInfo;

/**
 * Simple component implementation of the DeveloperInfo Service. 
 * @author Kevin Nennig (nennig@adobe.com)
 */
@Component(metatype = true, label = "Training Developer Info")
@Service(DeveloperInfo.class)
public class DeveloperInfoImpl implements DeveloperInfo{
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Activate
	//http://blogs.adobe.com/experiencedelivers/experience-management/osgi_activate_deactivatesignatures/
	protected void activate(Map<String, Object> properties) {
		configure(properties, "Activiated");
	}
	
	@Modified
	protected void modified(Map<String, Object> properties) {
		configure(properties, "Modified");
	}
	
	@Deactivate
	protected void deactivated(Map<String, Object> properties) {
		logger.info("#############Component (Deactivated) Good-bye");
	}
	
	protected void configure(Map<String, Object> properties, String status) {
		logger.info("#############Component (" +status+ ") " + getDeveloperInfo());
	}
	
	public String getDeveloperInfo(){
		return "Hello! I do not know who my developer is. I am a product of random development!!!";
	}
}
