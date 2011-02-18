package bugs.bug5.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.digitalprimates.persistence.hibernate.proxy.HibernateProxy;

/**
 * 岗位表，此类还用于记录用户数据权限信息（利用层级结构记录可访问的数据权限）
 * 
 * @author chenjianxin, Jinni 2007-08-15
 *
 */
public class Station extends HibernateProxy
{
	private static final long serialVersionUID = -7694832307945224952L;

	protected Long stationID=new Date().getTime();

	protected String name=null;
	
	// parentList和parent只能有一个存在，另外一个为null
	protected List<Station> parentList=null;

	protected List<Station> childList=null;
	
	//层级顺序
	private String layerOrder;
	//备注
	private String remark;
	//岗位关联的角色
	private Set<Role> roles;
	//岗位类型（分为实际所属岗位，权限分责岗位） */
	private String type;
	//该岗位所拥有的用户
	private Set<User> users;
	//当前岗位是否可用
	private Boolean enabled;
	//是否可维护
	private Boolean visiable;
	//索引号
	private String sn;
	//当前岗位所关联的部门
	private Department department;

	public String getRemark()
    {
    	return remark;
    }

	public void setRemark(String remark)
    {
    	this.remark = remark;
    }

	public Set<Role> getRoles()
    {
    	return roles;
    }

	public void setRoles(Set<Role> roles)
    {
    	this.roles = roles;
    }

	public Set<User> getUsers()
    {
		if(this.users == null)
		{
			this.users = new HashSet<User>();
		}
    	return users;
    }

	public void setUsers(Set<User> users)
    {
    	this.users = users;
    }
	
	public String getType()
    {
    	return type;
    }

	public void setType(String type)
    {
    	this.type = type;
    }

	public Boolean getEnabled()
    {
    	return enabled;
    }

	public void setEnabled(Boolean enabled)
    {
    	this.enabled = enabled;
    }

	public String getSn()
    {
    	return sn;
    }

	public void setSn(String sn)
    {
    	this.sn = sn;
    }

	public Department getDepartment()
    {
    	return department;
    }

	public void setDepartment(Department department)
    {
    	this.department = department;
    }

	public Boolean getVisiable()
    {
    	return visiable;
    }

	public void setVisiable(Boolean visiable)
    {
    	this.visiable = visiable;
    }

	public boolean equals(Object anObject)
	{
		if (anObject != null && anObject instanceof Station)
		{
			Station p = (Station) anObject;

			if (p.getStationID() != null && p.getStationID().equals(getStationID()))
			{
				return true;
			}
		}

		return false;
	}

	public Long getStationID() {
		return stationID;
	}

	public void setStationID(Long stationID) {
		this.stationID = stationID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Station> getParentList() {
		return parentList;
	}

	public void setParentList(List<Station> parentList) {
		this.parentList = parentList;
	}

	public List<Station> getChildList() {
		return childList;
	}

	public void setChildList(List<Station> childList) {
		this.childList = childList;
	}

	public String getLayerOrder() {
		return layerOrder;
	}

	public void setLayerOrder(String layerOrder) {
		this.layerOrder = layerOrder;
	}
}
