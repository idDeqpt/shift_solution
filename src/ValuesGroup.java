package util;

import util.CombinedValue;
import java.util.ArrayList;


public class ValuesGroup {
	protected String m_regex;
	protected ArrayList<CombinedValue> m_values;

	public ValuesGroup(String regex) {
		m_regex = regex;
		clear();
	}

	public void clear() {
		m_values = new ArrayList<CombinedValue>(0);
	}

	public void add(CombinedValue new_value) {
		m_values.add(new_value);
	}

	public void addAll(ArrayList<CombinedValue> values) {
		m_values.addAll(values);
	}

	public String getRegex() {
		return m_regex;
	}

	public ArrayList<CombinedValue> getValues() {
		return m_values;
	}
}