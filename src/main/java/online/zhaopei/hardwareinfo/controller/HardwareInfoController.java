package online.zhaopei.hardwareinfo.controller;

import java.io.File;
import java.io.FileFilter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hyperic.sigar.CpuInfo;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.FileSystem;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.NetFlags;
import org.hyperic.sigar.NetInterfaceConfig;
import org.hyperic.sigar.OperatingSystem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.Swap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import online.zhaopei.hardwareinfo.domain.Cpu;
import online.zhaopei.hardwareinfo.domain.FileSystemInfo;
import online.zhaopei.hardwareinfo.domain.NetInterfaceInfo;

@RestController
public class HardwareInfoController implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -2448259772853577670L;

	public static Sigar sigar = new Sigar();

	@RequestMapping("/memInfo")
	public Mem getMemInfo() throws Exception {
		return sigar.getMem();
	}

	@RequestMapping("/swapInfo")
	public Swap getSwapInfo() throws Exception {
		return sigar.getSwap();
	}

	@RequestMapping("/cpuPerc")
	public CpuPerc getCpuPerc() throws Exception {
		return sigar.getCpuPerc();
	}

	@RequestMapping("/cpuList")
	public List<Cpu> getCpuList() throws Exception {
		List<Cpu> cpuList = new ArrayList<Cpu>();
		CpuInfo infos[] = sigar.getCpuInfoList();
		CpuPerc percs[] = sigar.getCpuPercList();

		for (int i = 0; i < infos.length; i++) {
			cpuList.add(new Cpu(infos[i], percs[i]));
		}

		return cpuList;
	}

	@RequestMapping("/osInfo")
	public OperatingSystem getOsInfo() throws Exception {
		return OperatingSystem.getInstance();
	}

	@RequestMapping("/getDirectoryFileCount")
	public Integer getDirectoryFileCount(String directory, boolean containSubdirectory) {
		File[] files = new File[]{};
		File fileDir = new File(directory);
		if (fileDir.exists()) {
			files = fileDir.listFiles(new FileFilter() {
				
				@Override
				public boolean accept(File pathname) {
					if (!containSubdirectory && pathname.isDirectory()) {
						return false;
					}
					return true;
				}
			});
		}
		return files.length;
	}
	
	@RequestMapping("/fileSystemList")
	public List<FileSystemInfo> getFileSystem() throws Exception {
		List<FileSystemInfo> fileSystemInfoList = new ArrayList<FileSystemInfo>();
		FileSystem[] fileSystems = sigar.getFileSystemList();
		FileSystem tmpFs = null;

		for (int i = 0; i < fileSystems.length; i++) {
			tmpFs = fileSystems[i];
			if (FileSystem.TYPE_LOCAL_DISK == tmpFs.getType()) {
				fileSystemInfoList.add(new FileSystemInfo(tmpFs, sigar.getFileSystemUsage(tmpFs.getDirName())));
			}
		}

		return fileSystemInfoList;
	}

	@RequestMapping("/netInterfaceInfo")
	public List<NetInterfaceInfo> getNetInterfaceInfo() throws Exception {
		List<NetInterfaceInfo> netInterfaceInfoList = new ArrayList<NetInterfaceInfo>();
		String ifNames[] = sigar.getNetInterfaceList();
		String name = null;
		for (int i = 0; i < ifNames.length; i++) {
			name = ifNames[i];
			netInterfaceInfoList.add(
					new NetInterfaceInfo(name, sigar.getNetInterfaceConfig(name), sigar.getNetInterfaceStat(name)));
		}

		return netInterfaceInfoList;
	}

	@RequestMapping("/networkInterfaceInfo")
	public List<NetInterfaceInfo> getNetworkInterfaceInfo() throws Exception {
		List<NetInterfaceInfo> netInterfaceInfoList = new ArrayList<NetInterfaceInfo>();
		String ifNames[] = sigar.getNetInterfaceList();
		NetInterfaceConfig cfg = null;
		String name = null;
		for (int i = 0; i < ifNames.length; i++) {
			name = ifNames[i];
			cfg = sigar.getNetInterfaceConfig(name);
			if (NetFlags.LOOPBACK_ADDRESS.equals(cfg.getAddress()) || (cfg.getFlags() & NetFlags.IFF_LOOPBACK) != 0
					|| NetFlags.NULL_HWADDR.equals(cfg.getHwaddr())) {
				continue;
			}
			netInterfaceInfoList.add(new NetInterfaceInfo(name, cfg, sigar.getNetInterfaceStat(name)));
		}

		return netInterfaceInfoList;
	}
}
