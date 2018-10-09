package com.hzy.pojo;

public class TbUser {
    private String userId;

    private String userName;

    private String userPassword;
    
    private String roles;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword == null ? null : userPassword.trim();
    }

	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}

	@Override
	public String toString() {
		return "TbUser [userId=" + userId + ", userName=" + userName + ", userPassword=" + userPassword + ", roles="
				+ roles + "]";
	}

	
    
    
}