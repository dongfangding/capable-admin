package com.ddf.boot.capableadmin.infra.mapper.ext;

import com.ddf.boot.capableadmin.model.request.sys.SysLogListRequest;
import com.ddf.boot.capableadmin.model.response.sys.SysLogResponse;
import java.util.List;
import java.util.Set;
import org.apache.ibatis.annotations.Param;

public interface SysLogExtMapper {

    List<SysLogResponse> listByCondition(SysLogListRequest request);

    int deleteByIds(@Param("ids") Set<Long> ids);
}
