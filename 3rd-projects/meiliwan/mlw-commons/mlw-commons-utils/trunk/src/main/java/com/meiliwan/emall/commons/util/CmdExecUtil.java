package com.meiliwan.emall.commons.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.PumpStreamHandler;

import com.meiliwan.emall.commons.exception.CmdExecException;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;

/**
 * 
 * @author lsf
 *
 */
public class CmdExecUtil {
	
	private final static MLWLogger LOG = MLWLoggerFactory.getLogger(CmdExecUtil.class);

	/**
	 * 
	 * @param cmdLineStr
	 * @return
	 */
	public static String execCmdForStdout(String cmdLineStr)
			 {
		LOG.debug("exec cmd '" + cmdLineStr + "'");
		String out = null;
		CommandLine cmdLine = CommandLine.parse(cmdLineStr);
		DefaultExecutor executor = new DefaultExecutor();
		ByteArrayOutputStream outputStream = null;
		try {
			outputStream = new ByteArrayOutputStream();
			PumpStreamHandler streamHandler = new PumpStreamHandler(
					outputStream);

			executor.setStreamHandler(streamHandler);
			executor.execute(cmdLine);

			out = outputStream.toString("UTF-8");

		} catch (ExecuteException e) {
			throw new CmdExecException("cmd-exec-1", "exec cmd '" + cmdLineStr + "' error", e);
		} catch (IOException e) {
			throw new CmdExecException("cmd-exec-2", "exec cmd '" + cmdLineStr + "' error", e);
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					LOG.error(e, "close ByteArrayOutputStream for cmdLine '"
							+ cmdLineStr + "' error", null);
				}
			}
		}

		return out;
	}

	/**
	 * 
	 * @param cmdLineStr
	 */
	public static void execCmd(String cmdLineStr) {
		LOG.debug("exec cmd '" + cmdLineStr + "'");
		CommandLine cmdLine = CommandLine.parse(cmdLineStr);
		DefaultExecutor executor = new DefaultExecutor();
		try {
			executor.execute(cmdLine);
		} catch (ExecuteException e) {
			throw new CmdExecException("cmd-exec-3", "exec cmd '" + cmdLineStr + "' error", e);
		} catch (IOException e) {
			throw new CmdExecException("cmd-exec-4", "exec cmd '" + cmdLineStr + "' error", e);
		}
	}
	
}
