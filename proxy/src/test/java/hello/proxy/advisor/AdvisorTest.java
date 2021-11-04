package hello.proxy.advisor;

import hello.proxy.common.advice.TimeAdvice;
import hello.proxy.common.service.ServiceImpl;
import hello.proxy.common.service.ServiceInterface;
import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;

@Slf4j
class AdvisorTest {

    @Test
    void advisorTest1() {
        ServiceInterface target = new ServiceImpl();

        ProxyFactory proxyFactory = new ProxyFactory(target);

        // DefaultPointcutAdvisor 는 가장 일반적인 Advisor 구현체
        // 항상 true 인 pointcut
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(Pointcut.TRUE,
            new TimeAdvice());

        proxyFactory.addAdvisor(advisor); // ProxyFactory 에 적용할 Advisor 지정
        // addAdvice 를 사용해도 결과적으로는 DefaultPointcutAdvisor 가 생성되어 지정된다.

        ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();

        proxy.save();
        proxy.find();
    }

    @Test
    void advisorTest2() {
        ServiceInterface target = new ServiceImpl();

        ProxyFactory proxyFactory = new ProxyFactory(target);

        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(new MyPointcut(),
            new TimeAdvice());

        proxyFactory.addAdvisor(advisor);

        ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();

        proxy.save();
        proxy.find();
    }

    static class MyPointcut implements Pointcut {

        @Override
        public ClassFilter getClassFilter() {
            return ClassFilter.TRUE; // 항상 true
        }

        @Override
        public MethodMatcher getMethodMatcher() {
            return new MyMethodMatcher(); // 예시며, 실 사용시 성능최적화를 해야한다.
        }
    }

    @Slf4j
    static class MyMethodMatcher implements MethodMatcher {

        private static final String MATCH_NAME = "save";

        @Override
        public boolean matches(Method method, Class<?> targetClass) {

            boolean result = method.getName().equals(MATCH_NAME);

            log.info("Pointcut Call method={} targetClass={}", method.getName(),
                targetClass);
            log.info("Pointcut result={}", result);

            return result;
        }

        /**
         * true 인 경우 arguments 가 있는 matches() 가 호출된다.
         * <pre>
         * - arguments 가 있어 Spring 내부에서 cache 을 진행하여 성능향상을 기대하기 한다.
         * - 하지만, Cache 를 하기 때문에 성능상 문제가 될 수 있어 사용하지 않는다.
         * </pre>
         *
         * <p>
         * false 인 경우 matches() 가 호출된다.
         *
         * @return
         */
        @Override
        public boolean isRuntime() {
            return false;
        }

        @Override
        public boolean matches(Method method, Class<?> targetClass, Object... args) {
            return false;
        }
    }


    /**
     * Spring 이 제공하는 pointcut
     * <p>
     * ! Spring 은 엄청 많은 pointcut 을 제공한다.
     */
    @Test
    void advisorTest3() {
        ServiceInterface target = new ServiceImpl();

        ProxyFactory proxyFactory = new ProxyFactory(target);

        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedName("save");

        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(pointcut,
            new TimeAdvice());

        proxyFactory.addAdvisor(advisor);

        ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();

        proxy.save();
        proxy.find();
    }

}
