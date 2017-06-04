package com.polaris.manage.model.mysql.auth;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.polaris.common.base.BaseObject;

/**
 * 菜单表
 * 
 * @author John
 *
 */
@Entity
@Table(name = "PMS_MENU")
public class Menu extends BaseObject implements Serializable {

	private static final long serialVersionUID = 8773879710657400266L;

	private String id;

	private String parentId;

	private String name;

	private String accessUrl;

	private Integer level;

	private String path;
	
	private Integer orderIndex;

	private String creator; // 创建者

	private Timestamp createTime; // 创建时间

	private String updater; // 更新者

	private Timestamp updateTime; // 更新时间
	
	@Id
	@GeneratedValue(generator = "idGenerator")
	@GenericGenerator(name = "idGenerator", strategy = "uuid")
	@Column(name = "ID", nullable = false, columnDefinition = "varchar(64) default '' comment '主键唯一标识'")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "NAME", nullable = false, length = 20, columnDefinition = "varchar(20) default '' comment '菜单名'")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "PARENT_ID", nullable = false, length = 64, columnDefinition = "varchar(64) default '' comment '父菜单ID'")
	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	@Column(name = "LEVEL", nullable = false, columnDefinition = "int(2) default 0 comment '菜单级数'")
	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	@Column(name = "PATH", nullable = false, length = 512, columnDefinition = "varchar(512) default '' comment '父子菜单关系路径'")
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Column(name = "ACCESS_URL", nullable = false, length = 128, columnDefinition = "varchar(128) default '' comment '访问路径'")
	public String getAccessUrl() {
		return accessUrl;
	}

	public void setAccessUrl(String accessUrl) {
		this.accessUrl = accessUrl;
	}

	@Column(name = "ORDER_INDEX", nullable = false, columnDefinition = "int(10) default 0 comment '菜单排序索引'")
	public Integer getOrderIndex() {
		return orderIndex;
	}

	public void setOrderIndex(Integer orderIndex) {
		this.orderIndex = orderIndex;
	}
	
	@Column(name = "CREATOR", nullable = false, columnDefinition = "varchar(64) default '' comment '创建者ID'")
	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	@Column(name = "CREATE_TIME", nullable = false, updatable = false, columnDefinition = "DATETIME default CURRENT_TIMESTAMP comment '创建时间'")
	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	@Column(name = "UPDATER", nullable = false, columnDefinition = "varchar(64) default '' comment '更新者ID'")
	public String getUpdater() {
		return updater;
	}

	public void setUpdater(String updater) {
		this.updater = updater;
	}

	@Column(name = "UPDATE_TIME", nullable = true, columnDefinition = "DATETIME default NULL comment '更新时间'")
	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

}
