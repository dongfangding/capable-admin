package com.ddf.boot.capableadmin.application;

import com.ddf.boot.capableadmin.enums.PrettyAdminExceptionCode;
import com.ddf.boot.capableadmin.infra.mapper.SysDeptMapper;
import com.ddf.boot.capableadmin.infra.mapper.SysUserDeptMapper;
import com.ddf.boot.capableadmin.infra.mapper.SysUserJobMapper;
import com.ddf.boot.capableadmin.infra.mapper.SysUserMapper;
import com.ddf.boot.capableadmin.infra.mapper.SysUserRoleMapper;
import com.ddf.boot.capableadmin.model.entity.SysDept;
import com.ddf.boot.capableadmin.model.entity.SysUser;
import com.ddf.boot.capableadmin.model.entity.SysUserDept;
import com.ddf.boot.capableadmin.model.request.sys.EnableRequest;
import com.ddf.boot.capableadmin.model.request.sys.SysDeptQuery;
import com.ddf.boot.capableadmin.model.request.sys.SysUserCreateRequest;
import com.ddf.boot.capableadmin.model.request.sys.SysUserHomePageModifyRequest;
import com.ddf.boot.capableadmin.model.request.sys.SysUserListRequest;
import com.ddf.boot.capableadmin.model.response.sys.SysDeptNode;
import com.ddf.boot.capableadmin.model.response.sys.SysUserRes;
import com.ddf.boot.capableadmin.service.SysDeptService;
import com.ddf.boot.common.api.exception.BusinessException;
import com.ddf.boot.common.api.model.common.response.PageResult;
import com.ddf.boot.common.core.encode.BCryptPasswordEncoder;
import com.ddf.boot.common.core.util.BeanCopierUtils;
import com.ddf.boot.common.core.util.PageUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
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
import org.springframework.util.CollectionUtils;

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
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional(rollbackFor = Exception.class)
    public void persistUser(SysUserCreateRequest request) {
        final boolean isUpdate = Objects.nonNull(request.getUserId());
        SysUser oldUser = null;
        if (isUpdate) {
            oldUser = sysUserMapper.selectByPrimaryKey(request.getUserId());
            if (Objects.isNull(oldUser)) {
                throw new BusinessException(PrettyAdminExceptionCode.SYS_USER_NOT_EXISTS);
            }
        }

        checkDuplicateUserFields(request, isUpdate);

        final Long userId;
        if (!isUpdate) {
            final SysUser sysUser = BeanCopierUtils.copy(request, SysUser.class);
            sysUser.setPassword(bCryptPasswordEncoder.encode("123456"));
            sysUserMapper.insertSelective(sysUser);
            userId = sysUser.getUserId();
        } else {
            BeanCopierUtils.copy(request, oldUser);
            sysUserMapper.updateByPrimaryKeySelective(oldUser);
            userId = oldUser.getUserId();
            sysUserDeptMapper.deleteByUserId(userId);
            sysUserJobMapper.deleteByUserId(userId);
            sysUserRoleMapper.deleteByUserId(userId);
        }

        saveUserRelations(userId, request);
    }

    public PageResult<SysUserRes> list(SysUserListRequest request) {
        final PageResult<SysUserRes> result = PageUtil.startPage(
                request,
                () -> sysUserMapper.listAll(request),
                list -> BeanCopierUtils.copy(list, SysUserRes.class));
        fillUserRelations(result.getContent());
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public void resetPassword(Long userId) {
        final SysUser sysUser = sysUserMapper.selectByPrimaryKey(userId);
        if (Objects.isNull(sysUser)) {
            throw new BusinessException(PrettyAdminExceptionCode.SYS_USER_NOT_EXISTS);
        }
        sysUserMapper.updatePassword(userId, bCryptPasswordEncoder.encode("123456"), new Date());
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Long userId) {
        final SysUser sysUser = sysUserMapper.selectByPrimaryKey(userId);
        if (Objects.isNull(sysUser)) {
            throw new BusinessException(PrettyAdminExceptionCode.SYS_USER_NOT_EXISTS);
        }
        sysUserDeptMapper.deleteByUserId(userId);
        sysUserJobMapper.deleteByUserId(userId);
        sysUserRoleMapper.deleteByUserId(userId);
        sysUserMapper.deleteByPrimaryKey(userId);
    }

    public Boolean updateEnable(EnableRequest request) {
        return sysUserMapper.updateEnable(request.getId(), request.getEnabled()) > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    public List<SysDeptNode> deleteDept(Set<Long> deptIds) {
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

    public int userSelfUpdateHomePage(SysUserHomePageModifyRequest request) {
        final Long userId = 1L;
        final SysUser sysUser = sysUserMapper.selectByPrimaryKey(userId);
        if (Objects.isNull(sysUser)) {
            return 0;
        }
        BeanCopierUtils.copy(request, sysUser);
        return sysUserMapper.updateByPrimaryKeySelective(sysUser);
    }

    private void checkDuplicateUserFields(SysUserCreateRequest request, boolean isUpdate) {
        final SysUser byUsername = sysUserMapper.selectByUsername(request.getUsername());
        if (Objects.nonNull(byUsername) && (!isUpdate || !Objects.equals(byUsername.getUserId(), request.getUserId()))) {
            throw new BusinessException(PrettyAdminExceptionCode.USER_NAME_EXISTS);
        }

        final String email = request.getEmail();
        if (email != null && !email.isBlank()) {
            final SysUser byEmail = sysUserMapper.selectByEmail(email);
            if (Objects.nonNull(byEmail) && (!isUpdate || !Objects.equals(byEmail.getUserId(), request.getUserId()))) {
                throw new BusinessException(PrettyAdminExceptionCode.EMAIL_EXISTS);
            }
        }

        final String mobile = request.getMobile();
        if (mobile != null && !mobile.isBlank()) {
            final SysUser byMobile = sysUserMapper.selectByMobile(mobile);
            if (Objects.nonNull(byMobile) && (!isUpdate || !Objects.equals(byMobile.getUserId(), request.getUserId()))) {
                throw new BusinessException(PrettyAdminExceptionCode.MOBILE_EXISTS);
            }
        }
    }

    private void saveUserRelations(Long userId, SysUserCreateRequest request) {
        final Set<Long> deptIds = request.getDeptIds();
        final List<SysUserDept> userDeptList = new ArrayList<>();
        for (Long deptId : deptIds) {
            final SysUserDept sysUserDept = new SysUserDept();
            sysUserDept.setUserId(userId);
            sysUserDept.setDeptId(deptId);
            userDeptList.add(sysUserDept);
        }
        if (!CollectionUtils.isEmpty(userDeptList)) {
            sysUserDeptMapper.batchInsert(userDeptList);
        }

        if (!CollectionUtils.isEmpty(request.getJobIds())) {
            sysUserJobMapper.batchInsertUserJob(userId, request.getJobIds());
        }
        if (!CollectionUtils.isEmpty(request.getRoleIds())) {
            sysUserRoleMapper.batchInsertUserRole(userId, request.getRoleIds());
        }
    }

    private void fillUserRelations(List<SysUserRes> users) {
        if (CollectionUtils.isEmpty(users)) {
            return;
        }
        final List<Long> userIds = users.stream().map(SysUserRes::getUserId).toList();
        final Map<Long, Set<Long>> deptMap = buildRelationMap(sysUserDeptMapper.selectDeptIdsByUserIds(userIds));
        final Map<Long, Set<Long>> roleMap = buildRelationMap(sysUserRoleMapper.selectRoleIdsByUserIds(userIds));
        for (SysUserRes user : users) {
            user.setDeptIds(deptMap.getOrDefault(user.getUserId(), new HashSet<>()));
            user.setRoleIds(roleMap.getOrDefault(user.getUserId(), new HashSet<>()));
        }
    }

    private Map<Long, Set<Long>> buildRelationMap(List<Map<String, Object>> relationList) {
        final Map<Long, Set<Long>> relationMap = new HashMap<>();
        if (CollectionUtils.isEmpty(relationList)) {
            return relationMap;
        }
        for (Map<String, Object> relation : relationList) {
            final Object userId = relation.get("userId");
            final Object relationId = relation.get("relationId");
            if (!(userId instanceof Number userIdNumber) || !(relationId instanceof Number relationIdNumber)) {
                continue;
            }
            relationMap.computeIfAbsent(userIdNumber.longValue(), key -> new HashSet<>()).add(relationIdNumber.longValue());
        }
        return relationMap;
    }
}
