package hello.proxy.configure.v6_aop.aspect;

import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;


/**
 *
 * 설명
 *
 * <pre>
 *     * @Aspect
 *     - AnnotationAwareAspectJAutoProxyCreator 는 Advisor 를 자동으로 찾아와서 필요한 곳에 Proxy 를 생성하고 적용
 *     - 추가로, @Aspect 를 찾아서 Advisor 를 생성해줌
 * </pre>
 *
 */
@Slf4j
@Aspect
public class LogTraceAspect {

    private final LogTrace trace;

    public LogTraceAspect(LogTrace trace) {
        this.trace = trace;
    }

    @Around("execution(* hello.proxy.app..*(..))")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        TraceStatus status = null;

        try {

            String message = joinPoint.getSignature().toShortString();

            status = trace.begin(message);

            // target 호출
            Object result = joinPoint.proceed();

            trace.end(status);

            return result;

        } catch (Exception e) {
            trace.exception(status, e);

            throw e;
        }
    }
}
