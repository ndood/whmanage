package com.wlt.webm.business.bean.app;


import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Hashtable;
import java.util.Random;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Result;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.wlt.webm.tool.DES;

/**
 * ��ά�빤����
 * 
 */
public class QRCodeUtil {

	private static final String CHARSET = "utf-8";
	private static final String FORMAT_NAME = "JPG";
	// ��ά��ߴ�
	private static final int QRCODE_SIZE = 260;
	// LOGO����
	private static final int WIDTH = 60;
	// LOGO�߶�
	private static final int HEIGHT = 60;

	private static BufferedImage createImage(String content, String imgPath,
			boolean needCompress) throws Exception {
		Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
		hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
//		hints.put(EncodeHintType.MARGIN, 1);
		BitMatrix bitMatrix = new MultiFormatWriter().encode(content,
				BarcodeFormat.QR_CODE, QRCODE_SIZE, QRCODE_SIZE, hints);
		int width = bitMatrix.getWidth();
		int height = bitMatrix.getHeight();
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000
						: 0xFFFFFFFF);
			}
		}
		if (imgPath == null || "".equals(imgPath)) {
			return image;
		}
		// ����ͼƬ
		QRCodeUtil.insertImage(image, imgPath, needCompress);
		return image;
	}

	/**
	 * ����LOGO
	 * 
	 * @param source
	 *            ��ά��ͼƬ
	 * @param imgPath
	 *            LOGOͼƬ��ַ
	 * @param needCompress
	 *            �Ƿ�ѹ��
	 * @throws Exception
	 */
	private static void insertImage(BufferedImage source, String imgPath,
			boolean needCompress) throws Exception {
		File file = new File(imgPath);
		if (!file.exists()) {
			System.err.println(""+imgPath+"   ���ļ������ڣ�");
			return;
		}
		Image src = ImageIO.read(new File(imgPath));
		int width = src.getWidth(null);
		int height = src.getHeight(null);
		if (needCompress) { // ѹ��LOGO
			if (width > WIDTH) {
				width = WIDTH;
			}
			if (height > HEIGHT) {
				height = HEIGHT;
			}
			Image image = src.getScaledInstance(width, height,
					Image.SCALE_SMOOTH);
			BufferedImage tag = new BufferedImage(width, height,
					BufferedImage.TYPE_INT_RGB);
			Graphics g = tag.getGraphics();
			g.drawImage(image, 0, 0, null); // ������С���ͼ
			g.dispose();
			src = image;
		}
		// ����LOGO
		Graphics2D graph = source.createGraphics();
		int x = (QRCODE_SIZE - width) / 2;
		int y = (QRCODE_SIZE - height) / 2;
		graph.drawImage(src, x, y, width, height, null);
		Shape shape = new RoundRectangle2D.Float(x, y, width, width, 6, 6);
		graph.setStroke(new BasicStroke(3f));
		graph.draw(shape);
		graph.dispose();
	}

	/**
	 * ���ɶ�ά��(��ǶLOGO)
	 * 
	 * @param content
	 *            ����
	 * @param imgPath
	 *            LOGO��ַ
	 * @param destPath
	 *            ���Ŀ¼
	 * @param needCompress
	 *            �Ƿ�ѹ��LOGO
	 * @return 
	 * @throws Exception
	 */
	public static boolean encode(String content, String imgPath, String destPath,
			boolean needCompress) throws Exception {
		BufferedImage image = QRCodeUtil.createImage(content, imgPath,
				needCompress);
		mkdirs(destPath);
//		String file = new Random().nextInt(99999999)+".jpg";
		return ImageIO.write(image, FORMAT_NAME, new File(destPath));
	}

	/**
	 * ���ļ��в�����ʱ��mkdirs���Զ��������Ŀ¼��������mkdir��(mkdir�����Ŀ¼����������׳��쳣)
	 * @author lanyuan
	 * Email: mmm333zzz520@163.com
	 * @date 2013-12-11 ����10:16:36
	 * @param destPath ���Ŀ¼
	 */
	public static void mkdirs(String destPath) {
		File file =new File(destPath);    
		//���ļ��в�����ʱ��mkdirs���Զ��������Ŀ¼��������mkdir��(mkdir�����Ŀ¼����������׳��쳣)
		if (!file.exists() && !file.isDirectory()) {
			file.mkdirs();
		}
	}

	/**
	 * ���ɶ�ά��(��ǶLOGO)
	 * 
	 * @param content
	 *            ����
	 * @param imgPath
	 *            LOGO��ַ
	 * @param destPath
	 *            �洢��ַ
	 * @throws Exception
	 */
	public static void encode(String content, String imgPath, String destPath)
			throws Exception {
		QRCodeUtil.encode(content, imgPath, destPath, true);
	}

	/**
	 * ���ɶ�ά��
	 * 
	 * @param content
	 *            ����
	 * @param destPath
	 *            �洢��ַ
	 * @param needCompress
	 *            �Ƿ�ѹ��LOGO
	 * @throws Exception
	 */
	public static void encode(String content, String destPath,
			boolean needCompress) throws Exception {
		QRCodeUtil.encode(content, null, destPath, needCompress);
	}

	/**
	 * ���ɶ�ά��
	 * 
	 * @param content
	 *            ����
	 * @param destPath
	 *            �洢��ַ
	 * @throws Exception
	 */
	public static void encode(String content, String destPath) throws Exception {
		QRCodeUtil.encode(content, null, destPath, false);
	}

	/**
	 * ���ɶ�ά��(��ǶLOGO)
	 * 
	 * @param content
	 *            ����
	 * @param imgPath
	 *            LOGO��ַ
	 * @param output
	 *            �����
	 * @param needCompress
	 *            �Ƿ�ѹ��LOGO
	 * @throws Exception
	 */
	public static void encode(String content, String imgPath,
			OutputStream output, boolean needCompress) throws Exception {
		BufferedImage image = QRCodeUtil.createImage(content, imgPath,
				needCompress);
		ImageIO.write(image, FORMAT_NAME, output);
	}

	/**
	 * ���ɶ�ά��
	 * 
	 * @param content
	 *            ����
	 * @param output
	 *            �����
	 * @throws Exception
	 */
	public static void encode(String content, OutputStream output)
			throws Exception {
		QRCodeUtil.encode(content, null, output, false);
	}

	/**
	 * ������ά��
	 * 
	 * @param file
	 *            ��ά��ͼƬ
	 * @return
	 * @throws Exception
	 */
	public static String decode(File file) throws Exception {
		BufferedImage image;
		image = ImageIO.read(file);
		if (image == null) {
			return null;
		}
		BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(
				image);
		BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
		Result result;
		Hashtable<DecodeHintType, Object> hints = new Hashtable<DecodeHintType, Object>();
		hints.put(DecodeHintType.CHARACTER_SET, CHARSET);
		result = new MultiFormatReader().decode(bitmap, hints);
		String resultStr = result.getText();
		return resultStr;
	}

	/**
	 * ������ά��
	 * 
	 * @param path
	 * ��ά��ͼƬ��ַ
	 * @return
	 * @throws Exception
	 */
	public static String decode(String path) throws Exception {
		return QRCodeUtil.decode(new File(path));
	}

	public static void main(String[] args) throws Exception {
		DES des=new DES();
		des.setKey("wh#@!a59");
		Class.forName("com.mysql.jdbc.Driver");
		Connection con=DriverManager.getConnection("jdbc:mysql://163.177.40.115:3306/wh?useUnicode=true&characterEncoding=UTF-8&useCursorFetch=true&defaultFetchSize=1000"
				, "wh", "wh2013");
		int n=0;
		con.setAutoCommit(false);
		PreparedStatement pre=con.prepareStatement("insert into wht_app_url(url_id,url,imgedir,imgename,imgedirname) values(?,?,?,?,?)");
		for(int i=4;i<14;i++){
			String text = "http://www.wanhuipay.net/wh/business/QR.jsp?&id="+i;//ע���ʱȥ���˼���
			QRCodeUtil.encode(text, "C:\\picture\\logo.png", "C:\\picture\\"+i+".png", true);
			n++;
			pre.setInt(1,i);
			pre.setString(2, text);
			pre.setString(3, "C:\\picture");
			pre.setString(4, i+".png");
			pre.setString(5, "C:\\picture\\"+i+".png");
			pre.addBatch();
			if((n+1)%30==0){
				pre.executeBatch();
				pre.clearBatch();
			}
		}
		pre.executeBatch();
		con.commit();
	}
}