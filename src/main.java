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
		Object[][] paramsShifts = {
			{"-s", 1},
			{"-f", 1},
			{"-a", 1},
			{"-o", 2},
			{"-p", 2}
		};

		int firstFileIndex = 0;
		ValuesStatistics.Type statisticsType = null;
		String outFilePath = "";
		String outFilePrefix = "";
		boolean appendData = false;

		for (int i = 0; i < args.length; i++) {
			for (int j = 0; j < paramsShifts.length; j++)
				if (args[i].equals(paramsShifts[j][0])) firstFileIndex += (int)paramsShifts[j][1];

			if (args[i].equals("-s"))
				statisticsType = ValuesStatistics.Type.SHORT;
			else if (args[i].equals("-f"))
				statisticsType = ValuesStatistics.Type.FULL;
			else if (args[i].equals("-a"))
				appendData = true;
			else if (args[i].equals("-o"))
				outFilePath = args[i + 1] + "/";
			else if (args[i].equals("-p"))
				outFilePrefix = args[i + 1];
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

		String[] filePaths = Arrays.copyOfRange(args, firstFileIndex, args.length);
		if (filePaths.length == 0) {
			System.out.println("No files are selected");
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
				String currentFilename = outFilePath + outFilePrefix + targetTypes[i] + "s.txt";
				try {
					Path outFile = Paths.get(currentFilename);
					if (!Files.exists(outFile)) {
						if (!Files.exists(Paths.get(outFilePath)))
							Files.createDirectory(Paths.get(outFilePath));
						outFile = Files.createFile(Paths.get(currentFilename));
					}

					FileWriter writer = new FileWriter(outFile.toString(), appendData);
					for (CombinedValue value : valuesArray)
						writer.write(value.getString() + "\n");
					writer.flush(); writer.close();
				}
				catch (IOException e) {
					System.out.println("Error: error of write data to file " + currentFilename);
				}

				if (statisticsType != null) {
					ValuesStatistics statistics = new ValuesStatistics(valuesArray, statisticsType);
					System.out.println("Statistics of " + targetTypes[i] + " values:");
					System.out.println("\tCount of values: " + statistics.getCount());
					if (statisticsType == ValuesStatistics.Type.FULL) {
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