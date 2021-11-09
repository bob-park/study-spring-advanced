package hello.aop.internalcall.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Slf4j
@Aspect
public class CallLogAspect {

    /**
     * 프록시의 내부 메서드 호출는 적용되지 않는다.
     * <p>
     * proxy 가 아닌 target 의 내부 메서드를 호출하기 때문
     *
     * @param joinPoint
     */
    @Before("execution(* hello.aop.internalcall..*.*(..))")
    public void doLog(JoinPoint joinPoint) {
        log.info("aop={}", joinPoint.getSignature());
    }

}
