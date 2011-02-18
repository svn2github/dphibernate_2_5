package org.dphibernate.serialization;

public class PropertyValuePair
{
	private final Object value;
	private final String propertyName;

	public PropertyValuePair(String propertyName, Object value)
	{
		this.propertyName = propertyName;
		this.value = value;
		
	}

	public Object getValue()
	{
		return value;
	}

	public String getPropertyName()
	{
		return propertyName;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((propertyName == null) ? 0 : propertyName.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PropertyValuePair other = (PropertyValuePair) obj;
		if (propertyName == null)
		{
			if (other.propertyName != null)
				return false;
		} else if (!propertyName.equals(other.propertyName))
			return false;
		if (value == null)
		{
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
}
