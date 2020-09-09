package nucom.module.msgraphs.token;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TokenSerial implements Serializable
{
	private static final long serialVersionUID = 1694667372909526837L;
	private static SimpleDateFormat SDF = new SimpleDateFormat("dd.MM.yy HH:mm:ss");
	private String ClientID="";
	private String TenantID="";
	private String Token="";
	private String RefreshToken="";
	private Long ExpiresTime=0L;
	
	public TokenSerial(String ClientID, String TenantID, String Token, String RefreshToken, long ExpiresTime)
	{
		this.ClientID=ClientID;
		this.TenantID=TenantID;
		this.Token=Token;
		this.RefreshToken=RefreshToken;
		this.ExpiresTime=ExpiresTime;
	}
	
	public String getTenantID() {
		return TenantID;
	}

	public String getClientID() {
		return ClientID;
	}
	public String getToken() {
		return Token;
	}
	public String getRefreshToken() {
		return RefreshToken;
	}

	public Long ExpiresinSeconds()
	{
		Long Now = new Date().getTime();
		Long Difference = ExpiresTime - Now;
		
		return Difference/1000;		
	}
	
	public boolean isexpired() 
	{
		return (ExpiresinSeconds() <=0);
	}

	public String getExpiresDateFormatted() 
	{
		return SDF.format(new Date(ExpiresTime));
	}
	
}
