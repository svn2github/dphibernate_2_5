package bugs.bug5.domain;

import java.util.Date;
import java.util.Set;

import net.digitalprimates.persistence.hibernate.proxy.HibernateProxy;

/**
 * 权限资源类，记录安全框架所管理的最小单元，权限的判断应该使用Permission，
 * 而不应该使用Role，这样做可以做到动态控制角色。
 * 
 * @author chenjianxin, Jinni 2007-08-15
 *
 */
public class Permission extends HibernateProxy
{	
	private static final long serialVersionUID = -4056701504138097248L;

	private Long permissionID = new Date().getTime();
    
    private String name;
    
    //权限内容，如果为方法，记录方法名称
    private String content;
    
    //权限类别
    private String type;
    
    //当前状态(可用，不可用)
	private Boolean enabled;
	
	//是否可维护
	private Boolean visiable;
	
	private String layerOrder;

	private String remark;
	
	private Set<Role> roles;

	/**
     * @return the remark
     */
    public String getRemark()
    {
    	return remark;
    }

	/**
     * @param remark the remark to set
     */
    public void setRemark(String remark)
    {
    	this.remark = remark;
    }

	public Boolean getEnabled()
    {
    	return enabled;
    }

	public void setEnabled(Boolean enabled)
    {
    	this.enabled = enabled;
    }

	/**
     * @return the id
     */
    public Long getPermissionID()
    {
    	return permissionID;
    }

	/**
     * @param id the id to set
     */
    public void setPermissionID(Long permissionID)
    {
    	this.permissionID = permissionID;
    }

	/**
     * @return the layerOrder
     */
    public String getLayerOrder()
    {
    	return layerOrder;
    }

	/**
     * @param layerOrder the layerOrder to set
     */
    public void setLayerOrder(String layerOrder)
    {
    	this.layerOrder = layerOrder;
    }

	/**
     * @return the name
     */
    public String getName()
    {
    	return name;
    }

	/**
     * @param name the name to set
     */
    public void setName(String name)
    {
    	this.name = name;
    }

	/**
     * @return the roles
     */
    public Set<Role> getRoles()
    {
    	return roles;
    }

	/**
     * @param roles the roles to set
     */
    public void setRoles(Set<Role> roles)
    {
    	this.roles = roles;
    }
    
    public String getType()
    {
    	return type;
    }

	public void setType(String type)
    {
    	this.type = type;
    }

	public String getContent()
    {
    	return content;
    }

	public void setContent(String content)
    {
    	this.content = content;
    }

	public Boolean getVisiable()
    {
    	return visiable;
    }

	public void setVisiable(Boolean visiable)
    {
    	this.visiable = visiable;
    }

	public boolean equals(Object anObject) {
		if (anObject != null && anObject instanceof Permission) {
			Permission p = (Permission) anObject;

			if (p.getPermissionID() != null && p.getPermissionID().equals(getPermissionID()))
				return true;
		}

		return false;
	}
}
