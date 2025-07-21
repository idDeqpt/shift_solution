package util;

import util.CombinedValue;
import util.ValuesGroup;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;


public class Parser {
	protected ArrayList<ValuesGroup> m_valuesGroups;

	public Parser() {
		m_valuesGroups = new ArrayList<ValuesGroup>(0);
	}

	public void addGroupFilter(String regex) {
		m_valuesGroups.add(new ValuesGroup(regex));
	}

	public void clearGroups() {
		for (ValuesGroup group : m_valuesGroups)
			group.clear();
	}

	public void parse(String filePath) throws FileNotFoundException, IOException {
		clearGroups();

		try {
			BufferedReader reader = new BufferedReader(new FileReader(filePath));
			String line = reader.readLine();
			while (line != null) {
				for (ValuesGroup group : m_valuesGroups)
					if (line.matches(group.getRegex()))
						group.add(new CombinedValue(line));
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
	}

	public ArrayList<ValuesGroup> getResultGroups() {
		return m_valuesGroups;
	}
}