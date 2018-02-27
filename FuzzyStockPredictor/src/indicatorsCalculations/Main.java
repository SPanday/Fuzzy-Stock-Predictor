package indicatorsCalculations;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import nrc.fuzzy.jess.*;
import jess.*;

public class Main {
	public static void main(String[] args) throws Exception {

		OnBalanceVolume obv = new OnBalanceVolume();
		MovingAvgConvDiv macd = new MovingAvgConvDiv();

		String fileName = getInput();
		ArrayList<StockEntry> allData = readStockData(fileName);

		if (!allData.isEmpty()) {
			if (allData.size() >= 28) {
				obv.calculateOnBalanceVolumes(allData);
				System.out.println("Processing historical data...");
				macd.calculateMACD(allData);
				macd.calculateSignal(allData);
				System.out.println("\n\nfollowing are the values:");
				Rete rete = new Rete();
				Value val = rete.batch("FuzzyStockPredictor.clp");
				Data sampleData = new Data();
				sampleData.setMacd(allData.get(allData.size() - 1).getMacd());
				sampleData.setMacd_trigger(allData.get(allData.size() - 1)
						.getSignal());
				sampleData.setRsi(RelativeStrengthIndex.calculateRSI(allData));
				sampleData.setSo(StochasticOscillator.calculateSO(allData));

				sampleData
						.setObv(obv.isIncOrDec(obv.getOBVolumesLevel(allData)));
				rete.add(sampleData);
				rete.run();
				System.out.println("OBV is " + sampleData.getObv());
				String result = sampleData.getResult();
				System.out
						.println("\n\n______________________________________________________________________________________________________");
				System.out
						.println("\n\n		                   Prediction & Recommendation");

				if (result == null)
					System.out
							.println("\n\nThe four indicators considered are conflicting.\nThus, insufficient to effectively predict this stock's market movement.\nPlease contact your financial analyst.");
				else if (result.contains("Hold")) {
					System.out
							.println("\n\nSystem recommends to 'hold' i.e. to neither buy nor sell the stock."
									+ "\nThis company is expected to perform with the market or at the same pace as comparable companies."
									+ "\nThis rating is better than sell but worse than buy,\nmeaning that "
									+ "investors with existing long positions"
									+ "(already own the stocks) shouldn't sell,\nbut investors without a "
									+ "position(do not own the stocks) shouldn't purchase either.\n\n");
				}

				else if (result.contains("Buy")) {
					System.out
							.println("\n\nSystem recommends to purchase the aforementioned stock,"
									+ " as the security is currently undervalued and should rise in price."
									+"\n[NOTE : If you already own the stock, hold them for a short duration as prices are about to go up!]");
				}

				else if (result.contains("Sell")) {
					System.out
							.println("\n\nSystem recommends to sell the stock "
									+ "because of a possible risk of a price decline."
									+ "\n\n[NOTE : Since the act of selling an investment crystallizes a profit or a loss,\n"
									+ "depending on the initial purchase price, it MAY have *tax implications* for the investor.]");
				}

				System.out
						.println("\n______________________________________________________________________________________________________");

			} else {
				System.out
						.println("\nFile has insufficient historical market data.\nPlease check the file '"
								+ fileName + "'");
			}

		} else {
			System.out.println("\nFile has no data.\nPlease check the file '"
					+ fileName + "'");
		}
	}

	/**
	 * Reads the file for stock data
	 * 
	 * @param fileName
	 * @return
	 * @throws NumberFormatException
	 * @throws ParseException
	 */
	public static ArrayList<StockEntry> readStockData(String fileName)
			throws NumberFormatException, ParseException {
		// Read Stock Data
		System.out.println("Fetching Historical Stock Data...");
		String csvFile = new File("").getAbsolutePath() + "/Stock Data/" + fileName + ".csv";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		ArrayList<StockEntry> result = new ArrayList<StockEntry>();
		try {
			br = new BufferedReader(new FileReader(csvFile));
			int iterator = 0;
			while ((line = br.readLine()) != null) {
				if (iterator == 0 || iterator == 1)
					iterator++;
				else {
					String[] stockData = line.split(cvsSplitBy);
					result.add(new StockEntry(0,
							stockData[0].replace("\"", ""),
							Double.parseDouble(stockData[1].replace("\"", "")),
							Double.parseDouble(stockData[2].replace("\"", "")),
							Double.parseDouble(stockData[4].replace("\"", "")),
							Double.parseDouble(stockData[5].replace("\"", ""))));
				}
			}
			int totalDataRows = result.size();
			for (StockEntry eachDataEntry : result) {
				eachDataEntry.setsNo(totalDataRows--);
			}

		} catch (FileNotFoundException e) {
			System.out
					.println("\nFile was not found where expected.\nPlease check the folder path ("
							+ csvFile + ")");
		} catch (Exception e) {
			System.out
					.println("Some issue with the file content.\nPlease check the folder path ("
							+ csvFile + ")");
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					System.out
							.println("Unexpected error has occured. Please verify the file to be read.");
					e.printStackTrace();
				}
			}
		}
		return getSortedDataBySNo(result);
	}

	public static ArrayList<StockEntry> getSortedDataBySNo(
			ArrayList<StockEntry> result) {
		Collections.sort(result, StockEntry.sNoComparator);
		return result;
	}

	/**
	 * takes the input of user
	 * 
	 * @return filename
	 */
	public static String getInput() {
		String[] filenames = { "AAPL", "GOOGL", "FB" };
		String[] Companynames = { "Apple Inc. (AAPL)", "Alphabet Inc. (GOOGL)", "Facebook Inc. (FB)" };
		System.out
				.println("\n\n___________________________________________________________");
		System.out.println("\n\n		Fuzzy Stock Predictor");
		System.out
				.println("\n___________________________________________________________");
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		while (true) {
			System.out
					.println("\n\nEnter the option number of the Company you want to trade in:");
			System.out.println("	[1]		Apple Inc. [Stock Symbol : AAPL]");
			System.out.println("	[2]		Alphabet Inc. [Stock Symbol : GOOGL]");
			System.out.println("	[3]		Facebook Inc. [Stock Symbol : FB]");

			String fileChoice = sc.next();
			try {
				int i = Integer.parseInt(fileChoice) - 1;
				System.out
						.println("\n\n	You chose " + Companynames[i] + "\n\n");
				return filenames[i];
			} catch (Exception ne) {
				System.out
						.println("\nThere was some error in input. Please enter available option number only.");
				System.out.println("Please, try again.");
			}
		}

	}

}