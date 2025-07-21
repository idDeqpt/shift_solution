package util;


public class CombinedValue {
	protected float m_number;
	protected String m_string;

	public CombinedValue(String value) {
		try {
			m_number = (float)Long.parseLong(value);
		} catch(NumberFormatException intErr) {
			try {
				m_number = Float.parseFloat(value);
			} catch(NumberFormatException floatErr) {
				m_number = (float)value.length();
			}
		}
		m_string = value;
	}

	public float getNumber() {
		return m_number;
	}

	public String getString() {
		return m_string;
	}
}