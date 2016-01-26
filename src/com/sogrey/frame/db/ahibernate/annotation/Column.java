package com.sogrey.frame.db.ahibernate.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ java.lang.annotation.ElementType.FIELD })
public @interface Column {
	/**
	 * 列名
	 * 
	 * @return
	 */
	public abstract String name();

	/**
	 * <p>
	 * 1.0 存储类型与数据类型
	 * </p>
	 * 
	 * 存储在 SQLite 数据库中的每个值（或是由数据库引擎所操作的值）都有一个以下的存储类型：<br>
	 * <ul>
	 * <li>NULL. 值是空值。</li>
	 * <li>INTEGER. 值是有符号整数，根据值的大小以1，2，3，4，6 或8字节存储。</li>
	 * <li>REAL. 值是浮点数，以8字节 IEEE 浮点数存储。</li>
	 * <li>TEXT. 值是文本字符串，使用数据库编码（UTF-8, UTF-16BE 或 UTF-16LE）进行存储。</li>
	 * <li>BLOB. 值是一个数据块，按它的输入原样存储。</li>
	 * </ul>
	 * 注意，存储类型比数据类型更笼统。以 INTEGER 存储类型为例，它包括6种不同的长度不等的整数类型，这在磁盘上是不同的。但是只要 INTEGER
	 * 值从磁盘读取到内存进行处理，它们就被转换为更为一般的数据类型（8字节有符号整型）。因此在一般情况下，“存储类型” 与 “数据类型”
	 * 没什么差别，这两个术语可以互换使用。 <br>
	 * SQLite 版本3数据库中的任何列，除了整型主键列，都可用于存储任何存储类型的值。<br>
	 * 
	 * SQL 语句中的任何值，无论它们是嵌入到 SQL 语句中的字面量还是绑定到预编译 SQL
	 * 语句中的参数，都有一个隐含的存储类型。在下述情况下，数据库引擎会在执行查询时在数值存储类型（INTEGER 和 REAL）和 TEXT
	 * 之间进行转换。 <br>
	 * <p>
	 * 1.1 布尔类型
	 * </p>
	 * 
	 * SQLite 并没有单独的布尔存储类型，而是将布尔值存储为整数 0 (false) 和 1 (true)。<br>
	 * <p>
	 * 1.2 日期和时间类型
	 * </p>
	 * 
	 * SQLite 没有另外的存储类型来存储日期和时间。SQLite 的内置的日期和时间函数能够将日期和时间存为 TEXT、REAL 或 INTEGER
	 * 值：<br>
	 * <ul>
	 * <li>TEXT ISO8601 字符串 ("YYYY-MM-DD HH:MM:SS.SSS")。</li>
	 * <li>REAL 儒略日数 (Julian Day Numbers)，按照前公历，自格林威治时间公元前4714年11月24日中午以来的天数。</li>
	 * <li>INTEGER Unix 时间，自 1970-01-01 00:00:00 UTC 以来的秒数。</li>
	 * </ul>
	 * 
	 * 应用可以选择这些格式中的任一种存储日期和时间，并使用内置的日期和时间函数在这些格式间自由转换。<br>
	 * 
	 * <p>
	 * 2.0 类型亲和性
	 * </p>
	 * 
	 * 为了最大限度地提高 SQLite 和其它数据库引擎之间的兼容性，SQLite
	 * 支持列的“类型亲和性”的概念。列的类型亲和性是指数据存储于该列的推荐类型
	 * 。这里重要的思想是类型是推荐的，而不是必须的。任何列仍可以存储任何类型的数据
	 * 。这只是让一些列有选择性地优先使用某种存储类型。一个列的首选存储类型被称为它的“亲和性”。 <br>
	 * 每个 SQLite 3 数据库中的列都归于以下的类型亲和性中的一种：<br>
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