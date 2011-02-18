package com.universalmind.hStore.data;

import java.util.ArrayList;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.universalmind.hStore.model.vo.Item;

public class ItemDAO extends HibernateDaoSupport implements IItemDAO {
	
	public ItemDAO(){
		super();
	}
	
	/* (non-Javadoc)
	 * @see com.universalmind.hStore.data.IItemDAO#create(com.universalmind.hStore.model.vo.Item)
	 */
	public Item merge(Item item){
		Item _item = (Item) getHibernateTemplate().merge(item);
		return _item;
	}
	
	/* (non-Javadoc)
	 * @see com.universalmind.hStore.data.IItemDAO#create(com.universalmind.hStore.model.vo.Item)
	 */
	public Item create(Item item){
		String id = (String) getHibernateTemplate().save(item);
		return (Item) read(id);
	}
	
	/* (non-Javadoc)
	 * @see com.universalmind.hStore.data.IItemDAO#read(java.lang.String)
	 */
	public Item read(String id){
		Item result = (Item) getHibernateTemplate().load(Item.class, id);
		return result;
	}
	
	/* (non-Javadoc)
	 * @see com.universalmind.hStore.data.IItemDAO#update(com.universalmind.hStore.model.vo.Item)
	 */
	public void update(Item item){
		getHibernateTemplate().update(item);
	}
	
	/* (non-Javadoc)
	 * @see com.universalmind.hStore.data.IItemDAO#delete(com.universalmind.hStore.model.vo.Item)
	 */
	public void delete(Item item){
		getHibernateTemplate().delete(item);
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Item> getAllItems(){
		return (ArrayList<Item>) getHibernateTemplate().findByNamedQuery("all.items");
	}
}
