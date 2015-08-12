package org.bonitasoft.resources.utils.connection;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bonitasoft.engine.api.ApiAccessType;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.engine.util.APITypeManager;

public class V6APIUtils {
	private static final String PROP_NAME_URL = "server.url";
	private static String PROP_VALUE_URL = "http://localhost:8080";
	private static final String PROP_NAME_APP = "application.name";
	private static String PROP_VALUE_APP = "bonita";
	private static final String PROP_NAME_LOGIN = "login";
	private static String PROP_VALUE_LOGIN = "walter.bates";
	private static final String PROP_NAME_PASSWORD = "password";
	private static String PROP_VALUE_PASSWORD = "bpm";
	private static final ApiAccessType PROP_VALUE_TYPE = ApiAccessType.HTTP;
	
	private APISession apiSession = null;
	
	private final static Logger LOGGER = Logger.getLogger(V6APIUtils.class.getName());
	
	public V6APIUtils() throws Exception {
		Properties properties = new Properties(getClass().getClassLoader().getResourceAsStream("bonitav6.properties"));
		PROP_VALUE_URL = properties.getMandatoryProperty(PROP_NAME_URL);
		PROP_VALUE_APP = properties.getMandatoryProperty(PROP_NAME_APP);
		PROP_VALUE_LOGIN = properties.getMandatoryProperty(PROP_NAME_LOGIN);
		PROP_VALUE_PASSWORD = properties.getMandatoryProperty(PROP_NAME_PASSWORD);
		
		initialize();
	}

	private void initialize() {
		Map<String, String> map = new HashMap<String, String>();
		map.put(PROP_NAME_URL, PROP_VALUE_URL);
		map.put(PROP_NAME_APP, PROP_VALUE_APP);
		APITypeManager.setAPITypeAndParams(PROP_VALUE_TYPE, map);
	}
	
	public APISession login() throws Exception {
		try {
			apiSession = TenantAPIAccessor.getLoginAPI().login(PROP_VALUE_LOGIN, PROP_VALUE_PASSWORD);
			return apiSession;
		} catch (Exception e) {
			if(LOGGER.isLoggable(Level.SEVERE)) {
				LOGGER.severe("Failed to connect: " + e.getMessage());
			}
			throw e;
		}
	}
	
	public void logout() {
		if(apiSession == null) {
			return;
		}
		
		try {
			TenantAPIAccessor.getLoginAPI().logout(apiSession);
		} catch (Exception e) {
			if(LOGGER.isLoggable(Level.SEVERE)) {
				LOGGER.severe("Failed to disconnect: "+ e.getMessage());
			}
		}
	}
}
