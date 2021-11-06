package hello.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;

/**
 *
 * Advice 실행 순서 (동일한 Aspect 안에서만 적용)
 *
 * <pre>
 *     - Around -> Before -> After -> AfterReturning -> AfterThrowing
 *     - Advice 적용 순서는 이렇게 되지만, 호출 순서와 리턴 순서는 서로 반대인 것을 생각하자
 * </pre>
 *
 *
 */
@Slf4j
@Aspect
public class AspectV6Advice {

    /**
     * Around
     *
     * <pre>
     *     - Before, AfterReturning, AfterThrowing, After 등을 모두 한번에 할 수 있음
     *     - 무조건 파라미터에 ProceedJoinPoint 를 첫번쨰 파라미터로 받아야 한다.
     *     - Method 실행 전 - 후 에 실행됨
     *     - JoinPoint 의 실행 여부 선택 가능
     *     - 전달값 변환 가능
     *     - 반환값 변환 가능
     *     - 예외 변환 가능
     *     - Transaction 과 같이 try ~ catch ~ finally 사용 가능
     *     - ProceedJoinPoint.proceed() 여러번 실행 가능
     * </pre>
     *
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("hello.aop.order.aop.Pointcuts.orderAndService()")
    public Object doTransaction(ProceedingJoinPoint joinPoint) throws Throwable {

        try {
            // @Before
            log.info("Transaction start... {}", joinPoint.getSignature());
            Object result = joinPoint.proceed();

            // @AfterReturning
            log.info("Transaction commit... {}", joinPoint.getSignature());

            return result;
        } catch (Exception e) {
            // @AfterThrowing
            log.info("Transaction rollback... {}", joinPoint.getSignature());
            throw e;
        } finally {
            // @After
            log.info("Resource release... {}", joinPoint.getSignature());
        }
    }

    /**
     * join point 의 실행 여부 및 예외 발생 여부 상관없이 실행되므로, join point 실행 및 throws 를 할 수 없다.
     *
     * <pre>
     *     - JoinPoint 됨
     *     - 작업의 흐름을 변경할 수 없음
     *     - Method 종료시 바로 다음 타겟이 실행됨
     *     - Exception 발생시 다음 타겟가 실행되지 않음
     * </pre>
     *
     * @param joinPoint
     */
    @Before("hello.aop.order.aop.Pointcuts.orderAndService()")
    public void doBefore(JoinPoint joinPoint) {
        log.info("[doBefore] {}", joinPoint.getSignature());
    }

    /**
     * ! 주의사항
     * <pre>
     *     - return 을 변경할 수 없다. (조작은 가능 - setter)
     *     - Method 가 정상적으로 실행될때 실행됨
     *     - returning 속성에 사용된 이름과 advice method 의 매개변수가 일치해야함
     *     - returning 절에 지정된 타입과 맞은 타입을 대상으로 실행된다
     *          (returning = String, method result type = void -> 실행 안됨)
     *          (returning = Object, method result type = void -> 실행됨)
     *
     * </pre>
     *
     * @param joinPoint
     * @param result
     */
    @AfterReturning(value = "hello.aop.order.aop.Pointcuts.orderAndService()", returning = "result")
    public void doReturn(JoinPoint joinPoint, Object result) {
        log.info("[return] {} return={}", joinPoint.getSignature(), result);
    }

    /**
     * AfterThrowing
     *
     * <pre>
     *     - throwing 속성에 사용된 이름과 advice method 의 매개변수가 일치해야함
     *     - throwing 절에 지정된 타입과 맞은 예외를 대상으로 실행된다. (@AtfterReturning 과 같은 원리)
     * </pre>
     *
     * @param joinPoint
     * @param ex
     */
    @AfterThrowing(value = "hello.aop.order.aop.Pointcuts.orderAndService()", throwing = "ex")
    public void doThrowing(JoinPoint joinPoint, Exception ex) {
        log.info("[ex] {} message={}", joinPoint.getSignature(), ex.getMessage());
    }

    /**
     * After
     *
     * <pre>
     *     - Method 실행이 종료되면 실행됨 (finally 를 생각하면 됨)
     *     - 정상 또는 예외 상관없이 모두 실행된다.
     *     - 일반적으로 리소스를 해제하는데 사용
     * </pre>
     *
     * @param joinPoint
     */
    @After(value = "hello.aop.order.aop.Pointcuts.orderAndService()")
    public void doAfter(JoinPoint joinPoint) {
        log.info("[after] {}", joinPoint.getSignature());
    }
}
