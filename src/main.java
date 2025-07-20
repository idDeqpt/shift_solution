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

		String[] targetTypes = {
			"integer",
			"float",
			"string"
		};
		ArrayList<ArrayList<CombinedValue>> valuesArrays = new ArrayList<ArrayList<CombinedValue>>(0);
		valuesArrays.add(new ArrayList<CombinedValue>(0)); //for ints
		valuesArrays.add(new ArrayList<CombinedValue>(0)); //for floats
		valuesArrays.add(new ArrayList<CombinedValue>(0)); //for strings

		ArrayList<String> filePaths = optionManager.getSourceFiles();
		if (filePaths.size() == 0) {
			System.out.println("Not entered source files");
			return;
		}

		for (String path : filePaths) {
			try {
				Parser parser = new Parser(path);
				parser.parse();

				valuesArrays.get(0).addAll(parser.getInts());
				valuesArrays.get(1).addAll(parser.getFloats());
				valuesArrays.get(2).addAll(parser.getStrings());
			}
			catch (FileNotFoundException e) {
				System.out.println("Error: file " + path + " not found");
			}
			catch (IOException e) {
				System.out.println("Error: error of read file " + path);
			}
		}

		if (valuesArrays.size() == 0)
			System.out.println("Not found data for processing");

		for (int i = 0; i < valuesArrays.size(); i++) {
			ArrayList<CombinedValue> valuesArray = valuesArrays.get(i);
			if (valuesArray.size() > 0) {
				String currentFilename = optionManager.getOutFilePath() + optionManager.getOutFilePrefix() + targetTypes[i] + "s.txt";
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
					System.out.println("Statistics of " + targetTypes[i] + " values:");
					System.out.println("\tCount of values: " + statistics.getCount());
					if (optionManager.getStatisticsType() == ValuesStatistics.Type.FULL) {
						System.out.println("\tMin value: " + statistics.getMin());
						System.out.println("\tMax value: " + statistics.getMax());
						System.out.println("\tAverage value: " + statistics.getAverage());
						System.out.println("\tSum of values: " + statistics.getSum());
					}
				}
			}
		}
	}
}