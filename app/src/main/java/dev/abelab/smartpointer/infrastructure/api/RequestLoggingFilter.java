package dev.abelab.smartpointer.infrastructure.api;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.AbstractRequestLoggingFilter;

import lombok.extern.slf4j.Slf4j;

/**
 * Request Logging Filter
 */
@Slf4j
@Component
public class RequestLoggingFilter extends AbstractRequestLoggingFilter {

    private static final String ANONYMOUS_USER = "anonymousUser";

    /**
     * ロギングがアクティブか取得
     *
     * @param request current HTTP request
     */
    @Override
    protected boolean shouldLog(final HttpServletRequest request) {
        return log.isInfoEnabled();
    }

    /**
     * リクエスト前のロギング
     *
     * @param request current HTTP request
     * @param message the message to log
     */
    @Override
    protected void beforeRequest(final HttpServletRequest request, final String message) {
        log.info(message);
    }

    /**
     * リクエスト後のロギング
     *
     * @param request current HTTP request
     * @param message the message to log
     */
    @Override
    protected void afterRequest(final HttpServletRequest request, final String message) {
        // リクエスト前にロギングするので、ここでは何もしない
    }

    /**
     * ログメッセージを作成
     *
     * @param request current HTTP request
     * @param prefix prefix
     * @param suffix suffix
     * @return the message to log
     */
    @Override
    protected String createMessage(final HttpServletRequest request, final String prefix, final String suffix) {
        final var message = new StringBuilder();
        message.append(prefix);

        // HTTPリクエスト
        message.append(request.getMethod()).append(" ");
        message.append(request.getRequestURI());

        // クエリパラメータ
        final var queryString = request.getQueryString();
        if (Objects.nonNull(queryString)) {
            message.append("?").append(queryString);
        }

        // クライアント情報
        final var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.nonNull(authentication) && !Objects.equals(authentication.getName(), ANONYMOUS_USER)) {
            message.append(", user=").append(authentication.getName());
        }

        message.append(suffix);
        return message.toString();
    }

}
