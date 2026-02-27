/*
 Navicat Premium Data Transfer

 Source Server         : 虚拟机
 Source Server Type    : MySQL
 Source Server Version : 80032
 Source Host           : 10.88.1.88:3306
 Source Schema         : capable_admin

 Target Server Type    : MySQL
 Target Server Version : 80032
 File Encoding         : 65001

 Date: 27/02/2026 18:43:40
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
) ENGINE = InnoDB AUTO_INCREMENT = 241 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

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
) ENGINE = InnoDB AUTO_INCREMENT = 35 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '部门' ROW_FORMAT = DYNAMIC;

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
) ENGINE = InnoDB AUTO_INCREMENT = 36 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数据字典' ROW_FORMAT = DYNAMIC;

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
) ENGINE = InnoDB AUTO_INCREMENT = 179 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数据字典详情' ROW_FORMAT = DYNAMIC;

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
) ENGINE = InnoDB AUTO_INCREMENT = 40 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '岗位' ROW_FORMAT = DYNAMIC;

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
) ENGINE = InnoDB AUTO_INCREMENT = 32736 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统日志' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_log
-- ----------------------------

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
                             `menu_id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
                             `pid` bigint NOT NULL DEFAULT 0 COMMENT '上级菜单ID，默认0为一级节点',
                             `type` int NOT NULL COMMENT '菜单类型0：目录 1：菜单 2：按钮',
                             `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单标题',
                             `component_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '组件名称',
                             `component` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '组件',
                             `sort` int NOT NULL DEFAULT 0 COMMENT '排序',
                             `icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '图标',
                             `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '链接地址',
                             `is_frame` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否外链',
                             `cache` bit(1) NOT NULL DEFAULT b'0' COMMENT '缓存',
                             `hidden` bit(1) NOT NULL DEFAULT b'0' COMMENT '隐藏',
                             `permission` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '权限',
                             `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建者',
                             `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新者',
                             `create_time` bigint NULL DEFAULT NULL COMMENT '创建日期',
                             `update_time` bigint NULL DEFAULT NULL COMMENT '更新时间',
                             `sub_count` int NOT NULL DEFAULT 0 COMMENT '子节点数量',
                             PRIMARY KEY (`menu_id`) USING BTREE,
                             UNIQUE INDEX `UK_title`(`title` ASC) USING BTREE,
                             UNIQUE INDEX `UK_name`(`component_name` ASC) USING BTREE,
                             INDEX `INX_pid`(`pid` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 451 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统菜单' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, 0, 0, '系统管理', NULL, '', 1, 'system', 'system', b'0', b'0', b'0', NULL, 'admin', 'admin', 1683010740, 1683010740, 9);
INSERT INTO `sys_menu` VALUES (2, 1, 1, '系统用户管理', 'User', 'system/user/index', 2, 'peoples', 'user', b'0', b'0', b'0', 'user:list', 'admin', 'admin', 1683010740, 1683010740, 3);
INSERT INTO `sys_menu` VALUES (3, 1, 1, '角色管理', 'Role', 'system/role/index', 3, 'role', 'role', b'0', b'0', b'0', 'roles:list', 'admin', 'admin', 1683010740, 1683010740, 3);
INSERT INTO `sys_menu` VALUES (5, 1, 1, '菜单管理', 'Menu', 'system/menu/index', 5, 'menu', 'menu', b'0', b'0', b'0', 'menu:list', 'admin', 'admin', 1683010740, 1683010740, 3);
INSERT INTO `sys_menu` VALUES (6, 0, 0, '系统监控', NULL, '', 6, 'monitor', 'monitor', b'0', b'0', b'1', NULL, 'admin', 'admin', 1683010740, 1683010740, 3);
INSERT INTO `sys_menu` VALUES (7, 1, 1, '操作日志', 'Log', 'monitor/log/index', 7, 'log', 'logs', b'0', b'1', b'0', NULL, 'admin', 'admin', 1683010740, 1683010740, 0);
INSERT INTO `sys_menu` VALUES (9, 6, 1, 'SQL监控', 'Sql', 'monitor/sql/index', 9, 'sqlMonitor', 'druid', b'0', b'0', b'0', NULL, 'admin', 'admin', 1683010740, 1683010740, 0);
INSERT INTO `sys_menu` VALUES (10, 0, 0, '组件管理', NULL, '', 10, 'zujian', 'components', b'0', b'0', b'1', NULL, 'admin', 'admin', 1683010740, 1683010740, 5);
INSERT INTO `sys_menu` VALUES (11, 10, 1, '图标库', 'Icons', 'components/icons/index', 11, 'icon', 'icon', b'0', b'0', b'0', NULL, 'admin', 'admin', 1683010740, 1683010740, 0);
INSERT INTO `sys_menu` VALUES (14, 36, 1, '邮件工具', 'Email', 'tools/email/index', 14, 'email', 'email', b'0', b'0', b'0', NULL, 'admin', 'admin', 1683010740, 1683010740, 0);
INSERT INTO `sys_menu` VALUES (15, 10, 1, '富文本', 'Editor', 'components/Editor', 15, 'fwb', 'tinymce', b'0', b'0', b'0', NULL, 'admin', 'admin', 1683010740, 1683010740, 0);
INSERT INTO `sys_menu` VALUES (18, 36, 1, '存储管理', 'Storage', 'tools/storage/index', 18, 'qiniu', 'storage', b'0', b'0', b'0', 'storage:list', 'admin', 'admin', 1683010740, 1683010740, 3);
INSERT INTO `sys_menu` VALUES (19, 36, 1, '支付宝工具', 'AliPay', 'tools/aliPay/index', 19, 'alipay', 'aliPay', b'0', b'0', b'0', NULL, 'admin', 'admin', 1683010740, 1683010740, 0);
INSERT INTO `sys_menu` VALUES (21, 0, 0, '多级菜单', NULL, '', 21, 'menu', 'nested', b'0', b'0', b'1', NULL, 'admin', 'admin', 1683010740, 1683010740, 2);
INSERT INTO `sys_menu` VALUES (22, 21, 0, '二级菜单1', NULL, '', 22, 'menu', 'menu1', b'0', b'0', b'0', NULL, 'admin', 'admin', 1683010740, 1683010740, 2);
INSERT INTO `sys_menu` VALUES (23, 21, 1, '二级菜单2', NULL, 'nested/menu2/index', 23, 'menu', 'menu2', b'0', b'0', b'0', NULL, 'admin', 'admin', 1683010740, 1683010740, 0);
INSERT INTO `sys_menu` VALUES (24, 22, 1, '三级菜单1', 'Test', 'nested/menu1/menu1-1', 24, 'menu', 'menu1-1', b'0', b'0', b'0', NULL, 'admin', 'admin', 1683010740, 1683010740, 0);
INSERT INTO `sys_menu` VALUES (27, 22, 1, '三级菜单2', NULL, 'nested/menu1/menu1-2', 27, 'menu', 'menu1-2', b'0', b'0', b'0', NULL, 'admin', 'admin', 1683010740, 1683010740, 0);
INSERT INTO `sys_menu` VALUES (28, 1, 1, '任务调度', 'Timing', 'system/timing/index', 28, 'timing', 'timing', b'0', b'0', b'1', 'timing:list', 'admin', 'admin', 1683010740, 1683010740, 3);
INSERT INTO `sys_menu` VALUES (30, 36, 1, '代码生成', 'GeneratorIndex', 'generator/index', 30, 'dev', 'generator', b'0', b'1', b'0', NULL, 'admin', 'admin', 1683010740, 1683010740, 0);
INSERT INTO `sys_menu` VALUES (32, 6, 1, '异常日志', 'ErrorLog', 'monitor/log/errorLog', 32, 'error', 'errorLog', b'0', b'0', b'0', NULL, 'admin', 'admin', 1683010740, 1683010740, 0);
INSERT INTO `sys_menu` VALUES (33, 10, 1, 'Markdown', 'Markdown', 'components/MarkDown', 33, 'markdown', 'markdown', b'0', b'0', b'0', NULL, 'admin', 'admin', 1683010740, 1683010740, 0);
INSERT INTO `sys_menu` VALUES (34, 10, 1, 'Yaml编辑器', 'YamlEdit', 'components/YamlEdit', 34, 'dev', 'yaml', b'0', b'0', b'0', NULL, 'admin', 'admin', 1683010740, 1683010740, 0);
INSERT INTO `sys_menu` VALUES (35, 1, 1, '部门管理', 'Dept', 'system/dept/index', 35, 'dept', 'dept', b'0', b'0', b'0', 'dept:list', 'admin', 'admin', 1683010740, 1683010740, 3);
INSERT INTO `sys_menu` VALUES (36, 0, 0, '系统工具', NULL, '', 36, 'sys-tools', 'sys-tools', b'0', b'0', b'1', NULL, 'admin', 'admin', 1683010740, 1683010740, 7);
INSERT INTO `sys_menu` VALUES (37, 1, 1, '岗位管理', 'Job', 'system/job/index', 37, 'Steve-Jobs', 'job', b'0', b'0', b'0', 'job:list', 'admin', 'admin', 1683010740, 1683010740, 3);
INSERT INTO `sys_menu` VALUES (38, 36, 1, '接口文档', 'Swagger', 'tools/swagger/index', 38, 'swagger', 'swagger2', b'0', b'0', b'0', NULL, 'admin', 'admin', 1683010740, 1683010740, 0);
INSERT INTO `sys_menu` VALUES (39, 1, 1, '字典管理', 'Dict', 'system/dict/index', 39, 'dictionary', 'dict', b'0', b'0', b'0', 'dict:list', 'admin', 'admin', 1683010740, 1683010740, 3);
INSERT INTO `sys_menu` VALUES (41, 1, 1, '在线用户', 'OnlineUser', 'monitor/online/index', 41, 'Steve-Jobs', 'online', b'0', b'0', b'0', NULL, 'admin', 'admin', 1683010740, 1683010740, 0);
INSERT INTO `sys_menu` VALUES (44, 2, 2, '用户新增', NULL, '', 44, '', '', b'0', b'0', b'0', 'user:add', 'admin', 'admin', 1683010740, 1683010740, 0);
INSERT INTO `sys_menu` VALUES (45, 2, 2, '用户编辑', NULL, '', 45, '', '', b'0', b'0', b'0', 'user:edit', 'admin', 'admin', 1683010740, 1683010740, 0);
INSERT INTO `sys_menu` VALUES (46, 2, 2, '用户删除', NULL, '', 46, '', '', b'0', b'0', b'0', 'user:del', 'admin', 'admin', 1683010740, 1683010740, 0);
INSERT INTO `sys_menu` VALUES (48, 3, 2, '角色创建', NULL, '', 48, '', '', b'0', b'0', b'0', 'roles:add', 'admin', 'admin', 1683010740, 1683010740, 0);
INSERT INTO `sys_menu` VALUES (49, 3, 2, '角色修改', NULL, '', 49, '', '', b'0', b'0', b'0', 'roles:edit', 'admin', 'admin', 1683010740, 1683010740, 0);
INSERT INTO `sys_menu` VALUES (50, 3, 2, '角色删除', NULL, '', 50, '', '', b'0', b'0', b'0', 'roles:del', 'admin', 'admin', 1683010740, 1683010740, 0);
INSERT INTO `sys_menu` VALUES (52, 5, 2, '菜单新增', NULL, '', 52, '', '', b'0', b'0', b'0', 'menu:add', 'admin', 'admin', 1683010740, 1683010740, 0);
INSERT INTO `sys_menu` VALUES (53, 5, 2, '菜单编辑', NULL, '', 53, '', '', b'0', b'0', b'0', 'menu:edit', 'admin', 'admin', 1683010740, 1683010740, 0);
INSERT INTO `sys_menu` VALUES (54, 5, 2, '菜单删除', NULL, '', 54, '', '', b'0', b'0', b'0', 'menu:del', 'admin', 'admin', 1683010740, 1683010740, 0);
INSERT INTO `sys_menu` VALUES (56, 35, 2, '部门新增', NULL, '', 56, '', '', b'0', b'0', b'0', 'dept:add', 'admin', 'admin', 1683010740, 1683010740, 0);
INSERT INTO `sys_menu` VALUES (57, 35, 2, '部门编辑', NULL, '', 57, '', '', b'0', b'0', b'0', 'dept:edit', 'admin', 'admin', 1683010740, 1683010740, 0);
INSERT INTO `sys_menu` VALUES (58, 35, 2, '部门删除', NULL, '', 58, '', '', b'0', b'0', b'0', 'dept:del', 'admin', 'admin', 1683010740, 1683010740, 0);
INSERT INTO `sys_menu` VALUES (60, 37, 2, '岗位新增', NULL, '', 60, '', '', b'0', b'0', b'0', 'job:add', 'admin', 'admin', 1683010740, 1683010740, 0);
INSERT INTO `sys_menu` VALUES (61, 37, 2, '岗位编辑', NULL, '', 61, '', '', b'0', b'0', b'0', 'job:edit', 'admin', 'admin', 1683010740, 1683010740, 0);
INSERT INTO `sys_menu` VALUES (62, 37, 2, '岗位删除', NULL, '', 62, '', '', b'0', b'0', b'0', 'job:del', 'admin', 'admin', 1683010740, 1683010740, 0);
INSERT INTO `sys_menu` VALUES (64, 39, 2, '字典新增', NULL, '', 64, '', '', b'0', b'0', b'0', 'dict:add', 'admin', 'admin', 1683010740, 1683010740, 0);
INSERT INTO `sys_menu` VALUES (65, 39, 2, '字典编辑', NULL, '', 65, '', '', b'0', b'0', b'0', 'dict:edit', 'admin', 'admin', 1683010740, 1683010740, 0);
INSERT INTO `sys_menu` VALUES (66, 39, 2, '字典删除', NULL, '', 66, '', '', b'0', b'0', b'0', 'dict:del', 'admin', 'admin', 1683010740, 1683010740, 0);

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
                             PRIMARY KEY (`role_id`) USING BTREE,
                             UNIQUE INDEX `UK_name`(`name` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '超级管理员', 1, '超级管理员', '0', 'admin', 'admin', 1683010740, 1683010740, 0, b'1');

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
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '七牛云文件存储' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of tool_qiniu_content
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
