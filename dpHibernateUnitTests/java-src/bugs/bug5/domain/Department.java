package bugs.bug5.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.digitalprimates.persistence.hibernate.proxy.HibernateProxy;

/**
 * 组织结构类，该类存放的不仅仅是部门，同时包含部门的所有上层组织结构信息，如公司，集团等
 * 
 * @author chenjianxin, Jinni 2007-08-15
 * 
 */
public class Department extends HibernateProxy{
	private static final long serialVersionUID = -7268510204675159707L;
	protected Long departmentID = new Date().getTime();
	
	protected String name = null;

	protected Department parent = null;

	// parentList和parent只能有一个存在，另外一个为null
	protected List<Department> parentList = null;

	protected List<Department> childList = null;

	// 层级顺序
	private String layerOrder;

	// 索引号
	private String sn;
	// 备注
	private String remark;
	// 关联的岗位
	private Set<Station> stations;
	// 关联的用户
	private Set<User> users;

	// 部门类型，子部门，部门，公司等
	private String type;

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Set<Station> getStations() {
		if (this.stations == null) {
			this.stations = new HashSet<Station>();
		}
		return stations;
	}

	public void setStations(Set<Station> stations) {
		this.stations = stations;
	}

	public Set<User> getUsers() {
		if (this.users == null) {
			this.users = new HashSet<User>();
		}
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	public boolean equals(Object anObject) {
		if (anObject != null && anObject instanceof Department) {
			Department p = (Department) anObject;

			if (p.getDepartmentID() != null && p.getDepartmentID().equals(getDepartmentID())) {
				return true;
			}
		}

		return false;
	}

	public Long getDepartmentID() {
		return departmentID;
	}

	public void setDepartmentID(Long departmentID) {
		this.departmentID = departmentID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Department getParent() {
		return parent;
	}

	public void setParent(Department parent) {
		this.parent = parent;
	}

	public List<Department> getParentList() {
		return parentList;
	}

	public void setParentList(List<Department> parentList) {
		this.parentList = parentList;
	}

	public List<Department> getChildList() {
		return childList;
	}

	public void setChildList(List<Department> childList) {
		this.childList = childList;
	}

	public String getLayerOrder() {
		return layerOrder;
	}

	public void setLayerOrder(String layerOrder) {
		this.layerOrder = layerOrder;
	}
}
