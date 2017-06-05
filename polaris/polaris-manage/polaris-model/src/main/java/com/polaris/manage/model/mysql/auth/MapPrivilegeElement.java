package com.polaris.manage.model.mysql.auth;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.polaris.manage.model.mysql.BaseMysqlObject;

/**
 * 权限菜单映射表
 * @author John
 *
 */
@Entity
@Table(name = "PMS_MAP_PRIVILEGE_ELEMENT")
public class MapPrivilegeElement extends BaseMysqlObject {

	private static final long serialVersionUID = -7208177982189783654L;

	private String privilegeId;

	private String elementId;

	@Column(name = "PRIVILEGE_ID", nullable = false, length = 64, columnDefinition = "varchar(64) default '' comment '外键-权限ID'")
	public String getPrivilegeId() {
		return privilegeId;
	}

	public void setPrivilegeId(String privilegeId) {
		this.privilegeId = privilegeId;
	}

	@Column(name = "ELEMENT_ID", nullable = false, length = 64, columnDefinition = "varchar(64) default '' comment '外键-页面元素ID'")
	public String getElementId() {
		return elementId;
	}

	public void setElementId(String elementId) {
		this.elementId = elementId;
	}

}
