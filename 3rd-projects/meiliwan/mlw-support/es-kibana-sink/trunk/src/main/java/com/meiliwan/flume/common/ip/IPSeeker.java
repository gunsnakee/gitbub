package com.meiliwan.flume.common.ip;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * <p>
 * 参考文章: http://www.javaeye.com/topic/340548
 * 数据格式：http://lumaqq.linuxsir.org/article/qqwry_format_detail.html
 * </p>
 * 
 * author:Junsen_ye
 */
public class IPSeeker {
	// 纯真IP数据库文件
	private String IP_FILE = "qqwry.dat";

	// 记录长度
	private static final int IP_RECORD_LENGTH = 7;
	
	//数据记录的模式：重复的国家与地区信息使用偏移量
	private static final byte REDIRECT_MODE_1 = 0x01;//国家和地区都在偏移后
	private static final byte REDIRECT_MODE_2 = 0x02;//国家偏移，地区不偏移

	// 用来做为cache，查询一个ip时首先查看cache，以减少不必要的重复查找
	private Map<String, IPLocation> ipCache;
	
	// 随机文件访问类
	private RandomAccessFile ipFile;
	
	// 内存映射文件
	private MappedByteBuffer mbb;
	
	// 文件头只有8个字节。首四个字节是第一条索引的绝对偏移，后四个字节是最后一条索引的绝对偏移
	private long ipBegin, ipEnd;
	
	// 为提高效率而采用的临时变量
	private IPLocation loc;
	private byte[] b4;
	private byte[] b3;
	
	
	public IPSeeker(RandomAccessFile ipFile) throws FileNotFoundException{
		this.ipFile = ipFile;
		setUp();
	}
	public IPSeeker() throws FileNotFoundException {
		setUp();
	}
	
	private void setUp()throws FileNotFoundException {
		ipCache = new HashMap<String, IPLocation>();
		loc = new IPLocation();
		b4 = new byte[4];
		b3 = new byte[3];
		
		try {
			URL url = IPSeeker.class.getClassLoader().getResource(IP_FILE);
//			System.out.println(url);
			try {
				
				ipFile = new RandomAccessFile(new File(url.toURI()), "r");
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			throw(e);
		}
		// 如果打开文件成功，读取文件头信息
		if (ipFile != null) {
			try {
				ipBegin = readLong4(0);
				ipEnd = readLong4(4);
				if (ipBegin == -1 || ipEnd == -1) {
					ipFile.close();
					ipFile = null;
				}
			} catch (IOException e) {
				ipFile = null;
			}
		}
	}

	/**
	 * 给定一个ip国家地区记录的偏移，返回一个IPLocation结构，此方法应用与内存映射文件方式
	 * 
	 * @param offset
	 *            国家记录的起始偏移
	 * @return IPLocation对象
	 */
	private IPLocation getIPLocation(int offset) {
		// 跳过4字节ip
		mbb.position(offset + 4);
		// 读取第一个字节判断是否标志字节
		byte b = mbb.get();
		if (b == REDIRECT_MODE_1) {
			// 读取国家偏移
			int countryOffset = readInt3();
			// 跳转至偏移处
			mbb.position(countryOffset);
			// 再检查一次标志字节，因为这个时候这个地方仍然可能是个重定向
			b = mbb.get();
			if (b == REDIRECT_MODE_2) {
				loc.setCountry(readString(readInt3()));
				mbb.position(countryOffset + 4);
			} else
				loc.setCountry(readString(countryOffset));
			// 读取地区标志
			loc.setArea(readArea(mbb.position()));
		} else if (b == REDIRECT_MODE_2) {
			loc.setCountry(readString(readInt3()));
			loc.setArea(readArea(offset + 8));
		} else {
			loc.setCountry(readString(mbb.position() - 1));
			loc.setArea(readArea(mbb.position()));
		}
		return loc;
	}

	/**
	 * 给定一个ip国家地区记录的偏移，返回一个IPLocation结构
	 * 
	 * @param offset
	 *            国家记录的起始偏移
	 * @return IPLocation对象
	 */
	private IPLocation getIPLocation(long offset) {
		try {
			// 跳过4字节ip
			ipFile.seek(offset + 4);
			// 读取第一个字节判断是否标志字节
			byte b = ipFile.readByte();
			if (b == REDIRECT_MODE_1) {
				// 读取国家偏移
				long countryOffset = readLong3();
				// 跳转至偏移处
				ipFile.seek(countryOffset);
				// 再检查一次标志字节，因为这个时候这个地方仍然可能是个重定向
				b = ipFile.readByte();
				if (b == REDIRECT_MODE_2) {
					loc.setCountry(readString(readLong3()));
					ipFile.seek(countryOffset + 4);
				} else
					loc.setCountry(readString(countryOffset));
				// 读取地区标志
				loc.setArea(readArea(ipFile.getFilePointer()));
			} else if (b == REDIRECT_MODE_2) {
				loc.setCountry(readString(readLong3()));
				loc.setArea(readArea(offset + 8));
			} else {
				loc.setCountry(readString(ipFile.getFilePointer() - 1));
				loc.setArea(readArea(ipFile.getFilePointer()));
			}
			return loc;
		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * 根据ip搜索ip信息文件，得到IPLocation结构，所搜索的ip参数从类成员ip中得到
	 * 
	 * @param ip
	 *            要查询的IP
	 * @return IPLocation结构
	 */
	private IPLocation getIPLocation(byte[] ip) {
		IPLocation info = null;
		long offset = locateIP(ip);
		if (offset != -1)
			info = getIPLocation(offset);
		if (info == null) {
			info = new IPLocation();
			info.setCountry(Message.unknown_country);
			info.setArea(Message.unknown_area);
		}
		return info;
	}

	/**
	 * 根据IP得到国家名
	 * 
	 * @param ip
	 *            ip的字节数组形式
	 * @return 国家名字符串
	 */
	public IPLocation getIPLocation(String ip) {
		if (ipFile == null){// 检查ip地址文件是否正常
			// Message.bad_ip_file;
			return null;
		}
		byte[] ipByte = IPUtil.getIpByteArrayFromString(ip);
		if (ipCache.containsKey(ip)) {
			IPLocation ipLoc = ipCache.get(ip);
			return ipLoc;
		} else {
			IPLocation ipLoc = getIPLocation(ipByte);
			ipCache.put(ip, ipLoc.getCopy());
			return ipLoc;
		}
	}

	/**
	 * 给定一个地点的不完全名字，得到一系列包含s子串的IP范围记录
	 * 
	 * @param s
	 *            地点子串
	 * @return 包含IPEntry类型的List
	 */
	public List<IPEntry> getIPEntries(String s) {
		List<IPEntry> ret = new ArrayList<IPEntry>();
		try {
			// 映射IP信息文件到内存中
			if (mbb == null) {
				FileChannel fc = ipFile.getChannel();
				mbb = fc.map(FileChannel.MapMode.READ_ONLY, 0, ipFile.length());
				mbb.order(ByteOrder.LITTLE_ENDIAN);
			}

			int endOffset = (int) ipEnd;
			for (int offset = (int) ipBegin + 4; offset <= endOffset; offset += IP_RECORD_LENGTH) {
				int temp = readInt3(offset);
				if (temp != -1) {
					IPLocation ipLoc = getIPLocation(temp);
					// 判断是否这个地点里面包含了s子串，如果包含了，添加这个记录到List中，如果没有，继续
					if (ipLoc.getCountry().indexOf(s) != -1
							|| ipLoc.getArea().indexOf(s) != -1) {
						IPEntry entry = new IPEntry();
						entry.country = ipLoc.getCountry();
						entry.area = ipLoc.getArea();
						// 得到起始IP
						readIP(offset - 4, b4);
						entry.beginIp = IPUtil.getIpStringFromBytes(b4);
						// 得到结束IP
						readIP(temp, b4);
						entry.endIp = IPUtil.getIpStringFromBytes(b4);
						// 添加该记录
						ret.add(entry);
					}
				}
			}
		} catch (IOException e) {
		}
		return ret;
	}

	/**
	 * 给定一个地点的不完全名字，得到一系列包含s子串的IP范围记录
	 * 
	 * @param s
	 *            地点子串
	 * @return 包含IPEntry类型的List
	 */
	public List getIPEntriesDebug(String s) {
		List<IPEntry> ret = new ArrayList<IPEntry>();
		long endOffset = ipEnd + 4;
		for (long offset = ipBegin + 4; offset <= endOffset; offset += IP_RECORD_LENGTH) {
			// 读取结束IP偏移
			long temp = readLong3(offset);
			// 如果temp不等于-1，读取IP的地点信息
			if (temp != -1) {
				IPLocation ipLoc = getIPLocation(temp);
				// 判断是否这个地点里面包含了s子串，如果包含了，添加这个记录到List中，如果没有，继续
				if (ipLoc.getCountry().indexOf(s) != -1
						|| ipLoc.getArea().indexOf(s) != -1) {
					IPEntry entry = new IPEntry();
					entry.country = ipLoc.getCountry();
					entry.area = ipLoc.getArea();
					// 得到起始IP
					readIP(offset - 4, b4);
					entry.beginIp = IPUtil.getIpStringFromBytes(b4);
					// 得到结束IP
					readIP(temp, b4);
					entry.endIp = IPUtil.getIpStringFromBytes(b4);
					// 添加该记录
					ret.add(entry);
				}
			}
		}
		return ret;
	}

	/**
	 * 从内存映射文件的当前位置开始的3个字节读取一个int
	 * 
	 * @return
	 */
	private int readInt3() {
		return mbb.getInt() & 0x00FFFFFF;
	}

	/**
	 * 从内存映射文件的offset位置开始的3个字节读取一个int
	 * 
	 * @param offset
	 * @return
	 */
	private int readInt3(int offset) {
		mbb.position(offset);
		return mbb.getInt() & 0x00FFFFFF;
	}

	/**
	 * 从offset位置读取3个字节为一个long，因为java为big-endian格式，所以没办法 用了这么一个函数来做转换
	 * 
	 * @param offset
	 *            整数的起始偏移
	 * @return 读取的long值，返回-1表示读取文件失败
	 */
	private long readLong3(long offset) {
		long ret = 0;
		try {
			ipFile.seek(offset);
			ipFile.readFully(b3);
			ret |= (b3[0] & 0xFF);
			ret |= ((b3[1] << 8) & 0xFF00);
			ret |= ((b3[2] << 16) & 0xFF0000);
			return ret;
		} catch (IOException e) {
			return -1;
		}
	}

	/**
	 * 从当前位置读取3个字节转换成long
	 * 
	 * @return 读取的long值，返回-1表示读取文件失败
	 */
	private long readLong3() {
		long ret = 0;
		try {
			ipFile.readFully(b3);
			ret |= (b3[0] & 0xFF);
			ret |= ((b3[1] << 8) & 0xFF00);
			ret |= ((b3[2] << 16) & 0xFF0000);
			return ret;
		} catch (IOException e) {
			return -1;
		}
	}

	/**
	 * 从offset位置读取4个字节为一个long，因为java为big-endian格式
	 * 
	 * @param offset
	 * @return 读取的long值，返回-1表示读取文件失败
	 */
	private long readLong4(long offset) {
		long ret = 0;
		try {
			ipFile.seek(offset);
			ret |= (ipFile.readByte() & 0xFF);
			ret |= ((ipFile.readByte() << 8) & 0xFF00);
			ret |= ((ipFile.readByte() << 16) & 0xFF0000);
			ret |= ((ipFile.readByte() << 24) & 0xFF000000);
			return ret;
		} catch (IOException e) {
			return -1;
		}
	}

	/**
	 * 从offset位置读取四个字节的ip地址放入ip数组中，读取后的ip为big-endian格式，但是
	 * 文件中是little-endian形式，将会进行转换
	 * 
	 * @param offset
	 * @param ip
	 */
	private void readIP(long offset, byte[] ip) {
		try {
			ipFile.seek(offset);
			ipFile.readFully(ip);
			byte temp = ip[0];
			ip[0] = ip[3];
			ip[3] = temp;
			temp = ip[1];
			ip[1] = ip[2];
			ip[2] = temp;
		} catch (IOException e) {
		}
	}

	/**
	 * 从offset位置读取四个字节的ip地址放入ip数组中，读取后的ip为big-endian格式，但是
	 * 文件中是little-endian形式，将会进行转换
	 * 
	 * @param offset
	 * @param ip
	 */
	private void readIP(int offset, byte[] ip) {
		mbb.position(offset);
		mbb.get(ip);
		byte temp = ip[0];
		ip[0] = ip[3];
		ip[3] = temp;
		temp = ip[1];
		ip[1] = ip[2];
		ip[2] = temp;
	}

	/**
	 * @param offset
	 *            地区记录的起始偏移
	 * @return 地区名字符串
	 */
	private String readArea(int offset) {
		mbb.position(offset);
		byte b = mbb.get();
		if (b == REDIRECT_MODE_1 || b == REDIRECT_MODE_2) {
			int areaOffset = readInt3();
			if (areaOffset == 0)
				return Message.unknown_area;
			else
				return readString(areaOffset);
		} else
			return readString(offset);
	}

	/**
	 * 从offset偏移开始解析后面的字节，读出一个地区名
	 * 
	 * @param offset
	 *            地区记录的起始偏移
	 * @return 地区名字符串
	 * @throws java.io.IOException
	 */
	private String readArea(long offset) throws IOException {
		ipFile.seek(offset);
		byte b = ipFile.readByte();
		if (b == REDIRECT_MODE_1 || b == REDIRECT_MODE_2) {
			long areaOffset = readLong3(offset + 1);
			if (areaOffset == 0)
				return Message.unknown_area;
			else
				return readString(areaOffset);
		} else
			return readString(offset);
	}

	/**
	 * 把类成员ip和beginIp比较，注意这个beginIp是big-endian的
	 * 
	 * @param ip
	 *            要查询的IP
	 * @param beginIp
	 *            和被查询IP相比较的IP
	 * @return 相等返回0，ip大于beginIp则返回1，小于返回-1。
	 */
	private int compareIP(byte[] ip, byte[] beginIp) {
		for (int i = 0; i < 4; i++) {
			int r = compareByte(ip[i], beginIp[i]);
			if (r != 0)
				return r;
		}
		return 0;
	}

	/**
	 * 把两个byte当作无符号数进行比较
	 * 
	 * @param b1
	 * @param b2
	 * @return 若b1大于b2则返回1，相等返回0，小于返回-1
	 */
	private int compareByte(byte b1, byte b2) {
		if ((b1 & 0xFF) > (b2 & 0xFF)) // 比较是否大于
			return 1;
		else if ((b1 ^ b2) == 0)// 判断是否相等
			return 0;
		else
			return -1;
	}

	/**
	 * 这个方法将根据ip的内容，定位到包含这个ip国家地区的记录处，返回一个绝对偏移 方法使用二分法查找。
	 * 
	 * @param ip
	 *            要查询的IP
	 * @return 如果找到了，返回结束IP的偏移，如果没有找到，返回-1
	 */
	private long locateIP(byte[] ip) {
		long m = 0;
		int r;
		// 比较第一个ip项
		readIP(ipBegin, b4);
		r = compareIP(ip, b4);
		if (r == 0)
			return ipBegin;
		else if (r < 0)
			return -1;
		// 开始二分搜索
		for (long i = ipBegin, j = ipEnd; i < j;) {
			m = getMiddleOffset(i, j);
			readIP(m, b4);
			r = compareIP(ip, b4);
			// log.debug(Utils.getIpStringFromBytes(b));
			if (r > 0)
				i = m;
			else if (r < 0) {
				if (m == j) {
					j -= IP_RECORD_LENGTH;
					m = j;
				} else
					j = m;
			} else
				return readLong3(m + 4);
		}
		// 如果循环结束了，那么i和j必定是相等的，这个记录为最可能的记录，但是并非
		// 肯定就是，还要检查一下，如果是，就返回结束地址区的绝对偏移
		m = readLong3(m + 4);
		readIP(m, b4);
		r = compareIP(ip, b4);
		if (r <= 0)
			return m;
		else
			return -1;
	}

	/**
	 * 得到begin偏移和end偏移中间位置记录的偏移
	 * 
	 * @param begin
	 * @param end
	 * @return
	 */
	private long getMiddleOffset(long begin, long end) {
		long records = (end - begin) / IP_RECORD_LENGTH;
		records >>= 1;
		if (records == 0)
			records = 1;
		return begin + records * IP_RECORD_LENGTH;
	}

	/**
	 * 从offset偏移处读取一个以0结束的字符串
	 * 
	 * @param offset
	 *            字符串起始偏移
	 * @return 读取的字符串，出错返回空字符串
	 */
	private String readString(long offset) {
		try {
			ipFile.seek(offset);
			int i = 0;
			byte[] buf = new byte[100];
			while ((buf[i] = ipFile.readByte()) != 0) {
			    ++ i;
			    if (i >= buf.length) {
			        byte[] tmp = new byte[i + 100];
			        System.arraycopy(buf, 0, tmp, 0, i);
			        buf = tmp;
			    }
			}
			
			if (i != 0)
				return IPUtil.getString(buf, 0, i, "GBK");
		} catch (IOException e) {
		}
		return "";
	}

	/**
	 * 从内存映射文件的offset位置得到一个0结尾字符串
	 * 
	 * @param offset
	 *            字符串起始偏移
	 * @return 读取的字符串，出错返回空字符串
	 */
	private String readString(int offset) {
		try {
			mbb.position(offset);
			int i = 0;
			byte[] buf = new byte[100];
			while ((buf[i] = mbb.get()) != 0) {
			    ++ i;
			    if (i >= buf.length) {
			        byte[] tmp = new byte[i + 100];
			        System.arraycopy(buf, 0, tmp, 0, i);
			        buf = tmp;
			    }
			}
			
			if (i != 0)
				return IPUtil.getString(buf, 0, i, "GBK");
		} catch (IllegalArgumentException e) {
		}
		return "";
	}
	
	
	public static void main(String args[]) throws FileNotFoundException{
		IPSeeker ip = new IPSeeker();
		IPLocation location = ip.getIPLocation("58.20.43.13");
		System.out.println(location);
		
//		List<IPEntry> list = ip.getIPEntries("清华");
//		for(IPEntry e: list){
//			System.out.println(e);
//		}
	}
}
