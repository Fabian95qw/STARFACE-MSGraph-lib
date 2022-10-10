package si.module.msgraphs.o365provider;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.JsonObject;
import com.microsoft.graph.auth.enums.NationalCloud;
import com.microsoft.graph.core.ClientException;
import com.microsoft.graph.models.extensions.IGraphServiceClient;
import com.microsoft.graph.requests.extensions.GraphServiceClient;
import com.microsoft.graph.requests.extensions.IContactCollectionPage;
import com.microsoft.graph.requests.extensions.IContactFolderCollectionPage;
import com.microsoft.graph.requests.extensions.IUserCollectionPage;

import si.module.msgraphs.utility.LogHelper;
import si.module.msgraphs.utility.EnumHelper.ProviderType;

public class O365Provider 
{
	IGraphServiceClient GC = null;
	Logger log =null;
	
	private ProviderType PT = ProviderType.NONE;
	
	public O365Provider(String TenantID, String ClientID, String ClientSecret, List<String> Scopes, Logger logger)
	{
		this.log=logger;
		ClientCredentialProvider authProvider = new ClientCredentialProvider(ClientID, Scopes, ClientSecret, TenantID, NationalCloud.Global, logger);
		GC = GraphServiceClient.builder().authenticationProvider(authProvider).buildClient();
		GC.validate();		
		PT =ProviderType.Client;
	}
	
	public O365Provider(DeviceCodeFlowProvider DCP, Logger log2) 
	{
		this.log=log2;
		GC = GraphServiceClient.builder().authenticationProvider(DCP).buildClient();
		GC.validate();
		PT = ProviderType.DeviceCode;
	}

	public IUserCollectionPage getUsers()
	{
		try
		{
			return GC.users().buildRequest().get();
		}
		catch(Exception e)
		{
			LogHelper.EtoStringLog(log, e);
			return null;
		}
	}
	
	public IContactFolderCollectionPage getContactFoldersforUser(String IDorUPN)
	{
		try
		{
			return GC.users(IDorUPN).contactFolders().buildRequest().get();
		}
		catch(Exception e)
		{
			LogHelper.EtoStringLog(log, e);
			return null;
		}
	}
	
	public IContactCollectionPage getContactsforUser(String IDorUPN, String FolderID)
	{
		try
		{
			if(FolderID.isEmpty())
			{
				return GC.users(IDorUPN).contacts().buildRequest().get();
			}
			else
			{
				return GC.users(IDorUPN).contactFolders(FolderID).contacts().buildRequest().get();
			}
		}
		catch(Exception e)
		{
			LogHelper.EtoStringLog(log, e);
			return null;
		}
	}
	
	public JSONObject genericGetRequest(String SubURL) throws ClientException, ParseException
	{	
		JSONParser JP = new JSONParser();
		return (JSONObject) JP.parse(GC.customRequest(SubURL).buildRequest().get().toString());
	}
	
	public JSONObject genericPutRequest(String SubURL, Map<String, String>Body) throws ClientException, ParseException
	{	
		if(Body == null)
		{
			log.debug("NO BODY SUPPLIED!");
			return null;
		}
		
		JsonObject putObject = new JsonObject();
		
		for(Entry<String, String> Entry : Body.entrySet())
		{
			putObject.addProperty(Entry.getKey(), Entry.getValue());
		}
				
		JSONParser JP = new JSONParser();
		return (JSONObject) JP.parse(GC.customRequest(SubURL).buildRequest().put(putObject).toString());
	}
	
	

	public String getAccessToken()
	{
		switch(PT)
		{
		case Client:
			ClientCredentialProvider CCP = (ClientCredentialProvider) GC.getAuthenticationProvider();
			return CCP.getAcccessToken();
		case DeviceCode:
			DeviceCodeFlowProvider DCP = (DeviceCodeFlowProvider) GC.getAuthenticationProvider();
			return DCP.getAccessToken();
		default:
			return "__INVALID__TOKEN__";
		}
	}

	public void setBranch(String Branch) 
	{
		GC.setServiceRoot(Branch);
	}
}
