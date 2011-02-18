package net.digitalprimates.persistence.hibernate.tests.oneToMany;

import java.util.Set;
import java.util.UUID;
import java.util.Collection;

@SuppressWarnings("unchecked")
public class O2MPerson
{
    public String id = UUID.randomUUID().toString();
    public String firstName;
    public String lastName;
    public Collection addresses;

    
    
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
    

    public Collection getAddresses()
    {
        return addresses;
    }
    

    public void setAddresses(Collection addresses)
    {
        this.addresses = addresses;
    }
    
}
