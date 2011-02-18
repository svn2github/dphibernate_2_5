package net.digitalprimates.persistence.hibernate.tests.manyToMany;

import java.util.Set;
import java.util.UUID;


@SuppressWarnings("unchecked")
public class M2MPerson
{
    public String id = UUID.randomUUID().toString();
    public String firstName;
    public String lastName;
    public M2MUserConnectInfo connectInfo;
    public Set addresses;

    
    
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
    

    public M2MUserConnectInfo getConnectInfo()
    {
        return connectInfo;
    }
    

    public void setConnectInfo(M2MUserConnectInfo connectInfo)
    {
        this.connectInfo = connectInfo;
    }
    

    public Set getAddresses()
    {
        return addresses;
    }
    

    public void setAddresses(Set addresses)
    {
        this.addresses = addresses;
    }
    
}
