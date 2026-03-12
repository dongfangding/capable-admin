package com.ddf.boot.capableadmin.infra.util;

import com.ddf.boot.common.core.helper.SpringContextHolder;
import net.dreamlu.mica.ip2region.core.Ip2regionSearcher;
import net.dreamlu.mica.ip2region.core.IpInfo;

/**
 * <p>description</p >
 *
 * @author Snowball
 * @version 1.0
 * @since 2026/03/12 18:34
 */
public class CapableAdminUtils {

	/**
	 * ip低于搜索
	 */
	private final static Ip2regionSearcher IP_REGION_SEARCHER = SpringContextHolder.getBean(Ip2regionSearcher.class);

	/**
	 * 根据ip获取详细地址
	 */
	public static String getAddressByIp(String ip) {
		IpInfo ipInfo = IP_REGION_SEARCHER.memorySearch(ip);
		if (ipInfo != null) {
			return ipInfo.getAddress();
		}
		return null;
	}
}
