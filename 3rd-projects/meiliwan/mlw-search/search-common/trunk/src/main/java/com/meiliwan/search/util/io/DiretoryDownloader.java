package com.meiliwan.search.util.io;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.TimeZone;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import com.meiliwan.emall.commons.util.BaseConfig;

public abstract class DiretoryDownloader {
	/**
	 * different implementation may have relative or absolute path
	 */
	public abstract boolean download(String srcDir, String destDir) throws IOException;
	
	private static class FtpFSDownloader extends DiretoryDownloader {

	    private FTPClient ftpClient;  
	    private String strIp;  
	    private int intPort;  
	    private String user;  
	    private String password;  
	    private String root;

		private FtpFSDownloader(String strIp, int intPort, String user, String password,String root)
		{
			//设置连接信息
	        this.strIp = strIp;  
	        this.intPort = intPort;  
	        this.user = user;  
	        this.password = password;
	        if(root.length() == 1)
	        	this.root = "";
	        else
	        	this.root = root;
	        
	        this.ftpClient = new FTPClient();  
		}
		
		private FtpFSDownloader()
		{
			//设置连接信息
	        this.strIp = "localhost";  
	        this.intPort = 21;  
	        this.user = "username";  
	        this.password = "password";
	        this.root = "/ftproot";
	        this.ftpClient = new FTPClient();  
		}
		
		private boolean initFTPClient() {
	        FTPClientConfig ftpClientConfig = new FTPClientConfig();  
	        ftpClientConfig.setServerTimeZoneId(TimeZone.getDefault().getID());  
	        ftpClient.setControlEncoding("UTF-8");  
	        ftpClient.configure(ftpClientConfig);  
	        try {  
	            if (intPort > 0) {  
	                ftpClient.connect(strIp, intPort);  
	            } else {  
	                ftpClient.connect(strIp);  
	            }  
	            // FTP服务器连接回答  
	            int reply = ftpClient.getReplyCode();  
	            if (!FTPReply.isPositiveCompletion(reply)) {  
	                ftpClient.disconnect();  
	                return false;
	            }  
	            if(!ftpClient.login(user, password))
	            	return false;
	            // 设置传输协议  
	            ftpClient.enterLocalPassiveMode();  
	            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        }  
	        ftpClient.setBufferSize(1024 * 64);  
	        ftpClient.setDataTimeout(30 * 1000);
	        return true;
		}
		
	    public void closeFTP() {  
	        if (null != this.ftpClient && this.ftpClient.isConnected()) {  
	            try {  
	                this.ftpClient.logout();// 退出FTP服务器  
	            } catch (IOException e) {  
	                e.printStackTrace();  
	            } finally {  
	                try {  
	                    this.ftpClient.disconnect();// 关闭FTP服务器的连接  
	                } catch (IOException e) {  
	                    e.printStackTrace();  
	                }  
	            }  
	        }  
	    }
		
		@Override
		public boolean download(String srcDir, String destDir)
				throws IOException {
			if(!initFTPClient()) {
				closeFTP();
				return false;
			}
			boolean success = downLoadDirectory(destDir, this.root + srcDir);
			closeFTP();
			if(success) 
				return true;
			return false;
		}
		
	    /*** 
	     * 下载文件 
	     * @param remoteFileName   待下载文件名称 
	     * @param localDires 下载到当地那个路径下 
	     * @param remoteDownLoadPath remoteFileName所在的路径 
	     * */  
		private boolean downloadFile(String remoteFileName, String localDires,  
	            String remoteDownLoadPath) {  
	        String strFilePath = localDires + "/" + remoteFileName;
	        File localFile = new File(localDires);
	        if(!localFile.exists() || !localFile.isDirectory())
	        	localFile.mkdirs();
	        BufferedOutputStream outStream = null;  
	        boolean success = false;  
	        try {  
	            ftpClient.changeWorkingDirectory(remoteDownLoadPath);  
	            outStream = new BufferedOutputStream(new FileOutputStream(  
	                    strFilePath));  
	            success = ftpClient.retrieveFile(remoteFileName, outStream);  
	            if (success == true) {  
	                return success;  
	            }  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        } finally {  
	            if (null != outStream) {  
	                try {  
	                    outStream.flush();  
	                    outStream.close();  
	                } catch (IOException e) {  
	                    e.printStackTrace();  
	                }  
	            }  
	        }  
	        return success;  
	    }
	    
	    /*** 
	     * @下载文件夹 
	     * @param localDirectoryPath本地地址 
	     * @param remoteDirectory 远程文件夹 
	     * */  
		private boolean downLoadDirectory(String localDirectoryPath,String remoteDirectory) {  
	        try {  
	            FTPFile[] allFile = ftpClient.listFiles(remoteDirectory); 
	            if(allFile.length == 0) return false;
	            for (int currentFile = 0; currentFile < allFile.length; currentFile++) {
	            	String newLocalPathString = new String(localDirectoryPath + "/" + allFile[currentFile].getName());
	                if (!allFile[currentFile].isDirectory()) {
	                    downloadFile(allFile[currentFile].getName(),localDirectoryPath, remoteDirectory);  
	                } else if(allFile[currentFile].isDirectory()) {
	                    String strremoteDirectoryPath = remoteDirectory + "/"+ allFile[currentFile].getName();
	                    new File(newLocalPathString).mkdirs(); 
	                    downLoadDirectory(newLocalPathString,strremoteDirectoryPath);  
	                }
	            }  
	        } catch (IOException e) {  
	            e.printStackTrace();  
	            return false;  
	        }  
	        return true;  
	    }
	}
	
	
	private static class LocalFSDownloader extends DiretoryDownloader{

		public boolean download(String srcDir, String destDir) throws IOException {
			System.out.println(srcDir);
			System.out.println(destDir);
			FileUtil.copyFolder(BaseConfig.getValue("remotedir") + "/" + srcDir, destDir);
			File file = new File(destDir);
			return file.isDirectory();
		}
	}
	
	
	private static DiretoryDownloader getLocalFSDownloader(){
		return new LocalFSDownloader();
	}
	
	private static DiretoryDownloader getFtpFSDownloader(String strIp, int intPort, String user, String password,String root){
		return new FtpFSDownloader(strIp,intPort,user,password,root);
	}
	
	private  static DiretoryDownloader getFtpFSDownloader(){
		return new FtpFSDownloader();
	}
	public static DiretoryDownloader getDownloader(String cfg) {
		if(cfg!= null && !cfg.isEmpty() && cfg.toLowerCase().startsWith("ftp")) {
			String[] cfgArr = cfg.substring(4, cfg.length()-1).split(",");
			if(cfgArr.length == 5)
				return getFtpFSDownloader(cfgArr[1],Integer.parseInt(cfgArr[2]),cfgArr[3],cfgArr[4],cfgArr[0]);
			else 
				return getFtpFSDownloader();
		}
		else 
			return getLocalFSDownloader();
	}

}