package com.ddf.boot.capableadmin.model.dto;

import java.io.Serializable;
import lombok.Data;

/**
 * <p>description</p >
 *
 * @author Snowball
 * @version 1.0
 * @date 2025/01/06 20:45
 */
@Data
public class SimpleDept implements Serializable {

    /**
     * 部门id
     */
    private Long deptId;

    /**
     * 部门名称
     */
    private String name;
}
