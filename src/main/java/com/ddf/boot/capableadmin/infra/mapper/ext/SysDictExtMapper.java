package com.ddf.boot.capableadmin.infra.mapper.ext;

import com.ddf.boot.capableadmin.model.request.sys.SysDictListRequest;
import com.ddf.boot.capableadmin.model.response.sys.SysDictResponse;
import java.util.List;

public interface SysDictExtMapper {

    List<SysDictResponse> listByCondition(SysDictListRequest request);
}
