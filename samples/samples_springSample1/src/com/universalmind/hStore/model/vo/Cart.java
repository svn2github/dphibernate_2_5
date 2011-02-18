package com.universalmind.hStore.model.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import net.digitalprimates.persistence.hibernate.proxy.*;

public class Cart implements Serializable {

	private static final long serialVersionUID = -8030997145136302581L;
	private String 			id;
	private Date   			cartDate;
	private String 			orderID;
	@SuppressWarnings("unchecked")
	private List			items;
	private Double			total;
	
	public Cart(){}

	public Cart(Date cartDate) {
		super();
		this.cartDate = cartDate;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getCartDate() {
		return cartDate;
	}

	public void setCartDate(Date cartDate) {
		this.cartDate = cartDate;
	}

	public String getOrderID() {
		return orderID;
	}

	public void setOrderID(String orderID) {
		
		this.orderID = orderID;
	}

	@SuppressWarnings("unchecked")
	public List getItems() {
		return items;
	}
	
	@SuppressWarnings("unchecked")
	public void addItem(Item item){
		this.items.add(item);
		this.total += item.getPrice();
	}
	
	public void removeItem(Item item){
		if(this.items.contains(item)){
			this.items.remove(item);
			this.total -= item.getPrice();
		}
	}

	@SuppressWarnings("unchecked")
	public void setItems(List items) {
		this.items = items;
	}
	
	@SuppressWarnings("unchecked")
	public void setItems() {
		this.items = new ArrayList();
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}
	
	
	
	
}
