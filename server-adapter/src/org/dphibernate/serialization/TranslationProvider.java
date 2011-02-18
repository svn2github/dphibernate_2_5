package org.dphibernate.serialization;

import java.math.BigDecimal;

public class TranslationProvider implements ITranslationProvider {

	public boolean willTranslate(Object object) {
		if (object.getClass().isPrimitive()) return false;
		if (object.getClass().isEnum()) return false;
		if (object instanceof String) return false;
		if (object instanceof Boolean) return false;
		if (object instanceof Integer) return false;
		if (object instanceof BigDecimal) return false;
		if (object instanceof Double) return false;
		if (object instanceof Float) return false;
		return true;
	}

}
