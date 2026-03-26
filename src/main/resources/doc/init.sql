/*
 Navicat Premium Data Transfer

 Source Server         : 虚拟机
 Source Server Type    : MySQL
 Source Server Version : 80032
 Source Host           : 10.88.1.165:3306
 Source Schema         : capable_admin

 Target Server Type    : MySQL
 Target Server Version : 80032
 File Encoding         : 65001

 Date: 25/03/2026 11:10:56
*/

create database if not exists capable_admin charset utf8mb4 collate utf8mb4_0900_ai_ci;

use capable_admin;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

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
                             `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
                             PRIMARY KEY (`dept_id`) USING BTREE,
                             UNIQUE INDEX `UK_name`(`name` ASC) USING BTREE,
                             INDEX `IDX_pid`(`pid` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '部门' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES (1, 0, '天堂部', 1, b'1', 'admin', 'admin', 1683010740, 1683010740, 1, '天堂部');
INSERT INTO `sys_dept` VALUES (2, 0, '地狱部', 2, b'1', 'admin', 'admin', 1683010740, 1683010740, 1, '地狱部');
INSERT INTO `sys_dept` VALUES (3, 1, '人上人部', 0, b'1', 'admin', 'admin', 1683010740, 1772708137, 0, '人上人部');
INSERT INTO `sys_dept` VALUES (4, 2, '牛马部', 4, b'1', 'admin', 'admin', 1683010740, 1683010740, 0, '牛马部');

-- ----------------------------
-- Table structure for sys_dict
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict`  (
                             `dict_id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
                             `dict_code` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典代码',
                             `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典名称',
                             `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
                             `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建者',
                             `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新者',
                             `create_time` datetime NULL DEFAULT NULL COMMENT '创建日期',
                             `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
                             PRIMARY KEY (`dict_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数据字典' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dict
-- ----------------------------
INSERT INTO `sys_dict` VALUES (1, 'enabled', '启用禁用标识', '通用表述启用禁用的标识', NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for sys_dict_detail
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_detail`;
CREATE TABLE `sys_dict_detail`  (
                                    `detail_id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                    `dict_id` bigint NULL DEFAULT NULL COMMENT '字典id',
                                    `detail_code` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典明细编码',
                                    `label` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典标签',
                                    `value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典值',
                                    `dict_sort` int NOT NULL DEFAULT 0 COMMENT '排序',
                                    `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建者',
                                    `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新者',
                                    `create_time` datetime NULL DEFAULT NULL COMMENT '创建日期',
                                    `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
                                    PRIMARY KEY (`detail_id`) USING BTREE,
                                    INDEX `IDX_dict_id`(`dict_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数据字典详情' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dict_detail
-- ----------------------------
INSERT INTO `sys_dict_detail` VALUES (1, 1, 'enabled', '启用', '1', 1, NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_detail` VALUES (2, 1, 'disabled', '禁用', '0', 0, NULL, NULL, NULL, NULL);

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
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '岗位' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_job
-- ----------------------------
INSERT INTO `sys_job` VALUES (1, '监管', b'1', 1, 'admin', 'snowball', 1683010740, 1773312937);
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
                            `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
                            `log_type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '结果',
                            `method` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '方法',
                            `params` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '入参',
                            `request_ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求ip',
                            `time` bigint NULL DEFAULT NULL COMMENT '耗时',
                            `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名',
                            `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '地址信息',
                            `browser` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'user_agent',
                            `exception_detail` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '异常详情',
                            `create_time` bigint NULL DEFAULT NULL COMMENT '创建时间',
                            PRIMARY KEY (`log_id`) USING BTREE,
                            INDEX `log_create_time_index`(`create_time` ASC) USING BTREE,
                            INDEX `inx_log_type`(`log_type` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 28 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统日志' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_log
-- ----------------------------
INSERT INTO `sys_log` VALUES (21, '岗位管理:保存岗位', 'SUCCESS', 'public com.ddf.boot.common.api.model.common.response.ResponseData<java.lang.Boolean> com.ddf.boot.capableadmin.controller.sys.SysJobController.persist(com.ddf.boot.capableadmin.model.request.sys.SysJobCreateRequest)', '{\"request\":{\"jobId\":1,\"name\":\"监管\",\"enabled\":false,\"sort\":1}}', '0:0:0:0:0:0:0:1', 27, 'snowball', 'IANA保留地址', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36 Edg/145.0.0.0', NULL, 1773312695089);
INSERT INTO `sys_log` VALUES (22, '岗位管理:保存岗位', 'SUCCESS', 'public com.ddf.boot.common.api.model.common.response.ResponseData<java.lang.Boolean> com.ddf.boot.capableadmin.controller.sys.SysJobController.persist(com.ddf.boot.capableadmin.model.request.sys.SysJobCreateRequest)', '{\"request\":{\"jobId\":1,\"name\":\"监管\",\"enabled\":false,\"sort\":1}}', '0:0:0:0:0:0:0:1', 31, 'snowball', 'IANA保留地址', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36 Edg/145.0.0.0', NULL, 1773312934163);
INSERT INTO `sys_log` VALUES (23, '岗位管理:保存岗位', 'SUCCESS', 'public com.ddf.boot.common.api.model.common.response.ResponseData<java.lang.Boolean> com.ddf.boot.capableadmin.controller.sys.SysJobController.persist(com.ddf.boot.capableadmin.model.request.sys.SysJobCreateRequest)', '{\"request\":{\"jobId\":1,\"name\":\"监管\",\"enabled\":true,\"sort\":1}}', '0:0:0:0:0:0:0:1', 21, 'snowball', 'IANA保留地址', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36 Edg/145.0.0.0', NULL, 1773312937573);
INSERT INTO `sys_log` VALUES (24, '字典管理:保存字典', 'SUCCESS', 'public com.ddf.boot.common.api.model.common.response.ResponseData<java.lang.Boolean> com.ddf.boot.capableadmin.controller.sys.SysDictController.persist(com.ddf.boot.capableadmin.model.request.sys.SysDictPersistRequest)', '{\"request\":{\"dictId\":1,\"name\":\"启用禁用标识\",\"dictCode\":\"enabled\",\"description\":\"通用表述启用禁用的标识\"}}', '0:0:0:0:0:0:0:1', 20, 'snowball', 'IANA保留地址', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36 Edg/145.0.0.0', NULL, 1773313415307);
INSERT INTO `sys_log` VALUES (25, '字典明细管理:保存字典明细', 'SUCCESS', 'public com.ddf.boot.common.api.model.common.response.ResponseData<java.lang.Boolean> com.ddf.boot.capableadmin.controller.sys.SysDictDetailController.persist(com.ddf.boot.capableadmin.model.request.sys.SysDictDetailPersistRequest)', '{\"request\":{\"detailId\":2,\"dictId\":1,\"label\":\"禁用\",\"value\":\"0\",\"dictSort\":0}}', '0:0:0:0:0:0:0:1', 10, 'snowball', 'IANA保留地址', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36 Edg/145.0.0.0', NULL, 1773313425285);
INSERT INTO `sys_log` VALUES (26, '字典明细管理:保存字典明细', 'SUCCESS', 'public com.ddf.boot.common.api.model.common.response.ResponseData<java.lang.Boolean> com.ddf.boot.capableadmin.controller.sys.SysDictDetailController.persist(com.ddf.boot.capableadmin.model.request.sys.SysDictDetailPersistRequest)', '{\"request\":{\"detailId\":2,\"dictId\":1,\"label\":\"禁用\",\"value\":\"0\",\"dictSort\":0}}', '0:0:0:0:0:0:0:1', 5, 'snowball', 'IANA保留地址', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36 Edg/145.0.0.0', NULL, 1773313436455);
INSERT INTO `sys_log` VALUES (27, '字典明细管理:保存字典明细', 'SUCCESS', 'public com.ddf.boot.common.api.model.common.response.ResponseData<java.lang.Boolean> com.ddf.boot.capableadmin.controller.sys.SysDictDetailController.persist(com.ddf.boot.capableadmin.model.request.sys.SysDictDetailPersistRequest)', '{\"request\":{\"detailId\":2,\"dictId\":1,\"detailCode\":\"disabled\",\"label\":\"禁用\",\"value\":\"0\",\"dictSort\":0}}', '0:0:0:0:0:0:0:1', 160, 'snowball', 'IANA保留地址', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36 Edg/145.0.0.0', NULL, 1773313512643);
INSERT INTO `sys_log` VALUES (28, '字典明细管理:保存字典明细', 'SUCCESS', 'public com.ddf.boot.common.api.model.common.response.ResponseData<java.lang.Boolean> com.ddf.boot.capableadmin.controller.sys.SysDictDetailController.persist(com.ddf.boot.capableadmin.model.request.sys.SysDictDetailPersistRequest)', '{\"request\":{\"detailId\":1,\"dictId\":1,\"detailCode\":\"enabled\",\"label\":\"启用\",\"value\":\"1\",\"dictSort\":1}}', '0:0:0:0:0:0:0:1', 21, 'snowball', 'IANA保留地址', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36 Edg/145.0.0.0', NULL, 1773313517724);

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
                             `menu_id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
                             `pid` bigint NOT NULL DEFAULT 0 COMMENT '上级菜单ID，默认0为一级节点',
                             `type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单类型，catalog：目录；menu：菜单；embedded： 内嵌；link：外链 ；button： 按钮；',
                             `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单名称，一般是前端使用给程序用的，  如Vue Router 路由标识 ，前端用 name 做路由跳转 ，router.push({ name: \'SystemUser\' })',
                             `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单标题，给人看的，即界面上显示的菜单标题',
                             `icon` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单图标',
                             `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '，即显示在浏览器地址栏上的路径，实际这个路径对应的显示内容则要看component组件',
                             `active_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '一般都是path, 当详情页不在左侧菜单中显示，但需要高亮父菜单时使用。左侧菜单，用户管理 (path: /system/user, activePath: /system/user/***)，访问 /system/user/100 时：\r\n- path 是 /system/user/100，匹配不到菜单\r\n- 但 activePath 是 /system/user/***，能匹配上\r\n- 所以\"用户管理\"菜单保持高亮',
                             `component` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '前端组件，为实际前端路由组件。对应view注册的组件地址，如前端静态路由写法component: () => import(\'#/views/system/user/list.vue\'),常用后端返回\"component\": \"/system/user/list\"',
                             `sort` int NOT NULL DEFAULT 0 COMMENT '排序',
                             `enabled` bit(1) NOT NULL DEFAULT b'1' COMMENT '是否启用',
                             `meta` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单元数据，介于菜单可以配置的东西太多，且每个前端都不一样，用这个大json存储，前端要用啥自己存，后端只负责存储。如菜单的图标，是否隐藏，是否缓存，是否外链，是否固定标签页等等等等，各种自定义的用于控制界面表现的参数全放到这个大json里',
                             `permission` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '权限',
                             `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建者',
                             `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新者',
                             `create_time` bigint NULL DEFAULT NULL COMMENT '创建日期',
                             `update_time` bigint NULL DEFAULT NULL COMMENT '更新时间',
                             `sub_count` int NOT NULL DEFAULT 0 COMMENT '子节点数量',
                             PRIMARY KEY (`menu_id`) USING BTREE,
                             INDEX `INX_pid`(`pid` ASC) USING BTREE,
                             INDEX `UK_name`(`name` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 804200 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统菜单' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (800000, 0, 'catalog', 'Dashboard', 'Dashboard', 'lucide:layout-dashboard', '/dashboard', NULL, NULL, 0, b'1', '{\"icon\":\"lucide:layout-dashboard\",\"order\":-1,\"title\":\"page.dashboard.title\"}', '', NULL, NULL, 1773224290, 1773224290, 2);
INSERT INTO `sys_menu` VALUES (800001, 800000, 'menu', 'Analytics', 'Analytics', 'lucide:area-chart', '/analytics', NULL, '/dashboard/analytics/index', 1, b'1', '{\"affixTab\":true,\"icon\":\"lucide:area-chart\",\"keepAlive\":true,\"title\":\"page.dashboard.analytics\"}', '', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (800002, 800000, 'menu', 'Workspace', 'Workspace', 'carbon:workspace', '/workspace', NULL, '/dashboard/workspace/index', 2, b'1', '{\"icon\":\"carbon:workspace\",\"title\":\"page.dashboard.workspace\"}', '', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (801000, 0, 'catalog', 'Examples', 'Examples', 'ion:layers-outline', '/examples', NULL, NULL, 1000, b'1', '{\"icon\":\"ion:layers-outline\",\"keepAlive\":true,\"order\":1000,\"title\":\"examples.title\"}', '', NULL, NULL, 1773224290, 1773224290, 14);
INSERT INTO `sys_menu` VALUES (801100, 801000, 'catalog', 'FormExample', 'Form', 'mdi:form-select', '/examples/form', NULL, NULL, 1, b'1', '{\"icon\":\"mdi:form-select\",\"title\":\"examples.form.title\"}', '', NULL, NULL, 1773224290, 1773224290, 9);
INSERT INTO `sys_menu` VALUES (801101, 801100, 'menu', 'FormBasicExample', 'Form Basic', NULL, '/examples/form/basic', NULL, '/examples/form/basic', 1, b'1', '{\"title\":\"examples.form.basic\"}', '', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (801102, 801100, 'menu', 'FormQueryExample', 'Form Query', NULL, '/examples/form/query', NULL, '/examples/form/query', 2, b'1', '{\"title\":\"examples.form.query\"}', '', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (801103, 801100, 'menu', 'FormRulesExample', 'Form Rules', NULL, '/examples/form/rules', NULL, '/examples/form/rules', 3, b'1', '{\"title\":\"examples.form.rules\"}', '', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (801104, 801100, 'menu', 'FormDynamicExample', 'Form Dynamic', NULL, '/examples/form/dynamic', NULL, '/examples/form/dynamic', 4, b'1', '{\"title\":\"examples.form.dynamic\"}', '', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (801105, 801100, 'menu', 'FormLayoutExample', 'Form Layout', NULL, '/examples/form/custom-layout', NULL, '/examples/form/custom-layout', 5, b'1', '{\"title\":\"examples.form.layout\"}', '', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (801106, 801100, 'menu', 'FormCustomExample', 'Form Custom', NULL, '/examples/form/custom', NULL, '/examples/form/custom', 6, b'1', '{\"title\":\"examples.form.custom\"}', '', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (801107, 801100, 'menu', 'FormApiExample', 'Form Api', NULL, '/examples/form/api', NULL, '/examples/form/api', 7, b'1', '{\"title\":\"examples.form.api\"}', '', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (801108, 801100, 'menu', 'FormMergeExample', 'Form Merge', NULL, '/examples/form/merge', NULL, '/examples/form/merge', 8, b'1', '{\"title\":\"examples.form.merge\"}', '', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (801109, 801100, 'menu', 'FormScrollToErrorExample', 'Form Scroll To Error', NULL, '/examples/form/scroll-to-error-test', NULL, '/examples/form/scroll-to-error-test', 9, b'1', '{\"title\":\"examples.form.scrollToError\"}', '', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (801200, 801000, 'catalog', 'VxeTableExample', 'Vxe Table', 'lucide:table', '/examples/vxe-table', NULL, NULL, 2, b'1', '{\"icon\":\"lucide:table\",\"title\":\"examples.vxeTable.title\"}', '', NULL, NULL, 1773224290, 1773224290, 9);
INSERT INTO `sys_menu` VALUES (801201, 801200, 'menu', 'VxeTableBasicExample', 'Vxe Table Basic', NULL, '/examples/vxe-table/basic', NULL, '/examples/vxe-table/basic', 1, b'1', '{\"title\":\"examples.vxeTable.basic\"}', '', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (801202, 801200, 'menu', 'VxeTableRemoteExample', 'Vxe Table Remote', NULL, '/examples/vxe-table/remote', NULL, '/examples/vxe-table/remote', 2, b'1', '{\"title\":\"examples.vxeTable.remote\"}', '', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (801203, 801200, 'menu', 'VxeTableTreeExample', 'Vxe Table Tree', NULL, '/examples/vxe-table/tree', NULL, '/examples/vxe-table/tree', 3, b'1', '{\"title\":\"examples.vxeTable.tree\"}', '', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (801204, 801200, 'menu', 'VxeTableFixedExample', 'Vxe Table Fixed', NULL, '/examples/vxe-table/fixed', NULL, '/examples/vxe-table/fixed', 4, b'1', '{\"title\":\"examples.vxeTable.fixed\"}', '', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (801205, 801200, 'menu', 'VxeTableCustomCellExample', 'Vxe Table Custom Cell', NULL, '/examples/vxe-table/custom-cell', NULL, '/examples/vxe-table/custom-cell', 5, b'1', '{\"title\":\"examples.vxeTable.custom-cell\"}', '', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (801206, 801200, 'menu', 'VxeTableFormExample', 'Vxe Table Form', NULL, '/examples/vxe-table/form', NULL, '/examples/vxe-table/form', 6, b'1', '{\"title\":\"examples.vxeTable.form\"}', '', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (801207, 801200, 'menu', 'VxeTableEditCellExample', 'Vxe Table Edit Cell', NULL, '/examples/vxe-table/edit-cell', NULL, '/examples/vxe-table/edit-cell', 7, b'1', '{\"title\":\"examples.vxeTable.editCell\"}', '', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (801208, 801200, 'menu', 'VxeTableEditRowExample', 'Vxe Table Edit Row', NULL, '/examples/vxe-table/edit-row', NULL, '/examples/vxe-table/edit-row', 8, b'1', '{\"title\":\"examples.vxeTable.editRow\"}', '', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (801209, 801200, 'menu', 'VxeTableVirtualExample', 'Vxe Table Virtual', NULL, '/examples/vxe-table/virtual', NULL, '/examples/vxe-table/virtual', 9, b'1', '{\"title\":\"examples.vxeTable.virtual\"}', '', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (801300, 801000, 'catalog', 'CaptchaExample', 'Captcha', 'logos:recaptcha', '/examples/captcha', NULL, NULL, 3, b'1', '{\"icon\":\"logos:recaptcha\",\"title\":\"examples.captcha.title\"}', '', NULL, NULL, 1773224290, 1773224290, 4);
INSERT INTO `sys_menu` VALUES (801301, 801300, 'menu', 'DragVerifyExample', 'Slider Captcha', NULL, '/examples/captcha/slider', NULL, '/examples/captcha/slider-captcha', 1, b'1', '{\"title\":\"examples.captcha.sliderCaptcha\"}', '', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (801302, 801300, 'menu', 'RotateVerifyExample', 'Rotate Captcha', NULL, '/examples/captcha/slider-rotate', NULL, '/examples/captcha/slider-rotate-captcha', 2, b'1', '{\"title\":\"examples.captcha.sliderRotateCaptcha\"}', '', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (801303, 801300, 'menu', 'TranslateVerifyExample', 'Translate Captcha', NULL, '/examples/captcha/slider-translate', NULL, '/examples/captcha/slider-translate-captcha', 3, b'1', '{\"title\":\"examples.captcha.sliderTranslateCaptcha\"}', '', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (801304, 801300, 'menu', 'CaptchaPointSelectionExample', 'Point Selection Captcha', NULL, '/examples/captcha/point-selection', NULL, '/examples/captcha/point-selection-captcha', 4, b'1', '{\"title\":\"examples.captcha.pointSelection\"}', '', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (801400, 801000, 'menu', 'ModalExample', 'Modal', 'system-uicons:window-content', '/examples/modal', NULL, '/examples/modal/index', 4, b'1', '{\"icon\":\"system-uicons:window-content\",\"keepAlive\":true,\"title\":\"examples.modal.title\"}', '', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (801401, 801000, 'menu', 'DrawerExample', 'Drawer', 'iconoir:drawer', '/examples/drawer', NULL, '/examples/drawer/index', 5, b'1', '{\"icon\":\"iconoir:drawer\",\"keepAlive\":true,\"title\":\"examples.drawer.title\"}', '', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (801402, 801000, 'menu', 'EllipsisExample', 'Ellipsis', 'ion:ellipsis-horizontal', '/examples/ellipsis', NULL, '/examples/ellipsis/index', 6, b'1', '{\"icon\":\"ion:ellipsis-horizontal\",\"title\":\"examples.ellipsis.title\"}', '', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (801403, 801000, 'menu', 'VueResizeDemo', 'Resize', 'material-symbols:resize', '/demos/resize/basic', NULL, '/examples/resize/basic', 7, b'1', '{\"icon\":\"material-symbols:resize\",\"title\":\"examples.resize.title\"}', '', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (801404, 801000, 'menu', 'ColPageDemo', 'Col Page', 'material-symbols:horizontal-distribute', '/examples/layout/col-page', NULL, '/examples/layout/col-page', 8, b'1', '{\"badge\":\"Alpha\",\"badgeVariants\":\"destructive\",\"icon\":\"material-symbols:horizontal-distribute\",\"title\":\"examples.layout.col-page\"}', '', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (801405, 801000, 'menu', 'TippyDemo', 'Tippy', 'mdi:message-settings-outline', '/examples/tippy', NULL, '/examples/tippy/index', 9, b'1', '{\"icon\":\"mdi:message-settings-outline\",\"title\":\"Tippy\"}', '', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (801406, 801000, 'menu', 'JsonViewer', 'JsonViewer', 'tabler:json', '/examples/json-viewer', NULL, '/examples/json-viewer/index', 10, b'1', '{\"icon\":\"tabler:json\",\"title\":\"JsonViewer\"}', '', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (801407, 801000, 'menu', 'Motion', 'Motion', 'mdi:animation-play', '/examples/motion', NULL, '/examples/motion/index', 11, b'1', '{\"icon\":\"mdi:animation-play\",\"title\":\"Motion\"}', '', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (801408, 801000, 'menu', 'CountTo', 'CountTo', 'mdi:animation-play', '/examples/count-to', NULL, '/examples/count-to/index', 12, b'1', '{\"icon\":\"mdi:animation-play\",\"title\":\"CountTo\"}', '', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (801409, 801000, 'menu', 'Loading', 'Loading', 'mdi:circle-double', '/examples/loading', NULL, '/examples/loading/index', 13, b'1', '{\"icon\":\"mdi:circle-double\",\"title\":\"Loading\"}', '', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (801410, 801000, 'menu', 'ButtonGroup', 'Button Group', 'mdi:check-circle', '/examples/button-group', NULL, '/examples/button-group/index', 14, b'1', '{\"icon\":\"mdi:check-circle\",\"title\":\"examples.button-group.title\"}', '', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (801411, 801000, 'menu', 'ContextMenu', 'Context Menu', 'mdi:menu', '/examples/context-menu', NULL, '/examples/context-menu/index', 15, b'1', '{\"icon\":\"mdi:menu\",\"title\":\"examples.function.contentMenu\"}', '', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (801412, 801000, 'menu', 'CropperDemo', 'Cropper', 'mdi:crop', '/examples/cropper', NULL, '/examples/cropper/index', 16, b'1', '{\"icon\":\"mdi:crop\",\"title\":\"examples.cropper.title\"}', '', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (802000, 0, 'catalog', 'Demos', 'Demos', 'ic:baseline-view-in-ar', '/demos', NULL, NULL, 1001, b'1', '{\"icon\":\"ic:baseline-view-in-ar\",\"keepAlive\":true,\"order\":1000,\"title\":\"demos.title\"}', '', NULL, NULL, 1773224290, 1773224290, 8);
INSERT INTO `sys_menu` VALUES (802100, 802000, 'catalog', 'AccessDemos', 'Access Demos', 'mdi:shield-key-outline', '/demos/access', NULL, NULL, 1, b'1', '{\"icon\":\"mdi:shield-key-outline\",\"title\":\"demos.access.frontendPermissions\"}', '', NULL, NULL, 1773224290, 1773224290, 6);
INSERT INTO `sys_menu` VALUES (802101, 802100, 'menu', 'AccessPageControlDemo', 'Page Control', 'mdi:page-previous-outline', '/demos/access/page-control', NULL, '/demos/access/index', 1, b'1', '{\"icon\":\"mdi:page-previous-outline\",\"title\":\"demos.access.pageAccess\"}', '', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (802102, 802100, 'menu', 'AccessButtonControlDemo', 'Button Control', 'mdi:button-cursor', '/demos/access/button-control', NULL, '/demos/access/button-control', 2, b'1', '{\"icon\":\"mdi:button-cursor\",\"title\":\"demos.access.buttonControl\"}', '', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (802103, 802100, 'menu', 'AccessMenuVisible403Demo', 'Menu Visible 403', 'mdi:button-cursor', '/demos/access/menu-visible-403', NULL, '/demos/access/menu-visible-403', 3, b'1', '{\"authority\":[\"no-body\"],\"icon\":\"mdi:button-cursor\",\"menuVisibleWithForbidden\":true,\"title\":\"demos.access.menuVisible403\"}', '', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (802104, 802100, 'menu', 'AccessSuperVisibleDemo', 'Super Visible', 'mdi:button-cursor', '/demos/access/super-visible', NULL, '/demos/access/super-visible', 4, b'1', '{\"authority\":[\"super\"],\"icon\":\"mdi:button-cursor\",\"title\":\"demos.access.superVisible\"}', '', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (802105, 802100, 'menu', 'AccessAdminVisibleDemo', 'Admin Visible', 'mdi:button-cursor', '/demos/access/admin-visible', NULL, '/demos/access/admin-visible', 5, b'1', '{\"authority\":[\"admin\"],\"icon\":\"mdi:button-cursor\",\"title\":\"demos.access.adminVisible\"}', '', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (802106, 802100, 'menu', 'AccessUserVisibleDemo', 'User Visible', 'mdi:button-cursor', '/demos/access/user-visible', NULL, '/demos/access/user-visible', 6, b'1', '{\"authority\":[\"user\"],\"icon\":\"mdi:button-cursor\",\"title\":\"demos.access.userVisible\"}', '', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (802200, 802000, 'catalog', 'FeaturesDemos', 'Feature Demos', 'mdi:feature-highlight', '/demos/features', NULL, NULL, 2, b'1', '{\"icon\":\"mdi:feature-highlight\",\"title\":\"demos.features.title\"}', '', NULL, NULL, 1773224290, 1773224290, 11);
INSERT INTO `sys_menu` VALUES (802201, 802200, 'menu', 'LoginExpiredDemo', 'Login Expired', 'mdi:encryption-expiration', '/demos/features/login-expired', NULL, '/demos/features/login-expired/index', 1, b'1', '{\"icon\":\"mdi:encryption-expiration\",\"title\":\"demos.features.loginExpired\"}', '', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (802202, 802200, 'menu', 'IconsDemo', 'Icons', 'lucide:annoyed', '/demos/features/icons', NULL, '/demos/features/icons/index', 2, b'1', '{\"icon\":\"lucide:annoyed\",\"title\":\"demos.features.icons\"}', '', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (802203, 802200, 'menu', 'WatermarkDemo', 'Watermark', 'lucide:tags', '/demos/features/watermark', NULL, '/demos/features/watermark/index', 3, b'1', '{\"icon\":\"lucide:tags\",\"title\":\"demos.features.watermark\"}', '', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (802204, 802200, 'menu', 'FeatureTabsDemo', 'Tabs', 'lucide:app-window', '/demos/features/tabs', NULL, '/demos/features/tabs/index', 4, b'1', '{\"icon\":\"lucide:app-window\",\"title\":\"demos.features.tabs\"}', '', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (802205, 802200, 'menu', 'FeatureTabDetailDemo', 'Tab Detail', NULL, '/demos/features/tabs/detail/:id', '/demos/features/tabs', '/demos/features/tabs/tab-detail', 5, b'1', '{\"activePath\":\"/demos/features/tabs\",\"hideInMenu\":true,\"maxNumOfOpenTab\":3,\"title\":\"demos.features.tabDetail\"}', '', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (802206, 802200, 'catalog', 'HideChildrenInMenuParentDemo', 'Hide Children In Menu', 'ic:round-menu', '/demos/features/hide-menu-children', NULL, NULL, 6, b'1', '{\"hideChildrenInMenu\":true,\"icon\":\"ic:round-menu\",\"title\":\"demos.features.hideChildrenInMenu\"}', '', NULL, NULL, 1773224290, 1773224290, 2);
INSERT INTO `sys_menu` VALUES (802207, 802206, 'menu', 'HideChildrenInMenuDemo', 'Hide Children In Menu', NULL, '', NULL, '/demos/features/hide-menu-children/parent', 1, b'1', '{\"title\":\"demos.features.hideChildrenInMenu\"}', '', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (802208, 802206, 'menu', 'HideChildrenInMenuChildrenDemo', 'Hide Children In Menu Child', NULL, '/demos/features/hide-menu-children/children', '/demos/features/hide-menu-children', '/demos/features/hide-menu-children/children', 2, b'1', '{\"activePath\":\"/demos/features/hide-menu-children\",\"title\":\"demos.features.hideChildrenInMenu\"}', '', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (802209, 802200, 'menu', 'FullScreenDemo', 'Full Screen', 'lucide:fullscreen', '/demos/features/full-screen', NULL, '/demos/features/full-screen/index', 7, b'1', '{\"icon\":\"lucide:fullscreen\",\"title\":\"demos.features.fullScreen\"}', '', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (802210, 802200, 'menu', 'FileDownloadDemo', 'File Download', 'lucide:hard-drive-download', '/demos/features/file-download', NULL, '/demos/features/file-download/index', 8, b'1', '{\"icon\":\"lucide:hard-drive-download\",\"title\":\"demos.features.fileDownload\"}', '', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (802211, 802200, 'menu', 'ClipboardDemo', 'Clipboard', 'lucide:copy', '/demos/features/clipboard', NULL, '/demos/features/clipboard/index', 9, b'1', '{\"icon\":\"lucide:copy\",\"title\":\"demos.features.clipboard\"}', '', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (802212, 802200, 'menu', 'MenuQueryDemo', 'Menu Query', 'lucide:curly-braces', '/demos/menu-query', NULL, '/demos/features/menu-query/index', 10, b'1', '{\"icon\":\"lucide:curly-braces\",\"query\":{\"id\":1},\"title\":\"demos.features.menuWithQuery\"}', '', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (802213, 802200, 'menu', 'NewWindowDemo', 'New Window', 'lucide:app-window', '/demos/new-window', NULL, '/demos/features/new-window/index', 11, b'1', '{\"icon\":\"lucide:app-window\",\"openInNewWindow\":true,\"title\":\"demos.features.openInNewWindow\"}', '', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (802214, 802200, 'menu', 'VueQueryDemo', 'Tanstack Query', 'lucide:git-pull-request-arrow', '/demos/features/vue-query', NULL, '/demos/features/vue-query/index', 12, b'1', '{\"icon\":\"lucide:git-pull-request-arrow\",\"title\":\"Tanstack Query\"}', '', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (802215, 802200, 'menu', 'RequestParamsSerializerDemo', 'Request Params Serializer', 'lucide:git-pull-request-arrow', '/demos/features/request-params-serializer', NULL, '/demos/features/request-params-serializer/index', 13, b'1', '{\"icon\":\"lucide:git-pull-request-arrow\",\"title\":\"demos.features.requestParamsSerializer\"}', '', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (802216, 802200, 'menu', 'BigIntDemo', 'JSON BigInt', 'lucide:grape', '/demos/features/json-bigint', NULL, '/demos/features/json-bigint/index', 14, b'1', '{\"icon\":\"lucide:grape\",\"title\":\"JSON BigInt\"}', '', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (802300, 802000, 'catalog', 'BreadcrumbDemos', 'Breadcrumb', 'lucide:navigation', '/demos/breadcrumb', NULL, NULL, 3, b'1', '{\"icon\":\"lucide:navigation\",\"title\":\"demos.breadcrumb.navigation\"}', '', NULL, NULL, 1773224290, 1773224290, 3);
INSERT INTO `sys_menu` VALUES (802301, 802300, 'menu', 'BreadcrumbLateralDemo', 'Breadcrumb Lateral', 'lucide:navigation', '/demos/breadcrumb/lateral', NULL, '/demos/breadcrumb/lateral', 1, b'1', '{\"icon\":\"lucide:navigation\",\"title\":\"demos.breadcrumb.lateral\"}', '', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (802302, 802300, 'menu', 'BreadcrumbLateralDetailDemo', 'Breadcrumb Lateral Detail', NULL, '/demos/breadcrumb/lateral-detail', '/demos/breadcrumb/lateral', '/demos/breadcrumb/lateral-detail', 2, b'1', '{\"activePath\":\"/demos/breadcrumb/lateral\",\"hideInMenu\":true,\"title\":\"demos.breadcrumb.lateralDetail\"}', '', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (802303, 802300, 'catalog', 'BreadcrumbLevelDemo', 'Breadcrumb Level', 'lucide:navigation', '/demos/breadcrumb/level', NULL, NULL, 3, b'1', '{\"icon\":\"lucide:navigation\",\"title\":\"demos.breadcrumb.level\"}', '', NULL, NULL, 1773224290, 1773224290, 1);
INSERT INTO `sys_menu` VALUES (802304, 802303, 'menu', 'BreadcrumbLevelDetailDemo', 'Breadcrumb Level Detail', NULL, '/demos/breadcrumb/level/detail', NULL, '/demos/breadcrumb/level-detail', 1, b'1', '{\"title\":\"demos.breadcrumb.levelDetail\"}', '', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (802400, 802000, 'catalog', 'FallbackDemos', 'Fallback', 'mdi:lightbulb-error-outline', '/demos/fallback', NULL, NULL, 4, b'1', '{\"icon\":\"mdi:lightbulb-error-outline\",\"title\":\"demos.fallback.title\"}', '', NULL, NULL, 1773224290, 1773224290, 4);
INSERT INTO `sys_menu` VALUES (802401, 802400, 'menu', 'Fallback403Demo', '403', 'mdi:do-not-disturb-alt', '/demos/fallback/403', NULL, '/_core/fallback/forbidden', 1, b'1', '{\"icon\":\"mdi:do-not-disturb-alt\",\"title\":\"403\"}', '', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (802402, 802400, 'menu', 'Fallback404Demo', '404', 'mdi:table-off', '/demos/fallback/404', NULL, '/_core/fallback/not-found', 2, b'1', '{\"icon\":\"mdi:table-off\",\"title\":\"404\"}', '', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (802403, 802400, 'menu', 'Fallback500Demo', '500', 'mdi:server-network-off', '/demos/fallback/500', NULL, '/_core/fallback/internal-error', 3, b'1', '{\"icon\":\"mdi:server-network-off\",\"title\":\"500\"}', '', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (802404, 802400, 'menu', 'FallbackOfflineDemo', 'Offline', 'mdi:offline', '/demos/fallback/offline', NULL, '/_core/fallback/offline', 4, b'1', '{\"icon\":\"mdi:offline\",\"title\":\"ui.fallback.offline\"}', '', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (802500, 802000, 'catalog', 'BadgeDemos', 'Badge', 'lucide:circle-dot', '/demos/badge', NULL, NULL, 5, b'1', '{\"badgeType\":\"dot\",\"badgeVariants\":\"destructive\",\"icon\":\"lucide:circle-dot\",\"title\":\"demos.badge.title\"}', '', NULL, NULL, 1773224290, 1773224290, 3);
INSERT INTO `sys_menu` VALUES (802501, 802500, 'menu', 'BadgeDotDemo', 'Badge Dot', 'lucide:square-dot', '/demos/badge/dot', NULL, '/demos/badge/index', 1, b'1', '{\"badgeType\":\"dot\",\"icon\":\"lucide:square-dot\",\"title\":\"demos.badge.dot\"}', '', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (802502, 802500, 'menu', 'BadgeTextDemo', 'Badge Text', 'lucide:square-dot', '/demos/badge/text', NULL, '/demos/badge/index', 2, b'1', '{\"badge\":\"10\",\"icon\":\"lucide:square-dot\",\"title\":\"demos.badge.text\"}', '', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (802503, 802500, 'menu', 'BadgeColorDemo', 'Badge Color', 'lucide:square-dot', '/demos/badge/color', NULL, '/demos/badge/index', 3, b'1', '{\"badge\":\"Hot\",\"badgeVariants\":\"destructive\",\"icon\":\"lucide:square-dot\",\"title\":\"demos.badge.color\"}', '', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (802600, 802000, 'catalog', 'ActiveIconDemos', 'Active Icon', 'bi:radioactive', '/demos/active-icon', NULL, NULL, 6, b'1', '{\"activeIcon\":\"fluent-emoji:radioactive\",\"icon\":\"bi:radioactive\",\"title\":\"demos.activeIcon.title\"}', '', NULL, NULL, 1773224290, 1773224290, 1);
INSERT INTO `sys_menu` VALUES (802601, 802600, 'menu', 'ActiveIconDemo', 'Active Icon Child', 'bi:radioactive', '/demos/active-icon/children', NULL, '/demos/active-icon/index', 1, b'1', '{\"activeIcon\":\"fluent-emoji:radioactive\",\"icon\":\"bi:radioactive\",\"title\":\"demos.activeIcon.children\"}', '', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (802700, 802000, 'catalog', 'OutsideDemos', 'Outside', 'ic:round-settings-input-composite', '/demos/outside', NULL, NULL, 7, b'1', '{\"icon\":\"ic:round-settings-input-composite\",\"title\":\"demos.outside.title\"}', '', NULL, NULL, 1773224290, 1773224290, 2);
INSERT INTO `sys_menu` VALUES (802701, 802700, 'catalog', 'IframeDemos', 'Iframe', 'mdi:newspaper-variant-outline', '/demos/outside/iframe', NULL, NULL, 1, b'1', '{\"icon\":\"mdi:newspaper-variant-outline\",\"title\":\"demos.outside.embedded\"}', '', NULL, NULL, 1773224290, 1773224290, 2);
INSERT INTO `sys_menu` VALUES (802702, 802701, 'embedded', 'VueDocumentDemo', 'Vue', 'logos:vue', '/demos/outside/iframe/vue-document', NULL, 'IFrameView', 1, b'1', '{\"icon\":\"logos:vue\",\"iframeSrc\":\"https://cn.vuejs.org/\",\"keepAlive\":true,\"title\":\"Vue\"}', '', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (802703, 802701, 'embedded', 'TailwindcssDemo', 'Tailwindcss', 'devicon:tailwindcss', '/demos/outside/iframe/tailwindcss', NULL, 'IFrameView', 2, b'1', '{\"icon\":\"devicon:tailwindcss\",\"iframeSrc\":\"https://tailwindcss.com/\",\"title\":\"Tailwindcss\"}', '', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (802704, 802700, 'catalog', 'ExternalLinkDemos', 'External Link', 'mdi:newspaper-variant-multiple-outline', '/demos/outside/external-link', NULL, NULL, 2, b'1', '{\"icon\":\"mdi:newspaper-variant-multiple-outline\",\"title\":\"demos.outside.externalLink\"}', '', NULL, NULL, 1773224290, 1773224290, 2);
INSERT INTO `sys_menu` VALUES (802705, 802704, 'link', 'ViteDemo', 'Vite', 'logos:vitejs', '/demos/outside/external-link/vite', NULL, 'IFrameView', 1, b'1', '{\"icon\":\"logos:vitejs\",\"link\":\"https://vitejs.dev/\",\"title\":\"Vite\"}', '', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (802706, 802704, 'link', 'VueUseDemo', 'VueUse', 'logos:vueuse', '/demos/outside/external-link/vue-use', NULL, 'IFrameView', 2, b'1', '{\"icon\":\"logos:vueuse\",\"link\":\"https://vueuse.org\",\"title\":\"VueUse\"}', '', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (802800, 802000, 'catalog', 'NestedDemos', 'Nested', 'ic:round-menu', '/demos/nested', NULL, NULL, 8, b'1', '{\"icon\":\"ic:round-menu\",\"title\":\"demos.nested.title\"}', '', NULL, NULL, 1773224290, 1773224290, 3);
INSERT INTO `sys_menu` VALUES (802801, 802800, 'menu', 'Menu1Demo', 'Menu1', 'ic:round-menu', '/demos/nested/menu1', NULL, '/demos/nested/menu-1', 1, b'1', '{\"icon\":\"ic:round-menu\",\"keepAlive\":true,\"title\":\"demos.nested.menu1\"}', '', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (802802, 802800, 'catalog', 'Menu2Demo', 'Menu2', 'ic:round-menu', '/demos/nested/menu2', NULL, NULL, 2, b'1', '{\"icon\":\"ic:round-menu\",\"keepAlive\":true,\"title\":\"demos.nested.menu2\"}', '', NULL, NULL, 1773224290, 1773224290, 1);
INSERT INTO `sys_menu` VALUES (802803, 802802, 'menu', 'Menu21Demo', 'Menu2-1', 'ic:round-menu', '/demos/nested/menu2/menu2-1', NULL, '/demos/nested/menu-2-1', 1, b'1', '{\"icon\":\"ic:round-menu\",\"keepAlive\":true,\"title\":\"demos.nested.menu2_1\"}', '', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (802804, 802800, 'catalog', 'Menu3Demo', 'Menu3', 'ic:round-menu', '/demos/nested/menu3', NULL, NULL, 3, b'1', '{\"icon\":\"ic:round-menu\",\"title\":\"demos.nested.menu3\"}', '', NULL, NULL, 1773224290, 1773224290, 2);
INSERT INTO `sys_menu` VALUES (802805, 802804, 'menu', 'Menu31Demo', 'Menu3-1', 'ic:round-menu', '/demos/nested/menu3/menu3-1', NULL, '/demos/nested/menu-3-1', 1, b'1', '{\"icon\":\"ic:round-menu\",\"keepAlive\":true,\"title\":\"demos.nested.menu3_1\"}', '', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (802806, 802804, 'catalog', 'Menu32Demo', 'Menu3-2', 'ic:round-menu', '/demos/nested/menu3/menu3-2', NULL, NULL, 2, b'1', '{\"icon\":\"ic:round-menu\",\"title\":\"demos.nested.menu3_2\"}', '', NULL, NULL, 1773224290, 1773224290, 1);
INSERT INTO `sys_menu` VALUES (802807, 802806, 'menu', 'Menu321Demo', 'Menu3-2-1', 'ic:round-menu', '/demos/nested/menu3/menu3-2/menu3-2-1', NULL, '/demos/nested/menu-3-2-1', 1, b'1', '{\"icon\":\"ic:round-menu\",\"keepAlive\":true,\"title\":\"demos.nested.menu3_2_1\"}', '', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (803000, 0, 'catalog', 'System', 'System', 'ion:settings-outline', '/system', NULL, NULL, 9997, b'1', '{\"icon\":\"ion:settings-outline\",\"order\":9997,\"title\":\"system.title\"}', '', NULL, NULL, 1773224290, 1773224290, 7);
INSERT INTO `sys_menu` VALUES (803001, 803000, 'menu', 'SystemRole', 'Role', 'mdi:account-group', '/system/role', NULL, '/system/role/list', 1, b'1', '{\"icon\":\"mdi:account-group\",\"title\":\"system.role.title\"}', 'roles:list', NULL, NULL, 1773224290, 1773224290, 4);
INSERT INTO `sys_menu` VALUES (803002, 803000, 'menu', 'SystemMenu', 'Menu', 'mdi:menu', '/system/menu', NULL, '/system/menu/list', 2, b'1', '{\"icon\":\"mdi:menu\",\"title\":\"system.menu.title\"}', 'menu:list', NULL, NULL, 1773224290, 1773224290, 4);
INSERT INTO `sys_menu` VALUES (803003, 803000, 'menu', 'SystemDept', 'Dept', 'charm:organisation', '/system/dept', NULL, '/system/dept/list', 3, b'1', '{\"icon\":\"charm:organisation\",\"title\":\"system.dept.title\"}', '', NULL, NULL, 1773224290, 1773224290, 3);
INSERT INTO `sys_menu` VALUES (803004, 803000, 'menu', 'SystemUser', 'User', 'lucide:users', '/system/user', NULL, '/system/user/list', 4, b'1', '{\"icon\":\"lucide:users\",\"title\":\"system.user.title\"}', '', NULL, NULL, 1773224290, 1773224290, 4);
INSERT INTO `sys_menu` VALUES (803005, 803000, 'menu', 'SystemJob', 'Job', 'mdi:briefcase-outline', '/system/job', NULL, '/system/job/list', 5, b'1', '{\"icon\":\"mdi:briefcase-outline\",\"title\":\"system.job.title\"}', '', NULL, NULL, 1773224290, 1773224290, 2);
INSERT INTO `sys_menu` VALUES (803006, 803000, 'menu', 'SystemDict', 'Dict', 'mdi:book-open-variant-outline', '/system/dict', NULL, '/system/dict/list', 6, b'1', '{\"icon\":\"mdi:book-open-variant-outline\",\"title\":\"system.dict.title\"}', '', NULL, NULL, 1773224290, 1773224290, 6);
INSERT INTO `sys_menu` VALUES (803007, 803000, 'menu', 'SystemLog', 'Log', 'mdi:file-document-outline', '/system/log', NULL, '/system/log/list', 7, b'1', '{\"icon\":\"mdi:file-document-outline\",\"title\":\"system.log.title\"}', '', NULL, NULL, 1773224290, 1773224290, 1);
INSERT INTO `sys_menu` VALUES (803011, 803001, 'button', 'SystemRoleCreate', 'Create Role', NULL, '', NULL, NULL, 1, b'1', '{\"title\":\"common.create\"}', 'roles:add', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (803012, 803001, 'button', 'SystemRoleEdit', 'Edit Role', NULL, '', NULL, NULL, 2, b'1', '{\"title\":\"common.edit\"}', 'roles:edit', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (803013, 803001, 'button', 'SystemRoleEnable', 'Enable Role', NULL, '', NULL, NULL, 3, b'1', '{\"title\":\"common.update\"}', 'roles:persist', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (803014, 803001, 'button', 'SystemRoleDelete', 'Delete Role', NULL, '', NULL, NULL, 4, b'1', '{\"title\":\"common.delete\"}', 'roles:del', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (803021, 803002, 'button', 'SystemMenuCreate', 'Create Menu', NULL, '', NULL, NULL, 1, b'1', '{\"title\":\"common.create\"}', 'menu:add', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (803022, 803002, 'button', 'SystemMenuEdit', 'Edit Menu', NULL, '', NULL, NULL, 2, b'1', '{\"title\":\"common.edit\"}', 'menu:edit', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (803023, 803002, 'button', 'SystemMenuDelete', 'Delete Menu', NULL, '', NULL, NULL, 3, b'1', '{\"title\":\"common.delete\"}', 'menu:del', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (803024, 803002, 'button', 'SystemMenuSync', 'Sync Menu', NULL, '', NULL, NULL, 4, b'1', '{\"title\":\"common.update\"}', 'menu:sync', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (803031, 803003, 'button', 'SystemDeptCreate', 'Create Dept', NULL, '', NULL, NULL, 1, b'1', '{\"title\":\"common.create\"}', 'dept:add', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (803032, 803003, 'button', 'SystemDeptEdit', 'Edit Dept', NULL, '', NULL, NULL, 2, b'1', '{\"title\":\"common.edit\"}', 'dept:edit', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (803033, 803003, 'button', 'SystemDeptDelete', 'Delete Dept', NULL, '', NULL, NULL, 3, b'1', '{\"title\":\"common.delete\"}', 'dept:del', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (803041, 803004, 'button', 'SystemUserCreate', 'Create User', NULL, '', NULL, NULL, 1, b'1', '{\"title\":\"common.create\"}', 'user:add', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (803042, 803004, 'button', 'SystemUserEdit', 'Edit User', NULL, '', NULL, NULL, 2, b'1', '{\"title\":\"common.edit\"}', 'user:edit', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (803043, 803004, 'button', 'SystemUserResetPassword', 'Reset Password', NULL, '', NULL, NULL, 3, b'1', '{\"title\":\"Reset Password\"}', 'user:reset-password', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (803044, 803004, 'button', 'SystemUserDelete', 'Delete User', NULL, '', NULL, NULL, 4, b'1', '{\"title\":\"common.delete\"}', 'user:del', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (803051, 803005, 'button', 'SystemJobCreate', 'Create Job', NULL, '', NULL, NULL, 1, b'1', '{\"title\":\"common.create\"}', 'job:add', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (803052, 803005, 'button', 'SystemJobDelete', 'Delete Job', NULL, '', NULL, NULL, 2, b'1', '{\"title\":\"common.delete\"}', 'job:del', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (803061, 803006, 'button', 'SystemDictCreate', 'Create Dict', NULL, '', NULL, NULL, 1, b'1', '{\"title\":\"common.create\"}', 'dict:add', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (803062, 803006, 'button', 'SystemDictEdit', 'Edit Dict', NULL, '', NULL, NULL, 2, b'1', '{\"title\":\"common.edit\"}', 'dict:edit', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (803063, 803006, 'button', 'SystemDictDelete', 'Delete Dict', NULL, '', NULL, NULL, 3, b'1', '{\"title\":\"common.delete\"}', 'dict:del', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (803064, 803006, 'button', 'SystemDictDetailCreate', 'Create Dict Detail', NULL, '', NULL, NULL, 4, b'1', '{\"title\":\"common.create\"}', 'dict-detail:add', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (803065, 803006, 'button', 'SystemDictDetailEdit', 'Edit Dict Detail', NULL, '', NULL, NULL, 5, b'1', '{\"title\":\"common.edit\"}', 'dict-detail:edit', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (803066, 803006, 'button', 'SystemDictDetailDelete', 'Delete Dict Detail', NULL, '', NULL, NULL, 6, b'1', '{\"title\":\"common.delete\"}', 'dict-detail:del', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (803071, 803007, 'button', 'SystemLogDelete', 'Delete Log', NULL, '', NULL, NULL, 1, b'1', '{\"title\":\"common.delete\"}', 'log:del', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (804000, 0, 'catalog', 'VbenProject', 'Project', NULL, '/vben-admin', NULL, NULL, 9998, b'1', '{\"badgeType\":\"dot\",\"icon\":\"https://unpkg.com/@vbenjs/static-source@0.1.7/source/logo-v1.webp\",\"order\":9998,\"title\":\"demos.vben.title\"}', '', NULL, NULL, 1773224290, 1773224290, 7);
INSERT INTO `sys_menu` VALUES (804001, 804000, 'link', 'VbenDocument', 'Document', 'lucide:book-open-text', '/vben-admin/document', NULL, 'IFrameView', 1, b'1', '{\"icon\":\"lucide:book-open-text\",\"link\":\"https://doc.vben.pro\",\"title\":\"demos.vben.document\"}', '', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (804002, 804000, 'link', 'VbenGithub', 'Github', 'mdi:github', '/vben-admin/github', NULL, 'IFrameView', 2, b'1', '{\"icon\":\"mdi:github\",\"link\":\"https://github.com/vbenjs/vue-vben-admin\",\"title\":\"Github\"}', '', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (804003, 804000, 'link', 'VbenAntdv', 'Ant Design Vue', 'simple-icons:antdesign', '/vben-admin/antdv', NULL, 'IFrameView', 3, b'1', '{\"badgeType\":\"dot\",\"icon\":\"simple-icons:antdesign\",\"link\":\"https://antd.vben.pro\",\"title\":\"demos.vben.antdv\"}', '', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (804004, 804000, 'link', 'VbenAntdVNext', 'Antdv Next', 'simple-icons:antdesign', '/vben-admin/antdv-next', NULL, 'IFrameView', 4, b'1', '{\"badgeType\":\"dot\",\"icon\":\"simple-icons:antdesign\",\"link\":\"https://antdv-next.vben.pro\",\"title\":\"demos.vben.antdv-next\"}', '', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (804005, 804000, 'link', 'VbenNaive', 'Naive UI', 'logos:naiveui', '/vben-admin/naive', NULL, 'IFrameView', 5, b'1', '{\"badgeType\":\"dot\",\"icon\":\"logos:naiveui\",\"link\":\"https://naive.vben.pro\",\"title\":\"demos.vben.naive-ui\"}', '', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (804006, 804000, 'link', 'VbenElementPlus', 'Element Plus', 'logos:element', '/vben-admin/ele', NULL, 'IFrameView', 6, b'1', '{\"badgeType\":\"dot\",\"icon\":\"logos:element\",\"link\":\"https://ele.vben.pro\",\"title\":\"demos.vben.element-plus\"}', '', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (804007, 804000, 'link', 'VbenTDesign', 'TDesign', 'mdi:alpha-t-circle-outline', '/vben-admin/tdesign', NULL, 'IFrameView', 7, b'1', '{\"badgeType\":\"dot\",\"icon\":\"mdi:alpha-t-circle-outline\",\"link\":\"https://tdesign.vben.pro\",\"title\":\"demos.vben.tdesign\"}', '', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (804100, 0, 'menu', 'VbenAbout', 'About', 'lucide:copyright', '/vben-admin/about', NULL, '/_core/about/index', 9999, b'1', '{\"icon\":\"lucide:copyright\",\"order\":9999,\"title\":\"demos.vben.about\"}', '', NULL, NULL, 1773224290, 1773224290, 0);
INSERT INTO `sys_menu` VALUES (804200, 0, 'menu', 'Profile', 'Profile', 'lucide:user', '/profile', NULL, '/_core/profile/index', 10000, b'1', '{\"hideInMenu\":true,\"icon\":\"lucide:user\",\"title\":\"page.auth.profile\"}', '', NULL, NULL, 1773224290, 1773224290, 0);

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
                             `enabled` bit(1) NOT NULL DEFAULT b'1' COMMENT '是否启用， 0否1是',
                             `menu_ids` varchar(3000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '拥有的菜单id集合，逗号分隔，程序自行处理，不需要外部传参',
                             PRIMARY KEY (`role_id`) USING BTREE,
                             UNIQUE INDEX `UK_name`(`name` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '超级管理员', 0, '超级管理员', '', 'admin', 'admin', 1683010740, 1683010740, 0, b'1', b'1', '2,804100,9,10,800000,802304,800001,800002,801304,801300,801301,801302,801303,802600,802601,802104,802105,802106,802100,802101,802102,802103,801100,801101,801102,801103,801108,801109,801104,801105,801106,801107,804200,102,802404,802400,802401,802402,802403,801404,801405,801406,801407,801400,801401,801402,801403,802700,802701,802702,802703,901,20101,902,20102,20103,801412,801408,801409,801410,801411,802204,802205,802206,802207,802200,802201,802202,802203,802704,802705,802706,802216,802212,804004,802213,804005,802214,804006,802215,804007,802208,804000,802209,804001,802210,804002,802211,804003,803004,803005,803006,803007,801208,803000,801209,803001,803002,803003,801204,801205,801206,801207,801200,801201,801202,801203,803021,803022,803023,802500,803012,201,802501,803013,202,802502,803014,802503,803011,803032,803033,803031,802000,803024,803052,801000,803051,803044,20201,20202,20203,803041,803042,803043,802300,802301,802302,802303,803071,803064,803065,803066,802804,802805,803061,802806,803062,802807,803063,802800,802801,802802,802803');

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
INSERT INTO `sys_role_menu` VALUES (2, 1);
INSERT INTO `sys_role_menu` VALUES (9, 1);
INSERT INTO `sys_role_menu` VALUES (10, 1);
INSERT INTO `sys_role_menu` VALUES (102, 1);
INSERT INTO `sys_role_menu` VALUES (201, 1);
INSERT INTO `sys_role_menu` VALUES (202, 1);
INSERT INTO `sys_role_menu` VALUES (901, 1);
INSERT INTO `sys_role_menu` VALUES (902, 1);
INSERT INTO `sys_role_menu` VALUES (20101, 1);
INSERT INTO `sys_role_menu` VALUES (20102, 1);
INSERT INTO `sys_role_menu` VALUES (20103, 1);
INSERT INTO `sys_role_menu` VALUES (20201, 1);
INSERT INTO `sys_role_menu` VALUES (20202, 1);
INSERT INTO `sys_role_menu` VALUES (20203, 1);
INSERT INTO `sys_role_menu` VALUES (800000, 1);
INSERT INTO `sys_role_menu` VALUES (800001, 1);
INSERT INTO `sys_role_menu` VALUES (800002, 1);
INSERT INTO `sys_role_menu` VALUES (801000, 1);
INSERT INTO `sys_role_menu` VALUES (801100, 1);
INSERT INTO `sys_role_menu` VALUES (801101, 1);
INSERT INTO `sys_role_menu` VALUES (801102, 1);
INSERT INTO `sys_role_menu` VALUES (801103, 1);
INSERT INTO `sys_role_menu` VALUES (801104, 1);
INSERT INTO `sys_role_menu` VALUES (801105, 1);
INSERT INTO `sys_role_menu` VALUES (801106, 1);
INSERT INTO `sys_role_menu` VALUES (801107, 1);
INSERT INTO `sys_role_menu` VALUES (801108, 1);
INSERT INTO `sys_role_menu` VALUES (801109, 1);
INSERT INTO `sys_role_menu` VALUES (801200, 1);
INSERT INTO `sys_role_menu` VALUES (801201, 1);
INSERT INTO `sys_role_menu` VALUES (801202, 1);
INSERT INTO `sys_role_menu` VALUES (801203, 1);
INSERT INTO `sys_role_menu` VALUES (801204, 1);
INSERT INTO `sys_role_menu` VALUES (801205, 1);
INSERT INTO `sys_role_menu` VALUES (801206, 1);
INSERT INTO `sys_role_menu` VALUES (801207, 1);
INSERT INTO `sys_role_menu` VALUES (801208, 1);
INSERT INTO `sys_role_menu` VALUES (801209, 1);
INSERT INTO `sys_role_menu` VALUES (801300, 1);
INSERT INTO `sys_role_menu` VALUES (801301, 1);
INSERT INTO `sys_role_menu` VALUES (801302, 1);
INSERT INTO `sys_role_menu` VALUES (801303, 1);
INSERT INTO `sys_role_menu` VALUES (801304, 1);
INSERT INTO `sys_role_menu` VALUES (801400, 1);
INSERT INTO `sys_role_menu` VALUES (801401, 1);
INSERT INTO `sys_role_menu` VALUES (801402, 1);
INSERT INTO `sys_role_menu` VALUES (801403, 1);
INSERT INTO `sys_role_menu` VALUES (801404, 1);
INSERT INTO `sys_role_menu` VALUES (801405, 1);
INSERT INTO `sys_role_menu` VALUES (801406, 1);
INSERT INTO `sys_role_menu` VALUES (801407, 1);
INSERT INTO `sys_role_menu` VALUES (801408, 1);
INSERT INTO `sys_role_menu` VALUES (801409, 1);
INSERT INTO `sys_role_menu` VALUES (801410, 1);
INSERT INTO `sys_role_menu` VALUES (801411, 1);
INSERT INTO `sys_role_menu` VALUES (801412, 1);
INSERT INTO `sys_role_menu` VALUES (802000, 1);
INSERT INTO `sys_role_menu` VALUES (802100, 1);
INSERT INTO `sys_role_menu` VALUES (802101, 1);
INSERT INTO `sys_role_menu` VALUES (802102, 1);
INSERT INTO `sys_role_menu` VALUES (802103, 1);
INSERT INTO `sys_role_menu` VALUES (802104, 1);
INSERT INTO `sys_role_menu` VALUES (802105, 1);
INSERT INTO `sys_role_menu` VALUES (802106, 1);
INSERT INTO `sys_role_menu` VALUES (802200, 1);
INSERT INTO `sys_role_menu` VALUES (802201, 1);
INSERT INTO `sys_role_menu` VALUES (802202, 1);
INSERT INTO `sys_role_menu` VALUES (802203, 1);
INSERT INTO `sys_role_menu` VALUES (802204, 1);
INSERT INTO `sys_role_menu` VALUES (802205, 1);
INSERT INTO `sys_role_menu` VALUES (802206, 1);
INSERT INTO `sys_role_menu` VALUES (802207, 1);
INSERT INTO `sys_role_menu` VALUES (802208, 1);
INSERT INTO `sys_role_menu` VALUES (802209, 1);
INSERT INTO `sys_role_menu` VALUES (802210, 1);
INSERT INTO `sys_role_menu` VALUES (802211, 1);
INSERT INTO `sys_role_menu` VALUES (802212, 1);
INSERT INTO `sys_role_menu` VALUES (802213, 1);
INSERT INTO `sys_role_menu` VALUES (802214, 1);
INSERT INTO `sys_role_menu` VALUES (802215, 1);
INSERT INTO `sys_role_menu` VALUES (802216, 1);
INSERT INTO `sys_role_menu` VALUES (802300, 1);
INSERT INTO `sys_role_menu` VALUES (802301, 1);
INSERT INTO `sys_role_menu` VALUES (802302, 1);
INSERT INTO `sys_role_menu` VALUES (802303, 1);
INSERT INTO `sys_role_menu` VALUES (802304, 1);
INSERT INTO `sys_role_menu` VALUES (802400, 1);
INSERT INTO `sys_role_menu` VALUES (802401, 1);
INSERT INTO `sys_role_menu` VALUES (802402, 1);
INSERT INTO `sys_role_menu` VALUES (802403, 1);
INSERT INTO `sys_role_menu` VALUES (802404, 1);
INSERT INTO `sys_role_menu` VALUES (802500, 1);
INSERT INTO `sys_role_menu` VALUES (802501, 1);
INSERT INTO `sys_role_menu` VALUES (802502, 1);
INSERT INTO `sys_role_menu` VALUES (802503, 1);
INSERT INTO `sys_role_menu` VALUES (802600, 1);
INSERT INTO `sys_role_menu` VALUES (802601, 1);
INSERT INTO `sys_role_menu` VALUES (802700, 1);
INSERT INTO `sys_role_menu` VALUES (802701, 1);
INSERT INTO `sys_role_menu` VALUES (802702, 1);
INSERT INTO `sys_role_menu` VALUES (802703, 1);
INSERT INTO `sys_role_menu` VALUES (802704, 1);
INSERT INTO `sys_role_menu` VALUES (802705, 1);
INSERT INTO `sys_role_menu` VALUES (802706, 1);
INSERT INTO `sys_role_menu` VALUES (802800, 1);
INSERT INTO `sys_role_menu` VALUES (802801, 1);
INSERT INTO `sys_role_menu` VALUES (802802, 1);
INSERT INTO `sys_role_menu` VALUES (802803, 1);
INSERT INTO `sys_role_menu` VALUES (802804, 1);
INSERT INTO `sys_role_menu` VALUES (802805, 1);
INSERT INTO `sys_role_menu` VALUES (802806, 1);
INSERT INTO `sys_role_menu` VALUES (802807, 1);
INSERT INTO `sys_role_menu` VALUES (803000, 1);
INSERT INTO `sys_role_menu` VALUES (803001, 1);
INSERT INTO `sys_role_menu` VALUES (803002, 1);
INSERT INTO `sys_role_menu` VALUES (803003, 1);
INSERT INTO `sys_role_menu` VALUES (803004, 1);
INSERT INTO `sys_role_menu` VALUES (803005, 1);
INSERT INTO `sys_role_menu` VALUES (803006, 1);
INSERT INTO `sys_role_menu` VALUES (803007, 1);
INSERT INTO `sys_role_menu` VALUES (803011, 1);
INSERT INTO `sys_role_menu` VALUES (803012, 1);
INSERT INTO `sys_role_menu` VALUES (803013, 1);
INSERT INTO `sys_role_menu` VALUES (803014, 1);
INSERT INTO `sys_role_menu` VALUES (803021, 1);
INSERT INTO `sys_role_menu` VALUES (803022, 1);
INSERT INTO `sys_role_menu` VALUES (803023, 1);
INSERT INTO `sys_role_menu` VALUES (803024, 1);
INSERT INTO `sys_role_menu` VALUES (803031, 1);
INSERT INTO `sys_role_menu` VALUES (803032, 1);
INSERT INTO `sys_role_menu` VALUES (803033, 1);
INSERT INTO `sys_role_menu` VALUES (803041, 1);
INSERT INTO `sys_role_menu` VALUES (803042, 1);
INSERT INTO `sys_role_menu` VALUES (803043, 1);
INSERT INTO `sys_role_menu` VALUES (803044, 1);
INSERT INTO `sys_role_menu` VALUES (803051, 1);
INSERT INTO `sys_role_menu` VALUES (803052, 1);
INSERT INTO `sys_role_menu` VALUES (803061, 1);
INSERT INTO `sys_role_menu` VALUES (803062, 1);
INSERT INTO `sys_role_menu` VALUES (803063, 1);
INSERT INTO `sys_role_menu` VALUES (803064, 1);
INSERT INTO `sys_role_menu` VALUES (803065, 1);
INSERT INTO `sys_role_menu` VALUES (803066, 1);
INSERT INTO `sys_role_menu` VALUES (803071, 1);
INSERT INTO `sys_role_menu` VALUES (804000, 1);
INSERT INTO `sys_role_menu` VALUES (804001, 1);
INSERT INTO `sys_role_menu` VALUES (804002, 1);
INSERT INTO `sys_role_menu` VALUES (804003, 1);
INSERT INTO `sys_role_menu` VALUES (804004, 1);
INSERT INTO `sys_role_menu` VALUES (804005, 1);
INSERT INTO `sys_role_menu` VALUES (804006, 1);
INSERT INTO `sys_role_menu` VALUES (804007, 1);
INSERT INTO `sys_role_menu` VALUES (804100, 1);
INSERT INTO `sys_role_menu` VALUES (804200, 1);

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
) ENGINE = InnoDB AUTO_INCREMENT = 161 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统用户' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'snowball', 'snowball', 1, '1000000000', 'snowball@gmail.com', '2222', '$2a$10$R4m6xQQ3vsxyk9kMt7zDfu0lCxmL/Upygn4xM7fbsKjHd9cGWl2n.', b'1', NULL, NULL, '2026-03-12 18:43:53', NULL, NULL);

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
INSERT INTO `sys_user_dept` VALUES (161, 4);

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

SET FOREIGN_KEY_CHECKS = 1;
