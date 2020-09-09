package nucom.module.msgraphs.o365provider;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.logging.Log;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.microsoft.graph.authentication.IAuthenticationProvider;
import com.microsoft.graph.http.IHttpRequest;

import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import nucom.module.msgraphs.token.TokenCache;
import nucom.module.msgraphs.token.TokenCacheManager;
import nucom.module.msgraphs.utility.LogHelper;

public class DeviceCodeFlowProvider implements IAuthenticationProvider 
{
	private String ClientID="";
	private String TenantID="";
	private String Device_Code="";
	
	private CompletableFuture<String> Message = new CompletableFuture<String>();
	
	private TokenCache TC = null;
	private IRuntimeEnvironment context =null;
	private Log log = null;	
	
	public DeviceCodeFlowProvider(String ClientID, String TenantID, IRuntimeEnvironment context)
	{
		this.log=context.getLog();
		this.context=context;
		TokenCacheManager TCM =(TokenCacheManager)context.provider().fetch(TokenCacheManager.class);
		
		this.ClientID=ClientID;
		this.TenantID=TenantID;
		
		try
		{
			log.debug("Attempting to load Cache for: "+ ClientID);
			TC = TCM.GetCacheforClientID(ClientID);
		}
		catch(Exception e)
		{
			LogHelper.EtoStringLog(log, e);
		}
		
		if (TC == null || TC.isExpired())
		{
			try
			{
				initauth();
			}
			catch(Exception e)
			{
				LogHelper.EtoStringLog(log, e);
			}
		}

	}
			
	private String encode(String S)
	{
		try 
		{
			return URLEncoder.encode(S, "UTF-8");
		} 
		catch (UnsupportedEncodingException e)
		{
			LogHelper.EtoStringLog(log, e);
		}
		return null;
	}
	 
	 private void initauth() throws Exception 
	 {
		 String BaseURL ="https://login.microsoftonline.com/"+TenantID+"/oauth2/v2.0/devicecode";
		 
		 URL U = new URL (BaseURL);
		 HttpsURLConnection HTTPC = (HttpsURLConnection)U.openConnection();
		 HTTPC.setRequestMethod("POST");
		 HTTPC.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		
		 String ID="client_id="+encode(ClientID);
		 String Scope="scope="+encode("offline_access .default");
		 String Param=ID+"&"+Scope;
		 		 
		 log.debug(Param);
		 
		 byte[] Data = Param.getBytes();
		 
		 HTTPC.setRequestProperty("charset", "utf-8");
		 HTTPC.setRequestProperty("Content-Length", Integer.toString(Data.length));
		 HTTPC.setUseCaches(false);
		 HTTPC.setDoOutput(true);
		 
		 DataOutputStream DOS =null;
		 try
		 {
			DOS = new DataOutputStream(HTTPC.getOutputStream());
		    DOS.write(Data);
		 }
		 catch(Exception e)
		 {
			 LogHelper.EtoStringLog(log, e);
			 DOS.close();
		 }
		 
		 try
		 {
			 BufferedReader BR = new BufferedReader(new InputStreamReader(HTTPC.getInputStream()));
			 StringBuilder SB = new StringBuilder();
			 
			 String Line;
			 while((Line = BR.readLine()) != null)
			 {
				 SB.append(Line);
			 }
			 BR.close();
			 DOS.close();
			 HTTPC.disconnect();
			 
			 log.debug(SB.toString());
			 JSONParser JP = new JSONParser();
			 JSONObject JSO = (JSONObject)JP.parse(SB.toString());
			 String User_Code = (String) JSO.get("user_code");
			 String SMessage= (String) JSO.get("message");
			 Message.complete(SMessage);
			 
			 Device_Code = (String) JSO.get("device_code");
			 
			 log.debug("Use Device-Code:"+User_Code);
		 }
		 catch(IOException e)
		 {
			 LogHelper.EtoStringLog(log, e);
			 BufferedReader BR = new BufferedReader(new InputStreamReader(HTTPC.getErrorStream()));
			 StringBuilder SB = new StringBuilder();
			 
			 String Line;
			 while((Line = BR.readLine()) != null)
			 {
				 SB.append(Line);
			 }
			 
			 BR.close();
			 HTTPC.disconnect();
			 
			 log.debug(SB.toString());
			 
		 }
		 catch(Exception e)
		 { 
			 LogHelper.EtoStringLog(log, e);
		 }
		 		 
	 }
	 
	 public void poll(Integer Timeout) throws Exception
	 {		 
		 Integer Counter =0;
		 
		 Timeout = Timeout/5;
		 HttpsURLConnection HTTPC=null;
		 
		 while(Timeout > Counter)
		 {
			 try
			 {
			
				 String BaseURL ="https://login.microsoftonline.com/common/oauth2/v2.0/token";
				 URL U = new URL (BaseURL);
				 HTTPC = (HttpsURLConnection)U.openConnection();
				 HTTPC.setRequestMethod("POST");
				 HTTPC.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
				
				 String ID="client_id="+ClientID;
				 String GrantType="grant_type="+"urn:ietf:params:oauth:grant-type:device_code";
				 String DeviceCode="device_code="+Device_Code;
				 String Param = GrantType+"&"+ID+"&"+DeviceCode;

				 byte[] Data = Param.getBytes();
				 		 
				 HTTPC.setRequestProperty("charset", "utf-8");
				 HTTPC.setRequestProperty("Content-Length", Integer.toString(Data.length));
				 HTTPC.setUseCaches(false);
				 HTTPC.setDoOutput(true);
				 
				 DataOutputStream DOS =null;
				 try
				 {
					DOS = new DataOutputStream(HTTPC.getOutputStream());
				    DOS.write(Data);
				 }
				 catch(Exception e)
				 {
					 LogHelper.EtoStringLog(log, e);
					 DOS.close();
				 }
				 DOS.close();
				 
				 BufferedReader BR = new BufferedReader(new InputStreamReader(HTTPC.getInputStream()));
				 StringBuilder SB = new StringBuilder();
				 
				 String Line;
				 while((Line = BR.readLine()) != null)
				 {
					 SB.append(Line);
				 }
				 BR.close();
				 HTTPC.disconnect();
				 
				 log.debug(SB.toString());
				 JSONParser JP = new JSONParser();
				 JSONObject JSO = (JSONObject)JP.parse(SB.toString());
				 
				 String Token = (String) JSO.get("access_token");
				 Long Expires= (Long) JSO.get("expires_in");
				 String RefreshToken ="";
				 try
				 {
					 RefreshToken = (String) JSO.get("refresh_token");
					 
				 }
				 catch(Exception e)
				 {
					 log.debug("No Refresh Token provided! Token will run out in:" + Expires+ " Seconds!");
				 }
				 TC = new TokenCache(ClientID, TenantID, Token, RefreshToken, Expires.intValue(), context);
				 TokenCacheManager TCM =(TokenCacheManager)context.provider().fetch(TokenCacheManager.class);
				 TCM.SaveCache(TC);
				 break;
			 }
			 catch(IOException e)
			 {
				 BufferedReader BR = new BufferedReader(new InputStreamReader(HTTPC.getErrorStream()));
				 StringBuilder SB = new StringBuilder();
				 
				 String Line;
				 while((Line = BR.readLine()) != null)
				 {
					 SB.append(Line);
				 }
				 
				 BR.close();
				 HTTPC.disconnect();
				 //log.debug(SB.toString());
				 JSONParser JP = new JSONParser();
				 JSONObject JSO = (JSONObject)JP.parse(SB.toString());
				 String Error = (String) JSO.get("error");
				 switch(Error)
				 {
					 case "authorization_pending":
						 log.debug("Authorization is pending");
						 break;
					 case "authorization_declined":
						 log.debug("Authorization for this Action was declined");
						 return;
					 case "bad_verification_code":
						 log.debug("Verification code is invalid");
						 return;
					 case "expired_token":
						 log.debug("Token has expired");
						 return;
					 default:
						 break;
				 }
			 }
			 catch(Exception e)
			 { 
				 LogHelper.EtoStringLog(log, e);
			 }
			 Counter++;
			 Thread.sleep(5000);
		 }
		 
	 }

	 	 
	@Override
	public void authenticateRequest(IHttpRequest request) 
	{
		if(TC == null)
		{
			return;
		}
		request.addHeader("Authorization", "Bearer " + TC.getToken());
	}

	public String getMessage(Integer Timeout)
	{
		try 
		{
			return Message.get(Timeout, TimeUnit.SECONDS);
		} 
		catch (InterruptedException | ExecutionException | TimeoutException e) 
		{
			LogHelper.EtoStringLog(log, e);
			return "CODEFLOW_ERROR";
		}
	}
	
	public String getDeviceCode()
	{
		return Device_Code;
	}

	public boolean hasToken() 
	{
		if(TC == null) {return false;}
		return TC.hasToken();
	}
	
	public String getAccessToken()
	{
		if(TC == null) {return "";}
		return TC.getToken();
	}
	
	
}