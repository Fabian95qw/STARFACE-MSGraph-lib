package si.module.msgraphs.entrypoints.msgraph;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import de.starface.core.component.StarfaceComponentProvider;
import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.InputVar;
import de.vertico.starface.module.core.runtime.annotations.OutputVar;
import si.module.msgraphs.o365provider.O365Provider;
import si.module.msgraphs.utility.JSONConverter;
import si.module.msgraphs.utility.LogHelper;
import si.module.msgraphs.utility.EnumHelper.OutputType;

@Function(visibility=Visibility.Public, rookieFunction=false, description="Make a generic PUT request to the API")
public class GenericPutRequest implements IBaseExecutable 
{
	//##########################################################################################
	@InputVar(label="O365Provider", description="Office365 Provider",type=VariableType.OBJECT)
	public Object O = null;
	
	@InputVar(label="TargetURL", description="Target Sub URL of the GRAPH REST API for example: /users/user@example.com/",type=VariableType.STRING)
	public String TargetURL="";
	
	@InputVar(label="Body", description="",type=VariableType.MAP)
	public Map<String, String>Body = null;
	
	@InputVar(label="OutputType", description="", valueByReferenceAllowed=true)
	public OutputType OT = OutputType.RAWJSON;
	
	@OutputVar(label="RAWJson", description="Returns a RAW JSON String",type=VariableType.STRING)
	public String RAWJson ="";
	
	@OutputVar(label="LIST", description="Returns a List<Object>",type=VariableType.LIST)
	public List<Object> RAWList = null;
	
	@OutputVar(label="MAP", description="Returns a Map<String,Object>",type=VariableType.MAP)
	public Map<String, Object> RAWMap = null;
	
    StarfaceComponentProvider componentProvider = StarfaceComponentProvider.getInstance(); 
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception 
	{
		Logger log = context.getLog();
		
		log.debug("Loading Provider...");
		O365Provider Provider = null;
		try
		{
			Provider = (O365Provider)O;
		}
		catch(Exception e)
		{
			log.debug("Provider is not valid!");
			LogHelper.EtoStringLog(log, e);
			return;
		}
		
		log.debug("Executing Generic Request:" + TargetURL);
		try
		{
		Object JSON = Provider.genericPutRequest(TargetURL, Body);
		
			switch(OT)
			{
			case LIST:
				RAWList = JSONConverter.toList((JSONArray) JSON);
				break;
			case MAP:
				RAWMap = JSONConverter.toMap((JSONObject) JSON);
				break;
			case RAWJSON:
				RAWJson = JSON.toString();
				break;
			default:
				break;
			}
		}
		catch(Exception e)
		{
			LogHelper.EtoStringLog(log, e);
		}
		
	}//END OF EXECUTION

    
}
