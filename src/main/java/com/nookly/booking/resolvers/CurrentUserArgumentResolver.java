package com.nookly.booking.resolvers;

import com.nookly.booking.annotation.CurrentUser;
import com.nookly.booking.security.model.CustomUserDetails;
import com.nookly.booking.user.model.User;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.UUID;

@Component
public class CurrentUserArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CurrentUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new SecurityException("Authentication required");
        }

        Object principal = authentication.getPrincipal();

        Class<?> targetType = parameter.getParameterType();

        if (principal instanceof CustomUserDetails userDetails) {

            if (targetType.equals(UUID.class)) return userDetails.getId();
            if (targetType.equals(User.class)) return userDetails.getUser();
            if (targetType.equals(CustomUserDetails.class)) return principal;
        }

        throw new IllegalArgumentException("Unsupported parameter type");
    }
}
