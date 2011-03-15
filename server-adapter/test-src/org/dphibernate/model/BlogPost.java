package org.dphibernate.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = PostType.BLOG)
public class BlogPost extends Post {

}
