package com.ddf.boot.capableadmin.infra.repository;

import com.ddf.boot.capableadmin.infra.mapper.SysMenuMapper;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 * <p>description</p >
 *
 * @author Snowball
 * @version 1.0
 * @since 2026/03/02 15:28
 */
@RequiredArgsConstructor
@Slf4j
@Repository
public class SysMenuRepository {

	private final SysMenuMapper sysMenuMapper;

	/**
	 * 查询所有菜单id, 给超管用
	 *
	 * @return 权限标识集合
	 */
	public Set<Long> getAllMenuIds() {
		return sysMenuMapper.findAllMenuIds();
	}
}
