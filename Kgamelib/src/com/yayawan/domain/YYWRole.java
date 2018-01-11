package com.yayawan.domain;

public class YYWRole {



	private String roleId;

	private String roleName;

	private String roleLevel;

	private String zoneId;

	private String zoneName;
	
	private String roleCTime;
	
	private String ext;
	

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

	public String getRoleCTime() {
		return roleCTime;
	}

	public void setRoleCTime(String roleCTime) {
		this.roleCTime = roleCTime;
	}

	public String getExt() {
		return ext;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}

	public YYWRole (){};


	public YYWRole(String roleId, String roleName, String roleLevel, String zoneId, String zoneName) {
		super();
		this.roleId = roleId;
		this.roleName = roleName;
		this.roleLevel = roleLevel;
		this.zoneId = zoneId;
		this.zoneName = zoneName;
	};
	public YYWRole(String roleId, String roleName, String roleLevel, String zoneId, String zoneName,String roleCTime,String ext) {
		super();
		this.roleId = roleId;
		this.roleName = roleName;
		this.roleLevel = roleLevel;
		this.zoneId = zoneId;
		this.zoneName = zoneName;
		this.roleCTime = roleCTime;
		this.ext = ext;
	};

	@Override
	public String toString() {
		return "YYWRole [roleId=" + roleId + ", roleName=" + roleName
				+ ", roleLevel=" + roleLevel + ", zoneId=" + zoneId
				+ ", zoneName=" + zoneName + ", roleCTime=" + roleCTime
				+ ", ext=" + ext + "]";
	}


}
