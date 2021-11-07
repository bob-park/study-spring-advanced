package hello.aop.order.aop.order;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;

@Slf4j
public class AspectV5Order {

    @Aspect
    @Order(2) // ! Order 는 Class 단위에서만 적용된다.
    public static class LogAspect {

        @Around("hello.aop.order.aop.order.Pointcuts.allOrder()")
        public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable {
            log.info("[log] {}", joinPoint.getSignature());

            return joinPoint.proceed();
        }
    }

    @Aspect
    @Order(1)
    public static class TxAspect {

        @Around("hello.aop.order.aop.order.Pointcuts.orderAndService()")
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


}
