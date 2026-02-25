package com.ddf.boot.capableadmin.model.entity;

import java.util.Date;
import lombok.Data;

/**
* <p>description</p >
*
* @author Snowball
* @version 1.0
* @date 2025/01/03 16:50
*/


/**
 * 七牛云文件存储
 */
@Data
public class ToolQiniuContent {
    /**
     * ID
     */
    private Long contentId;

    /**
     * Bucket 识别符
     */
    private String bucket;

    /**
     * 文件名称
     */
    private String name;

    /**
     * 文件大小
     */
    private String size;

    /**
     * 文件类型：私有或公开
     */
    private String type;

    /**
     * 文件url
     */
    private String url;

    /**
     * 文件后缀
     */
    private String suffix;

    /**
     * 上传或同步的时间
     */
    private Date updateTime;
}
