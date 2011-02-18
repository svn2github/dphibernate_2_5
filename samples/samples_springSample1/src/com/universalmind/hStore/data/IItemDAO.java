package com.universalmind.hStore.data;

import java.util.ArrayList;

import com.universalmind.hStore.model.vo.Item;

public interface IItemDAO {

	public abstract Item merge(Item item);

	public abstract Item create(Item item);

	public abstract Item read(String id);

	public abstract void update(Item item);

	public abstract void delete(Item item);

	public abstract ArrayList<Item> getAllItems();
}