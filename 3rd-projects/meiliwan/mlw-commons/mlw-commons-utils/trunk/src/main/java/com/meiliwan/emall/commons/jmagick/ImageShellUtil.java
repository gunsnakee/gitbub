package com.meiliwan.emall.commons.jmagick;

import java.awt.Dimension;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.MemoryCacheImageInputStream;

import magick.MagickException;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import com.meiliwan.emall.commons.exception.CmdExecException;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.CmdExecUtil;

/**
 * 
 * @author lsf
 * 
 */
public class ImageShellUtil {

	private final static MLWLogger LOG = MLWLoggerFactory
			.getLogger(ImageShellUtil.class);

	public static final String W_H_SPLITER = "x";
	public static final String COMPRESSION_IDENTITY = "comp";

	public static final String TMP_SUFFIX = ".tmp.jpg";

	private static final String CONVERT_CMD_PATH = "/usr/bin/convert ";

	/**
	 * 
	 * @param fromImg
	 * @param toImg
	 * @param widthPx
	 * @param heightPx
	 */
	public static void scaleImg(String fromImg, String toImg, int widthPx,
			int heightPx) throws MagickException {
		String cmdLineStr = CONVERT_CMD_PATH + fromImg + " -resize " + widthPx
				+ "x" + heightPx + " " + toImg;

		CmdExecUtil.execCmd(cmdLineStr);
	}

	/**
	 * 
	 * @param fromImg
	 *            原图路径
	 * @param widthPx
	 *            被缩放的宽度
	 * @param heightPx
	 *            被缩放的高度
	 * @return 如果压缩成功，则返回压缩后的图片名称；如果压缩失败，则返回原图的文件名
	 */
	public static String scaleImg(String fromImg, int widthPx, int heightPx) {
		final String size = widthPx + W_H_SPLITER + heightPx;
		String filename = FilenameUtils.getName(fromImg);
		try {
			String baseDir = FilenameUtils.getFullPath(fromImg);

			String ext = FilenameUtils.getExtension(filename);
			filename = filename + size + "." + ext;

			String filePath = baseDir + filename;

			scaleImg(fromImg, filePath, widthPx, heightPx);

		} catch (MagickException e) {
			LOG.error(e, "fromImg:" + fromImg + ",widthPx:" + widthPx
					+ ",heightPx:" + heightPx, null);

			return FilenameUtils.getName(fromImg);
		}

		return filename; // 压缩成功，则将resultDir+"/"+文件名返回
	}

	/**
	 * 
	 * @param fromImg
	 *            原图路径
	 * @param toImg
	 *            被压缩后的图片路径
	 * @param quality
	 *            压缩质量，从0到100之间的数字
	 * @throws MagickException
	 *             压缩不成功，可能会跑出该异常
	 */
	public static void compressionImg(String fromImg, String toImg, int quality)
			throws MagickException {
		String cmdLineStr = CONVERT_CMD_PATH +" +profile '*' -strip "+ fromImg
				+ " -compress JPEG -quality " + quality + " " + toImg;

		CmdExecUtil.execCmd(cmdLineStr);
	}

	/**
	 * 
	 * @param fromImg
	 *            原图路径
	 * @param quality
	 *            压缩质量，从0到100之间的数字
	 * @return 如果压缩成功，则返回压缩后的图片名称；如果压缩失败，则返回原图的文件名
	 */
	public static String compressionImg(String fromImg, int quality) {
		String filename = FilenameUtils.getName(fromImg);
		try {
			String baseDir = FilenameUtils.getFullPath(fromImg);

			String ext = FilenameUtils.getExtension(filename);
			filename = filename + COMPRESSION_IDENTITY + "." + ext;

			String filePath = baseDir + filename;

			compressionImg(fromImg, filePath, quality);

		} catch (MagickException e) {
			LOG.error(e, "fromImg:" + fromImg + ",quality:" + quality, null);

			return FilenameUtils.getName(fromImg);
		}

		return filename; // 压缩成功，则将resultDir+"/"+文件名返回
	}

	/**
	 * 对图片进行裁切
	 * 
	 * @param fromImg
	 *            原图路径
	 * @param toImg
	 *            被裁切后的图片路径
	 * @param x
	 *            相对图片左上角的x坐标
	 * @param y
	 *            相对图片左上角的y坐标
	 * @param w
	 *            裁切宽度
	 * @param h
	 *            裁切高度
	 * @throws MagickException
	 *             压缩不成功，可能会跑出该异常
	 */
	public static void cutImg(String fromImg, String toImg, int x, int y,
			int w, int h) throws MagickException {
		String cmdLineStr = CONVERT_CMD_PATH + fromImg + " -crop " + w + "x"
				+ h + "+" + x + "+" + y + " " + toImg;

		CmdExecUtil.execCmd(cmdLineStr);
	}

	/**
	 * 
	 * @param fromImg
	 *            原图路径
	 * @param x
	 *            相对图片左上角的x坐标
	 * @param y
	 *            相对图片左上角的y坐标
	 * @param w
	 *            裁切宽度
	 * @param h
	 *            裁切高度
	 * @return 如果压缩成功，则返回压缩后的图片名称；如果压缩失败，则返回原图的文件名
	 */
	public static String cutImg(String fromImg, int x, int y, int w, int h) {
		final String size = w + W_H_SPLITER + h;
		String filename = FilenameUtils.getName(fromImg);
		try {
			String baseDir = FilenameUtils.getFullPath(fromImg);

			String ext = FilenameUtils.getExtension(filename);
			filename = filename + size + "." + ext;

			String filePath = baseDir + filename;

			cutImg(fromImg, filePath, x, y, w, h);

		} catch (MagickException e) {
			LOG.error(e, "fromImg:" + fromImg + ",x:" + x + ",y:" + y + ",w:"
					+ w + ",h:" + h, null);

			return FilenameUtils.getName(fromImg);
		}

		return filename; // 压缩成功，则将resultDir+"/"+文件名返回
	}

	/**
	 * 
	 * @param fromImg
	 *            原图路径
	 * @param x
	 *            相对图片左上角的x坐标
	 * @param y
	 *            相对图片左上角的y坐标
	 * @param leftx
	 *            相对图片左上角的另外一个x坐标
	 * @param topy
	 *            相对图片左上角的另外一个y坐标
	 * @return 如果压缩成功，则返回压缩后的图片名称；如果压缩失败，则返回原图的文件名
	 */
	public static String cutImgByPx(String fromImg, double x, double y,
			double leftx, double topy) {
		int width = (int) Math.abs(leftx - x);
		int height = (int) Math.abs(topy - y);

		return cutImg(fromImg, (int) x, (int) y, width, height);
	}

	/**
	 * 
	 * @param fromImg
	 *            原图路径
	 * @param toImg
	 *            被裁切后的图片路径
	 * @param x
	 *            相对图片左上角的x坐标
	 * @param y
	 *            相对图片左上角的y坐标
	 * @param leftx
	 *            相对图片左上角的另外一个x坐标
	 * @param topy
	 *            相对图片左上角的另外一个y坐标
	 * @return 如果压缩成功，则返回压缩后的图片名称；如果压缩失败，则返回原图的文件名
	 */
	public static boolean cutImgByPx(String fromImg, String toImg, double x,
			double y, double leftx, double topy) {
		int width = (int) Math.abs(leftx - x);
		int height = (int) Math.abs(topy - y);

		try {
			cutImg(fromImg, toImg, (int) x, (int) y, width, height);
		} catch (MagickException e) {
			LOG.error(e, "fromImg:" + fromImg + ",toImg:" + toImg, null);
		}

		return new File(toImg).exists();
	}

	/**
	 * 
	 * @param fromImg
	 *            原图路径
	 * @param width
	 *            压缩后的宽度
	 * @param height
	 *            压缩后的高度
	 * @param quality
	 *            压缩质量，从0到100之间的数字
	 * @return 如果压缩成功，则返回压缩后的图片名称；如果压缩失败，则返回原图的文件名
	 */
	public static String convertUploadImg(String fromImg, int width,
			int height, int quality) {
		final String size = width + W_H_SPLITER + height;

		String baseDir = FilenameUtils.getFullPath(fromImg);
		String filename = FilenameUtils.getName(fromImg);
		String ext = FilenameUtils.getExtension(filename);
		filename = filename + size + "." + ext;

		String filePath = baseDir + filename;
		String cmdLineStr = CONVERT_CMD_PATH + fromImg + " -resize " + width
				+ "x" + height + " -compress JPEG -quality " + quality + " "
				+ filePath;

		try {
			CmdExecUtil.execCmd(cmdLineStr);
		} catch (CmdExecException e) {
			LOG.error(e, "fromImg:" + fromImg + ",width:" + width + ",height:"
					+ height + ",quality:" + quality, null);

			return FilenameUtils.getName(fromImg);
		}

		return filename;
	}

	/**
	 * 
	 * @param fromImg
	 *            原图路径
	 * @param width
	 *            被缩放的宽度
	 * @param height
	 *            被缩放的高度
	 * @return 按指定的宽度或高度，计算出压缩比后进行压缩。如果压缩成功，则返回压缩后的图片名称；如果压缩失败，则返回原图的文件名
	 */
	public static String ratioScaleImg(String fromImg, int width, int height) {
		String filename = FilenameUtils.getName(fromImg);
		try {
			Dimension dim = getDimension(fromImg);

			int accessSize = width;
			double widthPx = dim.getWidth();
			double heightPx = dim.getHeight();
			if (width > 0) {
				height = (int) (heightPx * (width / widthPx));
			} else if (height > 0) {
				accessSize = height;
				width = (int) (widthPx * (height / heightPx));
			} else {
				LOG.warn("one of width or height should be over zero.",
						"fromImg:" + fromImg + ",width:" + width + ",height:"
								+ height, null);
				return filename;
			}

			String ext = FilenameUtils.getExtension(filename);
			filename = filename + accessSize + "." + ext;

			String baseDir = FilenameUtils.getFullPath(fromImg);

			scaleImg(fromImg, baseDir + filename, width, height);
		} catch (MagickException e) {
			LOG.error(e, "fromImg:" + fromImg + ",width:" + width + ",height:"
					+ height, null);

			return FilenameUtils.getName(fromImg);
		}

		return filename;
	}

	public static Dimension getDimension(String fromImg) throws MagickException {
		String cmdLineStr = "/usr/bin/identify "+fromImg;

		Dimension dim = null;
		String stdout = CmdExecUtil.execCmdForStdout(cmdLineStr);
		if (StringUtils.isNotEmpty(stdout) && stdout.startsWith(fromImg)) {
			String parts[] = stdout.split("\\s+");
			String dimStr = parts[2];

			System.out.println("dim:" +dimStr);
			
			String dimParts[] = dimStr.split("x");
			dim = new Dimension(Integer.valueOf(dimParts[0]),
					Integer.valueOf(dimParts[1]));
		}

		return dim;
	}

	/**
	 * 
	 * @param mapObj
	 *            图片的字节码数组
	 * @return 如果该字节码数组确实为图片格式，则返回true；否则返回false
	 */
	public static boolean isImageType(byte[] mapObj) {
		boolean ret = false;
		ByteArrayInputStream bais = null;
		MemoryCacheImageInputStream mcis = null;
		try {
			bais = new ByteArrayInputStream(mapObj);
			mcis = new MemoryCacheImageInputStream(bais);
			Iterator<ImageReader> itr = ImageIO.getImageReaders(mcis);

			while (itr.hasNext()) {
				ImageReader reader = (ImageReader) itr.next();

				String imageName = reader.getClass().getSimpleName();

				if (imageName != null
						&& ("GIFImageReader".equals(imageName)
								|| "JPEGImageReader".equals(imageName)
								|| "PNGImageReader".equals(imageName) || "BMPImageReader"
									.equals(imageName))) {
					ret = true;
				}
			}
		} finally {
			// 关闭流
			if (mcis != null) {
				try {
					mcis.close();
				} catch (IOException e) {
					LOG.error(e, null, null);
				}
			}

			if (bais != null) {
				try {
					bais.close();
				} catch (IOException e) {
					LOG.error(e, null, null);
				}
			}
		}

		return ret;
	}

	/**
	 * 
	 * @param file
	 *            文件路径
	 * @return 如果该文件确实为图片格式，则返回true；否则返回false
	 * @throws java.io.IOException
	 */
	public static boolean isImageType(String fileName) {
		return isImageType(new File(fileName));
	}

	/**
	 * 
	 * @param file
	 *            文件对象
	 * @return 如果该文件确实为图片格式，则返回true；否则返回false
	 * @throws java.io.FileNotFoundException
	 * @throws java.io.IOException
	 */
	public static boolean isImageType(File file) {
		try {
			return isImageType(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			LOG.error(e, file == null ? null : file.getAbsolutePath(), null);
		}

		return false;
	}

	/**
	 * 
	 * @param input
	 *            文件流
	 * @return 如果该文件流确实为图片格式，则返回true；否则返回false
	 * @throws java.io.IOException
	 */
	public static boolean isImageType(InputStream input) {
		try {
			return isImageType(IOUtils.toByteArray(input));
		} catch (IOException e) {
			LOG.error(e, null, null);
		}

		return false;
	}

//	public static void main(String[] args) {
//		String method = args[0];
//		String filename = null;
//		if ("scaleImg".equals(method)) {
//			filename = ImageShellUtil.scaleImg(args[1], 70,
//					70);
//		} else if ("compressImg".equals(args[0])) {
//			filename=ImageShellUtil.compressionImg(args[1], 50);
//		} else if ("cutImg".equals(args[0])){
//			filename=ImageShellUtil.cutImg(args[1], 50, 50, 50, 50);
//		} else if("ratioScaleImg".equals(args[0])){
//			filename=ImageShellUtil.ratioScaleImg(args[1], 125, 0);
//		} else if("getDimension".equals(args[0])){
//			try {
//				Dimension dim = ImageShellUtil.getDimension(args[1]);
//				System.out.println("w:" + dim.getWidth() + ",h:" + dim.getHeight());
//			} catch (MagickException e) {
//				e.printStackTrace();
//			}
//		}else if("convertUploadImg".equals(args[0])){
//			filename = ImageShellUtil.convertUploadImg(args[1], 68, 72, 60);
//		}
//		
//		System.out.println("convert filename:" + filename);
//
//	}

}
