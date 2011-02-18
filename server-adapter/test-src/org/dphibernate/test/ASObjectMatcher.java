package org.dphibernate.test;

import flex.messaging.io.amf.ASObject;

/**
 * Created by IntelliJ IDEA.
 * User: marty
 * Date: 05-Jul-2010
 */
public class ASObjectMatcher {
    private final ASObject asObject;

    public static ASObjectMatcher assertASObject(ASObject asObject)
    {
        return new ASObjectMatcher(asObject);
    }
    public ASObjectMatcher(ASObject asObject)
    {
        this.asObject = asObject;
    }

    public void matches(Object o) {
        //To change body of created methods use File | Settings | File Templates.
    }
}
