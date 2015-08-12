package org.bonitasoft.resources.utils;

import java.util.HashMap;
import java.util.List;

import org.bonitasoft.engine.api.ApplicationAPI;
import org.bonitasoft.engine.api.PageAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.api.TenantAdministrationAPI;
import org.bonitasoft.engine.api.ThemeAPI;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.resources.utils.connection.V6APIUtils;

public class ResourceManager {
	private V6APIUtils v6APIUtils = null;
	private APISession apiSession = null;

	public ResourceManager() throws Exception {
		load();
	}

	public void load() throws Exception {
		v6APIUtils = null;
		v6APIUtils = new V6APIUtils();
		apiSession = v6APIUtils.login();
		v6APIUtils.logout();
	}

	public void beforeAnyActionExecution() throws Exception {
		apiSession = v6APIUtils.login();
	}

	public void afterAnyActionExecution() throws Exception {
		if(v6APIUtils != null) {
			apiSession = null;
			v6APIUtils.logout();
		}
	}
	
	public HashMap<String, List<Object>> getAllResources() throws Exception {
		HashMap<String, List<Object>> resources = new HashMap<String, List<Object>>();
		
		try {
			beforeAnyActionExecution();
			
			//Get all application related resources
			ApplicationAPI applicationAPI = TenantAPIAccessor.getLivingApplicationAPI(apiSession);
			TenantAdministrationAPI tenantAdministrationAPI = TenantAPIAccessor.getTenantAdministrationAPI(apiSession);
			ThemeAPI themeAPI = TenantAPIAccessor.getThemeAPI(apiSession);
			PageAPI pageAPI = TenantAPIAccessor.getCustomPageAPI(apiSession);
		} finally {
			afterAnyActionExecution();
		}
		
		return resources;
	}
}
