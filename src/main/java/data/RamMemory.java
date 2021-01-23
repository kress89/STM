package data;

import oshi.SystemInfo;
import oshi.hardware.GlobalMemory;

public class RamMemory {
	private SystemInfo si;

	public RamMemory() {
		si = new SystemInfo();
	}

	public RamInfo getRamInfo() {
		RamInfo info = new RamInfo();
		GlobalMemory memory = si.getHardware().getMemory();
		info.setMaxRam(memory.getTotal());
		info.setAvaliableRam(memory.getAvailable());
		info.getUsedRam(info.getMaxRam() - info.getAvaliableRam());
		return info;
	}
}