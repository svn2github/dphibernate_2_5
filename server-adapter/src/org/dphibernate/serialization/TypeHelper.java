package org.dphibernate.serialization;

import java.util.Calendar;
import java.util.Date;

import org.w3c.dom.Document;

public class TypeHelper {
	public static boolean isSimple(Object obj) {
		return ((obj == null) 
				|| (obj instanceof String)
				|| (obj instanceof Character) 
				|| (obj instanceof Boolean)
				|| (obj instanceof Number) 
				|| (obj instanceof Date)
				|| (obj instanceof Calendar) 
				|| (obj instanceof Document))
				|| (obj instanceof Enum);
	}
}
