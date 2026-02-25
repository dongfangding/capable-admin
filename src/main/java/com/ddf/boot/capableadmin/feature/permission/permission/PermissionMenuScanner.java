package com.ddf.boot.capableadmin.feature.permission.permission;


import com.ddf.boot.common.core.util.TreeConvertUtil;
import com.google.common.collect.Sets;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * 菜单权限扫描
 */
@Component
public class PermissionMenuScanner {

    private final ApplicationContext applicationContext;

    public PermissionMenuScanner(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * 扫描菜单权限
     *
     * @return
     */
    public ScanPermissionPayload scanPermissionFunctionMethods() {
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(
                org.springframework.stereotype.Controller.class);
        beans.putAll(applicationContext.getBeansWithAnnotation(
                org.springframework.web.bind.annotation.RestController.class));
        Set<SysMenuFunction> menus = Sets.newHashSet();
        for (Map.Entry<String, Object> entry : beans.entrySet()) {
            Object bean = entry.getValue();
            Class<?> targetClass = AopUtils.getTargetClass(bean);
            PermissionMenu classPermissionMenu = targetClass.getAnnotation(PermissionMenu.class);
            // 为避免扫描类过多，必须类上加这个注解
            // 如果一个类中有多个菜单下的接口权限，那么就会在每个方法上标注当前接口所属的父类，类上的注解就变的没有意义，此时可以定义一个空注解，不能省略
            // 例如：@PermissionMenu(name = "", parentName = "")
            if (classPermissionMenu == null) {
                continue;
            }
            final SysMenuFunction currentMenu = new SysMenuFunction();
            if (StringUtils.isNotBlank(classPermissionMenu.name())) {
                currentMenu.setParentCode(
                        StringUtils.defaultIfBlank(classPermissionMenu.parentCode(), classPermissionMenu.parentName()));
                currentMenu.setParentName(classPermissionMenu.parentName());
                currentMenu.setType(classPermissionMenu.type());
                currentMenu.setName(classPermissionMenu.name());
                currentMenu.setCode(StringUtils.defaultIfBlank(classPermissionMenu.code(), classPermissionMenu.name()));
                currentMenu.setSort(1);
                currentMenu.setPermission(classPermissionMenu.permission());
                menus.add(currentMenu);
            }
            if (StringUtils.isNotBlank(classPermissionMenu.parentCode()) || StringUtils.isNotBlank(
                    classPermissionMenu.parentName())) {
                final SysMenuFunction parentMenu = new SysMenuFunction();
                // 类上的父类菜单，暂时属性太少，没有办法处理父类的父类，直接默认一级菜单了
                parentMenu.setParentCode("");
                parentMenu.setParentName("");
                parentMenu.setType(classPermissionMenu.parentType());
                parentMenu.setName(classPermissionMenu.parentName());
                parentMenu.setCode(
                        StringUtils.defaultIfBlank(classPermissionMenu.parentCode(), classPermissionMenu.parentName()));
                parentMenu.setSort(1);
                parentMenu.setPermission(classPermissionMenu.permission());
                menus.add(parentMenu);
            }

            for (Method method : targetClass.getDeclaredMethods()) {
                if (method.isAnnotationPresent(PermissionFunction.class)) {
                    PermissionFunction permissionFunction = method.getAnnotation(PermissionFunction.class);
                    PermissionMenu currentPermissionMenu = classPermissionMenu;
                    if (StringUtils.isNotBlank(permissionFunction
                            .menu()
                            .name())) {
                        currentPermissionMenu = permissionFunction.menu();
                    }
                    String parentName = currentPermissionMenu.name();
                    String parentCode = StringUtils.defaultIfBlank(currentPermissionMenu.code(), parentName);

                    // 处理当前PermissionFunction的父类菜单
                    if (StringUtils.isNotBlank(parentName) || StringUtils.isNotBlank(parentCode)) {
                        final SysMenuFunction parentMenu = new SysMenuFunction();
                        // 暂时属性太少，一直嵌套的父级没办法支持
                        parentMenu.setParentCode(StringUtils.defaultIfBlank(currentPermissionMenu.parentCode(),
                                currentPermissionMenu.parentName()
                        ));
                        parentMenu.setParentName(currentPermissionMenu.parentName());
                        parentMenu.setType(currentPermissionMenu.type());
                        parentMenu.setName(parentName);
                        parentMenu.setCode(parentCode);
                        parentMenu.setSort(1);
                        parentMenu.setPermission(currentPermissionMenu.permission());
                        menus.add(parentMenu);
                    }
                    // 处理当前PermissionFunction的父类的父类
                    if (StringUtils.isNotBlank(currentPermissionMenu.parentCode()) || StringUtils.isNotBlank(
                            currentPermissionMenu.parentName())) {
                        final SysMenuFunction parentMenu = new SysMenuFunction();
                        // 暂时属性太少，一直嵌套的父级没办法支持
                        parentMenu.setParentCode("");
                        parentMenu.setParentName("");
                        parentMenu.setType(currentPermissionMenu.parentType());
                        parentMenu.setName(currentPermissionMenu.parentName());
                        parentMenu.setCode(StringUtils.defaultIfBlank(currentPermissionMenu.parentCode(),
                                currentPermissionMenu.parentName()
                        ));
                        parentMenu.setSort(1);
                        parentMenu.setPermission(currentPermissionMenu.permission());
                        menus.add(parentMenu);
                    }

                    final SysMenuFunction function = new SysMenuFunction();
                    function.setParentCode(parentCode);
                    function.setParentName(parentName);
                    function.setType(PermissionMenuType.BUTTON);
                    function.setCode(StringUtils.defaultIfBlank(permissionFunction.code(), permissionFunction.name()));
                    function.setName(permissionFunction.name());
                    function.setSort(1);
                    function.setPermission(permissionFunction.permission());
                    menus.add(function);
                }
            }
        }
        final ArrayList<SysMenuFunction> menuList = new ArrayList<>(menus);
        final List<SysMenuFunction> menuTreeList = TreeConvertUtil.convert(menuList);
        final ScanPermissionPayload payload = new ScanPermissionPayload();
        payload.setMenuFunctions(menuTreeList);
        return payload;
    }
}
