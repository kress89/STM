package data;

public class RamInfo {
	private double maxRam;
	private double avaliableRam;
	private double usedRam;
	private double percentageUsed;

	public double getMaxRam() {
		return maxRam;
	}

	public void setMaxRam(double maxRam) {
		this.maxRam = maxRam;
	}

	public double getAvaliableRam() {
		return avaliableRam;
	}

	public void setAvaliableRam(double avaliableRam) {
		this.avaliableRam = avaliableRam;
	}

	public double getUsedRam(double v) {
		return usedRam;
	}

	public void setUsedRam(double usedRam) {
		this.usedRam = usedRam;
	}

	public double getPercentageUsed() {
		return percentageUsed;
	}

	public void setPercentageUsed(double percentageUsed) {
		this.percentageUsed = percentageUsed;
	}
}