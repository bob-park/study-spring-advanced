package hello.proxy.configure.v5_autoproxy;

import hello.proxy.configure.AppV1Config;
import hello.proxy.configure.AppV2Config;
import hello.proxy.configure.v3_proxyfactory.advice.LogTraceAdvice;
import hello.proxy.trace.logtrace.LogTrace;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({AppV1Config.class, AppV2Config.class})
public class AutoProxyConfig {

    /**
     * spring-boot-starter-aop 의 AutoProxyCreator 가 Advisor 의 pointcut 과 advice 를 확인 및 판단하여 자동으로
     * proxy 를 생성 및 pointcut 을 가지고 advice 가 적용 여부 판단 후 사용
     *
     * @param trace
     * @return
     */
//    @Bean
    public Advisor advisor1(LogTrace trace) {
        // pointcut
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();

        pointcut.setMappedNames("request*", "order*", "save*");

        // advice
        LogTraceAdvice advice = new LogTraceAdvice(trace);

        return new DefaultPointcutAdvisor(pointcut, advice);
    }

    //    @Bean
    public Advisor advisor2(LogTrace trace) {
        // pointcut
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();

        pointcut.setExpression("execution(* hello.proxy.app..*(..))");

        // advice
        LogTraceAdvice advice = new LogTraceAdvice(trace);

        return new DefaultPointcutAdvisor(pointcut, advice);
    }

    @Bean
    public Advisor advisor3(LogTrace trace) {
        // pointcut
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();

        pointcut.setExpression(
            "execution(* hello.proxy.app..*(..)) && !execution(* hello.proxy.app..noLog(..))");

        // advice
        LogTraceAdvice advice = new LogTraceAdvice(trace);

        return new DefaultPointcutAdvisor(pointcut, advice);
    }

}
