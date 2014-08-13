package com.meiliwan.emall.bkstage.bean;

import com.meiliwan.emall.core.bean.BaseEntity;

public class BksMenu extends BaseEntity implements Comparable<BksMenu> {
    private static final long serialVersionUID = 5684479063419391112L;
    private Integer menuId;

    private Integer parentId;

    private String roleKey;

    private String authorization;

    private String url;

    private Integer menuType;

    private String name;

    private String model;

    private String target;

    private Integer state;

    private Integer sequence;

    private String description;

    public String getRoleKey() {
        return roleKey;
    }

    public void setRoleKey(String roleKey) {
        this.roleKey = roleKey;
    }

    private Integer isDel;

    public Integer getIsDel() {
        return isDel;
    }

    public void setIsDel(Integer del) {
        isDel = del;
    }


    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getAuthorization() {
        return authorization;
    }

    public void setAuthorization(String authorization) {
        this.authorization = authorization == null ? null : authorization.trim();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public Integer getMenuType() {
        return menuType;
    }

    public void setMenuType(Integer menuType) {
        this.menuType = menuType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model == null ? null : model.trim();
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target == null ? null : target.trim();
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    @Override
    public Integer getId() {
        return this.getMenuId();
    }

    @Override
    public int compareTo(BksMenu o) {
        return o.getSequence().compareTo(this.sequence);
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((authorization == null) ? 0 : authorization.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((isDel == null) ? 0 : isDel.hashCode());
		result = prime * result + ((menuId == null) ? 0 : menuId.hashCode());
		result = prime * result
				+ ((menuType == null) ? 0 : menuType.hashCode());
		result = prime * result + ((model == null) ? 0 : model.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((parentId == null) ? 0 : parentId.hashCode());
		result = prime * result + ((roleKey == null) ? 0 : roleKey.hashCode());
		result = prime * result
				+ ((sequence == null) ? 0 : sequence.hashCode());
		result = prime * result + ((state == null) ? 0 : state.hashCode());
		result = prime * result + ((target == null) ? 0 : target.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BksMenu other = (BksMenu) obj;
		if (authorization == null) {
			if (other.authorization != null)
				return false;
		} else if (!authorization.equals(other.authorization))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (isDel == null) {
			if (other.isDel != null)
				return false;
		} else if (!isDel.equals(other.isDel))
			return false;
		if (menuId == null) {
			if (other.menuId != null)
				return false;
		} else if (!menuId.equals(other.menuId))
			return false;
		if (menuType == null) {
			if (other.menuType != null)
				return false;
		} else if (!menuType.equals(other.menuType))
			return false;
		if (model == null) {
			if (other.model != null)
				return false;
		} else if (!model.equals(other.model))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (parentId == null) {
			if (other.parentId != null)
				return false;
		} else if (!parentId.equals(other.parentId))
			return false;
		if (roleKey == null) {
			if (other.roleKey != null)
				return false;
		} else if (!roleKey.equals(other.roleKey))
			return false;
		if (sequence == null) {
			if (other.sequence != null)
				return false;
		} else if (!sequence.equals(other.sequence))
			return false;
		if (state == null) {
			if (other.state != null)
				return false;
		} else if (!state.equals(other.state))
			return false;
		if (target == null) {
			if (other.target != null)
				return false;
		} else if (!target.equals(other.target))
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		return true;
	}
    
}