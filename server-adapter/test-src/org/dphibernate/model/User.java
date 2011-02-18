package org.dphibernate.model;

import org.dphibernate.serialization.annotations.NeverSerialize;

public class User
{
	private final String username;
	private final String password;

	public User(String username,String password)
	{
		this.username = username;
		this.password = password;
		
	}

	public String getUsername()
	{
		return username;
	}
	@NeverSerialize
	public String getPassword()
	{
		return password;
	}
}
