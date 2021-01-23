package data;

import oshi.software.os.*;

import java.util.*;

public class ProcessManagerProcessHandle implements OperatingSystem {
	private long pid;
	private String cpu;
	private String memoryUsage;
	private String virtualMemory;
	private String residentSetSizeMemory;
	private String name;

	public ProcessManagerProcessHandle(long pid, String cpu, String memoryUsage, String virtualMemory,
			String residentSetSizeMemory, String name) {
		this.pid = pid;
		this.cpu = cpu;
		this.memoryUsage = memoryUsage;
		this.virtualMemory = virtualMemory;
		this.residentSetSizeMemory = residentSetSizeMemory;
		this.name = name;
	}

	@Override
	public OSService[] getServices() {
		List<OSService> services = new ArrayList<>();
		Set<String> running = new HashSet<>();
		for (OSProcess p : getChildProcesses(10, 0, ProcessSort.PID)) {
			OSService s = new OSService(p.getName(), p.getProcessID(), OSService.State.RUNNING);
			services.add(s);
			running.add(p.getName());
		}
		return services.toArray(new OSService[10]);
	}

	@Override
	public List<OSProcess> getProcesses(Collection<Integer> collection) {
		List<oshi.software.os.OSProcess> returnValue = new LinkedList<>();
		for (Integer pid : collection) {
			OSProcess process = getProcess(pid);
			if (process != null)
				returnValue.add(process);
		}
		return returnValue;
	}

	@Override
	public OSProcess getProcess(int i) {
		return null;
	}

	@Override
	public int getProcessId() {
		return 0;
	}

	@Override
	public int getProcessCount() {
		return 0;
	}

	@Override
	public int getThreadCount() {
		return 0;
	}

	@Override
	public int getBitness() {
		return 0;
	}

	@Override
	public long getSystemUptime() {
		return 0;
	}

	@Override
	public long getSystemBootTime() {
		return 0;
	}

	@Override
	public boolean isElevated() {
		return false;
	}

	@Override
	public NetworkParams getNetworkParams() {
		return null;
	}

	@Override
	public OSVersionInfo getVersionInfo() {
		return null;
	}

	@Override
	public List<OSProcess> getProcesses(int i, ProcessSort processSort) {
		return null;
	}

	@Override
	public List<OSProcess> getChildProcesses(int i, int i1, ProcessSort processSort) {
		return null;
	}

	@Override
	public String getFamily() {
		return null;
	}

	@Override
	public String getManufacturer() {
		return null;
	}

	@Override
	public FileSystem getFileSystem() {
		return null;
	}

	@Override
	public InternetProtocolStats getInternetProtocolStats() {
		return null;
	}

	@Override
	public List<OSSession> getSessions() {
		return null;
	}

	@Override
	public List<OSProcess> getProcesses() {
		return null;
	}

	public Long getPid() {
		return pid;
	}

	public String getCpu() {
		return cpu;
	}

	public void setCpu(String cpu) {
		this.cpu = cpu;
	}

	public String getMemoryUsage() {
		return memoryUsage;
	}

	public void setMemoryUsage(String memoryUsage) {
		this.memoryUsage = memoryUsage;
	}

	public String getVirtualMemory() {
		return virtualMemory;
	}

	public void setVirtualMemory(String virtualMemory) {
		this.virtualMemory = virtualMemory;
	}

	public String getResidentSetSizeMemory() {
		return residentSetSizeMemory;
	}

	public void setResidentSetSizeMemory(String residentSetSizeMemory) {
		this.residentSetSizeMemory = residentSetSizeMemory;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}