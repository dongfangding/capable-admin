package com.ddf.boot.capableadmin.model.entity;

import lombok.Data;

/**
* <p>description</p >
*
* @author Snowball
* @version 1.0
* @since 2026/02/27 17:52
*/


/**
 * 七牛云配置
 */
@Data
public class ToolQiniuConfig {
    /**
     * ID
     */
    private Long configId;

    /**
     * accessKey
     */
    private String accessKey;

    /**
     * Bucket 识别符
     */
    private String bucket;

    /**
     * 外链域名
     */
    private String host;

    /**
     * secretKey
     */
    private String secretKey;

    /**
     * 空间类型
     */
    private String type;

    /**
     * 机房
     */
    private String zone;
}
