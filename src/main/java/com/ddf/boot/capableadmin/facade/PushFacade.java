package com.ddf.boot.capableadmin.facade;

import com.ddf.boot.capableadmin.model.dto.LoginNoticeData;
import com.ddf.boot.common.stomp.helpere.StompMessageHelper;
import com.ddf.boot.common.stomp.model.req.StompMessageRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * <p>description</p >
 *
 * @author Snowball
 * @version 1.0
 * @since 2026/09/10 11:58
 */
@RequiredArgsConstructor
@Slf4j
@Component
public class PushFacade {

	public static final String LOGIN_TOPIC = "/topic/login";

	private final StompMessageHelper stompMessageHelper;

	/**
	 * 推送登录通知
	 *
	 * @param loginNoticeData
	 */
	public void pushLoginMsg(LoginNoticeData loginNoticeData) {
		final StompMessageRequest<LoginNoticeData> request = new StompMessageRequest<>();
		request.setTopic(LOGIN_TOPIC);
		request.setTitle("登录通知");
		request.setMessageCode("auth_login");
		request.setData(loginNoticeData);
		stompMessageHelper.sendMessage(request);
	}
}
