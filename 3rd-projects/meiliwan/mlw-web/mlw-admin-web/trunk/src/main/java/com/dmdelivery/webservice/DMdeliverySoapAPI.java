package com.dmdelivery.webservice;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This is the DMdelivery SOAP webservice. 
 * 
 * 	In order to be able to use this webservice, you need to have a login for DMdelivery. A login consists of a username and a password. Each SOAP call in the webservice needs this username and password for access! Please inquire with the DMdelivery administrator for a login.
 * 	
 *
 * This class was generated by Apache CXF 2.7.10
 * 2014-03-06T09:59:26.521+08:00
 * Generated source version: 2.7.10
 * 
 */
@WebServiceClient(name = "DMdeliverySoapAPI", 
                  wsdlLocation = "http://meiliwan1.dmdelivery.com/x/soap-v3/wsdl.php",
                  targetNamespace = "http://dmdelivery.com/webservice/") 
public class DMdeliverySoapAPI extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://dmdelivery.com/webservice/", "DMdeliverySoapAPI");
    public final static QName DMdeliverySoapAPIPort = new QName("http://dmdelivery.com/webservice/", "DMdeliverySoapAPIPort");
    static {
        URL url = null;
        try {
            url = new URL("http://meiliwan1.dmdelivery.com/x/soap-v3/wsdl.php");
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(DMdeliverySoapAPI.class.getName())
                .log(java.util.logging.Level.INFO, 
                     "Can not initialize the default wsdl from {0}", "http://meiliwan1.dmdelivery.com/x/soap-v3/wsdl.php");
        }
        WSDL_LOCATION = url;
    }

    public DMdeliverySoapAPI(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public DMdeliverySoapAPI(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public DMdeliverySoapAPI() {
        super(WSDL_LOCATION, SERVICE);
    }
    

    /**
     *
     * @return
     *     returns DMdeliverySoapAPIPort
     */
    @WebEndpoint(name = "DMdeliverySoapAPIPort")
    public DMdeliverySoapAPIPort getDMdeliverySoapAPIPort() {
        return super.getPort(DMdeliverySoapAPIPort, DMdeliverySoapAPIPort.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns DMdeliverySoapAPIPort
     */
    @WebEndpoint(name = "DMdeliverySoapAPIPort")
    public DMdeliverySoapAPIPort getDMdeliverySoapAPIPort(WebServiceFeature... features) {
        return super.getPort(DMdeliverySoapAPIPort, DMdeliverySoapAPIPort.class, features);
    }

}
