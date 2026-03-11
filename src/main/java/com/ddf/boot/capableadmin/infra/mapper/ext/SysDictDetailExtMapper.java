package com.ddf.boot.capableadmin.infra.mapper.ext;

import com.ddf.boot.capableadmin.model.request.sys.SysDictDetailListRequest;
import com.ddf.boot.capableadmin.model.response.sys.SysDictDetailResponse;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SysDictDetailExtMapper {

    List<SysDictDetailResponse> listByCondition(SysDictDetailListRequest request);

    int deleteByDictId(@Param("dictId") Long dictId);
}
