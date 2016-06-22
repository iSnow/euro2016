package de.isnow;

import javax.persistence.EntityManager;
import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;

import lombok.Getter;
import lombok.Setter;

public class WebApplication extends ResourceConfig {
	private static String deploymentInstance;
	private static WebApplication instance;

	final static Logger LOG = LogManager.getLogger(WebApplication.class.getName());	
	
	@Getter
	private static long startTime;
	
	@Setter
	@Context
	private ServletContext ctx;
    private static EntityManager em;
    	
	public WebApplication() {
		instance = this;
		startTime = System.currentTimeMillis();
		
		register(MultiPartFeature.class);
	}

	public void init(){
		
	}
	
	public static String getDeploymentInstance() {
		if (null == instance) {
			instance = new WebApplication();
		}
		
		if (null != deploymentInstance) {
			return deploymentInstance;
		}
		
		if ((null == instance.ctx) || null == instance.ctx.getInitParameter("instance")) {
			deploymentInstance = "DEV";
			LOG.warn("Deployment instance not set via context. Falling back to instance: " + deploymentInstance);
		} else {
			deploymentInstance = instance.ctx.getInitParameter("instance");
		}
		LOG.debug("Deployment instance: " + deploymentInstance);
		return deploymentInstance;
	}

	
	/**
	 * Can signal other parts of the application (eg. the REST backend)
	 * if time-consuming parts like loading the profile cache have completed
	 * and the application is in a consistent state.
	 * 
	 * If other calculations are necessary, add flags for them and return
	 * a status based on them.
	 * @return
	 */
	public static boolean isReady() {
		return true;
	}
	
	
	
}
