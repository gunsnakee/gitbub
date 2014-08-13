package com.meiliwan.emall.icetool;

import Ice.ObjectAdapter;

import com.meiliwan.emall.commons.exception.BaseException;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.BaseConfig;
import com.meiliwan.emall.commons.util.ConfigOnZk;
import com.meiliwan.emall.service.impl.CommonServiceIceImpl;

public class ServiceLoader {

	private static final MLWLogger LOG = MLWLoggerFactory
			.getLogger(ServiceLoader.class);

	public static void loadService(Ice.ObjectAdapter adapter,
			AdapterCreator creator) {
		String iceConfig = BaseConfig.getValue("projectName")
				+ "/ice_config.xml";

		String identity;
		try {
			identity = ConfigOnZk.getInstance().getValue(iceConfig,
					"services.service(0)[@identity]");

			String replicaGroup = ConfigOnZk.getInstance().getValue(iceConfig,
					"services.service(0)[@replicaGroup]");
			String localPort = ConfigOnZk.getInstance().getValue(iceConfig,
					"services.service(0)[@localPort]");
			if (adapter == null) {
				adapter = creator.createAdapter(replicaGroup, localPort);
			}

			LOG.debug("begin to add '" + identity + "@" + replicaGroup
					+ "' from config '" + iceConfig + "'...");

			adapter.add(new CommonServiceIceImpl(),
					Ice.Util.stringToIdentity(identity));

			LOG.debug("'" + identity + "@" + replicaGroup + "' added success!");

			adapter.activate();
		} catch (BaseException ex) {
			LOG.error(ex, "service load error", null);
		}
	}

	interface AdapterCreator {

		Ice.ObjectAdapter createAdapter(String adapterName, String port);

	}

	static class LocalAdapterCreator implements AdapterCreator {
		private Ice.Communicator ic;

		public LocalAdapterCreator(Ice.Communicator ic) {
			this.ic = ic;
		}

		@Override
		public ObjectAdapter createAdapter(String adapterName, String port) {
			return ic.createObjectAdapterWithEndpoints(adapterName,
					"default -p " + port);
		}

	}

}
