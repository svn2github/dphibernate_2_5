package net.digitalprimates.samples.sample1.beans;

import net.digitalprimates.persistence.hibernate.proxy.HibernateProxy;
import net.digitalprimates.samples.sample1.beans.User;

public class UserConnectInfo extends HibernateProxy
{
	public String id;
	public String email;
	public String yahooIM;
	public String aolIM;
	public User user;

	
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getYahooIM() {
		return yahooIM;
	}

	public void setYahooIM(String yahooIM) {
		this.yahooIM = yahooIM;
	}

	public String getAolIM() {
		return aolIM;
	}

	public void setAolIM(String aolIM) {
		this.aolIM = aolIM;
	}

}
