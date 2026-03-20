package com.ddf.boot.capableadmin.infra.util;

import com.ddf.boot.common.core.helper.SpringContextHolder;
import net.dreamlu.mica.ip2region.core.Ip2regionSearcher;
import net.dreamlu.mica.ip2region.core.IpInfo;

/**
 * IP 归属地工具类。
 *
 * @author Snowball
 * @version 1.0
 * @since 2026/03/12 18:34
 */
public class CapableAdminUtils {

	/**
	 * 根据 IP 获取详细地址。
	 */
	public static String getAddressByIp(String ip) {
		if (ip == null || ip.isBlank()) {
			return null;
		}
		try {
			// 非 Spring 上下文场景下降级为 null，避免类初始化失败影响业务流程和单元测试。
			Ip2regionSearcher ipRegionSearcher = SpringContextHolder.getBean(Ip2regionSearcher.class);
			IpInfo ipInfo = ipRegionSearcher.memorySearch(ip);
			if (ipInfo != null) {
				return ipInfo.getAddress();
			}
		} catch (Exception ignored) {
			return null;
		}
		return null;
	}
}
