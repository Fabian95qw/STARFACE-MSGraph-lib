package nucom.module.msgraphs.entrypoints.msgraph;

import org.apache.commons.logging.Log;

import de.starface.core.component.StarfaceComponentProvider;
import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.InputVar;
import de.vertico.starface.module.core.runtime.annotations.OutputVar;
import nucom.module.msgraphs.o365provider.O365Provider;
import nucom.module.msgraphs.utility.LogHelper;

@Function(visibility=Visibility.Public, rookieFunction=false, description="Returns a List of Users")
public class ExtractToken implements IBaseExecutable 
{
	//##########################################################################################
	@InputVar(label="O365Provider", description="Office365 Provider",type=VariableType.OBJECT)
	public Object O = null;
	
	@OutputVar(label="Token", description="Returns the current Token",type=VariableType.STRING)
	public String Token = "";
	
    StarfaceComponentProvider componentProvider = StarfaceComponentProvider.getInstance(); 
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception 
	{
		Log log = context.getLog();
		
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
		
		log.debug("Extracing Token..");
		
		Token=Provider.getAccessToken();
		
	}//END OF EXECUTION

}
