package com.sogrey.frame.db.sqlite.model;

import com.sogrey.frame.db.ahibernate.annotation.Column;
import com.sogrey.frame.db.ahibernate.annotation.Id;

//此处没有加Table属性,它是其他类的基类,本类中用@Column注解的字段在子类中同样会被创建到表中.
public class CommonEntity {
	@Id
	@Column(name = "_id", type = "INTEGER")
	private int _id; // 主键,int类型,数据库建表时此字段会设为自增长

	public int getKeyId() {
		return _id;
	}

	public void setKeyId(int keyId) {
		this._id = keyId;
	}
}
