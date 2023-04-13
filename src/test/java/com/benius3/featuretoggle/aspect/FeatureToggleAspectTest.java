package com.benius3.featuretoggle.aspect;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.benius3.featuretoggle.annotation.FeatureToggle;
import java.lang.annotation.Annotation;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.env.Environment;
import org.springframework.web.server.ResponseStatusException;

/**
 * <code>FeatureToggleAspectTest</code> is a test class for testing class {@link FeatureToggleAspect}.
 *
 * @author masonhsieh
 * @version 1.0
 */
public class FeatureToggleAspectTest {

    @Mock
    private Environment evn;

    @Mock
    private ProceedingJoinPoint joinPoint;

    @InjectMocks
    private FeatureToggleAspect featureToggleAspect;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * 假設功能名稱不是以「features.」開頭，功能切面{@link FeatureToggleAspect}將繼續執行原始方法。
     */
    @Test
    void shouldProceedWhenFeatureNameIsNotStartsWithFeatures() throws Throwable {
        String featureName = "test.enabled";
        assumeProceed(featureName);
        verifyProceeded(featureName);
    }

    /**
     * 假設功能已開啟，功能切面{@link FeatureToggleAspect}將繼續執行原始方法。
     */
    @Test
    void shouldProceedWhenFeatureIsEnabled() throws Throwable {
        String featureName = "features.test.enabled";
        assumeProceed(featureName);
        verifyProceeded(featureName);
    }

    /**
     * 假設功能已關閉，功能切面{@link FeatureToggleAspect}將引發{@link ResponseStatusException}並返回HTTP 403狀態碼。
     */
    @Test
    void shouldThrowResponseStatusExceptionWhenFeatureIsDisabled() throws Throwable {
        String featureName = "features.test.disabled";
        assumeNotProceedForFeature(featureName);
        verifyThrowResponseStatusException(featureName);
    }

    private void assumeNotProceedForFeature(String featureName) {
        // when property features.test.disabled is false, then throw ResponseStatusException
        when(evn.getProperty(featureName, Boolean.class, true)).thenReturn(false);
    }

    private void verifyThrowResponseStatusException(String featureName) {
        assertThrows(ResponseStatusException.class, () -> {
            featureToggleAspect.aroundAdvice(joinPoint, new FeatureToggleImpl(featureName));
        });
    }

    private void verifyProceeded(String featureName) throws Throwable {
        Object response = featureToggleAspect.aroundAdvice(joinPoint, new FeatureToggleImpl(featureName));
        assertEquals("Enabled feature response", response);
    }

    private void assumeProceed(String featureName) throws Throwable {
        // when property features.test.enabled is true, then proceed
        when(evn.getProperty(featureName, Boolean.class, true)).thenReturn(true);
        when(joinPoint.proceed()).thenReturn("Enabled feature response");
    }

    private static class FeatureToggleImpl implements FeatureToggle {

        private final String featureName;

        FeatureToggleImpl(String featureName) {
            this.featureName = featureName;
        }

        @Override
        public Class<? extends Annotation> annotationType() {
            return FeatureToggle.class;
        }

        @Override
        public String featureName() {
            return featureName;
        }
    }
}
