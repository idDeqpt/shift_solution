package util;

import util.CombinedValue;
import java.util.ArrayList;


public class ValuesStatistics {
	public enum Type {
		SHORT,
		FULL
	}

	protected int m_count;
	protected float m_min;
	protected float m_max;
	protected float m_sum;
	protected float m_average;

	public ValuesStatistics(ArrayList<CombinedValue> values, Type stat_type) {
		m_min = m_max = m_sum = m_average = 0;
		m_count = values.size();

		if (stat_type == Type.FULL) {
			m_min = Float.MAX_VALUE;
			m_max = Float.MIN_VALUE;
			for (CombinedValue value : values) {
				float num = value.getNumber();
				if (num < m_min)
					m_min = num;
				if (num > m_max)
					m_max = num;
				m_sum += num;

			}
			m_average = m_sum/m_count;
		}
	}

	public int getCount() {
		return m_count;
	}

	public float getMin() {
		return m_min;
	}

	public float getMax() {
		return m_max;
	}

	public float getSum() {
		return m_sum;
	}

	public float getAverage() {
		return m_average;
	}
}