package si.module.msgraphs.token;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Logger;

import de.starface.core.component.StarfaceComponent;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import si.module.msgraphs.utility.LogHelper;
public class TokenCacheManager extends StarfaceComponent
{
	private static boolean isRunning = false;
	private static Logger Sublog = null;
	private static IRuntimeEnvironment context=null;
	private static List<TokenCache> LoadedCaches = null;
	
	private static String CacheFolder = "/home/starface/msgraph/tokencache";
	
	
	@Override
	public void startComponent() throws Throwable 
	{
		isRunning = true;
		LoadedCaches = new ArrayList<TokenCache>();
	}
	

	public void updateContext(IRuntimeEnvironment Icontext)
	{
		context = Icontext;
		Sublog = Icontext.getLog();
		Sublog.debug("[TCM] TokenCacheManager OK!");
	}
	
	public void loadall()
	{
		File F = new File(CacheFolder);
		if(!F.exists())
		{
			F.mkdirs();
		}
		
		for(File eachFile : F.listFiles())
		{
			if(eachFile.isFile())
			{
				Sublog.debug("Loading: " + eachFile.getAbsolutePath());
				LoadCache(eachFile);
			}
		}
	}
	
	public void LoadCache(File F)
	{
		Sublog.debug("[TCM] Attempting to load Cache...");
		try
		{
			Sublog.debug("[TCM] Loading Cache...");
			Sublog.debug(F.getAbsolutePath());
			if(!F.isFile())
			{	
				Sublog.debug("[TCM] Cache is not a file!");
				return;
			}
			if(!F.exists())
			{
				log.debug("[TCM] File does not exist. aborting...");
				return;
			}
			
			FileInputStream FIS = new FileInputStream(F);
			ObjectInputStream OIS = new ObjectInputStream(FIS);
			TokenSerial TS = (TokenSerial) OIS.readObject();
			OIS.close();
			
			if(TS.isexpired())
			{
				Sublog.debug("[TCM]"+F.getAbsolutePath()+" is expired!");
				F.delete();
				return;
			}
			
			LoadedCaches.add(new TokenCache(TS, context));
		}
		catch(Exception e)
		{
			LogHelper.EtoStringLog(log, e);
		}
	}
	
	public TokenCache GetCacheforClientID(String ClientID)
	{
		Sublog.debug("[TCM] Looking for valid TokenCache for: + ClientID");
		for(TokenCache TC : LoadedCaches)
		{
			if(TC.getClientID().equals(ClientID))
			{
				Sublog.debug("[TCM] TokenCache found!");
				if(TC.isExpired())
				{
					Sublog.debug("[TCM] But cache was expired!");
					return null;
				}
				return TC;
			}
		}
		Sublog.debug("No valid Cache found!");
		return null;
	}
		
	
	public String Filename(TokenCache TC)
	{
		return CacheFolder+"/"+TC.getClientID()+".ser";
	}
	
	public void SaveCache(TokenCache TC)
	{		
		File F = new File(Filename(TC));
		Sublog.debug("[TCM] Attempting to save Cache...");
		try
		{
			Sublog.debug("[TCM] Saving Cache...");
			Sublog.debug(F.getAbsolutePath());
			if(F.exists() && F.isFile())
			{
				Sublog.debug("[TCM] File exists. Deleting...");
				F.delete();
			}
			
			if(!F.getParentFile().exists())
			{
				F.getParentFile().mkdirs();
			}
				
			FileOutputStream FOS = new FileOutputStream(F);
			ObjectOutputStream OOS = new ObjectOutputStream(FOS);
			OOS.writeObject(TC.getTS());
			OOS.close();
			Sublog.debug("[TCM] Saved!");
			if(GetCacheforClientID(TC.getClientID()) == null)
			{
				Sublog.debug("[TCM] Adding it to Loaded Caches...");
				LoadedCaches.add(TC);
			}
			
		}
		catch(Exception e)
		{
			LogHelper.EtoStringLog(Sublog, e);
		}
	}
	
	
	@Override
	protected boolean startupCondition() 
	{
		return true;
	}

	public boolean isRunning()
	{
		return isRunning;
	}

	@Override
	public void shutdownComponent() throws Throwable 
	{
		isRunning = false;
	}
	
	
	
}
