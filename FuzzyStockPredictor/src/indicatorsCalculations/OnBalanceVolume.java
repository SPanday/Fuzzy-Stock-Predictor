package indicatorsCalculations;

import java.util.ArrayList;

public class OnBalanceVolume {

	/**
	 * calculates on balance volumes for all the data entries
	 * 
	 * @param allData
	 */
	public void calculateOnBalanceVolumes(ArrayList<StockEntry> allData) {
		double currentVolume = 0;
		double currentClosePrice = 0;
		double previousOnBalanceVolume = 0;
		double previousClosePrice = 0;
		for (int i = 0; i < allData.size(); i++) {
			StockEntry currentEntry = allData.get(i);

			currentVolume = currentEntry.getVolume();

			if (i == 0) {
				currentEntry.setOnBalanceVolume(currentVolume);
			} else {
				currentClosePrice = currentEntry.getClosePrice();
				previousOnBalanceVolume = allData.get(i - 1)
						.getOnBalanceVolume();
				previousClosePrice = allData.get(i - 1).getClosePrice();

				if (currentClosePrice < previousClosePrice)
					currentVolume *= -1;

				currentEntry.setOnBalanceVolume(currentVolume
						+ previousOnBalanceVolume);
			}
		}
	}

	/**
	 * returns the last five values of the volumes
	 * 
	 * @param allData
	 * @return
	 */
	public double[] getOBVolumesLevel(ArrayList<StockEntry> allData) {
		double[] oBVolumes = new double[7];
		int i = 0;
		for (StockEntry eachDataEntry : allData) {
			if (eachDataEntry.getsNo() >= allData.size() - 6) {
				oBVolumes[i] = eachDataEntry.getOnBalanceVolume();
				i++;
			}
		}
		return oBVolumes;
	}

	/**
	 * To check if the values are overall increasing or decreasing
	 * 
	 * @param arr
	 * @return
	 */
	public String isIncOrDec(double[] arr) {
		int i = 0, incCount = 0, decCount = 0;
		while (i != arr.length - 1) {
			if (arr[i] < arr[i + 1])
				incCount++;
			else if (arr[i] > arr[i + 1])
				decCount++;

			i++;
		}
		if (incCount > decCount)
			return ("High");
		else if (incCount < decCount)
			return ("Low");
		else {
			if (arr[arr.length - 1] < arr[arr.length - 2])
				return ("Low");
			else
				return ("High");
		}
	}
}
