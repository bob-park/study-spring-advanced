package hello.aop.order.aop.order;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Slf4j
@Aspect
public class AspectV3 {

    // pointcut 분리
    // 같은 pointcut 에 여러개의 advice 를 적용할 떄 사용할 수 있음
    @Pointcut("execution(* hello.aop.order..*(..))")
    private void allOrder() {
        // pointcut signature
    } // pointcut signature (method name + parameters)

    // Transaction
    // class name pattern : *Service
    @Pointcut("execution(* *..*Service.*(..))")
    private void allService() {
        // pointcut signature
    }

    @Around("allOrder()") // pointcut 및 pointcut signature 사용가능
    public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("[log] {}", joinPoint.getSignature()); // join point signature

        return joinPoint.proceed();
    }

    // hello.aop.order package 및 하위 포함이면서 class name pattern 이 *Service
    @Around("allOrder() && allService()")
    public Object doTransaction(ProceedingJoinPoint joinPoint) throws Throwable {

        try {
            log.info("Transaction start... {}", joinPoint.getSignature());
            Object result = joinPoint.proceed();
            log.info("Transaction commit... {}", joinPoint.getSignature());

            return result;
        } catch (Exception e) {
            log.info("Transaction rollback... {}", joinPoint.getSignature());
            throw e;
        } finally {
            log.info("Resource release... {}", joinPoint.getSignature());
        }


    }


}
