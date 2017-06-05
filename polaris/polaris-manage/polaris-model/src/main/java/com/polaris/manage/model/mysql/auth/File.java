package com.polaris.manage.model.mysql.auth;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.polaris.manage.model.mysql.BaseMysqlObject;

/**
 * 文件表
 * 
 * @author John
 *
 */
@Entity
@Table(name = "PMS_FILE")
public class File extends BaseMysqlObject {

	private static final long serialVersionUID = 8867563744221935474L;
	
	private String name; // 文件名
	
	private String path; // 文件路径

	@Column(name = "NAME", nullable = false, length = 32, columnDefinition = "varchar(32) default '' comment '文件名'")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "PATH", nullable = false, length = 255, columnDefinition = "varchar(255) default '' comment '文件路径'")
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
}
