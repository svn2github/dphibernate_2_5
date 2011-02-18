package net.digitalprimates.persistence.hibernate.tests.manyToMany;

import net.digitalprimates.persistence.hibernate.proxy.HibernateProxy;

public class M2MUserConnectInfo extends HibernateProxy
{
	public String id;
	public String email;
	public String yahooIM;
	public String aolIM;
	public M2MPerson user;

	
	
	public M2MPerson getUser() {
		return user;
	}

	public void setUser(M2MPerson user) {
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
