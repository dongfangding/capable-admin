package com.ddf.boot.capableadmin.infra.extra;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.ddf.boot.common.mvc.permissionscan.PermissionValueSelector;
import java.lang.reflect.Method;
import org.springframework.stereotype.Component;

/**
 * <p>description</p >
 *
 * @author Snowball
 * @version 1.0
 * @date 2026/02/26 17:33
 */
@Component
public class PermissionValueSelectorImpl implements PermissionValueSelector {
	@Override
	public String getPermission(Method method) {
		if (!method.isAnnotationPresent(SaCheckPermission.class)) {
			return "";
		}
		final SaCheckPermission annotation = method.getAnnotation(SaCheckPermission.class);
		return annotation.value()[0];
	}
}
