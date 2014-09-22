package jobs;

import play.jobs.*;
import java.net.Authenticator;
import helpers.*;
 
@OnApplicationStart
public class Bootstrap extends Job {
    
    public void doJob() {
	// SOCKS Proxy
         QuotaGuardProxyAuthenticator proxy = new QuotaGuardProxyAuthenticator();
         Authenticator.setDefault(proxy.getAuth());
    }
    
}
