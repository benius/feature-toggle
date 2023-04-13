package com.benius3.featuretoggle.annotation;

import com.benius3.featuretoggle.aspect.FeatureToggleAspect;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <code>FeatureToggle</code>用來標記方法，以啟用功能切面{@link FeatureToggleAspect}以檢查指定的功能是否啟用。
 *
 * @author masonhsieh
 * @version 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FeatureToggle {

    String featureName() default "";
}
