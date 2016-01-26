package com.sogrey.frame.db.ahibernate.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ java.lang.annotation.ElementType.FIELD })
public @interface Column {
	/**
	 * ����
	 * 
	 * @return
	 */
	public abstract String name();

	/**
	 * <p>
	 * 1.0 �洢��������������
	 * </p>
	 * 
	 * �洢�� SQLite ���ݿ��е�ÿ��ֵ�����������ݿ�������������ֵ������һ�����µĴ洢���ͣ�<br>
	 * <ul>
	 * <li>NULL. ֵ�ǿ�ֵ��</li>
	 * <li>INTEGER. ֵ���з�������������ֵ�Ĵ�С��1��2��3��4��6 ��8�ֽڴ洢��</li>
	 * <li>REAL. ֵ�Ǹ���������8�ֽ� IEEE �������洢��</li>
	 * <li>TEXT. ֵ���ı��ַ�����ʹ�����ݿ���루UTF-8, UTF-16BE �� UTF-16LE�����д洢��</li>
	 * <li>BLOB. ֵ��һ�����ݿ飬����������ԭ���洢��</li>
	 * </ul>
	 * ע�⣬�洢���ͱ��������͸���ͳ���� INTEGER �洢����Ϊ����������6�ֲ�ͬ�ĳ��Ȳ��ȵ��������ͣ����ڴ������ǲ�ͬ�ġ�����ֻҪ INTEGER
	 * ֵ�Ӵ��̶�ȡ���ڴ���д������Ǿͱ�ת��Ϊ��Ϊһ����������ͣ�8�ֽ��з������ͣ��������һ������£����洢���͡� �� ���������͡�
	 * ûʲô���������������Ի���ʹ�á� <br>
	 * SQLite �汾3���ݿ��е��κ��У��������������У��������ڴ洢�κδ洢���͵�ֵ��<br>
	 * 
	 * SQL ����е��κ�ֵ������������Ƕ�뵽 SQL ����е����������ǰ󶨵�Ԥ���� SQL
	 * ����еĲ���������һ�������Ĵ洢���͡�����������£����ݿ��������ִ�в�ѯʱ����ֵ�洢���ͣ�INTEGER �� REAL���� TEXT
	 * ֮�����ת���� <br>
	 * <p>
	 * 1.1 ��������
	 * </p>
	 * 
	 * SQLite ��û�е����Ĳ����洢���ͣ����ǽ�����ֵ�洢Ϊ���� 0 (false) �� 1 (true)��<br>
	 * <p>
	 * 1.2 ���ں�ʱ������
	 * </p>
	 * 
	 * SQLite û������Ĵ洢�������洢���ں�ʱ�䡣SQLite �����õ����ں�ʱ�亯���ܹ������ں�ʱ���Ϊ TEXT��REAL �� INTEGER
	 * ֵ��<br>
	 * <ul>
	 * <li>TEXT ISO8601 �ַ��� ("YYYY-MM-DD HH:MM:SS.SSS")��</li>
	 * <li>REAL �������� (Julian Day Numbers)������ǰ�������Ը�������ʱ�乫Ԫǰ4714��11��24������������������</li>
	 * <li>INTEGER Unix ʱ�䣬�� 1970-01-01 00:00:00 UTC ������������</li>
	 * </ul>
	 * 
	 * Ӧ�ÿ���ѡ����Щ��ʽ�е���һ�ִ洢���ں�ʱ�䣬��ʹ�����õ����ں�ʱ�亯������Щ��ʽ������ת����<br>
	 * 
	 * <p>
	 * 2.0 �����׺���
	 * </p>
	 * 
	 * Ϊ������޶ȵ���� SQLite ���������ݿ�����֮��ļ����ԣ�SQLite
	 * ֧���еġ������׺��ԡ��ĸ���е������׺�����ָ���ݴ洢�ڸ��е��Ƽ�����
	 * ��������Ҫ��˼�����������Ƽ��ģ������Ǳ���ġ��κ����Կ��Դ洢�κ����͵�����
	 * ����ֻ����һЩ����ѡ���Ե�����ʹ��ĳ�ִ洢���͡�һ���е���ѡ�洢���ͱ���Ϊ���ġ��׺��ԡ��� <br>
	 * ÿ�� SQLite 3 ���ݿ��е��ж��������µ������׺����е�һ�֣�<br>
	 * <ul>
	 * <li>TEXT</li>
	 * <li>NUMERIC</li>
	 * <li>INTEGER</li>
	 * <li>REAL</li>
	 * <li>NONE</li>
	 * </ul>
	 * @see http://www.oschina.net/translate/data-types-in-sqlite-version-3?cmp
	 */
	public abstract String type() default "";

	public abstract int length() default 0;
}