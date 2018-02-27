/**
 * 
 */
package indicatorsCalculations;

import java.util.ArrayList;

/**
 * @author Sakshi
 * 
 */
public class RelativeStrengthIndex {

	/**
	 * calculates RSI for stock data
	 * 
	 * @param allData
	 * @return RSI
	 */
	public static double calculateRSI(ArrayList<StockEntry> allData) {
		double RSI = 0.0;
		RSI = 100.0 - (100.0 / (1.0 + calculateRS(allData)));
		return RSI;
	}

	private static double calculateRS(ArrayList<StockEntry> allData) {
		// TODO Auto-generated method stub
		calculateFirstProfitLoss(allData);
		double profitNetPrice = 0.0, rs = 0.0, lossNetPrice = 0.0, netPrice = 0.0;
		for (int i = 14; i < allData.size(); i++) {
			netPrice = allData.get(i - 1).getClosePrice()
					- allData.get(i).getClosePrice();

			if (netPrice >= 0) {
				profitNetPrice = netPrice;
				lossNetPrice = 0;
			}
			if (netPrice < 0) {
				lossNetPrice = (netPrice * -1);
				profitNetPrice = 0;
			}

			allData.get(i)
					.setAvgProfit(
							((allData.get(i - 1).getAvgProfit() * 13) + profitNetPrice) / 14);

			allData.get(i)
					.setAvgLoss(
							((allData.get(i - 1).getAvgLoss() * 13) + lossNetPrice) / 14);

		}
		rs = allData.get(allData.size() - 1).getAvgProfit()
				/ allData.get(allData.size() - 1).getAvgLoss();

		return rs;
	}

	/**
	 * calculates RS value for stock data
	 * 
	 * @param allData
	 * @return RS
	 */
	private static void calculateFirstProfitLoss(ArrayList<StockEntry> allData) {
		double netPrice = 0.0, avgProfit = 0.0, avgLoss = 0.0;
		for (int i = 1; i < 14; i++) {
			netPrice = allData.get(i - 1).getClosePrice()
					- allData.get(i).getClosePrice();
			if (netPrice >= 0) {
				avgProfit += netPrice;
			}
			if (netPrice < 0) {
				avgLoss += (netPrice * -1);
			}
		}

		allData.get(13).setAvgProfit(avgProfit / 14.0);
		allData.get(13).setAvgLoss(avgLoss / 14.0);
	}

}
