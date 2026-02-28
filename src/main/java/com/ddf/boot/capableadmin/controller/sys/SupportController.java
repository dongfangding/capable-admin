package com.ddf.boot.capableadmin.controller.sys;

import com.ddf.boot.common.api.model.common.response.ResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 支持类的控制器
 *
 * @author Snowball
 * @version 1.0
 * @since 2026/02/28 16:53
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("")
public class SupportController {


	@GetMapping("timezone/get")
	public ResponseData<String> getTimezone() {
		return ResponseData.success("Asia/Shanghai");
	}

}
