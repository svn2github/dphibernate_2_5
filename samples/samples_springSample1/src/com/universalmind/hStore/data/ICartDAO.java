package com.universalmind.hStore.data;

import java.util.ArrayList;

import com.universalmind.hStore.model.vo.Cart;

public interface ICartDAO {

	public abstract Cart create(Cart cart);

	public abstract Cart read(String id);

	public abstract void update(Cart cart);

	public abstract void delete(Cart cart);
	
	public abstract ArrayList<Cart> getAllCarts();

}