package org.dphibernate.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(PostType.COMMENT)
public class Comment extends Post {

}
