package indicatorsCalculations;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class StockEntry {

	public static Comparator<StockEntry> sNoComparator = new Comparator<StockEntry>() {
		public int compare(StockEntry jc1, StockEntry jc2) {
			return (jc2.getsNo() > jc1.getsNo() ? -1 : (jc2.getsNo() == jc1
					.getsNo() ? 0 : 1));
		}
	};

	public static Comparator<StockEntry> highComparator = new Comparator<StockEntry>() {
		public int compare(StockEntry jc1, StockEntry jc2) {
			return (jc2.getHighPrice() > jc1.getHighPrice() ? -1 : (jc2
					.getHighPrice() == jc1.getHighPrice() ? 0 : 1));
		}
	};

	public static Comparator<StockEntry> lowComparator = new Comparator<StockEntry>() {
		public int compare(StockEntry jc1, StockEntry jc2) {
			return (jc2.getLowPrice() > jc1.getLowPrice() ? -1 : (jc2
					.getLowPrice() == jc1.getLowPrice() ? 0 : 1));
		}
	};

	private double closePrice, volume, onBalanceVolume, highPrice, lowPrice,
			ema12, ema26, macd, signal, avgProfit, avgLoss, rs, kPercent;

	public double getAvgProfit() {
		return avgProfit;
	}

	public void setAvgProfit(double avgProfit) {
		this.avgProfit = avgProfit;
	}

	public double getAvgLoss() {
		return avgLoss;
	}

	public void setAvgLoss(double avgLoss) {
		this.avgLoss = avgLoss;
	}

	public double getRs() {
		return rs;
	}

	public void setRs(double rs) {
		this.rs = rs;
	}

	public double getkPercent() {
		return kPercent;
	}

	public void setkPercent(double kPercent) {
		this.kPercent = kPercent;
	}

	private int sNo;

	private Date tradeDate;

	public StockEntry() {
		super();
	}

	public StockEntry(int sno, String inDate, double inClose, double inVolume,
			double inHighPrice, double inLowPrice) throws ParseException {
		tradeDate = toDate(inDate);
		closePrice = inClose;
		volume = inVolume;
		sNo = sno;
		onBalanceVolume = 0;
		setHighPrice(inHighPrice);
		setLowPrice(inLowPrice);
		setEma12(0);
		setEma26(0);
		setMacd(0);
		setSignal(0);
		setRs(0);
		setAvgLoss(0);
		setAvgProfit(0);
		setkPercent(0);
	}

	public double getClosePrice() {
		return closePrice;
	}

	public double getHighPrice() {
		return highPrice;
	}

	public double getLowPrice() {
		return lowPrice;
	}

	public double getOnBalanceVolume() {
		return onBalanceVolume;
	}

	public int getsNo() {
		return sNo;
	}

	public Date getTradeDate() {
		return tradeDate;
	}

	public double getVolume() {
		return volume;
	}

	public void setClosePrice(double closePrice) {
		this.closePrice = closePrice;
	}

	public void setHighPrice(double highPrice) {
		this.highPrice = highPrice;
	}

	public void setLowPrice(double lowPrice) {
		this.lowPrice = lowPrice;
	}

	public void setOnBalanceVolume(double onBalanceVolume) {
		this.onBalanceVolume = onBalanceVolume;
	}

	public void setsNo(int sNo) {
		this.sNo = sNo;
	}

	public void setTradeDate(Date tradeDate) {
		this.tradeDate = tradeDate;
	}

	public void setVolume(double volume) {
		this.volume = volume;
	}

	private Date toDate(String inDate) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		Date utilDate = null;
		try {
			utilDate = format.parse(inDate);
		} catch (Exception e) {
			System.out
					.println("There is some error in the Date Format. Please verify the file.");
		}

		return utilDate;
	}

	@Override
	public String toString() {
		return ("\nS. No.:" + this.getsNo() + " Date: " + this.getTradeDate()
				+ " Close Price: " + this.getClosePrice() + " Volume : "
				+ this.getVolume() + " OBV : " + this.getOnBalanceVolume()
				+ " EMA 12 : " + this.getEma12() + " EMA 26 : "
				+ this.getEma26() + " MACD : " + this.getMacd()
				+ " MACD Signal : " + this.getSignal() + " Average Profit : "
				+ this.getAvgProfit() + " Average Loss : " + this.getAvgLoss()
				+ " k% : " + this.getkPercent());
	}

	public double getEma12() {
		return ema12;
	}

	public void setEma12(double ema12) {
		this.ema12 = ema12;
	}

	public double getEma26() {
		return ema26;
	}

	public void setEma26(double ema26) {
		this.ema26 = ema26;
	}

	public double getMacd() {
		return macd;
	}

	public void setMacd(double macd) {
		this.macd = macd;
	}

	public double getSignal() {
		return signal;
	}

	public void setSignal(double signal) {
		this.signal = signal;
	}
}
