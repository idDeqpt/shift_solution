import util.*;

import java.util.ArrayList;
import java.util.Arrays;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileWriter;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


class Main {
	public static void main(String args[]) {
		OptionManager optionManager = new OptionManager();
		try {
			optionManager.initParams(args);
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			return;
		}

		ArrayList<String> filePaths = optionManager.getSourceFiles();
		if (filePaths.size() == 0) {
			System.out.println("Not entered source files");
			return;
		}

		String[][] targetTypes = {
			{"integer", "^[+-]?\\d+$"},
			{"float",   "^[+-]?\\d+\\.\\d+([eE][+-]?\\d+)?$"},
			{"string",  "^(?!^[+-]?\\d+$)(?!^[+-]?\\d+\\.\\d+([eE][+-]?\\d+)?$)(?!\\n).*$"}
		};
		ArrayList<ValuesGroup> valuesGroups = new ArrayList<ValuesGroup>(0);
		for (String[] target : targetTypes)
			valuesGroups.add(new ValuesGroup(target[1]));

		Parser parser = new Parser();
		for (String[] target : targetTypes)
			parser.addGroupFilter(target[1]);

		for (String path : filePaths) {
			try {
				parser.parse(path);
				ArrayList<ValuesGroup> fileGroups = parser.getResultGroups();
				for (int i = 0; i < valuesGroups.size(); i++)
					valuesGroups.get(i).addAll(fileGroups.get(i).getValues());
			}
			catch (FileNotFoundException e) {
				System.out.println("Error: file " + path + " not found");
			}
			catch (IOException e) {
				System.out.println("Error: error of read file " + path);
			}
		}

		for (int i = 0; i < valuesGroups.size(); i++) {
			ArrayList<CombinedValue> valuesArray = valuesGroups.get(i).getValues();
			if (valuesArray.size() > 0) {
				String currentFilename = optionManager.getOutFilePath() + optionManager.getOutFilePrefix() + targetTypes[i][0] + "s.txt";
				try {
					Path outFile = Paths.get(currentFilename);
					if (!Files.exists(outFile)) {
						if (!Files.exists(Paths.get(optionManager.getOutFilePath())))
							Files.createDirectory(Paths.get(optionManager.getOutFilePath()));
						outFile = Files.createFile(Paths.get(currentFilename));
					}

					FileWriter writer = new FileWriter(outFile.toString(), optionManager.isAppend());
					for (CombinedValue value : valuesArray)
						writer.write(value.getString() + "\n");
					writer.flush(); writer.close();
				}
				catch (IOException e) {
					System.out.println("Error: error of write data to file " + currentFilename);
				}

				if (optionManager.getStatisticsType() != null) {
					ValuesStatistics statistics = new ValuesStatistics(valuesArray, optionManager.getStatisticsType());
					System.out.println("Statistics of " + targetTypes[i][0] + " values:");
					System.out.println("\t|-Count of values: " + statistics.getCount());
					if (optionManager.getStatisticsType() == ValuesStatistics.Type.FULL) {
						System.out.println("\t|-Min value: " + statistics.getMin());
						System.out.println("\t|-Max value: " + statistics.getMax());
						System.out.println("\t|-Average value: " + statistics.getAverage());
						System.out.println("\t|-Sum of values: " + statistics.getSum());
					}
				}
			}
		}
	}
}