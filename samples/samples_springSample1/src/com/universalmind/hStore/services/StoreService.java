package com.universalmind.hStore.services;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.proxy.HibernateProxy;

import com.universalmind.hStore.data.ICartDAO;
import com.universalmind.hStore.data.IItemDAO;
import com.universalmind.hStore.factories.SpringFactory;
import com.universalmind.hStore.model.vo.Cart;
import com.universalmind.hStore.model.vo.Item;
import com.universalmind.hStore.util.SpringSessionUtil;

public class StoreService //nimer: remove extends 
{

	//Injected at runtime
	private IItemDAO itemDAO;
	public void setItemDAO(IItemDAO itemDAO) {
		this.itemDAO = itemDAO;
	
	}
	
	//Injected at runtime
	private ICartDAO cartDAO;
	public void setCartDAO(ICartDAO cartDAO) {
		this.cartDAO = cartDAO;
	}
	
	public StoreService(){}
	
	
	
	public void resetDb()
	{
		Session session = SpringSessionUtil.getCurrentSession();
		
		try
		{
			PreparedStatement sa = session.connection().prepareStatement( "delete from public.items" );
			PreparedStatement sb = session.connection().prepareStatement( "delete from public.cart_items" );
			PreparedStatement sc = session.connection().prepareStatement( "delete from public.carts" );
			PreparedStatement s1 = session.connection().prepareStatement( "insert into public.items(id,sku,name,price,added)  values('1a674729-2a05-4ec5-b204-972684a93184',123456,'Test1',10,now())" );
			PreparedStatement s2 = session.connection().prepareStatement( "insert into public.items(id,sku,name,price,added)  values('1a67a729-2a05-4ec5-b204-972684a93184',123457,'Test2',10,now())" );
			PreparedStatement s3 = session.connection().prepareStatement( "insert into public.carts(id,cartdate,orderid,total) values('ea67a729-2a05-4ec5-b204-972684a93184',now(),'1a67a729-2a05-4ec5-b204-972684a9318a',20)" );
			PreparedStatement s4 = session.connection().prepareStatement( "insert into public.cart_items(id,item,idx) values('ea67a729-2a05-4ec5-b204-972684a93184','1a674729-2a05-4ec5-b204-972684a93184',0)" );
			PreparedStatement s5 = session.connection().prepareStatement( "insert into public.cart_items(id,item,idx) values('ea67a729-2a05-4ec5-b204-972684a93184','1a67a729-2a05-4ec5-b204-972684a93184',1)" );
			
			sb.execute();
			sc.execute();
			sa.execute();
			s1.execute();
			s2.execute();
			s3.execute();
			s4.execute();
			s5.execute();
			
		}catch(SQLException ex){
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}
			
	}
	
	
	public ArrayList<Item> getAllItems(){
		return this.itemDAO.getAllItems();
	}
	
	public ArrayList<Cart> getAllCarts(){
		return this.cartDAO.getAllCarts();
	}
	
	public Cart saveCart(Cart cart){
		return this.cartDAO.create(cart);
	}
	
	public Item saveItem(Item item){
		
		return this.itemDAO.merge(item);
		/*
		if(item.getId().length() == 0)
			return this.itemDAO.create(item);
		else{
			this.itemDAO.update(item);
			return null;			
		}
		 */
			
	}
	
	
	// nimer: add load method
	public Object load(Class clazz, Serializable id) 
	{
		Object result = null;
		if( "com.universalmind.hStore.model.vo.Item".equals(clazz.getName()) )
		{
			result = this.itemDAO.read(id.toString());			
		}
		else if( "com.universalmind.hStore.model.vo.Cart".equals(clazz.getName()) )
		{
			result = this.cartDAO.read(id.toString());			
		} 
		
		// if for some reason hibernate is waiting until the session is closed to initialize
		// the collection we need to force the issue now.
		if( !Hibernate.isInitialized(result) )
		{
			Hibernate.initialize(result);
			//SpringSessionUtil.getCurrentSession().flush();
		}
		return result;
	}
	
	
	
}