package util;

import util.ValuesStatistics;
import java.util.ArrayList;


public class OptionManager {
	protected ValuesStatistics.Type m_statisticsType;
	protected boolean m_append;
	protected String m_outFilePath;
	protected String m_outFilePrefix;
	protected ArrayList<String> m_sourceFiles;

	public OptionManager() {
		m_sourceFiles = new ArrayList<String>(0);
		clearParams();
	}

	public void initParams(String[] args) throws IndexOutOfBoundsException, IllegalArgumentException {
		clearParams();

		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-s")) {
				m_statisticsType = ValuesStatistics.Type.SHORT;
			} else if (args[i].equals("-f")) {
				m_statisticsType = ValuesStatistics.Type.FULL;
			} else if (args[i].equals("-a")) {
				m_append = true;
			} else if (args[i].equals("-o")) {
				if ((i + 1) >= args.length) throw new IndexOutOfBoundsException("Value of " + args[i] + " option is missing.\nFor example: -o <out-directory>");
				m_outFilePath = args[i + 1] + "/";
				i++;
			} else if (args[i].equals("-p")) {
				if ((i + 1) >= args.length) throw new IndexOutOfBoundsException("Value of " + args[i] + " option is missing.\nFor example: -p <prefix-value>");
				m_outFilePrefix = args[i + 1];
				i++;
			} else if (args[i].endsWith(".txt")) {
				m_sourceFiles.add(args[i]);
			} else {
				throw new IllegalArgumentException("Unknown option: " + args[i]);
			}
		}
	}

	public ValuesStatistics.Type getStatisticsType() {
		return m_statisticsType;
	}

	public boolean isAppend() {
		return m_append;
	}

	public String getOutFilePath() {
		return m_outFilePath;
	}

	public String getOutFilePrefix() {
		return m_outFilePrefix;
	}

	public ArrayList<String> getSourceFiles() {
		return m_sourceFiles;
	}

	protected void clearParams() {
		m_statisticsType = null;
		m_append = false;
		m_outFilePath = "";
		m_outFilePrefix = "";
		m_sourceFiles.clear();
	}
}