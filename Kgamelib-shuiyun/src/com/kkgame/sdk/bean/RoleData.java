package com.kkgame.sdk.bean;

public class RoleData {



	private String roleId;

	private String roleName;

	private String roleLevel;

	private String zoneId;

	private String zoneName;


	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleLevel() {
		return roleLevel;
	}

	public void setRoleLevel(String roleLevel) {
		this.roleLevel = roleLevel;
	}

	public String getZoneId() {
		return zoneId;
	}

	public void setZoneId(String zoneId) {
		this.zoneId = zoneId;
	}

	public String getZoneName() {
		return zoneName;
	}

	public void setZoneName(String zoneName) {
		this.zoneName = zoneName;
	}

	public RoleData (){};


	public RoleData(String roleId, String roleName, String roleLevel, String zoneId, String zoneName) {
		super();
		this.roleId = roleId;
		this.roleName = roleName;
		this.roleLevel = roleLevel;
		this.zoneId = zoneId;
		this.zoneName = zoneName;
	};

	@Override
	public String toString() {
		return "YYWRole [roleId=" + roleId + ", roleName=" + roleName
				+ ", roleLevel=" + roleLevel + ", zoneId=" + zoneId
				+ ", zoneName=" + zoneName + "]";
	}


}
