package online.zhaopei.hardwareinfo.util;

import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.Swap;

public final class SigarUtil {

	public static Sigar sigar = new Sigar();
	
	/**
	 * 获取内存信息
	 * @return
	 */
	public static Mem getMemInfo() {
		Mem mem = null;
		try {
			mem = sigar.getMem();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return mem;
	}
	
	/**
	 * 获取交换空间信息
	 * @return
	 */
	public static Swap getSwapInfo() {
		Swap swap = null;
		try {
			swap = sigar.getSwap();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return swap;
	}
	
	public static CpuPerc getTotalCpuPerc() {
		CpuPerc cpuPerc = null;
		try {
			cpuPerc = sigar.getCpuPerc();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cpuPerc;
	}
}
