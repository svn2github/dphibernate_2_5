package bugs.bug5.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import net.digitalprimates.persistence.hibernate.proxy.HibernateProxy;

/**
 * 角色类，角色一般不与用户直接关联，通过与岗位关联用户可以对用户的数据权限进行精确
 * <p/>
 * 的控制，如果需要关联到人，则由程序控制此人所拥有的数据权限
 * 
 * @author chenjianxin, Jinni 2007-08-15
 * 
 */
public class Role extends HibernateProxy{

	private static final long serialVersionUID = 2228952832035911684L;

	private String roleID = UUID.randomUUID().toString();

	private String name;

	// Fields
	private Boolean enabled;
	
	//是否可维护
	private Boolean visiable;

	private String remark;

	private Set<Station> stations;

	private Set<Permission> permissions;

	/** default constructor */
	public Role() {
	}

	/** constructor with id */
	public Role(String id) {
		this.roleID = id;
	}

	public Boolean getVisiable()
    {
    	return visiable;
    }

	public void setVisiable(Boolean visiable)
    {
    	this.visiable = visiable;
    }

	public Boolean getEnabled()
    {
    	return enabled;
    }

	public void setEnabled(Boolean enabled)
    {
    	this.enabled = enabled;
    }

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
     * @return the permissions
     */
    public Set<Permission> getPermissions()
    {
    	if(this.permissions == null)
    	{
    		this.permissions = new HashSet<Permission>();
    	}
    	return permissions;
    }

	/**
     * @param permissions the permissions to set
     */
    public void setPermissions(Set<Permission> permissions)
    {
    	this.permissions = permissions;
    }

	/**
     * @return the stations
     */
    public Set<Station> getStations()
    {
    	if(this.stations == null)
    	{
    		this.stations = new HashSet<Station>();
    	}
    	return stations;
    }

	/**
     * @param stations the stations to set
     */
    public void setStations(Set<Station> stations)
    {
    	this.stations = stations;
    }

	/**
	 * @return 返回 id。
	 * 
	 */
	public String getRoleID() {
		return roleID;
	}

	/**
	 * @param id
	 *            要设置的 id。
	 * 
	 */
	public void setRoleID(String roleID) {
		this.roleID = roleID;
	}

	/**
	 * @return 返回 name。
	 * 
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            要设置的 name。
	 * 
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	public boolean equals(Object anObject) {
		if (anObject != null && anObject instanceof Role) {
			Role p = (Role) anObject;

			if (p.getRoleID() != null && p.getRoleID().equals(getRoleID()))
				return true;
		}

		return false;
	}
}