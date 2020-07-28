package nucom.module.msgraphs.entrypoints.tokencache;

import org.apache.commons.logging.Log;

import de.starface.core.component.StarfaceComponentProvider;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import nucom.module.msgraphs.token.TokenCacheManager;

@Function(name="StartComponent", visibility=Visibility.Private, rookieFunction=false, description="")
public class StartComponent implements IBaseExecutable 
{
	//##########################################################################################		
    StarfaceComponentProvider componentProvider = StarfaceComponentProvider.getInstance(); 
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception 
	{	
		Log log = context.getLog();
		TokenCacheManager TC = (TokenCacheManager)context.provider().fetch(TokenCacheManager.class);

		
		log.debug("Checking on TokenCacheManager");
		if(!TC.isRunning())
		{
			log.debug("TokenCacheManager isn't Running!");
			log.debug("Starting TokenCacheManager...");
			try 
			{
				TC.startComponent();
				TC.updateContext(context);
				TC.loadall();
			} 
			catch (Throwable e) 
			{
				log.debug(e.getMessage());
			}
		}
		else
		{
			log.debug("TokenCacheManager is Running!");
		}


	}//END OF EXECUTION

	
}
