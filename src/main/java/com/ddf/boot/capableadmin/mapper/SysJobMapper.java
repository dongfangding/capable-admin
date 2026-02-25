package com.ddf.boot.capableadmin.mapper;

import com.ddf.boot.capableadmin.model.entity.SysJob;
import com.ddf.boot.capableadmin.model.request.sys.SysJobQuery;
import java.util.List;
import java.util.Set;
import org.apache.ibatis.annotations.Param;

/**
 * <p>description</p >
 *
 * @author Snowball
 * @version 1.0
 * @date 2025/01/07 17:48
 */
public interface SysJobMapper {
    int deleteByPrimaryKey(Long jobId);

    int insert(SysJob record);

    int insertSelective(SysJob record);

    SysJob selectByPrimaryKey(Long jobId);

    int updateByPrimaryKeySelective(SysJob record);

    int updateByPrimaryKey(SysJob record);

    List<SysJob> query(SysJobQuery query);

    int countByJobs(@Param("jobIds") Set<Long> jobIds);

    int deleteBatchIds(@Param("jobIds") Set<Long> ids);

    SysJob findByName(@Param("name") String name);
}
