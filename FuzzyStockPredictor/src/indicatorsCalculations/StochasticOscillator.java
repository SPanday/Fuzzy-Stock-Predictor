/**
 * 
 */
package indicatorsCalculations;

import java.util.ArrayList;
import java.util.Collections;

/**
 * @author Sakshi
 * 
 */
public class StochasticOscillator {

	public static double calculateSO(ArrayList<StockEntry> allData) {
		double SO = 0.0, currentClosePrice = 0.0, lowestLow = 0.0, highestHigh = 0.0, kPercent = 0.0;
		int kIndex = allData.size() - 3;

		while (kIndex < allData.size()) {
			ArrayList<StockEntry> kEntries = new ArrayList<StockEntry>();
			for (StockEntry eachDataEntry : allData) {
				if ((eachDataEntry.getsNo() <= kIndex + 1)
						&& (eachDataEntry.getsNo() > kIndex - 13)) {
					kEntries.add(eachDataEntry);
				}
			}
			highestHigh = Collections.max(kEntries, StockEntry.highComparator)
					.getHighPrice();
			lowestLow = Collections.min(kEntries, StockEntry.lowComparator)
					.getLowPrice();
			currentClosePrice = allData.get(kIndex).getClosePrice();
			kPercent = 100 * ((currentClosePrice - lowestLow) / (highestHigh - lowestLow));
			allData.get(kIndex).setkPercent(kPercent);

			kIndex++;
		}
		for (int i = 1; i <= 3; i++)
			SO += allData.get(allData.size() - i).getkPercent();
		SO = SO / 3;
		return SO;
	}
}
