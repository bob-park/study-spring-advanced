package hello.aop.exam.aop;

import hello.aop.exam.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Slf4j
@Aspect
public class RetryAspect {

    /**
     * ! @annotation(retry) -> retry 가 aspect method 의 파라미터로 대체된다.
     *
     * @param joinPoint
     * @param retry
     * @return
     * @throws Throwable
     */
    @Around("@annotation(retry)")
    public Object doRetry(ProceedingJoinPoint joinPoint, Retry retry) throws Throwable {

        log.info("[retry] {} retry={}", joinPoint.getSignature(), retry);

        int maxRetry = retry.value();

        Exception exceptionHolder = null;

        for (int retryCount = 1; retryCount <= maxRetry; retryCount++) {
            try {

                log.info("[retry] try count={}/{}", retryCount, maxRetry);

                return joinPoint.proceed();
            } catch (Exception e) {
                exceptionHolder = e;
            }
        }

        if (exceptionHolder == null) {
            throw new IllegalStateException("머냐 이거");
        }

        throw exceptionHolder;

    }

}
