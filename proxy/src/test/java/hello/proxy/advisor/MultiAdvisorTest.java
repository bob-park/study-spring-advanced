package hello.proxy.advisor;

import hello.proxy.common.service.ServiceImpl;
import hello.proxy.common.service.ServiceInterface;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.jupiter.api.Test;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;

@Slf4j
class MultiAdvisorTest {

    /**
     * ! 단점
     *
     * <pre>
     *     - proxy 를 2번 생성해야함
     * </pre>
     */
    @Test
    void multiAdvisorTest1() {

        // client -> proxy2(advisor2) -> proxy1(advisor1) -> target

        // proxy1
        ServiceInterface target = new ServiceImpl();

        ProxyFactory proxyFactory1 = new ProxyFactory(target);

        DefaultPointcutAdvisor advisor1 =
            new DefaultPointcutAdvisor(Pointcut.TRUE, new Advice1());

        proxyFactory1.addAdvisor(advisor1);

        ServiceInterface proxy1 = (ServiceInterface) proxyFactory1.getProxy();

        // proxy2
        ProxyFactory proxyFactory2 = new ProxyFactory(proxy1);

        DefaultPointcutAdvisor advisor2 =
            new DefaultPointcutAdvisor(Pointcut.TRUE, new Advice2());

        proxyFactory2.addAdvisor(advisor2);

        ServiceInterface proxy2 = (ServiceInterface) proxyFactory2.getProxy();

        // proxy 실행
        proxy2.save();

    }

    @Test
    void multiAdvisorTest2() {

        /*
           Spring AOP 를 적용할 떄, 최적화를 진행해서 Proxy 는 하나만 생성하고, 하나의 Proxy 에 Advisor 여러개를 적용한다.
         */

        // client -> proxy -> advisor2 -> advisor1 -> target

        // proxy1
        ServiceInterface target = new ServiceImpl();

        ProxyFactory proxyFactory1 = new ProxyFactory(target);

        DefaultPointcutAdvisor advisor1 =
            new DefaultPointcutAdvisor(Pointcut.TRUE, new Advice1());
        DefaultPointcutAdvisor advisor2 =
            new DefaultPointcutAdvisor(Pointcut.TRUE, new Advice2());

        // add 한 순서대로 advisor 를 실행한다.
        proxyFactory1.addAdvisor(advisor2);
        proxyFactory1.addAdvisor(advisor1);

        // proxy 실행
        ServiceInterface proxy = (ServiceInterface) proxyFactory1.getProxy();

        proxy.save();


    }

    @Slf4j
    static class Advice1 implements MethodInterceptor {

        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            log.info("advice1 호출");
            return invocation.proceed();
        }
    }

    @Slf4j
    static class Advice2 implements MethodInterceptor {

        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            log.info("advice2 호출");
            return invocation.proceed();
        }
    }
}
