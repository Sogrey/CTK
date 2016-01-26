package com.sogrey.frame.db.ahibernate.dao.impl;

import java.lang.reflect.Field;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;

import com.sogrey.frame.db.ahibernate.annotation.Column;
import com.sogrey.frame.db.ahibernate.annotation.Id;
import com.sogrey.frame.db.ahibernate.annotation.Table;
import com.sogrey.frame.db.ahibernate.dao.BaseDao;
import com.sogrey.frame.db.ahibernate.util.TableHelper;
import com.sogrey.frame.utils.LogUtil;

/**
 * AHibernate概要 <br/>
 * (一)支持功能: 1.自动建表,支持属性来自继承类:可根据注解自动完成建表,并且对于继承类中的注解字段也支持自动建表. 2.自动支持增删改
 * ,增改支持对象化操作:增删改是数据库操作的最基本单元,不用重复写这些增删改的代码,并且添加和更新支持类似于hibernate中的对象化操作.
 * 3.查询方式灵活:支持android框架提供的方式,也支持原生sql方式.
 * 4.查询结果对象化:对于查询结果可自动包装为实体对象,类似于hibernate框架.
 * 5.查询结果灵活:查询结果支持对象化,也支持结果为List<Map<String,String>>形式,这个方法在实际项目中很实用,且效率更好些.
 * 6.日志较详细:因为android开发不支持热部署调试,运行报错时可根据日志来定位错误,这样可以减少运行Android的次数. <br/>
 * (二)不足之处: <br/>
 * 1.id暂时只支持int类型,不支持uuid,在sqlite中不建议用uuid.
 * 2.现在每个方法都自己开启和关闭事务,暂时还不支持在一个事务中做多个操作然后统一提交事务. <br/>
 * (三)作者寄语:<br/>
 * 昔日有JavaScript借Java发展,今日也希望AHibernate借Hibernate之名发展.
 * 希望这个项目以后会成为开源社区的重要一员,更希望这个项目能给所有Android开发者带便利.
 * 欢迎访问我的博客:http://blog.csdn.net/lk_blog,
 * 这里有这个框架的使用范例和源码,希望朋友们多多交流完善这个框架,共同推动中国开源事业的发展,AHibernate期待与您共创美好未来!!!
 */
public class BaseDaoImpl<T> implements BaseDao<T> {
	private String TAG = "AHibernate";
	private SQLiteOpenHelper dbHelper;
	private String tableName;
	private String idColumn;
	private Class<T> clazz;
	private List<Field> allFields;
	private static final int METHOD_INSERT = 0;
	private static final int METHOD_UPDATE = 1;

	private static final int TYPE_NOT_INCREMENT = 0;// 主键设定
	private static final int TYPE_INCREMENT = 1;// 主键自增

	@SuppressWarnings("unchecked")
	public BaseDaoImpl(SQLiteOpenHelper dbHelper, Class<T> clazz) {
		this.dbHelper = dbHelper;
		if (clazz == null) {
			this.clazz = ((Class<T>) ((java.lang.reflect.ParameterizedType) super
					.getClass().getGenericSuperclass())
					.getActualTypeArguments()[0]);
		} else {
			this.clazz = clazz;
		}

		if (this.clazz.isAnnotationPresent(Table.class)) {
			Table table = (Table) this.clazz.getAnnotation(Table.class);
			this.tableName = table.name();
		}

		// 加载所有字段
		this.allFields = TableHelper.joinFields(this.clazz.getDeclaredFields(),
				this.clazz.getSuperclass().getDeclaredFields());

		// 找到主键
		for (Field field : this.allFields) {
			if (field.isAnnotationPresent(Id.class)) {
				Column column = (Column) field.getAnnotation(Column.class);
				this.idColumn = column.name();
				break;
			}
		}

		LogUtil.d(TAG, "clazz:" + this.clazz + " tableName:" + this.tableName
				+ " idColumn:" + this.idColumn);
	}

	public BaseDaoImpl(SQLiteOpenHelper dbHelper) {
		this(dbHelper, null);
	}

	public SQLiteOpenHelper getDbHelper() {
		return dbHelper;
	}

	/**
	 * 按主键_id 查询
	 */
	public T get(int id) {
		String selection = this.idColumn + " = ?";
		String[] selectionArgs = { Integer.toString(id) };
		LogUtil.d(TAG, "[get]: select * from " + this.tableName + " where "
				+ this.idColumn + " = '" + id + "'");
		List<T> list = find(null, selection, selectionArgs, null, null, null,
				null);
		if ((list != null) && (list.size() > 0)) {
			return (T) list.get(0);
		}
		return null;
	}

	/**
	 * SQl语句查询
	 */
	public List<T> rawQuery(String sql, String[] selectionArgs) {
		LogUtil.d(TAG, "[rawQuery]: " + getLogSql(sql, selectionArgs));

		List<T> list = new ArrayList<T>();
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			db = this.dbHelper.getReadableDatabase();
			cursor = db.rawQuery(sql, selectionArgs);

			getListFromCursor(list, cursor);
		} catch (Exception e) {
			LogUtil.e(this.TAG, "[rawQuery] from DB Exception.");
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			if (db != null) {
				db.close();
			}
		}

		return list;
	}

	/**
	 * 检查数据库是否已存在该数据（SQL语句）
	 */
	public boolean isExist(String sql, String[] selectionArgs) {
		LogUtil.d(TAG, "[isExist]: " + getLogSql(sql, selectionArgs));

		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			db = this.dbHelper.getReadableDatabase();
			cursor = db.rawQuery(sql, selectionArgs);
			if (cursor.getCount() > 0) {
				return true;
			}
		} catch (Exception e) {
			LogUtil.e(this.TAG, "[isExist] from DB Exception.");
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			if (db != null) {
				db.close();
			}
		}
		return false;
	}

	/**
	 * 检查数据库是否已存在该数据（条件==条件值）
	 */
	public boolean isExistBySelection(String selection, String[] selectionArgs) {
		LogUtil.d(
				TAG,
				"[isExist]: "
						+ getLogSqlBySelection("select", selection,
								selectionArgs));
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			db = this.dbHelper.getReadableDatabase();
			cursor = db.query(this.tableName, null, selection, selectionArgs,
					null, null, null);
			if (cursor != null && cursor.moveToNext()) {// 如果库里已存在这条数据-更新数据
				return true;
			}
		} catch (Exception e) {
			LogUtil.e(this.TAG, "[isExist] from DB Exception.");
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			if (db != null) {
				db.close();
			}
		}
		return false;
	}

	private void getListFromCursor(List<T> list, Cursor cursor)
			throws IllegalAccessException, InstantiationException {
		while (cursor.moveToNext()) {
			T entity = this.clazz.newInstance();

			for (Field field : this.allFields) {
				Column column = null;
				if (field.isAnnotationPresent(Column.class)) {
					column = (Column) field.getAnnotation(Column.class);

					field.setAccessible(true);
					Class<?> fieldType = field.getType();

					int c = cursor.getColumnIndex(column.name());
					if (c < 0) {
						continue; // 如果不存则循环下个属性值
					} else if ((Integer.TYPE == fieldType)
							|| (Integer.class == fieldType)) {
						field.set(entity, cursor.getInt(c));
					} else if (String.class == fieldType) {
						field.set(entity, cursor.getString(c));
					} else if ((Long.TYPE == fieldType)
							|| (Long.class == fieldType)) {
						field.set(entity, Long.valueOf(cursor.getLong(c)));
					} else if ((Float.TYPE == fieldType)
							|| (Float.class == fieldType)) {
						field.set(entity, Float.valueOf(cursor.getFloat(c)));
					} else if ((Short.TYPE == fieldType)
							|| (Short.class == fieldType)) {
						field.set(entity, Short.valueOf(cursor.getShort(c)));
					} else if ((Double.TYPE == fieldType)
							|| (Double.class == fieldType)) {
						field.set(entity, Double.valueOf(cursor.getDouble(c)));
					} else if (Date.class == fieldType) {// 处理java.util.Date类型,update2012-06-10
						Date date = new Date();
						date.setTime(cursor.getLong(c));
						field.set(entity, date);
					} else if (Blob.class == fieldType) {
						field.set(entity, cursor.getBlob(c));
					} else if (Character.TYPE == fieldType) {
						String fieldValue = cursor.getString(c);

						if ((fieldValue != null) && (fieldValue.length() > 0)) {
							field.set(entity,
									Character.valueOf(fieldValue.charAt(0)));
						}
					}
				}
			}

			list.add((T) entity);
		}
	}

	/**
	 * 插入数据（返回_id，_id==-1，则插入失败）<br>
	 * _id 自增
	 */
	public long insert(T entity) {
		return insert(entity, true);
	}

	/**
	 * 插入数据（返回_id，_id==-1，则插入失败）<br>
	 * _id 不自增，需设定
	 */
	@SuppressWarnings("resource")
	public long insert(T entity, boolean flag) {
		String sql = "";
		SQLiteDatabase db = null;
		try {
			if (db == null || !db.isOpen()) {
				db = this.dbHelper.getWritableDatabase();
			}
			ContentValues cv = new ContentValues();
			long row = 0L;
			if (flag) {
				sql = setContentValues(entity, cv, TYPE_INCREMENT,
						METHOD_INSERT);// id自增

				LogUtil.d(TAG, "[insert]: insert into " + this.tableName + " "
						+ sql);
				row = db.insert(this.tableName, null, cv);
			} else {
				sql = setContentValues(entity, cv, TYPE_NOT_INCREMENT,
						METHOD_INSERT);// id需指定-插入
				String selection = " _id = ? ";
				String[] selectionArgs = new String[] { String.valueOf(cv
						.getAsInteger("_id").intValue()) };

				boolean isExist = isExistBySelection(selection, selectionArgs);

				if (isExist) {// 如果库里已存在这条数据-更新数据
					sql = setContentValues(entity, cv, TYPE_NOT_INCREMENT,
							METHOD_UPDATE);// id需指定-更新
					LogUtil.d(TAG, "[update]: update " + this.tableName + " "
							+ sql);
					update(entity);
				} else {// 如果库里还没有这条数据-直接插入
					LogUtil.d(TAG, "[insert]: insert into " + this.tableName
							+ " " + sql);
					if (!db.isOpen()) {
						db = this.dbHelper.getWritableDatabase();
					}
					row = db.insert(this.tableName, null, cv);
				}
			}
			return row;
		} catch (Exception e) {
			LogUtil.d(this.TAG, "[insert] into DB Exception.");
			e.printStackTrace();
		} finally {
			if (db != null) {
				db.close();
			}
		}

		return 0L;
	}
	/**
	 * 执行SQL语句
	 * 
	 * @author Sogrey
	 * @date 2013-12-10下午2:16:44
	 * @param sql
	 */
	public void execSQL(String sql) {
		SQLiteDatabase db = this.dbHelper.getWritableDatabase();
		db.execSQL(sql);
		db.close();
	}
	/**
	 * 删除所有记录
	 * 
	 * @author Sogrey
	 * @date 2013-12-10下午1:54:16
	 */
	public void deleteAll() {
		String SqlStr = "delete from " + this.tableName;
		execSQL(SqlStr);
	}
	/**
	 * 按单个ID删除
	 */
	public void delete(int id) {
		String where = this.idColumn + " = ?";
		String[] whereValue = { Integer.toString(id) };

		LogUtil.d(TAG, "[delete]: delelte from " + this.tableName + " where "
				+ where.replace("?", String.valueOf(id)));

		delete(where, whereValue);
	}

	/**
	 * 按多个ID删除
	 */
	public void delete(Integer... ids) {
		if (ids.length > 0) {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < ids.length; i++) {
				sb.append('?').append(',');
			}
			sb.deleteCharAt(sb.length() - 1);
			SQLiteDatabase db = this.dbHelper.getWritableDatabase();
			String sql = "delete from " + this.tableName + " where "
					+ this.idColumn + " in (" + sb + ")";

			LogUtil.d(TAG, "[delete]: " + getLogSql(sql, ids));

			db.execSQL(sql, (Object[]) ids);
			db.close();
		}
	}

	/**
	 * 按条件删除
	 */
	public void delete(String whereClause, String[] whereArgs) {
		SQLiteDatabase db = this.dbHelper.getWritableDatabase();
		db.delete(this.tableName, whereClause, whereArgs);
		db.close();
	}

	/**
	 * 根据主键ID更新
	 */
	public void update(T entity) {
		SQLiteDatabase db = null;
		try {
			db = this.dbHelper.getWritableDatabase();
			ContentValues cv = new ContentValues();

			String sql = setContentValues(entity, cv, TYPE_NOT_INCREMENT,
					METHOD_UPDATE);

			String where = this.idColumn + " = ?";
			int id = Integer.parseInt(cv.get(this.idColumn).toString());
			cv.remove(this.idColumn);

			LogUtil.d(TAG, "[update]: update " + this.tableName + " set " + sql
					+ " where " + where.replace("?", String.valueOf(id)));

			String[] whereValue = { Integer.toString(id) };
			db.update(this.tableName, cv, where, whereValue);
		} catch (Exception e) {
			LogUtil.d(this.TAG, "[update] DB Exception.");
			e.printStackTrace();
		} finally {
			if (db != null)
				db.close();
		}
	}

	/**
	 * 根据条件更新
	 * 
	 * @author Sogrey
	 * @date 2015年5月20日
	 * @param entity
	 *            要入库的实体
	 * @param where
	 *            条件
	 * @param whereValue
	 *            条件对应值
	 */
	public void update(T entity, String where, String[] whereValue) {
		SQLiteDatabase db = null;
		try {
			db = this.dbHelper.getWritableDatabase();
			ContentValues cv = new ContentValues();

			String sql = setContentValues(entity, cv, TYPE_NOT_INCREMENT,
					METHOD_UPDATE);

			cv.remove(this.idColumn);

			LogUtil.d(TAG, "[update]: update " + this.tableName + " set " + sql
					+ " where " + where + "  values( " + whereValue.toString()
					+ ")");

			db.update(this.tableName, cv, where, whereValue);
		} catch (Exception e) {
			LogUtil.d(this.TAG, "[update] DB Exception.");
			e.printStackTrace();
		} finally {
			if (db != null)
				db.close();
		}
	}

	/**
	 * 查找全部
	 */
	public List<T> find() {
		return find(null, null, null, null, null, null, null);
	}

	/**
	 * 按条件查找（不分组，不限定排序包含,不限定查询列）
	 */
	public List<T> find(String selection, String[] selectionArgs) {
		return find(null, selection, selectionArgs, null, null, null, null);
	}

	/**
	 * 按条件查找（不分组，不限定排序包含）
	 */
	public List<T> find(String[] columns, String selection,
			String[] selectionArgs) {
		return find(columns, selection, selectionArgs, null, null, null, null);
	}

	/**
	 * 按条件查找（分组+限定）
	 */
	public List<T> find(String[] columns, String selection,
			String[] selectionArgs, String groupBy, String having,
			String orderBy, String limit) {
		String selectionArgsStr = "[";
		if (selectionArgs != null && selectionArgs.length > 0) {
			for (int i = 0; i < selectionArgs.length; i++) {
				selectionArgsStr += selectionArgs[i];
				if (i<selectionArgs.length-1) {
					selectionArgsStr += ",";
				}
			}
		}
		selectionArgsStr += "]";
		LogUtil.d(TAG, "[find from table <" + this.tableName + ">]columns>"
				+ columns + ", selection>" + selection + ",selectionArgs>"
				+ selectionArgsStr + ", groupBy>" + groupBy + ", having>"
				+ having + ", orderBy>" + orderBy + ", limit>" + limit);

		List<T> list = new ArrayList<T>();
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			db = this.dbHelper.getReadableDatabase();
			cursor = db.query(this.tableName, columns, selection,
					selectionArgs, groupBy, having, orderBy, limit);

			getListFromCursor(list, cursor);
		} catch (Exception e) {
			LogUtil.e(this.TAG, "[find] from DB Exception");
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			if (db != null) {
				db.close();
			}
		}
		return list;
	}

	/**
	 * 查找全部-异步
	 */
	public void FindAsyn(QueryAsynListener l) {
		FindAsyn(null, null, null, null, null, null, null, l);
	}

	/**
	 * 按条件查找（不分组，不限定排序包含,不限定查询列）-异步
	 */
	public void FindAsyn(String selection, String[] selectionArgs,
			QueryAsynListener l) {
		FindAsyn(null, selection, selectionArgs, null, null, null, null, l);
	}

	/**
	 * 按条件查找（不分组，不限定排序包含）-异步
	 */
	public void FindAsyn(String[] columns, String selection,
			String[] selectionArgs, QueryAsynListener l) {
		FindAsyn(columns, selection, selectionArgs, null, null, null, null, l);
	}

	/**
	 * 按条件查找（分组+限定）-异步
	 */
	public void FindAsyn(String[] columns, String selection,
			String[] selectionArgs, String groupBy, String having,
			String orderBy, String limit, QueryAsynListener l) {
		new QueryAsyn(columns, selection, selectionArgs, groupBy, having,
				orderBy, limit, l).execute();
	}

	private String setContentValues(T entity, ContentValues cv, int type,
			int method) throws IllegalAccessException {
		StringBuffer strField = new StringBuffer("(");
		StringBuffer strValue = new StringBuffer(" values(");
		StringBuffer strUpdate = new StringBuffer(" ");
		for (Field field : this.allFields) {
			if (!field.isAnnotationPresent(Column.class)) {
				continue;
			}
			Column column = (Column) field.getAnnotation(Column.class);

			field.setAccessible(true);
			Object fieldValue = field.get(entity);
			if (fieldValue == null)
				continue;
			if ((type == TYPE_INCREMENT)
					&& (field.isAnnotationPresent(Id.class))) {
				continue;
			}
			if (Date.class == field.getType()) {// 处理java.util.Date类型,update
				// 2012-06-10
				cv.put(column.name(), ((Date) fieldValue).getTime());
				continue;
			}
			String value = String.valueOf(fieldValue);
			cv.put(column.name(), value);
			if (method == METHOD_INSERT) {
				strField.append(column.name()).append(",");
				strValue.append("'").append(value).append("',");
			} else {
				strUpdate.append(column.name()).append("=").append("'")
						.append(value).append("',");
			}

		}
		if (method == METHOD_INSERT) {
			strField.deleteCharAt(strField.length() - 1).append(")");
			strValue.deleteCharAt(strValue.length() - 1).append(")");
			return strField.toString() + strValue.toString();
		} else {
			return strUpdate.deleteCharAt(strUpdate.length() - 1).append(" ")
					.toString();
		}
	}

	/**
	 * 将查询的结果保存为名值对map.-sql语句
	 * 
	 * @param sql
	 *            查询sql
	 * @param selectionArgs
	 *            参数值
	 * @return 返回的Map中的key（全部是小写形式）.
	 */
	@SuppressLint("DefaultLocale")
	public List<Map<String, String>> query2MapList(String sql,
			String[] selectionArgs) {
		LogUtil.d(TAG, "[query2MapList]: " + getLogSql(sql, selectionArgs));
		SQLiteDatabase db = null;
		Cursor cursor = null;
		List<Map<String, String>> retList = new ArrayList<Map<String, String>>();
		try {
			db = this.dbHelper.getReadableDatabase();
			cursor = db.rawQuery(sql, selectionArgs);
			while (cursor.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				for (String columnName : cursor.getColumnNames()) {
					int c = cursor.getColumnIndex(columnName);
					if (c < 0) {
						continue; // 如果不存在循环下个属性值
					} else {
						map.put(columnName.toLowerCase(), cursor.getString(c));// 返回的Map中的key全部是小写形式.
						map.put(columnName, cursor.getString(c));
					}
				}
				retList.add(map);
			}
		} catch (Exception e) {
			LogUtil.e(TAG, "[query2MapList] from DB exception");
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			if (db != null) {
				db.close();
			}
		}

		return retList;
	}

	/**
	 * 将查询的结果保存为名值对map.-条件查询
	 * 
	 * @param selection
	 *            查询条件
	 * @param selectionArgs
	 *            参数值
	 * @return 返回的Map中的key(全部是小写形式).
	 */
	public List<Map<String, String>> query2MapListBySelection(String selection,
			String[] selectionArgs) {
		LogUtil.d(
				TAG,
				"[query2MapListBySelection]: "
						+ getLogSqlBySelection("select", selection,
								selectionArgs));
		SQLiteDatabase db = null;
		Cursor cursor = null;
		List<Map<String, String>> retList = new ArrayList<Map<String, String>>();
		try {
			db = this.dbHelper.getReadableDatabase();
			cursor = db.query(this.tableName, null, selection, selectionArgs,
					null, null, null);
			while (cursor.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				for (String columnName : cursor.getColumnNames()) {
					int c = cursor.getColumnIndex(columnName);
					if (c < 0) {
						continue; // 如果不存在循环下个属性值
					} else {
						// map.put(columnName.toLowerCase(),
						// cursor.getString(c));// 返回的Map中的key全部是小写形式.
						map.put(columnName, cursor.getString(c));
					}
				}
				retList.add(map);
			}
		} catch (Exception e) {
			LogUtil.e(TAG, "[query2MapList] from DB exception");
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			if (db != null) {
				db.close();
			}
		}

		return retList;
	}

	/**
	 * 封装执行sql代码.
	 * 
	 * @param sql
	 * @param selectionArgs
	 */
	public void execSql(String sql, Object[] selectionArgs) {
		SQLiteDatabase db = null;
		LogUtil.d(TAG, "[execSql]: " + getLogSql(sql, selectionArgs));
		try {
			db = this.dbHelper.getWritableDatabase();
			if (selectionArgs == null) {
				db.execSQL(sql);
			} else {
				db.execSQL(sql, selectionArgs);
			}
		} catch (Exception e) {
			LogUtil.e(TAG, "[execSql] DB exception.");
			e.printStackTrace();
		} finally {
			if (db != null) {
				db.close();
			}
		}
	}

	/**
	 * LOG-sql
	 */
	private String getLogSql(String sql, Object[] args) {
		if (args == null || args.length == 0) {
			return sql;
		}
		for (int i = 0; i < args.length; i++) {
			sql = "[SQL]"
					+ sql.replaceFirst("\\?", "'" + String.valueOf(args[i])
							+ "'");
		}
		return sql;
	}

	/**
	 * LOG-selection
	 */
	private String getLogSqlBySelection(String doSthing, String selection,
			Object[] args) {
		if (args == null || args.length == 0) {
			return selection;
		}
		for (int i = 0; i < args.length; i++) {
			selection = "[DB]"
					+ (doSthing == null ? "" : doSthing)
					+ " in "
					+ this.tableName
					+ " where "
					+ selection.replaceFirst("\\?",
							"'" + String.valueOf(args[i]) + "'");
		}
		return selection;
	}

	/**
	 * 查询数据监听器
	 * 
	 * @author Sogrey
	 * @date 2015年6月30日
	 */
	public interface QueryAsynListener {
		/**
		 * 抛出查询结果
		 * 
		 * @author Sogrey
		 * @date 2015年6月30日
		 * @param result
		 */
		<T> void post(List<T> result);
	}

	/**
	 * 查询所有数据异步（所有任务）
	 * 
	 * @author Sogrey
	 * @date 2015年6月30日
	 */
	private class QueryAsyn extends AsyncTask<Void, Void, List<T>> {
		String[] columns;
		String selection;
		String[] selectionArgs;
		String groupBy;
		String having;
		String orderBy;
		String limit;
		QueryAsynListener l;

		public QueryAsyn(String[] columns, String selection,
				String[] selectionArgs, String groupBy, String having,
				String orderBy, String limit, QueryAsynListener l) {
			super();
			this.columns = columns;
			this.selection = selection;
			this.selectionArgs = selectionArgs;
			this.groupBy = groupBy;
			this.having = having;
			this.orderBy = orderBy;
			this.limit = limit;
			this.l = l;
		}

		@Override
		protected List<T> doInBackground(Void... params) {
			return find(columns, selection, selectionArgs, groupBy, having,
					orderBy, limit);
		}

		@Override
		protected void onPostExecute(List<T> result) {
			if (this.l != null) {
				this.l.post(result);
			}
			this.cancel(true);
		}

	}

}