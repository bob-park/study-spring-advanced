package hello.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Slf4j
@Aspect
public class AspectV2 {

    // pointcut 분리
    // 같은 pointcut 에 여러개의 advice 를 적용할 떄 사용할 수 있음
    @Pointcut("execution(* hello.aop.order..*(..))")
    private void allOrder() {
        // pointcut signature
    } // pointcut signature (method name + parameters)

    @Around("allOrder()") // pointcut 및 pointcut signature 사용가능
    public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("[log] {}", joinPoint.getSignature()); // join point signature

        return joinPoint.proceed();
    }


}
