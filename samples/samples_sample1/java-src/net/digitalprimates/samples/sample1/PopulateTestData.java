package net.digitalprimates.samples.sample1;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import net.digitalprimates.persistence.hibernate.utils.services.HibernateService;
import net.digitalprimates.samples.sample1.beans.User;
import net.digitalprimates.samples.sample1.beans.UserAddress;
import net.digitalprimates.samples.sample1.beans.UserConnectInfo;

@SuppressWarnings("unchecked")
public class PopulateTestData
{
    
    /**
     * @param args
     */
    public static void main(String[] args)
    {
        List users = new HibernateService().list(User.class);
        Iterator itr = users.iterator();
        while( itr.hasNext() )
        {
            new HibernateService().delete(itr.next(), true);
        }

        
        for( int j=0; j < 3; j++ )
        {
            User u1 = new User();
            u1.id = UUID.randomUUID().toString();
            u1.firstName = "first " +j;
            u1.lastName = "last " +j;
            
            u1.connectInfo = new UserConnectInfo();
            u1.connectInfo.email = "user" +j +"@foo.com";
            u1.connectInfo.user = u1;
            
            u1.addresses = new ArrayList();
            
            for(int i=0; i < 20; i++ )
            {
                UserAddress adr = new UserAddress();
                adr.user = u1;
                adr.address1 = "123 main st";
                adr.city = "Boston";
                adr.state = "MA";
                u1.addresses.add( adr );
            }
            
            new HibernateService().save(u1);
        }
    }
    
}
