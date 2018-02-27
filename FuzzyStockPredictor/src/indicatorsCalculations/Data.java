package indicatorsCalculations;

public class Data {
	private double macd;
	private double macd_trigger;
	private String obv;
	private double rsi;
	private double so;
	private String result;

	public double getMacd() {
		return macd;
	}

	public void setMacd(double macd) {
		this.macd = macd;
	}

	public double getMacd_trigger() {
		return macd_trigger;
	}

	public void setMacd_trigger(double macd_trigger) {
		this.macd_trigger = macd_trigger;
	}

	public String getObv() {
		return obv;
	}

	public void setObv(String obv) {
		this.obv = obv;
	}

	public double getRsi() {
		return rsi;
	}

	public void setRsi(double rsi) {
		this.rsi = rsi;
	}

	public double getSo() {
		return so;
	}

	public void setSo(double so) {
		this.so = so;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

}
