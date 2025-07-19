package util;


public class CombinedValue {
	protected float m_number;
	protected String m_string;

	public CombinedValue(Long value) {
		m_number = (float)value;
		m_string = value.toString();
	}

	public CombinedValue(Float value) {
		m_number = value;
		m_string = value.toString();
	}

	public CombinedValue(String value) {
		m_number = (float)value.length();
		m_string = value;
	}

	public float getNumber() {
		return m_number;
	}

	public String getString() {
		return m_string;
	}
}