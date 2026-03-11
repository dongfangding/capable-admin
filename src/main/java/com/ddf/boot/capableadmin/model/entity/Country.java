package com.ddf.boot.capableadmin.model.entity;

import lombok.Data;

/**
 * <p>description</p >
 *
 * @author Snowball
 * @version 1.0
 * @since 2026/02/27 17:52
 */
@Data
public class Country {
    /**
     * 自增id
     */
    private Integer id;

    /**
     * 区域名称
     */
    private String areaName;

    /**
     * 国家名称
     */
    private String currencyName;

    /**
     * 货币code
     */
    private String currencyCode;

    /**
     * 货币符
     */
    private String currencySymbol;

    /**
     * 地区code
     */
    private String areaCode;

    /**
     * 当地地区域名称
     */
    private String areaNameLocal;

    /**
     * 手机区号
     */
    private String phoneCode;

    /**
     * 国家名称中文
     */
    private String areaNameZh;

    /**
     * 繁体
     */
    private String areaNameZhTrad;

    /**
     * 英文
     */
    private String areaNameEn;

    /**
     * 菲律宾
     */
    private String areaNameFil;

    /**
     * 阿拉伯
     */
    private String areaNameAr;

    /**
     * 印地语
     */
    private String areaNameHi;

    /**
     * 孟加拉
     */
    private String areaNameBn;

    /**
     * 法语
     */
    private String areaNameFr;

    /**
     * 越南
     */
    private String areaNameVi;

    /**
     * 葡萄牙
     */
    private String areaNamePt;

    /**
     * 西班牙
     */
    private String areaNameEs;

    /**
     * 日文
     */
    private String areaNameJap;

    /**
     * 韩文
     */
    private String areaNameKo;

    /**
     * 印尼语
     */
    private String areaNameId;
}
