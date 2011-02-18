package org.dphibernate.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(PostType.IDEA)
public class IdeaPost extends Post {

}
