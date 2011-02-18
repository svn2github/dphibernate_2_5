package bugs.bug5.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.digitalprimates.persistence.hibernate.proxy.HibernateProxy;

/**
 * 菜单类,如果菜单与权限关联，用户需要拥有菜单所关联的权限中的任何一个，
 * <p/>
 * 才能访问此菜单（需要协同Acegi进行URL过滤）
 * 
 * @author chenjianxin, Jinni 2007-08-15
 * 
 */
public class Menu extends HibernateProxy
{
	private static final long serialVersionUID = 4307237041227013377L;

	protected Long menuID=new Date().getTime();

	protected String name=null;

	protected Menu parent=null;
	
	// parentList和parent只能有一个存在，另外一个为null
	protected List<Menu> parentList=null;

	protected List<Menu> childList=null;
	
	//层级顺序
	private String layerOrder;
	
	// 菜单类型（功能F，模块M）
	private String type;
	// 当前状态(可用1，不可用0)
	private Boolean enabled;
	//备注
	private String remark;
	//菜单的URL
	private String path;
	//URL所带的参数
	private String parameters;
	//该菜单所关联的权限
	private Set<Permission> permissions;

	/**
	 * @return 返回 remark。
	 * 
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * @param remark
	 *            要设置的 remark。
	 * 
	 */
	public void setRemark(String remark) {
		this.remark = remark;
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

	/**
	 * @return 返回 path。
	 * 
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path
	 *            要设置的 path。
	 * 
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * @return 返回 parameters。
	 * 
	 */
	public String getParameters() {
		return parameters;
	}

	/**
	 * @param parameters
	 *            要设置的 parameters。
	 * 
	 */
	public void setParameters(String parameters) {
		this.parameters = parameters;
	}
	
	public Set<Permission> getPermissions()
    {
		if(this.permissions == null)
		{
			this.permissions = new HashSet<Permission>();
		}
    	return permissions;
    }

	public void setPermissions(Set<Permission> permissions)
    {
    	this.permissions = permissions;
    }

	public boolean equals(Object anObject)
	{
		if (anObject != null && anObject instanceof Menu)
		{
			Menu p = (Menu) anObject;

			if (p.getMenuID() != null && p.getMenuID().equals(getMenuID()))
			{
				return true;
			}
		}

		return false;
	}

	public Long getMenuID() {
		return menuID;
	}

	public void setMenuID(Long menuID) {
		this.menuID = menuID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Menu getParent() {
		return parent;
	}

	public void setParent(Menu parent) {
		this.parent = parent;
	}

	public List<Menu> getParentList() {
		return parentList;
	}

	public void setParentList(List<Menu> parentList) {
		this.parentList = parentList;
	}

	public List<Menu> getChildList() {
		return childList;
	}

	public void setChildList(List<Menu> childList) {
		this.childList = childList;
	}

	public String getLayerOrder() {
		return layerOrder;
	}

	public void setLayerOrder(String layerOrder) {
		this.layerOrder = layerOrder;
	}
}
