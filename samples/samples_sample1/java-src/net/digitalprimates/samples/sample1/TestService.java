package net.digitalprimates.samples.sample1;

import java.util.Collection;

import net.digitalprimates.samples.sample1.beans.User;
import net.digitalprimates.persistence.hibernate.utils.services.HibernateService;

@SuppressWarnings("unchecked")
public class TestService extends HibernateService 
{

	public Collection getUsers()
	{
		Collection users = super.list(User.class);
		return users;
	}
	
	public User getUser(String id)
	{
	    User result = (User)super.load(User.class, id);
	    return result;
	}
	
	public User saveUser(User user)
	{
		User result = (User)super.merge(user);
		return result;
	}
}
