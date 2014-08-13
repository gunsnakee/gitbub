package com.meiliwan.emall.icetool;

import java.io.File;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;

import com.meiliwan.emall.commons.exception.BaseException;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.CmdExecUtil;
import com.meiliwan.emall.commons.util.ConfigOnZk;
import com.meiliwan.emall.pojo.IceConstant;

public class IceFileTool {

	private final static MLWLogger LOG = MLWLoggerFactory
			.getLogger(IceFileTool.class);

	/**
	 * 
	 * @param localFilePath
	 * @return
	 */
	public static String uploadFile(boolean canDel, String... localFilePaths) {
		if (localFilePaths == null || localFilePaths.length <= 0) {
			LOG.warn("param localFilePaths cannot be null", "localFilePaths:"
					+ localFilePaths, IceConstant.LOCAL_IP);
			return null;
		}

		File[] files = new File[localFilePaths.length];
		int index = 0;
		for (String filePath : localFilePaths) {
			if (StringUtils.isEmpty(filePath)) {
				LOG.warn("param localFilePath cannot be null",
						"localFilePaths:" + localFilePaths + ",index:" + index,
						IceConstant.LOCAL_IP);
				continue;
			}
			files[index++] = new File(filePath);
		}

		return uploadFile(canDel, files);
	}

	/**
	 * 
	 * @param file
	 * @return
	 */
	public static String uploadFile( boolean canDel, File... files) {
		if (files == null) {
			LOG.warn("param files cannot be null", "files:" + files,
					IceConstant.LOCAL_IP);
			return null;
		}

		StringBuilder fileList = new StringBuilder();
		
		for (File file : files) {
			if (file == null || !file.exists()) {
				LOG.warn("file not exists",
						"localFilePath:" + file.getAbsolutePath(),
						IceConstant.LOCAL_IP);
				continue;
			}
//			fileList.append("\"");
			fileList.append(file.getAbsolutePath());
//			fileList.append("\"");
			fileList.append(IceConstant.FILE_SPLITER);
		}

		try {

			String rsyncUser = ConfigOnZk.getInstance().getValue(
					IceConstant.iceBaseConfig, "rsync.user");
			String rsyncServerIP = ConfigOnZk.getInstance().getValue(
					IceConstant.iceBaseConfig, "rsync.serverIP");
			String rsyncModuleName = ConfigOnZk.getInstance().getValue(
					IceConstant.iceBaseConfig, "rsync.moduleName");

			String cmdLineStr = "/usr/bin/rsync -aurRz " + fileList.toString()
					+ " " + rsyncUser + "@" + rsyncServerIP + "::"
					+ rsyncModuleName + (canDel ? "/canDel" : "/notDel");
			CmdExecUtil.execCmd(cmdLineStr);

		} catch (BaseException e) {
			LOG.error(e, "get config on zk failure for rsync localFile:"
					+ fileList, IceConstant.LOCAL_IP);
		}

		return fileList.toString();
	}
	
	/**
	 * 
	 * @param localFilePaths
	 */
	public static void delFiles(String... localFilePaths){
		if(localFilePaths == null || localFilePaths.length <= 0){
			LOG.warn("param localFilePaths cannot be null", "localFilePaths:"
					+ localFilePaths, IceConstant.LOCAL_IP);
			return ;
		}
		for(String filePath : localFilePaths){
			try{
				new File(filePath).delete();
			} catch(Exception e){
				LOG.error(e, "del file '" + filePath +"' error!", null);
			}
		}
	}

	public static File[] downloadFile(boolean canDel, String... remoteFilePaths) {
		if (remoteFilePaths == null || remoteFilePaths.length <= 0) {
			LOG.warn("param remoteFilePaths cannot be null", "remoteFilePaths:"
					+ remoteFilePaths, IceConstant.LOCAL_IP);
			return null;
		}
		
		File[] files = new File[remoteFilePaths.length];

		try {
			int index = 0;
			String rsyncUser = ConfigOnZk.getInstance().getValue(
					IceConstant.iceBaseConfig, "rsync.user");
			String rsyncServerIP = ConfigOnZk.getInstance().getValue(
					IceConstant.iceBaseConfig, "rsync.serverIP");
			String rsyncModuleName = ConfigOnZk.getInstance().getValue(
					IceConstant.iceBaseConfig, "rsync.moduleName");

			String storagePath = ConfigOnZk.getInstance().getValue(
					IceConstant.iceConfig, "storagePath");
			for(String remoteFilePath : remoteFilePaths){
				remoteFilePath = remoteFilePath.replaceAll("\"", "");
				String cmdLineStr = "/usr/bin/rsync -auz "
						+ " " + rsyncUser + "@" + rsyncServerIP + "::" + rsyncModuleName + File.separator + (canDel ? "canDel" : "notDel")
						+ (remoteFilePath.startsWith(File.separator) ? remoteFilePath : File.separator + remoteFilePath)  + " " + storagePath;
				CmdExecUtil.execCmd(cmdLineStr);
				
				files[index++] = new File(storagePath  + File.separator + FilenameUtils.getName(remoteFilePath));
			}
		} catch (BaseException e) {
			LOG.error(e, "get config on zk failure for rsync remoteFilePaths:"
					+ remoteFilePaths, IceConstant.LOCAL_IP);
		}
		
		return files;
	}

}
