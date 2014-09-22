package jobs;

import play.jobs.*;
import java.net.Authenticator;
import helpers.*;
import java.net.*;

@OnApplicationStart
public class Bootstrap extends Job {
    
    public void doJob() {
	System.out.println("Initializing SOCKS Proxy");
	// SOCKS Proxy
        QuotaGuardProxyAuthenticator proxy = new QuotaGuardProxyAuthenticator();
        Authenticator.setDefault(proxy.getAuth());
        QuotaGuardProxySelector ps = new QuotaGuardProxySelector(ProxySelector.getDefault());
        ProxySelector.setDefault(ps);
    }
    
}
