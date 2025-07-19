package util;

import util.CombinedValue;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;


public class Parser {
	protected String m_filePath;
	protected boolean m_parsed;
	protected ArrayList<CombinedValue> m_intValues;
	protected ArrayList<CombinedValue> m_floatValues;
	protected ArrayList<CombinedValue> m_stringValues;

	public Parser(String filePath) {
		m_filePath = filePath;
		m_parsed = false;
		m_intValues = new ArrayList<CombinedValue>(0);
		m_floatValues = new ArrayList<CombinedValue>(0);
		m_stringValues = new ArrayList<CombinedValue>(0);
	}

	public void parse() throws FileNotFoundException, IOException {
		if (m_parsed) return;

		try {
			BufferedReader reader = new BufferedReader(new FileReader(m_filePath));
			String line = reader.readLine();
			while (line != null) {
				try {
					m_intValues.add(new CombinedValue(Long.parseLong(line)));
				}
				catch(NumberFormatException intErr) {
					try {
						m_floatValues.add(new CombinedValue(Float.parseFloat(line)));
					}
					catch(NumberFormatException floatErr) {
						m_stringValues.add(new CombinedValue(line));
					}
				}
				line = reader.readLine();
			}
			reader.close();
		}
		catch (FileNotFoundException e) {
			throw e;
		}
		catch (IOException e) {
			throw e;
		}

		m_parsed = true;
	}

	public ArrayList<CombinedValue> getInts() {
		return m_intValues;
	}

	public ArrayList<CombinedValue> getFloats() {
		return m_floatValues;
	}

	public ArrayList<CombinedValue> getStrings() {
		return m_stringValues;
	}
}