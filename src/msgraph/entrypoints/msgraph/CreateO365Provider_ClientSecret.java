package nucom.module.msgraphs.entrypoints.msgraph;

import java.util.List;

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

@Function(name="Create O365 Provider (App-Secret)",visibility=Visibility.Public, rookieFunction=false, description="")
public class CreateO365Provider_ClientSecret implements IBaseExecutable 
{
	//##########################################################################################
			    		
	@InputVar(label="TenantID", description="Microsoft Azure Tenant ID",type=VariableType.STRING)
	public String TenantID="";
	
	@InputVar(label="ClientID", description="Microsoft Azure APP ID",type=VariableType.STRING)
	public String ClientID="";
	
	@InputVar(label="ClientSecret", description="Microsoft Azure APP Secret",type=VariableType.STRING)
	public String ClientSecret="";
	
	@InputVar(label="Scopes", description="List of Ms Graph Scopes",type=VariableType.LIST)
	public List<List<String>> ScopesRaw= null;
	
	@InputVar(label="Branch", description="Change the Branch Provider. DO NOT CHANGE IF YOU DON'T KNOW WHAT YOU'RE DOING",type=VariableType.STRING)
	public String Branch="https://graph.microsoft.com";
	
	@OutputVar(label="O365Provider", description="Return an Office365 Provider",type=VariableType.OBJECT)
	public Object O365Provider = null;
	
    StarfaceComponentProvider componentProvider = StarfaceComponentProvider.getInstance(); 
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@SuppressWarnings("unchecked")
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception 
	{
		Log log = context.getLog();
		log.debug("Creating Provider...");
		
		List<String> Scopes = null;
		
		//For some Reason STARFACE Provides a List[List[]] instead of a List[]
		try
		{
			Scopes = ScopesRaw.get(0);
		}
		catch(Exception e)
		{
			Object O= ScopesRaw;
			Scopes = (List<String>) O;
		}
		
		log.debug(TenantID);
		log.debug(ClientID);
		log.debug(ClientSecret);
		log.debug(Scopes);
		
		O365Provider Provider = new O365Provider(TenantID, ClientID, ClientSecret, Scopes, context.getLog());
		Provider.setBranch(Branch);
		O365Provider = Provider;
				
		log.debug("Provider Sucessfully created!");

		
	}//END OF EXECUTION

	
}
