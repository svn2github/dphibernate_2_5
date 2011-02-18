package org.dphibernate.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = PostType.BUG)
public class BugReport extends Post {

}
