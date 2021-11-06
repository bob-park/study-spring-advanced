package hello.proxy.configure.v4_postprocessor;

import hello.proxy.configure.AppV1Config;
import hello.proxy.configure.AppV2Config;
import hello.proxy.configure.v3_proxyfactory.advice.LogTraceAdvice;
import hello.proxy.configure.v4_postprocessor.postprocessor.PackageLogTracePostProcessor;
import hello.proxy.trace.logtrace.LogTrace;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.Advisor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Slf4j
@Configuration
@Import({AppV1Config.class, AppV2Config.class})
public class BeanPostProcessorConfig {

    @Bean
    public PackageLogTracePostProcessor logTracePostProcessor(LogTrace trace) {

        return new PackageLogTracePostProcessor("hello.proxy.app", getAdvisor(trace));
    }

    private Advisor getAdvisor(LogTrace trace) {
        // pointcut
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();

        pointcut.setMappedNames("request*", "order*", "save*");

        // advice
        LogTraceAdvice advice = new LogTraceAdvice(trace);

        return new DefaultPointcutAdvisor(pointcut, advice);
    }

}
