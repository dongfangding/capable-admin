package com.ddf.boot.capableadmin.application;

import com.ddf.boot.common.api.exception.BusinessException;
import com.ddf.boot.common.core.util.BeanCopierUtils;
import com.ddf.boot.capableadmin.enums.PrettyAdminExceptionCode;
import com.ddf.boot.capableadmin.mapper.SysDeptMapper;
import com.ddf.boot.capableadmin.mapper.SysUserDeptMapper;
import com.ddf.boot.capableadmin.mapper.SysUserJobMapper;
import com.ddf.boot.capableadmin.mapper.SysUserMapper;
import com.ddf.boot.capableadmin.mapper.SysUserRoleMapper;
import com.ddf.boot.capableadmin.model.entity.SysDept;
import com.ddf.boot.capableadmin.model.entity.SysUser;
import com.ddf.boot.capableadmin.model.entity.SysUserDept;
import com.ddf.boot.capableadmin.model.request.sys.SysDeptQuery;
import com.ddf.boot.capableadmin.model.request.sys.SysUserCreateRequest;
import com.ddf.boot.capableadmin.model.request.sys.SysUserHomePageModifyRequest;
import com.ddf.boot.capableadmin.model.response.sys.SysDeptRes;
import com.ddf.boot.capableadmin.service.SysDeptService;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>description</p >
 *
 * @author Snowball
 * @version 1.0
 * @date 2025/01/04 17:11
 */
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@Slf4j
@Service
public class SysUserApplicationService {

    private final SysDeptMapper sysDeptMapper;
    private final SysDeptService sysDeptService;
    private final SysUserDeptMapper sysUserDeptMapper;
    private final SysUserMapper sysUserMapper;
    private final SysUserJobMapper sysUserJobMapper;
    private final SysUserRoleMapper sysUserRoleMapper;


    /**
     * 保存用户
     *
     * @param request
     */
    @Transactional(rollbackFor = Exception.class)
    public void persistUser(SysUserCreateRequest request) {
        boolean isUpdate = Objects.nonNull(request.getUserId());
        SysUser oldUser = null;
        if (isUpdate) {
            oldUser = sysUserMapper.selectByPrimaryKey(request.getUserId());
            if (Objects.isNull(oldUser)) {
                throw new BusinessException(PrettyAdminExceptionCode.MENU_NOT_EXISTS);
            }
        }
        final SysUser byUsername = sysUserMapper.selectByUsername(request.getUsername());
        if (Objects.nonNull(byUsername) && (!Objects.equals(byUsername.getUsername(), request.getUsername())) || (isUpdate
                && !Objects.equals(byUsername.getUserId(), request.getUserId()))) {
            throw new BusinessException(PrettyAdminExceptionCode.USER_NAME_EXISTS);
        }
        final SysUser byEmail = sysUserMapper.selectByEmail(request.getEmail());
        if (Objects.nonNull(byEmail) && (!Objects.equals(byEmail.getEmail(), request.getEmail())) || (isUpdate
                && !Objects.equals(byEmail.getUserId(), request.getUserId()))) {
            throw new BusinessException(PrettyAdminExceptionCode.EMAIL_EXISTS);
        }
        final SysUser byMobile = sysUserMapper.selectByMobile(request.getMobile());
        if (Objects.nonNull(byEmail) && (!Objects.equals(byMobile.getMobile(), request.getMobile())) || (isUpdate
                && !Objects.equals(byMobile.getUserId(), request.getUserId()))) {
            throw new BusinessException(PrettyAdminExceptionCode.MOBILE_EXISTS);
        }
        Long userId;
        if (!isUpdate) {
            final SysUser sysUser = BeanCopierUtils.copy(request, SysUser.class);
            sysUserMapper.insertSelective(sysUser);
            userId = sysUser.getUserId();
        } else {
            BeanCopierUtils.copy(request, oldUser);
            sysUserMapper.updateByPrimaryKeySelective(oldUser);
            userId = oldUser.getUserId();
        }
        // 保存用户部门
        final SysUserDept sysUserDept = new SysUserDept();
        sysUserDept.setUserId(userId);
        sysUserDept.setDeptId(request.getDeptId());
        sysUserDeptMapper.insertSelective(sysUserDept);

        if (isUpdate) {
            // 如果是更新的话，先删除掉老的数据
            sysUserJobMapper.deleteByUserId(userId);
            sysUserRoleMapper.deleteByUserId(userId);
        }

        // 保存用户岗位
        sysUserJobMapper.batchInsertUserJob(userId, request.getJobIds());
        // 保存用户角色
        sysUserRoleMapper.batchInsertUserRole(userId, request.getRoleIds());
    }


    /**
     * 删除部门，当解散做，不会校验部门下是否有用户，如果有用户的话，删除用户与部门关联
     *
     * @param deptIds
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public List<SysDeptRes> deleteDept(Set<Long> deptIds) {
        final int i = sysDeptMapper.deleteByDeptIds(deptIds);
        if (i > 0) {
            final Map<Long, SysDept> sysDeptMap = sysDeptMapper.selectByDeptIds(deptIds).stream().collect(
                    Collectors.toMap(SysDept::getDeptId, Function.identity()));
            for (Long deptId : deptIds) {
                final SysDept dept = sysDeptMap.get(deptId);
                if (Objects.nonNull(dept) && Objects.nonNull(dept.getPid())) {
                    sysDeptService.updateSubCount(dept.getPid(), true);
                }
            }
        }
        sysUserDeptMapper.deleteByDeptIds(deptIds);
        return sysDeptService.queryAll(new SysDeptQuery());
    }

    /**
     * 用户自己更新主页信息
     */
    public int userSelfUpdateHomePage(SysUserHomePageModifyRequest request) {
        Long userId = 1L;
        final SysUser sysUser = sysUserMapper.selectByPrimaryKey(userId);
        if (Objects.isNull(sysUser)) {
            return 0;
        }
        BeanCopierUtils.copy(request, sysUser);
        return sysUserMapper.updateByPrimaryKeySelective(sysUser);
    }
}
