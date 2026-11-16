package com.sqx.modules.utils;

import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Locale;

/**
 * @author chengming
 * @since 2025/9/22
 */
public class RequestUtil {
    /**
     * 获取当前请求的 HttpServletRequest
     */
    public static HttpServletRequest getCurrentRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            throw new IllegalStateException("当前线程中不存在 HttpServletRequest");
        }
        return attributes.getRequest();
    }

    /**
     * 获取 Accept-Language 请求头
     */
    public static String getAcceptLanguage() {
        return getCurrentRequest().getHeader(HttpHeaders.ACCEPT_LANGUAGE);
    }

    /**
     * 获取 Locale（根据 Accept-Language 解析）
     */
    public static Locale getLocale() {
        String acceptLanguage = getAcceptLanguage();
        if (acceptLanguage == null || acceptLanguage.trim().isEmpty()) {
            return Locale.getDefault();
        }
        return Locale.lookup(Locale.LanguageRange.parse(acceptLanguage), Arrays.asList(Locale.getAvailableLocales()));
    }

}
