package com.madao.article.handler;

import com.madao.enums.StatusEnum;
import com.madao.utils.JsonData;
import com.madao.utils.JsonUtil;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 无权访问返回的 JSON 格式数据给前端（否则为 403 html 页面）
 **/

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        httpServletResponse.setHeader("Content-type", "application/json;charset=UTF-8");
        httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        // 如果这里状态改为HttpServletResponse.SC_UNAUTHORIZED 会导致feign之间调用异常 see https://xujin.org/sc/sc-feign-4xx/
//		httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        JsonData<Object> jsonData = new JsonData<>(StatusEnum.UN_AUTHORIZED);
        httpServletResponse.getWriter().write(JsonUtil.toJsonString(jsonData));
    }
}
