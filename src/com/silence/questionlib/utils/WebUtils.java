package com.silence.questionlib.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Random;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;

import com.silence.questionlib.domain.Result;

public class WebUtils {

	// ������֤��ͼƬ����
	private static final int WEIGHT = 240;
	private static final int HEIGHT = 70;

	public static <T> T request2Bean(HttpServletRequest request, Class<T> clazz) {
		try {
			// ����Bean
			T t = clazz.newInstance();
			// ��Request�е�������䵽Bean
			Enumeration enumeration = request.getParameterNames();
			while (enumeration.hasMoreElements()) {
				String name = (String) enumeration.nextElement();
				String value = request.getParameter(name);
				BeanUtils.setProperty(t, name, value);
			}
			return t;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void copyBean(Object src, Object dest) {

		// ����Date���͵�ת����
		ConvertUtils.register(new Converter() {

			public Object convert(Class type, Object value) {
				if (value == null)
					return null;
				String str = (String) value;
				if (str.trim().equals("") || str.trim().equals("null"))
					return null;
				try {
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					return format.parse(str);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}, Date.class);
		try {
			BeanUtils.copyProperties(dest, src);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static String getUUID() {
		return UUID.randomUUID().toString();
	}

	public static Result makeCheckCode(HttpServletRequest request)
			throws IOException {
		// BufferImage����ͼƬ�����ó��������ͣ���֤������
		BufferedImage image = new BufferedImage(WEIGHT, HEIGHT,
				BufferedImage.TYPE_INT_RGB);
		// �õ�һ��ͼƬ�����Ա������мӹ�
		Graphics g = image.getGraphics();

		// 1.���ñ���ɫ
		setBackground(g);

		// 2.���ñ߿�
		setBorder(g);

		// 3.����������� ������
		drawRandomLine(g);

		// 4.�������� Graphics2D���������ת�Ĺ���
		String checkCode = drawRandomNum((Graphics2D) g);

		// 5.��ͼƬ���ɻ���
		String filename = WebUtils.getUUID() + ".jpg";
		String realPath = request.getRealPath("/temp") + "/temp_" + filename;
		String path = request.getContextPath() + "/temp/temp_" + filename;
		// System.out.println("realPath = " + realPath);
		// System.out.println("path = " + path);
		File file = new File(realPath);
		file.createNewFile();
		FileOutputStream fos = new FileOutputStream(file);
		ImageIO.write(image, "jpg", fos);
		fos.close();
		System.out.println(checkCode);

		Result result = new Result();
		result.checkCode = checkCode;
		result.path = path;
		result.realPath = realPath;
		request.getSession().setAttribute("result", result);
		return result;
	}

	private static String drawRandomNum(Graphics2D g) {

		g.setColor(Color.RED);// ����ͼƬ�������ɫ
		Font font = new Font("����", Font.BOLD, 35);
		g.setFont(font);// ����ͼƬ�������������

		// a-zA-Z0-9
		String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		// x������ĺ����� ��������ѭ������
		int x = 10;
		StringBuffer sb = new StringBuffer();// ��StringBuffer�����ַ��������֤����Ϣ
		for (int i = 0; i < 4; i++) {
			// ������ת�Ƕ� ��Χ-30 ~ 30
			int degree = new Random().nextInt(60) - 30;
			// �������ȡһ������ ������password��
			String ch = base.charAt(new Random().nextInt(base.length())) + "";
			sb.append(ch);
			// ����ͼƬ��ת ��ת����������ת����
			g.rotate(degree * Math.PI / 180, x, 35);
			// д������
			g.drawString(ch, x, 40);
			// �Ѹղ���ת�Ļ�����ת��������ֹ�´���ת�໥����
			g.rotate(-degree * Math.PI / 180, x, 35);
			x += 55;
		}
		return sb.toString();
	}

	private static void drawRandomLine(Graphics g) {
		// ������ɫ �����ݺ������껭������
		g.setColor(Color.RED);
		for (int i = 0; i < 5; i++) {
			int x1 = new Random().nextInt(WEIGHT);
			int x2 = new Random().nextInt(WEIGHT);
			int y1 = new Random().nextInt(HEIGHT);
			int y2 = new Random().nextInt(HEIGHT);
			g.drawLine(x1, y1, x2, y2);
		}
	}

	private static void setBorder(Graphics g) {

		// �� �� �� �� �� �� �� �� ɫ
		g.setColor(Color.BLUE);
		g.drawRect(1, 1, WEIGHT - 2, HEIGHT - 2);
	}

	private static void setBackground(Graphics g) {
		// ���ñ�����ɫ
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, WEIGHT, HEIGHT);
	}
}
