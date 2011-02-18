package net.digitalprimates.samples.sample1.beans;

import java.util.Collection;
import java.util.UUID;

import net.digitalprimates.persistence.hibernate.proxy.HibernateProxy;

@SuppressWarnings("unchecked")
public class User extends HibernateProxy
{
    public String id = UUID.randomUUID().toString();
    public String firstName;
    public String lastName;
    public UserConnectInfo connectInfo;
    public Collection addresses;
    public Collection addressList;
    /*
    public Collection addresses2;
    public Collection addressSet;
    public Collection addressBag;
    public Collection addressList;
    */
    
    
    public String getId()
    {
        return id;
    }
    

    public void setId(String id)
    {
        this.id = id;
    }
    

    public String getFirstName()
    {
        return firstName;
    }
    

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }
    

    public String getLastName()
    {
        return lastName;
    }
    

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }
    

    public UserConnectInfo getConnectInfo()
    {
        return connectInfo;
    }
    

    public void setConnectInfo(UserConnectInfo connectInfo)
    {
        this.connectInfo = connectInfo;
    }
    

    public Collection getAddresses()
    {
        return addresses;
    }
    

    public void setAddresses(Collection addresses)
    {
        this.addresses = addresses;
    }


	public Collection getAddressList()
	{
		return addressList;
	}


	public void setAddressList(Collection addressList)
	{
		this.addressList = addressList;
	}
    
    
}
