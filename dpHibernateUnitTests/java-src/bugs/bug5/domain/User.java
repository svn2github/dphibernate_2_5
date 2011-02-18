package bugs.bug5.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import net.digitalprimates.persistence.hibernate.proxy.HibernateProxy;

/**
 * User类，存储用户权限信息，继承自AbstractTreeLeaf代表此为树的叶子，树枝为组织结构。
 * <p/>
 * 如果当前用户信息从HR中获取，则可以在数据库中建一个视图关联HR的人员信息和系统的登录权限信息
 * <p/>
 * 当前用户与角色没有关联 <p/>
 * 
 * @author zsj 2006-11-28 修改 </br>
 * @author chenjianxin, Jinni 2007-08-15 修订 </br>
 * @author Jinni 2007-12-5 根据后勤项目实际需要进行了修改
 */
public class User extends HibernateProxy
{
	private static final long serialVersionUID = -8894210952016534842L;

	private Long userID = new Date().getTime();

	// 备注
	private String remark;

	// 用户名称
	private String name;

	// 登录名称
	private String loginName;

	// 系统登录密码
	private String password;
	
	// 工卡号
	private String workCode;

	// 当前状态（是否可用）
	private Boolean enabled;
	
	//是否可维护
	private Boolean visiable;

	// 层级顺序
	private String layerOrder;

	// 手机
	private String phoneNum;
	
	//所在岗位（可能包括多个权限岗位）
	private Set<Station> stations;
	
	//所属部门
	private Department department;

	public String getLoginName()
	{
		return this.loginName;
	}

	public void setLoginName(String name)
	{
		this.loginName = name;
	}

	public Long getUserID()
	{
		return this.userID;
	}

	public void setUserID(Long userID)
	{
		this.userID = userID;
	}

	public String getRemark()
	{
		return this.remark;
	}

	public void setRemark(String remark)
	{
		this.remark = remark;
	}

	public String getPassword()
	{
		return this.password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public Boolean getEnabled()
	{
		return enabled;
	}

	public void setEnabled(Boolean enabled)
	{
		this.enabled = enabled;
	}
	
/*	*//**
	 * 获取就职岗位
	 * 
	 * @return
	 *//*
	public Station getStation()
	{
		return null;
	}*/

	public Set<Station> getStations()
	{
		if (this.stations == null)
		{
			this.stations = new HashSet<Station>();
		}
		return stations;
	}

	public void setStations(Set<Station> stations)
	{
		this.stations = stations;
	}

	public String getLayerOrder()
	{
		return layerOrder;
	}

	public void setLayerOrder(String layerOrder)
	{
		this.layerOrder = layerOrder;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Department getDepartment()
	{
		return department;
	}

	public void setDepartment(Department department)
	{
		this.department = department;
	}

	public String getPhoneNum()
	{
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum)
	{
		this.phoneNum = phoneNum;
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
		if (anObject != null && anObject instanceof User)
		{
			User p = (User) anObject;

			if (p.getUserID() != null && p.getUserID().equals(getUserID()))
			{
				return true;
			}
		}

		return false;
	}

	public String getWorkCode() {
		return workCode;
	}

	public void setWorkCode(String workCode) {
		this.workCode = workCode;
	}
}
