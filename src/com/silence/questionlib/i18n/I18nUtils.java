package com.silence.questionlib.i18n;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * ���ʻ�������
 * 
 * 
 * 
 */
public class I18nUtils {

	/**
	 * ���ʻ�������ȡ����
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		ResourceBundle bundle = ResourceBundle.getBundle(
				"com.silence.questionlib.i18n.resource", Locale.CHINA);
		String username = bundle.getString("usex_male");
		String password = bundle.getString("password");
		System.out.println(username);
		System.out.println(password);

		System.out
				.println(I18nUtils.getI18nProperty(Locale.CHINA, "usex_male"));
	}

	/**
	 * �ӹ��һ��ļ�����ȡ��Ӧ����Ϣ
	 * 
	 * @param localeҪ��ȡ�ĵ���
	 * @param nameҪ��ȡ��Ϣ��
	 * @return ����ָ�������Ķ�Ӧ��Ϣ������Ϣ
	 */
	public static String getI18nProperty(Locale locale, String name) {
		ResourceBundle bundle = ResourceBundle.getBundle(
				"com.silence.questionlib.i18n.resource", locale);
		return bundle.getString(name);
	}
}
