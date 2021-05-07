package edu.cornell.mannlib.sample;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
 
import org.marc4j.MarcReader;
import org.marc4j.MarcStreamReader;
import org.marc4j.marc.DataField;
import org.marc4j.marc.Record;

import edu.cornell.mannlib.sample.services.ApiService;
import edu.cornell.mannlib.sample.util.LookupUtil;
import edu.cornell.mannlib.sample.util.MarcUtils;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject; 

 
public class Lookup {
	
	private static final Logger logger = Logger.getLogger(Lookup.class);
	private HashMap<String,String> lookupTable;
	private String tenant;
	private String token;
	private ApiService apiService;
	private CompositeConfiguration config = new CompositeConfiguration();
	PrintStream stdout = System.out;
	private String endpoint;
	
	 
	LookupUtil lookupUtil;
	
	 
	public CompositeConfiguration getConfig() {
		return config;
	}

	public void setConfig(CompositeConfiguration config) {
		this.config = config;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	} 
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getTenant() {
		return tenant;
	}

	public void setTenant(String tenant) {
		this.tenant = tenant;
	}

	public HashMap<String, String> getLookupTable() {
		return lookupTable;
	}

	public void setLookupTable(HashMap<String, String> lookupTable) {
		this.lookupTable = lookupTable;
	}

	public ApiService getApiService() {
		return apiService;
	}

	public void setApiService(ApiService apiService) {
		this.apiService = apiService;
	}

	 

	public LookupUtil getLookupUtil() {
		return lookupUtil;
	}

	public void setLookupUtil(LookupUtil lookupUtil) {
		this.lookupUtil = lookupUtil;
	}
	
	public Lookup() {
		// 
	}
	
	public static void main(String[] args) {
		Lookup app = new Lookup();
		System.out.println(app.getClass().getName()+ " begin");
		try {
			app.init();
			app.run(); 
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(app.getClass().getName()+ " end");
	} 

	public void init() throws Exception { 
		 
		this.setConfig(AppConfig.getConfig());
		 
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("username", getConfig().getProperty("okapi_username"));
		jsonObject.put("password", getConfig().getProperty("okapi_password"));
		
		setTenant((String) config.getProperty("tenant"));
		jsonObject.put("tenant",  getTenant());
		
		this.setApiService(new ApiService(getTenant()));
		this.setEndpoint((String) config.getProperty("baseOkapEndpoint"));
		  
		try {
			this.token =  getApiService().callApiAuth( getEndpoint() + "authn/login",  jsonObject); 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	} 
	
	
	public void run() throws Exception {
		 
		this.lookupUtil = new LookupUtil();
		this.lookupUtil.setBaseOkapEndpoint(getEndpoint());
		this.lookupUtil.setApiService(getApiService());
		this.lookupUtil.load();
		
		//this.lookupUtil.setEndPoint("identifier-types", "1000");
		//this.lookupUtil.setEndPoint("contributor-types", "1000");
		//this.lookupUtil.setEndPoint("classification-types", "1000");
		//this.lookupUtil.setEndPoint("contributor-types", "1000");
		//this.lookupUtil.setEndPoint("contributor-name-types", "1000");
		//this.lookupUtil.setEndPoint("locations", "10000");
		//this.lookupUtil.setEndPoint("loan-types", "1000");
		//this.lookupUtil.setEndPoint("note-types", "1000");
		//this.lookupUtil.setEndPoint("material-types", "1000");
		//this.lookupUtil.setEndPoint("instance-types", "1000");
		//this.lookupUtil.setEndPoint("holdings-types", "1000"); 
		//this.lookupUtil.setEndPoint("identifier-types", "1000");
		//this.lookupUtil.setEndPoint("configurations/entries", "100");
		
		setLookupTable(this.lookupUtil.getReferenceValues(this.getToken()));
		
		//System.setOut(new PrintStream(new FileOutputStream("/cul/src/SampleProject/output.json")));
		Iterator iter = getLookupTable().keySet().iterator();
		while (iter.hasNext()) { 
			String key = (String) iter.next();
			String value = this.getLookupTable().get(key);
			System.out.println("key: "+ key);
			System.out.println("value: "+ value);
		}
		System.out.println();
		String configEndpoint = lookupUtil.getBaseOkapEndpoint()+"configurations/entries?limit=100";
		Map<String, String> addresses = this.lookupUtil.getBillingAddresses(configEndpoint, this.getToken());
		Iterator iter2 = addresses.keySet().iterator();
		while (iter2.hasNext()) {
			String name = (String) iter2.next();
			String address = addresses.get(name);
			System.out.println("name: "+ name);
			System.out.println("address: "+ address);
			System.out.println();
		}
		//System.setOut(stdout);
         
	}
	 
	
	 

}
