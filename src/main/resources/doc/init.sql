-- 初始化角色
INSERT INTO `pretty-admin`.`sys_role` (`role_id`, `name`, `level`, `description`, `ip_limit`, `create_by`, `update_by`,
                                       `create_time`, `update_time`)
VALUES (1, '超级管理员', 1, '-', '全部', 'snowball', 'snowball', 1683010740, 1683010740);


-- 初始化岗位
INSERT INTO `pretty-admin`.`sys_job`
values (`job_id`, `name`, `enabled`, `sort`, `create_by`, `update_by`, `create_time`,
        `update_time`),
       (2, '产品经理', b'1', 2, 'admin', 'admin', 1683010740, 1683010740),
       (3, '研发', b'1', 3, 'admin', 'admin', 1683010740, 1683010740),
       (4, '测试', b'1', 4, 'admin', 'admin', 1683010740, 1683010740),
       (5, '财务', b'1', 5, 'admin', 'admin', 1683010740, 1683010740),
       (6, '运营', b'1', 6, 'admin', 'admin', 1683010740, 1683010740);

-- 初始化部门
INSERT INTO `pretty-admin`.`sys_dept` (`dept_id`, `pid`, `name`, `sort`, `enabled`, `create_by`, `update_by`,
                                       `create_time`, `update_time`, `sub_count`)
VALUES (1, 0, '天堂部', 1, b'1', 'admin', 'admin', 1683010740, 1683010740, 1),
       (2, 0, '地狱部', 2, b'1', 'admin', 'admin', 1683010740, 1683010740, 1),
       (3, 1, '人上人部', 3, b'1', 'admin', 'admin', 1683010740, 1683010740, 0),
       (4, 2, '牛马部', 4, b'1', 'admin', 'admin', 1683010740, 1683010740, 0);


-- 初始化菜单
INSERT INTO `pretty-admin`.`sys_menu` (`menu_id`, `pid`, `type`, `title`, `component_name`, `component`, `sort`, `icon`,
                                       `path`, `is_frame`, `cache`, `hidden`, `permission`, `create_by`, `update_by`,
                                       `create_time`, `update_time`, `sub_count`)
VALUES (1, 0, 0, '系统管理', NULL, '', 1, 'system', 'system', b'0', b'0', b'0', NULL, 'admin', 'admin', 1683010740,
        1683010740, 9),
(2, 1, 1, '系统用户管理', 'User', 'system/user/index', 2, 'peoples', 'user', b'0', b'0', b'0', 'user:list', 'admin', 'admin', 1683010740, 1683010740, 3),
(3, 1, 1, '角色管理', 'Role', 'system/role/index', 3, 'role', 'role', b'0', b'0', b'0', 'roles:list', 'admin', 'admin', 1683010740, 1683010740, 3),
(5, 1, 1, '菜单管理', 'Menu', 'system/menu/index', 5, 'menu', 'menu', b'0', b'0', b'0', 'menu:list', 'admin', 'admin', 1683010740, 1683010740, 3),
(6, 0, 0, '系统监控', NULL, '', 6, 'monitor', 'monitor', b'0', b'0', b'1', NULL, 'admin', 'admin', 1683010740, 1683010740, 3),
(7, 1, 1, '操作日志', 'Log', 'monitor/log/index', 7, 'log', 'logs', b'0', b'1', b'0', NULL, 'admin', 'admin', 1683010740, 1683010740, 0),
(9, 6, 1, 'SQL监控', 'Sql', 'monitor/sql/index', 9, 'sqlMonitor', 'druid', b'0', b'0', b'0', NULL, 'admin', 'admin', 1683010740, 1683010740, 0),
(10, 0, 0, '组件管理', NULL, '', 10, 'zujian', 'components', b'0', b'0', b'1', NULL, 'admin', 'admin', 1683010740, 1683010740, 5),
(11, 10, 1, '图标库', 'Icons', 'components/icons/index', 11, 'icon', 'icon', b'0', b'0', b'0', NULL, 'admin', 'admin', 1683010740, 1683010740, 0),
(14, 36, 1, '邮件工具', 'Email', 'tools/email/index', 14, 'email', 'email', b'0', b'0', b'0', NULL, 'admin', 'admin', 1683010740, 1683010740, 0),
(15, 10, 1, '富文本', 'Editor', 'components/Editor', 15, 'fwb', 'tinymce', b'0', b'0', b'0', NULL, 'admin', 'admin', 1683010740, 1683010740, 0),
(18, 36, 1, '存储管理', 'Storage', 'tools/storage/index', 18, 'qiniu', 'storage', b'0', b'0', b'0', 'storage:list', 'admin', 'admin', 1683010740, 1683010740, 3),
(19, 36, 1, '支付宝工具', 'AliPay', 'tools/aliPay/index', 19, 'alipay', 'aliPay', b'0', b'0', b'0', NULL, 'admin', 'admin', 1683010740, 1683010740, 0),
(21, 0, 0, '多级菜单', NULL, '', 21, 'menu', 'nested', b'0', b'0', b'1', NULL, 'admin', 'admin', 1683010740, 1683010740, 2),
(22, 21, 0, '二级菜单1', NULL, '', 22, 'menu', 'menu1', b'0', b'0', b'0', NULL, 'admin', 'admin', 1683010740, 1683010740, 2),
(23, 21, 1, '二级菜单2', NULL, 'nested/menu2/index', 23, 'menu', 'menu2', b'0', b'0', b'0', NULL, 'admin', 'admin', 1683010740, 1683010740, 0),
(24, 22, 1, '三级菜单1', 'Test', 'nested/menu1/menu1-1', 24, 'menu', 'menu1-1', b'0', b'0', b'0', NULL, 'admin', 'admin', 1683010740, 1683010740, 0),
(27, 22, 1, '三级菜单2', NULL, 'nested/menu1/menu1-2', 27, 'menu', 'menu1-2', b'0', b'0', b'0', NULL, 'admin', 'admin', 1683010740, 1683010740, 0),
(28, 1, 1, '任务调度', 'Timing', 'system/timing/index', 28, 'timing', 'timing', b'0', b'0', b'1', 'timing:list', 'admin', 'admin', 1683010740, 1683010740, 3),
(30, 36, 1, '代码生成', 'GeneratorIndex', 'generator/index', 30, 'dev', 'generator', b'0', b'1', b'0', NULL, 'admin', 'admin', 1683010740, 1683010740, 0),
(32, 6, 1, '异常日志', 'ErrorLog', 'monitor/log/errorLog', 32, 'error', 'errorLog', b'0', b'0', b'0', NULL, 'admin', 'admin', 1683010740, 1683010740, 0),
(33, 10, 1, 'Markdown', 'Markdown', 'components/MarkDown', 33, 'markdown', 'markdown', b'0', b'0', b'0', NULL, 'admin', 'admin', 1683010740, 1683010740, 0),
(34, 10, 1, 'Yaml编辑器', 'YamlEdit', 'components/YamlEdit', 34, 'dev', 'yaml', b'0', b'0', b'0', NULL, 'admin', 'admin', 1683010740, 1683010740, 0),
(35, 1, 1, '部门管理', 'Dept', 'system/dept/index', 35, 'dept', 'dept', b'0', b'0', b'0', 'dept:list', 'admin', 'admin', 1683010740, 1683010740, 3),
(36, 0, 0, '系统工具', NULL, '', 36, 'sys-tools', 'sys-tools', b'0', b'0', b'1', NULL, 'admin', 'admin', 1683010740, 1683010740, 7),
(37, 1, 1, '岗位管理', 'Job', 'system/job/index', 37, 'Steve-Jobs', 'job', b'0', b'0', b'0', 'job:list', 'admin', 'admin', 1683010740, 1683010740, 3),
(38, 36, 1, '接口文档', 'Swagger', 'tools/swagger/index', 38, 'swagger', 'swagger2', b'0', b'0', b'0', NULL, 'admin', 'admin', 1683010740, 1683010740, 0),
(39, 1, 1, '字典管理', 'Dict', 'system/dict/index', 39, 'dictionary', 'dict', b'0', b'0', b'0', 'dict:list', 'admin', 'admin', 1683010740, 1683010740, 3),
(41, 1, 1, '在线用户', 'OnlineUser', 'monitor/online/index', 41, 'Steve-Jobs', 'online', b'0', b'0', b'0', NULL, 'admin', 'admin', 1683010740, 1683010740, 0),
(44, 2, 2, '用户新增', NULL, '', 44, '', '', b'0', b'0', b'0', 'user:add', 'admin', 'admin', 1683010740, 1683010740, 0),
(45, 2, 2, '用户编辑', NULL, '', 45, '', '', b'0', b'0', b'0', 'user:edit', 'admin', 'admin', 1683010740, 1683010740, 0),
(46, 2, 2, '用户删除', NULL, '', 46, '', '', b'0', b'0', b'0', 'user:del', 'admin', 'admin', 1683010740, 1683010740, 0),
(48, 3, 2, '角色创建', NULL, '', 48, '', '', b'0', b'0', b'0', 'roles:add', 'admin', 'admin', 1683010740, 1683010740, 0),
(49, 3, 2, '角色修改', NULL, '', 49, '', '', b'0', b'0', b'0', 'roles:edit', 'admin', 'admin', 1683010740, 1683010740, 0),
(50, 3, 2, '角色删除', NULL, '', 50, '', '', b'0', b'0', b'0', 'roles:del', 'admin', 'admin', 1683010740, 1683010740, 0),
(52, 5, 2, '菜单新增', NULL, '', 52, '', '', b'0', b'0', b'0', 'menu:add', 'admin', 'admin', 1683010740, 1683010740, 0),
(53, 5, 2, '菜单编辑', NULL, '', 53, '', '', b'0', b'0', b'0', 'menu:edit', 'admin', 'admin', 1683010740, 1683010740, 0),
(54, 5, 2, '菜单删除', NULL, '', 54, '', '', b'0', b'0', b'0', 'menu:del', 'admin', 'admin', 1683010740, 1683010740, 0),
(56, 35, 2, '部门新增', NULL, '', 56, '', '', b'0', b'0', b'0', 'dept:add', 'admin', 'admin', 1683010740, 1683010740, 0),
(57, 35, 2, '部门编辑', NULL, '', 57, '', '', b'0', b'0', b'0', 'dept:edit', 'admin', 'admin', 1683010740, 1683010740, 0),
(58, 35, 2, '部门删除', NULL, '', 58, '', '', b'0', b'0', b'0', 'dept:del', 'admin', 'admin', 1683010740, 1683010740, 0),
(60, 37, 2, '岗位新增', NULL, '', 60, '', '', b'0', b'0', b'0', 'job:add', 'admin', 'admin', 1683010740, 1683010740, 0),
(61, 37, 2, '岗位编辑', NULL, '', 61, '', '', b'0', b'0', b'0', 'job:edit', 'admin', 'admin', 1683010740, 1683010740, 0),
(62, 37, 2, '岗位删除', NULL, '', 62, '', '', b'0', b'0', b'0', 'job:del', 'admin', 'admin', 1683010740, 1683010740, 0),
(64, 39, 2, '字典新增', NULL, '', 64, '', '', b'0', b'0', b'0', 'dict:add', 'admin', 'admin', 1683010740, 1683010740, 0),
(65, 39, 2, '字典编辑', NULL, '', 65, '', '', b'0', b'0', b'0', 'dict:edit', 'admin', 'admin', 1683010740, 1683010740, 0),
(66, 39, 2, '字典删除', NULL, '', 66, '', '', b'0', b'0', b'0', 'dict:del', 'admin', 'admin', 1683010740, 1683010740, 0);

