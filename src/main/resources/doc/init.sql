/*
 Navicat Premium Data Transfer

 Source Server         : 虚拟机
 Source Server Type    : MySQL
 Source Server Version : 80032
 Source Host           : 10.88.1.145:3306
 Source Schema         : capable_admin

 Target Server Type    : MySQL
 Target Server Version : 80032
 File Encoding         : 65001

 Date: 28/02/2026 22:27:48
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for country
-- ----------------------------
DROP TABLE IF EXISTS `country`;
CREATE TABLE `country`  (
                            `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增id',
                            `area_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '区域名称',
                            `currency_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '国家名称',
                            `currency_code` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '货币code',
                            `currency_symbol` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '货币符',
                            `area_code` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '地区code',
                            `area_name_local` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '当地地区域名称',
                            `phone_code` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '手机区号',
                            `area_name_zh` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '国家名称中文',
                            `area_name_zh_trad` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '繁体',
                            `area_name_en` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '英文',
                            `area_name_fil` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菲律宾',
                            `area_name_ar` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '阿拉伯',
                            `area_name_hi` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '印地语',
                            `area_name_bn` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '孟加拉',
                            `area_name_fr` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '法语',
                            `area_name_vi` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '越南',
                            `area_name_pt` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '葡萄牙',
                            `area_name_es` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '西班牙',
                            `area_name_jap` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '日文',
                            `area_name_ko` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '韩文',
                            `area_name_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '印尼语',
                            PRIMARY KEY (`id`) USING BTREE,
                            INDEX `idx_currency_code`(`currency_code` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of country
-- ----------------------------

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept`  (
                             `dept_id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
                             `pid` bigint NOT NULL DEFAULT 0 COMMENT '上级部门，默认为0，代表一级节点',
                             `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '名称',
                             `sort` int NOT NULL DEFAULT 0 COMMENT '排序',
                             `enabled` bit(1) NOT NULL DEFAULT b'1' COMMENT '状态, 0 禁用 1 启用',
                             `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
                             `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
                             `create_time` bigint NULL DEFAULT 0 COMMENT '创建日期',
                             `update_time` bigint NULL DEFAULT 0 COMMENT '更新时间',
                             `sub_count` int NOT NULL DEFAULT 0 COMMENT '子节点数量，用来判定是否需要展开',
                             PRIMARY KEY (`dept_id`) USING BTREE,
                             UNIQUE INDEX `UK_name`(`name` ASC) USING BTREE,
                             INDEX `IDX_pid`(`pid` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '部门' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES (1, 0, '天堂部', 1, b'1', 'admin', 'admin', 1683010740, 1683010740, 1);
INSERT INTO `sys_dept` VALUES (2, 0, '地狱部', 2, b'1', 'admin', 'admin', 1683010740, 1683010740, 1);
INSERT INTO `sys_dept` VALUES (3, 1, '人上人部', 3, b'1', 'admin', 'admin', 1683010740, 1683010740, 0);
INSERT INTO `sys_dept` VALUES (4, 2, '牛马部', 4, b'1', 'admin', 'admin', 1683010740, 1683010740, 0);

-- ----------------------------
-- Table structure for sys_dict
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict`  (
                             `dict_id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
                             `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典名称',
                             `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
                             `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建者',
                             `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新者',
                             `create_time` datetime NULL DEFAULT NULL COMMENT '创建日期',
                             `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
                             PRIMARY KEY (`dict_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数据字典' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dict
-- ----------------------------

-- ----------------------------
-- Table structure for sys_dict_detail
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_detail`;
CREATE TABLE `sys_dict_detail`  (
                                    `detail_id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                    `dict_id` bigint NULL DEFAULT NULL COMMENT '字典id',
                                    `label` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典标签',
                                    `value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典值',
                                    `dict_sort` int NOT NULL DEFAULT 0 COMMENT '排序',
                                    `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建者',
                                    `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新者',
                                    `create_time` datetime NULL DEFAULT NULL COMMENT '创建日期',
                                    `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
                                    PRIMARY KEY (`detail_id`) USING BTREE,
                                    INDEX `FK5tpkputc6d9nboxojdbgnpmyb`(`dict_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数据字典详情' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dict_detail
-- ----------------------------

-- ----------------------------
-- Table structure for sys_job
-- ----------------------------
DROP TABLE IF EXISTS `sys_job`;
CREATE TABLE `sys_job`  (
                            `job_id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
                            `name` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '岗位名称',
                            `enabled` bit(1) NOT NULL DEFAULT b'1' COMMENT '岗位状态',
                            `sort` int NOT NULL DEFAULT 0 COMMENT '排序',
                            `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建者',
                            `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新者',
                            `create_time` bigint NULL DEFAULT NULL COMMENT '创建日期',
                            `update_time` bigint NULL DEFAULT NULL COMMENT '更新时间',
                            PRIMARY KEY (`job_id`) USING BTREE,
                            UNIQUE INDEX `UK_name`(`name` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 34 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '岗位' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_job
-- ----------------------------
INSERT INTO `sys_job` VALUES (1, '监管', b'1', 1, 'admin', 'admin', 1683010740, 1683010740);
INSERT INTO `sys_job` VALUES (2, '研发', b'1', 2, 'admin', 'admin', 1683010740, 1683010740);
INSERT INTO `sys_job` VALUES (3, '测试', b'1', 3, 'admin', 'admin', 1683010740, 1683010740);
INSERT INTO `sys_job` VALUES (4, '财务', b'1', 4, 'admin', 'admin', 1683010740, 1683010740);
INSERT INTO `sys_job` VALUES (5, '运营', b'1', 5, 'admin', 'admin', 1683010740, 1683010740);
INSERT INTO `sys_job` VALUES (6, 'UI', b'1', 6, 'admin', 'admin', 1683010740, 1683010740);
INSERT INTO `sys_job` VALUES (7, '产品经理', b'1', 7, 'admin', 'admin', 1683010740, 1683010740);

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log`  (
                            `log_id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
                            `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                            `log_type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                            `method` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                            `params` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
                            `request_ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                            `time` bigint NULL DEFAULT NULL,
                            `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                            `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '',
                            `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                            `browser` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                            `exception_detail` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
                            `create_time` bigint NULL DEFAULT NULL,
                            PRIMARY KEY (`log_id`) USING BTREE,
                            INDEX `log_create_time_index`(`create_time` ASC) USING BTREE,
                            INDEX `inx_log_type`(`log_type` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统日志' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_log
-- ----------------------------

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
create table sys_menu
(
    menu_id     bigint auto_increment comment 'ID'
        primary key,
    pid         bigint       default 0    not null comment '上级菜单ID，默认0为一级节点',
    type        varchar(10)               not null comment '菜单类型，catalog：目录；menu：菜单；embedded： 内嵌；link：外链 ；button： 按钮；',
    name        varchar(64)               not null comment '菜单名称，一般是前端使用给程序用的，  如Vue Router 路由标识 ，前端用 name 做路由跳转 ，router.push({ name: ''SystemUser'' })',
    title       varchar(100)              not null comment '菜单标题，给人看的，即界面上显示的菜单标题',
    icon        varchar(32)               null comment '菜单图标',
    path        varchar(255) default ''   not null comment '，即显示在浏览器地址栏上的路径，实际这个路径对应的显示内容则要看component组件',
    component   varchar(255) default ''   null comment '前端组件，为实际前端路由组件。对应view注册的组件地址，如前端静态路由写法component: () => import(''#/views/system/user/list.vue''),常用后端返回"component": "/system/user/list"',
    sort        int          default 0    not null comment '排序',
    enable      bit          default b'1' not null comment '是否启用',
    meta        varchar(2000)             null comment '菜单元数据，介于菜单可以配置的东西太多，且每个前端都不一样，用这个大json存储，前端要用啥自己存，后端只负责存储。如菜单的图标，是否隐藏，是否缓存，是否外链，是否固定标签页等等等等，各种自定义的用于控制界面表现的参数全放到这个大json里',
    permission  varchar(255) default ''   null comment '权限',
    create_by   varchar(255)              null comment '创建者',
    update_by   varchar(255)              null comment '更新者',
    create_time bigint                    null comment '创建日期',
    update_time bigint                    null comment '更新时间',
    sub_count   int          default 0    not null comment '子节点数量'
)
    comment '系统菜单' collate = utf8mb4_general_ci
                       row_format = DYNAMIC;

create index INX_pid
    on sys_menu (pid);

create index UK_name
    on sys_menu (name);



-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO capable_admin.sys_menu (menu_id, pid, type, name, title, icon, path, component, sort, enable, meta, permission, create_by, update_by, create_time, update_time, sub_count) VALUES (2, 0, 'catalog', 'System', 'System', 'carbon:settings', '/system', null, 0, true, '{"icon": "carbon:menu", "title": "system.menu.title"}', '', null, null, 1772377377, 1772377377, 2);
INSERT INTO capable_admin.sys_menu (menu_id, pid, type, name, title, icon, path, component, sort, enable, meta, permission, create_by, update_by, create_time, update_time, sub_count) VALUES (9, 0, 'catalog', 'Project', 'Project', 'carbon:data-center', '/vben-admin', null, 0, true, '{"badgeType": "dot", "order": 9998, "title": "demos.vben.title", "icon": "carbon:data-center"}', '', null, null, 1772377377, 1772377377, 3);
INSERT INTO capable_admin.sys_menu (menu_id, pid, type, name, title, icon, path, component, sort, enable, meta, permission, create_by, update_by, create_time, update_time, sub_count) VALUES (10, 0, 'menu', 'About', '关于', 'lucide:copyright', '/about', '_core/about/index', 0, true, '{"icon": "lucide:copyright", "order": 9999, "title": "demos.vben.about"}', '', null, null, 1772377377, 1772377377, 0);
INSERT INTO capable_admin.sys_menu (menu_id, pid, type, name, title, icon, path, component, sort, enable, meta, permission, create_by, update_by, create_time, update_time, sub_count) VALUES (102, 0, 'menu', 'Workspace', 'Workspace', '', '/workspace', '/dashboard/workspace/index', 0, true, '{"icon": "carbon:workspace", "title": "page.dashboard.workspace", "affixTab": true, "order": 0}', '', null, null, 1772377377, 1772377377, 0);
INSERT INTO capable_admin.sys_menu (menu_id, pid, type, name, title, icon, path, component, sort, enable, meta, permission, create_by, update_by, create_time, update_time, sub_count) VALUES (201, 2, 'menu', 'SystemMenu', '菜单管理', 'carbon:menu', '/system/menu', '/system/menu/list', 0, true, '{"icon": "carbon:menu", "title": "system.menu.title"}', 'System:Menu:List', null, null, 1772377377, 1772377377, 3);
INSERT INTO capable_admin.sys_menu (menu_id, pid, type, name, title, icon, path, component, sort, enable, meta, permission, create_by, update_by, create_time, update_time, sub_count) VALUES (202, 2, 'menu', 'SystemDept', '部门管理', 'carbon:container-services', '/system/dept', '/system/dept/list', 0, true, '{"icon": "carbon:container-services", "title": "system.dept.title"}', 'System:Dept:List', null, null, 1772377377, 1772377377, 3);
INSERT INTO capable_admin.sys_menu (menu_id, pid, type, name, title, icon, path, component, sort, enable, meta, permission, create_by, update_by, create_time, update_time, sub_count) VALUES (901, 9, 'embedded', 'VbenDocument', '文档', 'carbon:book', '/vben-admin/document', 'IFrameView', 0, true, '{"icon": "carbon:book", "iframeSrc": "https://doc.vben.pro", "title": "demos.vben.document"}', '', null, null, 1772377377, 1772377377, 0);
INSERT INTO capable_admin.sys_menu (menu_id, pid, type, name, title, icon, path, component, sort, enable, meta, permission, create_by, update_by, create_time, update_time, sub_count) VALUES (902, 9, 'link', 'VbenGithub', 'Github', 'carbon:logo-github', '/vben-admin/github', 'IFrameView', 0, true, '{"icon": "carbon:logo-github", "link": "https://github.com/vbenjs/vue-vben-admin", "title": "Github"}', '', null, null, 1772377377, 1772377377, 0);
INSERT INTO capable_admin.sys_menu (menu_id, pid, type, name, title, icon, path, component, sort, enable, meta, permission, create_by, update_by, create_time, update_time, sub_count) VALUES (20101, 201, 'button', 'SystemMenuCreate', '新增', '', '', null, 0, true, '{"title": "common.create"}', 'System:Menu:Create', null, null, 1772377377, 1772377377, 0);
INSERT INTO capable_admin.sys_menu (menu_id, pid, type, name, title, icon, path, component, sort, enable, meta, permission, create_by, update_by, create_time, update_time, sub_count) VALUES (20102, 201, 'button', 'SystemMenuEdit', '编辑', '', '', null, 0, true, '{"title": "common.edit"}', 'System:Menu:Edit', null, null, 1772377377, 1772377377, 0);
INSERT INTO capable_admin.sys_menu (menu_id, pid, type, name, title, icon, path, component, sort, enable, meta, permission, create_by, update_by, create_time, update_time, sub_count) VALUES (20103, 201, 'button', 'SystemMenuDelete', '删除', '', '', null, 0, true, '{"title": "common.delete"}', 'System:Menu:Delete', null, null, 1772377377, 1772377377, 0);
INSERT INTO capable_admin.sys_menu (menu_id, pid, type, name, title, icon, path, component, sort, enable, meta, permission, create_by, update_by, create_time, update_time, sub_count) VALUES (20201, 202, 'button', 'SystemDeptCreate', '新增', '', '', null, 0, true, '{"title": "common.create"}', 'System:Dept:Create', null, null, 1772377377, 1772377377, 0);
INSERT INTO capable_admin.sys_menu (menu_id, pid, type, name, title, icon, path, component, sort, enable, meta, permission, create_by, update_by, create_time, update_time, sub_count) VALUES (20202, 202, 'button', 'SystemDeptEdit', '编辑', '', '', null, 0, true, '{"title": "common.edit"}"', 'System:Dept:Edit', null, null, 1772377377, 1772377377, 0);
INSERT INTO capable_admin.sys_menu (menu_id, pid, type, name, title, icon, path, component, sort, enable, meta, permission, create_by, update_by, create_time, update_time, sub_count) VALUES (20203, 202, 'button', 'SystemDeptDelete', '删除', '', '', null, 0, true, '{"title": "common.delete"}', 'System:Dept:Delete', null, null, 1772377377, 1772377377, 0);

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
                             `role_id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
                             `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '名称',
                             `level` int NOT NULL DEFAULT 0 COMMENT '角色级别',
                             `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '描述',
                             `ip_limit` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'ip白名单（指定IP登入）',
                             `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
                             `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
                             `create_time` bigint NULL DEFAULT NULL COMMENT '创建日期',
                             `update_time` bigint NULL DEFAULT NULL COMMENT '更新时间',
                             `sort` int NOT NULL DEFAULT 0 COMMENT '排序',
                             `is_admin` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否超管， 这个超管是虚拟的，如果是的话， 直接拥有全部权限，不需要手动关联子权限，只能初始化，不能接口新增',
                             `enable` bit(1) NOT NULL DEFAULT b'1' COMMENT '是否启用， 0否1是',
                             PRIMARY KEY (`role_id`) USING BTREE,
                             UNIQUE INDEX `UK_name`(`name` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '超级管理员', 1, '超级管理员', '0', 'admin', 'admin', 1683010740, 1683010740, 0, b'1', b'1');

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
                                  `menu_id` bigint NOT NULL COMMENT '菜单ID',
                                  `role_id` bigint NOT NULL COMMENT '角色ID',
                                  PRIMARY KEY (`menu_id`, `role_id`) USING BTREE,
                                  INDEX `IDX_role_id`(`role_id` ASC) USING BTREE,
                                  INDEX `IDX_menu_id`(`menu_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色菜单关联' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
                             `user_id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
                             `username` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
                             `nickname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '昵称',
                             `sex` int NOT NULL DEFAULT -1 COMMENT '性别0 女 1 男',
                             `mobile` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '手机号码',
                             `email` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '邮箱',
                             `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '头像地址',
                             `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
                             `enabled` bit(1) NOT NULL DEFAULT b'1' COMMENT '状态：1启用、0禁用',
                             `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建者',
                             `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新者',
                             `pwd_reset_time` datetime NULL DEFAULT NULL COMMENT '修改密码的时间',
                             `create_time` datetime NULL DEFAULT NULL COMMENT '创建日期',
                             `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
                             PRIMARY KEY (`user_id`) USING BTREE,
                             UNIQUE INDEX `UK_username`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 159 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统用户' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'snowball', 'snowball', 1, '10000000000', 'snowball@gmail.com', '', '$2a$10$TmU7VviHhXGW4ibcarlOY.loEOHioXKGgU6kIwECzY10cLc9avg.m', b'1', NULL, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for sys_user_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_dept`;
CREATE TABLE `sys_user_dept`  (
                                  `user_id` bigint NOT NULL COMMENT '用户id',
                                  `dept_id` bigint NOT NULL,
                                  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户部门关联' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_dept
-- ----------------------------
INSERT INTO `sys_user_dept` VALUES (1, 1);

-- ----------------------------
-- Table structure for sys_user_job
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_job`;
CREATE TABLE `sys_user_job`  (
                                 `user_id` bigint NOT NULL COMMENT '用户ID',
                                 `job_id` bigint NOT NULL COMMENT '岗位ID',
                                 PRIMARY KEY (`user_id`, `job_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_job
-- ----------------------------
INSERT INTO `sys_user_job` VALUES (1, 1);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
                                  `user_id` bigint NOT NULL COMMENT '用户ID',
                                  `role_id` bigint NOT NULL COMMENT '角色ID',
                                  PRIMARY KEY (`user_id`, `role_id`) USING BTREE,
                                  INDEX `FKq4eq273l04bpu4efj0jd0jb98`(`role_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户角色关联' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (1, 1);

-- ----------------------------
-- Table structure for tool_qiniu_config
-- ----------------------------
DROP TABLE IF EXISTS `tool_qiniu_config`;
CREATE TABLE `tool_qiniu_config`  (
                                      `config_id` bigint NOT NULL COMMENT 'ID',
                                      `access_key` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT 'accessKey',
                                      `bucket` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'Bucket 识别符',
                                      `host` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '外链域名',
                                      `secret_key` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT 'secretKey',
                                      `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '空间类型',
                                      `zone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '机房',
                                      PRIMARY KEY (`config_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '七牛云配置' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of tool_qiniu_config
-- ----------------------------

-- ----------------------------
-- Table structure for tool_qiniu_content
-- ----------------------------
DROP TABLE IF EXISTS `tool_qiniu_content`;
CREATE TABLE `tool_qiniu_content`  (
                                       `content_id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                       `bucket` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'Bucket 识别符',
                                       `name` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件名称',
                                       `size` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件大小',
                                       `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件类型：私有或公开',
                                       `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件url',
                                       `suffix` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件后缀',
                                       `update_time` datetime NULL DEFAULT NULL COMMENT '上传或同步的时间',
                                       PRIMARY KEY (`content_id`) USING BTREE,
                                       UNIQUE INDEX `uniq_name`(`name` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '七牛云文件存储' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of tool_qiniu_content
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
