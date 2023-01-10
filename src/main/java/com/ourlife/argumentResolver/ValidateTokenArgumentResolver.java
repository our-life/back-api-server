package com.ourlife.argumentResolver;

import com.ourlife.utils.Impl.JwtTokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
@RequiredArgsConstructor
@Component
public class ValidateTokenArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtTokenUtils jwtTokenUtils;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean isToken = parameter.getParameterAnnotation(ValidateToken.class) != null;
        boolean isString = String.class.equals(parameter.getParameterType());
        return isToken && isString;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {

        HttpServletRequest httpServletRequest = webRequest.getNativeRequest(HttpServletRequest.class);

        String token = jwtTokenUtils.resolveToken(httpServletRequest);

        if (!jwtTokenUtils.validateToken(token)) {
            throw new IllegalStateException("유효하지 않은 토큰입니다.");
        }
        // argumentResolver 에서 exception 던지는게 좋은 선택인가? 고민해보자.
        // null 을 던져서 controller 에서 처리하는 방법도 있다.

        return token;
    }
}
