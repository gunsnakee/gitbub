package com.meiliwan.emall.icetool;

import Ice.Properties;
import Ice.Util;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;

public class IceServerTester {
	
	private static final MLWLogger LOG = MLWLoggerFactory
			.getLogger(IceServerTester.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Ice.Communicator ic = null;

		try {
            Ice.InitializationData iceData = new Ice.InitializationData();
            Properties prop = Util.createProperties();
            prop.setProperty("Ice.MessageSizeMax", "40960");
            iceData.properties = prop;

            ic = Ice.Util.initialize(iceData);
			
			ServiceLoader.loadService(null, new ServiceLoader.LocalAdapterCreator(ic));
			
		} catch (Ice.LocalException le) {
			LOG.error(le, "service load error", null);
		} catch (Exception ex) {
			LOG.error(ex, "service load error", null);
		} 

	}

}
