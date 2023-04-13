package com.benius3.featuretoggle.aspect;

import com.benius3.featuretoggle.annotation.FeatureToggle;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

/**
 * <code>FeatureToggleAspect</code>為功能切面，用於檢查功能是否啟用。
 * 使用 annotation @FeatureToggle() 標記方法，即可啟用此切面，檢查指定的功能是否啟用。
 * 當功能被禁用時，將引發ResponseStatusException並返回HTTP 403狀態碼。
 *
 * @author masonhsieh
 * @version 1.0
 */
@Aspect
@Component
public class FeatureToggleAspect {

    @Autowired
    private Environment environment;

    @Around("@annotation(featureToggle)")
    public Object aroundAdvice(ProceedingJoinPoint joinPoint, FeatureToggle featureToggle) throws Throwable {
        // 如果功能被禁用，則引發ResponseStatusException並返回HTTP 403狀態碼
        if (!isFeatureEnabled(featureToggle.featureName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, String.format("Feature `%s` is not enabled.", featureToggle.featureName()));
        }
        
        // 如果功能啟用，則繼續執行原始方法
        return joinPoint.proceed();
    }

    private boolean isFeatureEnabled(String featureName) {
        // featureName必須以`features.`開頭才有效，否則視為此功能已啟用
        if (!featureName.startsWith("features.")) {
            return true;
        }

        // 使用Environment bean從application.properties文件中讀取功能狀態
        // 如果找不到該功能的設置，則預設為啟用
        return environment.getProperty(featureName, Boolean.class, true);
    }
}
