package com.universalmind.hStore.data;

import java.util.ArrayList;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.universalmind.hStore.model.vo.Cart;

public class CartDAO extends HibernateDaoSupport implements ICartDAO {
	
	public CartDAO(){
		super();
	}
	
	/* (non-Javadoc)
	 * @see com.universalmind.hStore.data.ICartDAO#create(com.universalmind.hStore.model.vo.Cart)
	 */
	public Cart create(Cart cart){
		String id = (String) getHibernateTemplate().save(cart);
		return read(id);
		
	}
	
	/* (non-Javadoc)
	 * @see com.universalmind.hStore.data.ICartDAO#read(java.lang.String)
	 */
	public Cart read(String id){
		Cart result = (Cart) getHibernateTemplate().load(Cart.class,id);
		return result;
	}
	
	/* (non-Javadoc)
	 * @see com.universalmind.hStore.data.ICartDAO#update(com.universalmind.hStore.model.vo.Cart)
	 */
	public void update(Cart cart){
		getHibernateTemplate().update(cart);
	}
	
	/* (non-Javadoc)
	 * @see com.universalmind.hStore.data.ICartDAO#delete(com.universalmind.hStore.model.vo.Cart)
	 */
	public void delete(Cart cart){
		getHibernateTemplate().delete(cart);
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Cart> getAllCarts() {
		return (ArrayList<Cart>) getHibernateTemplate().findByNamedQuery("all.carts");
	}
	
}
