package edu.cornell.mannlib.sample;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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

 
public class MarcToJson {
	
	private static final Logger logger = Logger.getLogger(MarcToJson.class);
	private HashMap<String,String> lookupTable;
	private String tenant;
	private String token;
	private ApiService apiService;
	
	private String endpoint;
	
	MarcUtils marcUtils = new MarcUtils();
	LookupUtil lookupUtil;
	
	CompositeConfiguration config = new CompositeConfiguration();
	PropertiesConfiguration props = new PropertiesConfiguration();
	
	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public CompositeConfiguration getConfig() {
		return config;
	}

	public void setConfig(CompositeConfiguration config) {
		this.config = config;
	}

	public PropertiesConfiguration getProps() {
		return props;
	}

	public void setProps(PropertiesConfiguration props) {
		this.props = props;
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

	public MarcUtils getMarcUtils() {
		return marcUtils;
	}

	public void setMarcUtils(MarcUtils marcUtils) {
		this.marcUtils = marcUtils;
	}

	public LookupUtil getLookupUtil() {
		return lookupUtil;
	}

	public void setLookupUtil(LookupUtil lookupUtil) {
		this.lookupUtil = lookupUtil;
	}
	
	public MarcToJson() {
		// 
	}
	
	public static void main(String[] args) {
		MarcToJson app = new MarcToJson();
		try {
			app.init();
			JSONArray responseMessages  = app.run();
			System.out.println(responseMessages.toString(3));
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

	public void init() throws Exception { 
		 
		PropertiesConfiguration props = new PropertiesConfiguration();
		try {
			props.load(ClassLoader.getSystemResourceAsStream("application.properties"));
		    
		} catch (ConfigurationException e) {
		      throw new RuntimeException(e);
		}
		getConfig().addConfiguration(props);
		 
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
		this.lookupUtil = new LookupUtil();
		this.lookupUtil.setBaseOkapEndpoint(getEndpoint());
		this.lookupUtil.setApiService(getApiService());
		this.lookupUtil.load();
		setLookupTable(this.lookupUtil.getReferenceValues(this.getToken()));
	} 
	
	
	public JSONArray run() throws Exception { 
		
        String[] fileArray = {
		   //"/cul/src/order-import-poc-dev/marc-test-files/building_bridges.mrc",
		   //"/cul/src/order-import-poc-dev/marc-test-files/gobi-sample-v2.mrc",
		   //"/cul/src/order-import-poc-dev/marc-test-files/physical.mrc",
		   "/cul/src/order-import-poc-dev/marc-test-files/requesters_5-records_2021-03-11.mrc",
		   //"/cul/src/order-import-poc-dev/marc-test-files/harrassowitz_9-records_2021-03-10.mrc",
		   //"/cul/src/order-import-poc-dev/marc-test-files/AmazonFO.1.mrc",
		   //"/cul/src/order-import-poc-dev/marc-test-files/CouttsUKFO.1.mrc",
		   //"/cul/src/order-import-poc-dev/marc-test-files/MIDWEST.1.mrc",
		   //"/cul/src/order-import-poc-dev/marc-test-files/save-for-masayo.mrc",
        };
        List<String> files = Arrays.asList(fileArray);
        
        String filePath =  "/cul/src/order-import-poc-dev/marc-test-files/AmazonFO.1.mrc";
        String materialTypeName = "Book";
        JSONArray responseMessages = new JSONArray();
		
		
		// GENERATE UUID for the PO
		   
	    UUID orderUUID = UUID.randomUUID();	    
	    Map<Integer, UUID> orderLineMap = new HashMap<Integer, UUID>(); 
	    
	    //GET THE NEXT PO NUMBER
		//logger.trace("get next PO number");
		//String poNumber = this.apiService.callApiGet( this.getEndpoint() + "orders/po-number", token); 
		JSONObject poNumberObj = new JSONObject();
		poNumberObj.put("poNumber","12345");
		logger.debug("NEXT PO NUMBER: " + poNumberObj.get("poNumber"));
		
		responseMessages.put(poNumberObj);
		
		// CREATING THE PURCHASE ORDER
		JSONObject order = new JSONObject();
		
		order.put("orderType", "One-Time");
		order.put("reEncumber", false);
		order.put("id", orderUUID.toString());
		order.put("approved", true);
		order.put("workflowStatus","Open");
		order.put("poNumber", poNumberObj.get("poNumber"));
		
		JSONArray poLines = new JSONArray();
		
		// iterator over records in the marc file.
		
		InputStream in = new FileInputStream(filePath);		
		MarcReader reader = new MarcStreamReader(in);
		
	    Record record = null;
		logger.debug("reading marc file");
		int numRec = 0;
		boolean electronic = false;
		
		while (reader.hasNext()) {
			try {
				record = reader.next();
				//System.out.println(record.toString());
				JSONObject responseMessage = new JSONObject();
				 
				DataField twoFourFive = (DataField) record.getVariableField("245");
				DataField nineEighty = (DataField) record.getVariableField("980");
			    DataField nineFiveTwo = (DataField) record.getVariableField("952");
			    DataField nineEightyOne = (DataField) record.getVariableField("981");
			    
				String title = marcUtils.getTitle(twoFourFive);
				System.out.println("Title: "+ title);
				responseMessage.put("title", title);
				
				String fundCode = marcUtils.getFundCode(nineEighty);
				String vendorCode =  marcUtils.getVendorCode(nineEighty);
				    
				String quantity =  marcUtils.getQuantity(nineEighty);
				Integer quantityNo = 0; //INIT
			    if (quantity != null)  quantityNo = Integer.valueOf(quantity);
			    
				String price = marcUtils.getPrice(nineEighty, nineEightyOne);
				    
				String electronicIndicator = marcUtils.getElectronicIndicator(nineEighty);
				if (StringUtils.isNotEmpty(electronicIndicator)) {
					electronic = true;
				}
				String vendorItemId = marcUtils.getVendorItemId(nineEighty);
				String selector = marcUtils.getSelector(nineEighty);
				String personName = marcUtils.getPersonName(nineEighty);
			    
			    String locationName = marcUtils.getLocation(nineFiveTwo);
			    responseMessage.put("location", locationName +" ("+ lookupTable.get(locationName + "-location") +")");
			    String requester = marcUtils.getRequester(nineEightyOne);
			    String rush = marcUtils.getRush(nineEightyOne);
			     
			    //LOOK UP VENDOR
			    //System.out.println("lookupVendor");
				String organizationEndpoint = this.getEndpoint() + "/organizations-storage/organizations?limit=30&offset=0&query=((code='" + vendorCode + "'))";
				String orgLookupResponse = this.apiService.callApiGet(organizationEndpoint,  token);
				JSONObject orgObject = new JSONObject(orgLookupResponse);
				String vendorId = (String) orgObject.getJSONArray("organizations").getJSONObject(0).get("id");
				
				//LOOK UP THE FUND
				//System.out.println("lookup Fund");
				String fundEndpoint = this.getEndpoint() + "finance/funds?limit=30&offset=0&query=((code='" + fundCode + "'))";
				String fundResponse = this.apiService.callApiGet(fundEndpoint, token);
				JSONObject fundsObject = new JSONObject(fundResponse);
				String fundId = (String) fundsObject.getJSONArray("funds").getJSONObject(0).get("id");				
				
				// CREATING THE PURCHASE ORDER				
				
				order.put("vendor", vendorId);				
				
				// POST ORDER LINE
				//FOLIO WILL CREATE THE INSTANCE, HOLDINGS, ITEM (IF PHYSICAL ITEM)
				JSONObject orderLine = new JSONObject();
				JSONObject cost = new JSONObject();
				JSONObject location = new JSONObject();
				JSONArray locations = new JSONArray(); 
				
				if (electronic) {
					logger.trace("electronic=true");
					orderLine.put("orderFormat", "Electronic Resource");
					JSONObject eResource = new JSONObject();
					eResource.put("activated", false);
					eResource.put("createInventory", "Instance, Holding");
					eResource.put("trial", false);
					eResource.put("accessProvider", vendorId);
					orderLine.put("eresource",eResource);
					orderLine.put("orderFormat", "Electronic Resource");
					cost.put("quantityElectronic", 1);
					cost.put("listUnitPriceElectronic", price);
					location.put("quantityElectronic", 1);
					location.put("locationId", lookupTable.get(locationName + "-location"));
					locations.put(location);
				} else {
					logger.trace("electronic=false");
					JSONObject physical = new JSONObject();
					physical.put("createInventory", "Instance, Holding, Item");
					physical.put("materialType", lookupTable.get(materialTypeName));
					orderLine.put("physical", physical);
					orderLine.put("orderFormat", "Physical Resource");
					cost.put("listUnitPrice", price);
					cost.put("quantityPhysical", 1);
					location.put("quantityPhysical",quantityNo);
					location.put("locationId", lookupTable.get(locationName + "-location"));
					locations.put(location);
				}
				
				//VENDOR REFERENCE NUMBER IF INCLUDED IN THE MARC RECORD:
				if (vendorItemId != null) {
					JSONObject vendorDetail = new JSONObject();
					vendorDetail.put("instructions", "");
					vendorDetail.put("refNumber", vendorItemId);
					vendorDetail.put("refNumberType", "Internal vendor number");
					vendorDetail.put("vendorAccount", "");
					orderLine.put("vendorDetail", vendorDetail);
				}				
				UUID orderLineUUID = UUID.randomUUID();
				orderLine.put("id", orderLineUUID);
				responseMessage.put("id", orderLineUUID.toString());
				orderLineMap.put(numRec, orderLineUUID); 
				
				orderLine.put("source", "User");
				cost.put("currency", "USD");
				orderLine.put("cost", cost);
				orderLine.put("locations", locations);
				orderLine.put("titleOrPackage", title);
				orderLine.put("acquisitionMethod", "Purchase");
				
				if (StringUtils.isNotEmpty(rush) && StringUtils.contains(rush.toLowerCase(), "rush")) {
					orderLine.put("rush", true);
				}
				
				if (StringUtils.isNotEmpty(selector)) {
					orderLine.put("selector", selector);
				}
				
				if (StringUtils.isNotEmpty(requester)) {
					orderLine.put("requester", requester);
				}
				
				JSONArray funds = new JSONArray();
				JSONObject fundDist = new JSONObject();
				fundDist.put("distributionType", "percentage");
				fundDist.put("value", 100);
				fundDist.put("fundId", fundId);
				funds.put(fundDist);
				orderLine.put("fundDistribution", funds);
				String numRecString = new String();
				if (numRec < 10 ) {
					numRecString = "0"+ String.valueOf(numRec);
				} else {
					numRecString = String.valueOf(numRec);
				} 
					
				String poLineNumber = "a" + poNumberObj.get("poNumber") + "-"+ numRecString;
				orderLine.put("poLineNumber", poLineNumber);
				orderLine.put("purchaseOrderId", orderUUID.toString());
				poLines.put(orderLine);
				order.put("compositePoLines", poLines);				
				responseMessages.put(responseMessage);
			} catch(Exception e) {
				logger.error(e.toString());
				JSONObject responseMessage = new JSONObject();
				responseMessage.put("error",e.toString());
				responseMessage.put("PONumber", poNumberObj.get("poNumber"));
				responseMessages.put(responseMessage);
				
			}
			
			numRec++;
		 
		}
		System.out.println("Here is the PO order number: "+ poNumberObj.get("poNumber"));
		System.out.println(order.toString(3));
		    
		System.out.println();
	    System.out.println("Number of records found: "+ numRec); 
		
	    return responseMessages;
	}
	 
	
	public void validateDataField(DataField df, String name) {
	    if (df == null)	{
	    	logger.error("Required Datafield "+ name +" is null");
	    	System.exit(1);
	    }
	}
    

}
