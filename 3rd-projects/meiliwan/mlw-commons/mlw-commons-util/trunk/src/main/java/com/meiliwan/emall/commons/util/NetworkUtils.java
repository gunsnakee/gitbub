package com.meiliwan.emall.commons.util;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;


public class NetworkUtils {
//	private final static ESLogger logger = Loggers.getLogger(NetworkUtils.class);

	static final MLWLogger logger = MLWLoggerFactory.getLogger(NetworkUtils.class);
	public static enum StackType {
		IPv4, IPv6, Unknown;
	}

	public static final String IPv4_SETTING = "java.net.preferIPv4Stack";
	public static final String IPv6_SETTING = "java.net.preferIPv6Addresses";

	public static final String NON_LOOPBACK_ADDRESS = "non_loopback_address";

	private final static InetAddress localAddress;

	static {
		InetAddress localAddressX = null;
		try {
			localAddressX = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			logger.error( e,localAddressX,null);
//			logger.error(e, "Failed to find local host", "");
		}
		localAddress = localAddressX;
	}

	public static Boolean defaultReuseAddress() {
		return true;
		// return OsUtils.WINDOWS ? null : true;
	}

	public static boolean isIPv4() {
		return System.getProperty("java.net.preferIPv4Stack") != null
				&& System.getProperty("java.net.preferIPv4Stack")
						.equals("true");
	}

	public static InetAddress getIPv4Localhost() throws UnknownHostException {
		return getLocalhost(StackType.IPv4);
	}

	public static InetAddress getIPv6Localhost() throws UnknownHostException {
		return getLocalhost(StackType.IPv6);
	}

	public static InetAddress getLocalAddress() {
		return localAddress;
	}

	public static InetAddress getLocalhost(StackType ip_version)
			throws UnknownHostException {
		if (ip_version == StackType.IPv4)
			return InetAddress.getByName("127.0.0.1");
		else
			return InetAddress.getByName("::1");
	}

	public static boolean canBindToMcastAddress() {
		return true;
		// return OsUtils.LINUX || OsUtils.SOLARIS || OsUtils.HP;
	}

	/**
	 * Returns the first non-loopback address on any interface on the current
	 * host.
	 * 
	 * @param ip_version
	 *            Constraint on IP version of address to be returned, 4 or 6
	 */
	public static InetAddress getFirstNonLoopbackAddress(StackType ip_version)
			throws SocketException {
		InetAddress address = null;

		Enumeration intfs = NetworkInterface.getNetworkInterfaces();
		while (intfs.hasMoreElements()) {
			NetworkInterface intf = (NetworkInterface) intfs.nextElement();
			if (!intf.isUp() || intf.isLoopback())
				continue;
			address = getFirstNonLoopbackAddress(intf, ip_version);
			if (address != null) {
				return address;
			}
		}
		return null;
	}

	/**
	 * Returns the first non-loopback address on the given interface on the
	 * current host.
	 * 
	 * @param intf
	 *            the interface to be checked
	 * @param ipVersion
	 *            Constraint on IP version of address to be returned, 4 or 6
	 */
	public static InetAddress getFirstNonLoopbackAddress(NetworkInterface intf,
			StackType ipVersion) throws SocketException {
		if (intf == null)
			throw new IllegalArgumentException(
					"Network interface pointer is null");

		for (Enumeration addresses = intf.getInetAddresses(); addresses
				.hasMoreElements();) {
			InetAddress address = (InetAddress) addresses.nextElement();
			if (!address.isLoopbackAddress()) {
				if ((address instanceof Inet4Address && ipVersion == StackType.IPv4)
						|| (address instanceof Inet6Address && ipVersion == StackType.IPv6))
					return address;
			}
		}
		return null;
	}

	/**
	 * A function to check if an interface supports an IP version (i.e has
	 * addresses defined for that IP version).
	 * 
	 * @param intf {@link java.net.NetworkInterface}
	 * @param ipVersion {@link com.meiliwan.emall.commons.util.NetworkUtils.StackType}
	 * @return boolean if true mean this interface have IP addresses, else not
	 */
	public static boolean interfaceHasIPAddresses(NetworkInterface intf,
			StackType ipVersion) throws SocketException, UnknownHostException {
		boolean supportsVersion = false;
		if (intf != null) {
			// get all the InetAddresses defined on the interface
			Enumeration addresses = intf.getInetAddresses();
			while (addresses != null && addresses.hasMoreElements()) {
				// get the next InetAddress for the current interface
				InetAddress address = (InetAddress) addresses.nextElement();

				// check if we find an address of correct version
				if ((address instanceof Inet4Address && (ipVersion == StackType.IPv4))
						|| (address instanceof Inet6Address && (ipVersion == StackType.IPv6))) {
					supportsVersion = true;
					break;
				}
			}
		} else {
			throw new UnknownHostException("network interface not found");
		}
		return supportsVersion;
	}

	/**
	 * Tries to determine the type of IP stack from the available interfaces and
	 * their addresses and from the system properties (java.net.preferIPv4Stack
	 * and java.net.preferIPv6Addresses)
	 * 
	 * @return StackType.IPv4 for an IPv4 only stack, StackYTypeIPv6 for an IPv6
	 *         only stack, and StackType.Unknown if the type cannot be detected
	 */
	public static StackType getIpStackType() {
		boolean isIPv4StackAvailable = isStackAvailable(true);
		boolean isIPv6StackAvailable = isStackAvailable(false);

		// if only IPv4 stack available
		if (isIPv4StackAvailable && !isIPv6StackAvailable) {
			return StackType.IPv4;
		}
		// if only IPv6 stack available
		else if (isIPv6StackAvailable && !isIPv4StackAvailable) {
			return StackType.IPv6;
		}
		// if dual stack
		else if (isIPv4StackAvailable && isIPv6StackAvailable) {
			// get the System property which records user preference for a stack
			// on a dual stack machine
			if (Boolean.getBoolean(IPv4_SETTING)) // has preference over
													// java.net.preferIPv6Addresses
				return StackType.IPv4;
			if (Boolean.getBoolean(IPv6_SETTING))
				return StackType.IPv6;
			return StackType.IPv6;
		}
		return StackType.Unknown;
	}

	public static boolean isStackAvailable(boolean ipv4) {
		Collection<InetAddress> allAddrs = getAllAvailableAddresses();
		for (InetAddress addr : allAddrs)
			if (ipv4 && addr instanceof Inet4Address
					|| (!ipv4 && addr instanceof Inet6Address))
				return true;
		return false;
	}

	public static List<NetworkInterface> getAllAvailableInterfaces()
			throws SocketException {
		List<NetworkInterface> allInterfaces = new ArrayList<NetworkInterface>(
				10);
		NetworkInterface intf;
		for (Enumeration en = NetworkInterface.getNetworkInterfaces(); en
				.hasMoreElements();) {
			intf = (NetworkInterface) en.nextElement();
			allInterfaces.add(intf);
		}
		return allInterfaces;
	}

	public static Collection<InetAddress> getAllAvailableAddresses() {
		Set<InetAddress> retval = new HashSet<InetAddress>();
		Enumeration en;

		try {
			en = NetworkInterface.getNetworkInterfaces();
			if (en == null)
				return retval;
			while (en.hasMoreElements()) {
				NetworkInterface intf = (NetworkInterface) en.nextElement();
				Enumeration<InetAddress> addrs = intf.getInetAddresses();
				while (addrs.hasMoreElements())
					retval.add(addrs.nextElement());
			}
		} catch (SocketException e) {
//			logger.error(e, "Failed to derive all available interfaces", "");
			logger.error(e,retval,null);
		}

		return retval;
	}

	static public int getNextAliablePort(String address, int startport)
			throws UnknownHostException {
		InetAddress a = InetAddress.getByName(address);
		return getNextAliablePort(a, startport);
	}

	static public int getNextAliablePort(InetAddress address, int startport) {
		int port = startport;
		while (port < 65535) {
			try {
				Socket socket = new Socket(address, port);
				socket.close();

				port++;
			} catch (Exception ex) {

				return port;

			}
		}

		return -1;

	}

	private NetworkUtils() {

	}

	
	public static Collection<InetAddress> getAllHostAddress() {  
        try {  
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();  
            Collection<InetAddress> addresses = new ArrayList<InetAddress>();  
              
            while (networkInterfaces.hasMoreElements()) {  
                NetworkInterface networkInterface = networkInterfaces.nextElement();  
                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();  
                while (inetAddresses.hasMoreElements()) {  
                    InetAddress inetAddress = inetAddresses.nextElement();  
                    addresses.add(inetAddress);  
                }  
            }  
              
            return addresses;  
        } catch (SocketException e) {  
            throw new RuntimeException(e.getMessage(), e);  
        }  
    }  
      
    public static Collection<String> getAllNoLoopbackAddresses() {  
        Collection<String> noLoopbackAddresses = new ArrayList<String>();  
        Collection<InetAddress> allInetAddresses = getAllHostAddress();  
          
        for (InetAddress address : allInetAddresses) {  
//        		System.out.println(address.isAnyLocalAddress());
//        		System.out.println(address.isLinkLocalAddress());//fe80:0:0:0:f6f9:51ff:fee5:65da%5 169.254.249.164 fe80:0:0:0:0:0:0:1%1 fe80:0:0:0:12dd:b1ff:fee2:b62f%4
//        		System.out.println(address.isLoopbackAddress());//127.0.0.1 0:0:0:0:0:0:0:1
//        		System.out.println(address.isMulticastAddress());
//        		System.out.println(address.isSiteLocalAddress());//192.168.2.1 10.249.9.187
//        		System.out.println(address.getHostAddress());
            if (!address.isLoopbackAddress()&&!address.isLinkLocalAddress()&&!address.isLoopbackAddress()) {  
                noLoopbackAddresses.add(address.getHostAddress());  
            }  
        }  
          
        return noLoopbackAddresses;  
    }  
      
    public static String getFirstNoLoopbackAddress() {  
        Collection<String> allNoLoopbackAddresses = getAllNoLoopbackAddresses();  
        if(allNoLoopbackAddresses.isEmpty()){
        		return  "";  
        }
        return allNoLoopbackAddresses.iterator().next();  
    }  

    
    
	public static void main(String[] ss) {
		System.out.println(getFirstNoLoopbackAddress());
		int a1=1;
		int a2=1;
		Integer b1 = new Integer(1);
		Integer b2 = new Integer(1);
		Short s1 = new Short((short)1);
		System.out.println(b1==b2);
		System.out.println(a1==b1);
		System.out.println(a1==a2);
		System.out.println(s1.equals(1));
	}
}
