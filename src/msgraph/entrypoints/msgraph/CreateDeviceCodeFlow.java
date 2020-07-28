package nucom.module.msgraphs.entrypoints.msgraph;

import de.starface.core.component.StarfaceComponentProvider;
import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.InputVar;
import de.vertico.starface.module.core.runtime.annotations.OutputVar;
import nucom.module.msgraphs.o365provider.DeviceCodeFlowProvider;
import nucom.module.msgraphs.utility.LogHelper;
import org.apache.commons.logging.Log;

@Function(name="CreateCodeFlowProvider", visibility=Visibility.Public, rookieFunction=false, description="Used to start the CodeFlow for a DeviceCode, which can be used for the O365Provider (DeviceCode)")
public class CreateDeviceCodeFlow implements IBaseExecutable 
{
	//##########################################################################################
	
	@InputVar(label="ClientID", description="Microsoft Azure APP ID",type=VariableType.STRING)
	public String ClientID="";
	
	@InputVar(label="TenantID", description="Microsoft Azure Tenant ID",type=VariableType.STRING)
	public String TenantID="";
		
	@InputVar(label="Timeout", description="Timeout for Message (stops Module from getting stuck)",type=VariableType.NUMBER)
	public Integer Timeout=60;
		
	@OutputVar(label="Message", description="Message with Instructions for User to Delegate Rights",type=VariableType.STRING)
	public String Message ="";
		
	@OutputVar(label="DeviceCodeFlowProvider", description="Use with Create O365 Provider (DeviceCode) once the Rights have been delegated",type=VariableType.OBJECT)
	public Object DeviceCodeFlowProvider = null;
	
    StarfaceComponentProvider componentProvider = StarfaceComponentProvider.getInstance(); 
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception 
	{
		Log log = context.getLog();
		log.debug("Creating Authorization with Device Code flow...");
		
		log.debug(ClientID);
				
		DeviceCodeFlowProvider DCP = new DeviceCodeFlowProvider(ClientID, TenantID, context);
		
		
		if(!DCP.hasToken())
		{
			log.debug("Trying to Probe for Message...");
			try
			{
				Message = DCP.getMessage(Timeout);
			}
			catch(Exception e)
			{
				LogHelper.EtoStringLog(log, e);
			}
		}
		DeviceCodeFlowProvider = DCP;
		
		
	}//END OF EXECUTION

	
}
