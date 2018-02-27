/**
 * 
 */
package indicatorsCalculations;

import java.util.ArrayList;

/**
 * @author Sakshi
 * 
 */
public class MovingAvgConvDiv {

	/**
	 * Calculate Exponential Moving Average for each data entry
	 * 
	 */
	public void calculateEMA(ArrayList<StockEntry> allData, double timePeriod) {
		double currentEMA = 0;
		double yesterdayEMA = calculateFirstDay(allData, timePeriod);
		double k = 2 / (timePeriod + 1);

		for (int i = 0; i < allData.size(); i++) {

			StockEntry currentEntry = allData.get(i);

			if (currentEntry.getsNo() == timePeriod) {
				currentEMA = (currentEntry.getClosePrice() * k)
						+ (yesterdayEMA * (1 - k));

			} else if (currentEntry.getsNo() > timePeriod) {

				StockEntry previousEntry = allData.get(i - 1);
				// EMA12
				if (timePeriod == 12) {

					yesterdayEMA = previousEntry.getEma12();
					currentEMA = (currentEntry.getClosePrice() * k)
							+ (yesterdayEMA * (1 - k));

					currentEntry.setEma12(currentEMA);
				}

				// EMA26
				if (timePeriod == 26) {
					yesterdayEMA = previousEntry.getEma26();
					currentEMA = (currentEntry.getClosePrice() * k)
							+ (yesterdayEMA * (1 - k));

					currentEntry.setEma26(currentEMA);
				}
			}
		}
	}

	/**
	 * calculates the first day value for EMA
	 * 
	 * @param allData
	 * @return trigger
	 */
	public double calculateFirstDay(ArrayList<StockEntry> allData,
			double timePeriod) {
		double firstDayAvg = 0;

		for (StockEntry eachDataEntry : allData) {
			if (eachDataEntry.getsNo() <= timePeriod)
				firstDayAvg += eachDataEntry.getClosePrice();
		}
		firstDayAvg = firstDayAvg / timePeriod;
		return firstDayAvg;
	}

	/**
	 * calculates the first day value for Signal
	 * 
	 * @param allData
	 * @return trigger
	 */
	public double calculateFirstSignal(ArrayList<StockEntry> allData,
			double timePeriod) {
		double firstSignal = 0;

		for (StockEntry eachDataEntry : allData) {
			if (26 <= eachDataEntry.getsNo()
					&& eachDataEntry.getsNo() <= 26 + timePeriod)
				firstSignal += eachDataEntry.getMacd();
		}
		firstSignal = firstSignal / timePeriod;
		return firstSignal;
	}

	/**
	 * /** calculates the first day value for Signal
	 * 
	 * @param allData
	 * @return trigger
	 */
	public void calculateMACD(ArrayList<StockEntry> allData) {
		calculateEMA(allData, 12);
		calculateEMA(allData, 26);
		for (StockEntry eachDataEntry : allData) {
			eachDataEntry.setMacd(eachDataEntry.getEma26()
					- eachDataEntry.getEma12());
		}
	}

	/**
	 * Calculates the Signal needed for MACD decision
	 * 
	 * @param allData
	 */
	public void calculateSignal(ArrayList<StockEntry> allData) {

		double currentSignal = 0;
		double timePeriod = 9.0;
		double yesterdaySignal = 0;
		double k = 2 / (timePeriod + 1);

		for (int i = (int) (26 + timePeriod + 1); i < allData.size(); i++) {
			StockEntry previousEntry = allData.get(i - 1);
			StockEntry currentEntry = allData.get(i);

			if (i == (int) (26 + timePeriod + 1))
				allData.get(i - 1).setSignal(
						calculateFirstSignal(allData, timePeriod));

			yesterdaySignal = previousEntry.getSignal();
			currentSignal = (currentEntry.getMacd() * k)
					+ (yesterdaySignal * (1 - k));
			currentEntry.setSignal(currentSignal);

		}

	}

}
