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
public class ClearTestData
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

    }
    
}
