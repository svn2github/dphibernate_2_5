package org.dphibernate.serialization;

import static org.dphibernate.test.ASObjectMatcher.assertASObject;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.dphibernate.model.Author;
import org.junit.Test;

import flex.messaging.io.amf.ASObject;

public class HibernateSerializerTests {

    @Test
    public void testIsPropertyOnSource() {
        Author author = TestDataProvider.getAuthor();
        HibernateSerializer serializer = new HibernateSerializer(author);
        assertFalse(serializer.sourceContainsProperty(author.getAge()));
        assertTrue(serializer.sourceContainsProperty(author.getBooks()));
        assertTrue(serializer.sourceContainsProperty(author.getName()));
        assertTrue(serializer.sourceContainsProperty(author.getPublisher()));

        assertFalse(serializer.sourceContainsProperty(author.getBook(0).getTitle()));
        assertFalse(serializer.sourceContainsProperty(TestDataProvider.getAuthor()));
    }

    @Test
    public void testSerializingSimpleObject() {
        Author author = TestDataProvider.getAuthor();
        HibernateSerializer serializer = new HibernateSerializer(author);
        Object value = serializer.serialize();
        assertTrue(value instanceof ASObject);
        ASObject asObject = (ASObject) value;
        assertASObject(asObject).matches(author);
    }

    @Test
    public void observesEagerlySerialize() {
        // TODO
        //     if (isLazyProxy(objectToSerialize) && !eagerlySerialize)
    }

    @Test
    public void observesAggressivelyProxy() {
        // } else if (shouldAggressivelyProxy(objectToSerialize, eagerlySerialize)) {
    }

    @Test
    public void serializesPersistentMap() {
        // else if (objectToSerialize instanceof PersistentMap)
    }

    @Test
    public void serializesArray() {

    }

    @Test
    public void serializesCollection() {
        //else if (objectToSerialize instanceof Collection)
    }

    @Test
    public void serializesMap() {
//        else if (objectToSerialize instanceof Map)
    }

    @Test
    public void serializesIHibernateProxy() {
        // else if (objectToSerialize instanceof IHibernateProxy)
    }
    @Test
    public void serializesSimpleTypes() {
        // } else if (objectToSerialize instanceof Object && (!TypeHelper.isSimple(objectToSerialize)) && !(objectToSerialize instanceof ASObject))
    }
}