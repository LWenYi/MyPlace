package com.org.mwd.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.org.mwd.vo.MessageBean;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;

public class MyUtil {

	// 将输入流转换成字符串
	public static String inStreamToString(InputStream is) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buf = new byte[1024];
		int len = -1;
		while ((len = is.read(buf)) != -1) {
			baos.write(buf, 0, len);
		}
		return new String(baos.toByteArray());
	}

	// 获取yyyy-MM-dd HH:mm:ss格式的日期时间
	public static String getDateTime(Long dtaeInSeconds) {
		String dateTime = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.getDefault());
		dateTime = sdf.format(dtaeInSeconds * 1000 + (8 * 3600 * 1000)); // 系统默认是1970年,要加上北京时区的八个小时

		return dateTime;
	}

	// 获取yyyy-MM-dd HH:mm:ss格式的日期时间
	public static String getDateTime() {
		String dateTime = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.getDefault());
		dateTime = sdf.format(new Date());
		return dateTime;
	}

	// 服务器 的IP
	public static String getIP() {
		return "172.16.10.198";
	}

	public static byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	// 二进制转字符串
	public static String byte2hex(byte[] b) {
		StringBuffer sb = new StringBuffer();
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = Integer.toHexString(b[n] & 0XFF);
			if (stmp.length() == 1) {
				sb.append("0" + stmp);
			} else {
				sb.append(stmp);
			}
		}
		return sb.toString();
	}

	// 字符串转二进制
	public static byte[] hex2byte(String str) {
		if (str == null)
			return null;
		str = str.trim();
		int len = str.length();
		if (len == 0 || len % 2 == 1)
			return null;
		byte[] b = new byte[len / 2];
		try {
			for (int i = 0; i < str.length(); i += 2) {
				b[i / 2] = (byte) Integer
						.decode("0X" + str.substring(i, i + 2)).intValue();
			}
			return b;
		} catch (Exception e) {
			return null;
		}
	}

	// 从文件路径获取文件名
	public static String getFileName(String pathandname) {
		int start = pathandname.lastIndexOf("/");
		int end = pathandname.lastIndexOf(".");
		if (start != -1 && end != -1) {
			return pathandname.substring(start + 1, end);
		} else {
			return null;
		}
	}

	// 从文件路径获取文件后缀名
	public static String getFileType(String pathandname) {
		int end = pathandname.lastIndexOf(".");
		if (end != -1) {
			return pathandname.substring(end + 1);
		} else {
			return null;
		}
	}

	// 从文件路径获取文件名包括后缀名
	public static String getFileAllName(String pathandname) {
		int start = pathandname.lastIndexOf("/");
		if (start != -1) {
			return pathandname.substring(start + 1);
		} else {
			return null;
		}
	}

	// 用于创建同一路径下的一个或多个文件夹
	public static boolean createFile(File file) throws IOException {
		if (!file.exists()) {
			makeDir(file.getParentFile());
		}
		return file.createNewFile();
	}

	// 用于createFile（）
	public static void makeDir(File dir) {
		if (!dir.getParentFile().exists()) {
			makeDir(dir.getParentFile());
		}
		dir.mkdir();
	}

	// 把字符串格式的图片转为图片保存
	public static void saveImgByStr(String imgPath, String imgStr) {
		byte[] imgByte = MyUtil.hex2byte(imgStr);
		File file = new File(imgPath);
		 @SuppressWarnings("unused")
		boolean created;
			try {
				created = MyUtil.createFile(file);
					 FileOutputStream fos=new FileOutputStream(file);
				     fos.write(imgByte,0,imgByte.length);
				     fos.flush();
				     fos.close();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

	// 根据图片路径得到字符串格式的图片
	public static String getImgStrByImgPath(String path) {
		String imgStr = null;
		if (path != null) {
			File f = new File(path);
			FileInputStream fis;
			try {
				fis = new FileInputStream(f);
				byte[] bytes = new byte[fis.available()];
				fis.read(bytes);
				fis.close();
				imgStr = MyUtil.byte2hex(bytes);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return imgStr;
	}

	// 用于两个List<MessageBean>数组组合，即将l2的值添加在l1后面
	public static void ArrayListCompose(List<MessageBean> l1,
			List<MessageBean> l2) {
		for (int i = 0; i < l2.size(); i++) {
			l1.add(l2.get(i));
		}
	}

	// 用于List<MessageBean>数组之间的值替换，即l2的值替换掉l1的值
	public static void ArrayListExchange(List<MessageBean> l1,
			List<MessageBean> l2) {
		l1.clear();
		for (int i = 0; i < l2.size(); i++) {
			l1.add(l2.get(i));
		}
	}

	/**
	 * 按照路径加载图片
	 * 
	 * @param path
	 *            图片资源的存放路径
	 * @param scalSize
	 *            缩小的倍数
	 * @return
	 */
	public static Bitmap loadResBitmap(String path, int scalSize) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);

		options.inSampleSize = computeSampleSize(options, -1, 128 * 128);
		options.inJustDecodeBounds = false;
		Bitmap bmp = BitmapFactory.decodeFile(path, options);

		return bmp;
	}

	public static int computeSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);

		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}

		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;

		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
				Math.floor(w / minSideLength), Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			// return the larger one when there is no overlapping zone.
			return lowerBound;
		}
		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

	// 把彩色图片转成灰色
	public static Bitmap toGrayscale(Bitmap bmpOriginal) {
		int width, height;
		height = bmpOriginal.getHeight();
		width = bmpOriginal.getWidth();

		Bitmap bmpGrayscale = Bitmap.createBitmap(width, height,
				Bitmap.Config.RGB_565);
		Canvas c = new Canvas(bmpGrayscale);
		Paint paint = new Paint();
		ColorMatrix cm = new ColorMatrix();
		cm.setSaturation(0);
		ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
		paint.setColorFilter(f);
		c.drawBitmap(bmpOriginal, 0, 0, paint);
		return bmpGrayscale;
	}

	// 判断电话格式
	public static boolean isPhoneNumberValid(String phoneNumber) {
		boolean isValid = false;

		String expression = "((^(13|15|18)[0-9]{9}$)|(^0[1,2]{1}\\d{1}-?\\d{8}$)|(^0[3-9] {1}\\d{2}-?\\d{7,8}$)|(^0[1,2]{1}\\d{1}-?\\d{8}-(\\d{1,4})$)|(^0[3-9]{1}\\d{2}-? \\d{7,8}-(\\d{1,4})$))";
		CharSequence inputStr = phoneNumber;

		Pattern pattern = Pattern.compile(expression);

		Matcher matcher = pattern.matcher(inputStr);

		if (matcher.matches()) {
			isValid = true;
		}

		return isValid;
	}
	// 判断邮箱格式
	public static boolean isEmail(String email) {
		String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);
		return m.matches();
	}
	
	public static Bitmap imageZoom(Bitmap bitMap) { 
        //图片允许最大空间   单位：KB 
        double maxSize =10.00; 
        //将bitmap放至数组中，意在bitmap的大小（与实际读取的原文件要大）   
        ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
        bitMap.compress(Bitmap.CompressFormat.JPEG, 100, baos); 
        byte[] b = baos.toByteArray(); 
        //将字节换成KB 
        double mid = b.length/1024; 
        //判断bitmap占用空间是否大于允许最大空间  如果大于则压缩 小于则不压缩 
        if (mid > maxSize) { 
                //获取bitmap大小 是允许最大大小的多少倍 
                double i = mid / maxSize; 
                //开始压缩  此处用到平方根 将宽带和高度压缩掉对应的平方根倍 （1.保持刻度和高度和原bitmap比率一致，压缩后也达到了最大大小占用空间的大小） 
                bitMap = zoomImage(bitMap, bitMap.getWidth() / Math.sqrt(i), 
                                bitMap.getHeight() / Math.sqrt(i)); 
                
        } 
        return bitMap;
        }
        
        /***
         * 图片的缩放方法
         *
         * @param bgimage
         *            ：源图片资源
         * @param newWidth
         *            ：缩放后宽度
         * @param newHeight
         *            ：缩放后高度
         * @return
         */ 
        public static Bitmap zoomImage(Bitmap bgimage, double newWidth, 
                        double newHeight) { 
                // 获取这个图片的宽和高 
                float width = bgimage.getWidth(); 
                float height = bgimage.getHeight(); 
                // 创建操作图片用的matrix对象 
                Matrix matrix = new Matrix(); 
                // 计算宽高缩放率 
                float scaleWidth = ((float) newWidth) / width; 
                float scaleHeight = ((float) newHeight) / height; 
                // 缩放图片动作 
                matrix.postScale(scaleWidth, scaleHeight); 
                Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width, 
                                (int) height, matrix, true); 
                return bitmap; 
        } 

}
