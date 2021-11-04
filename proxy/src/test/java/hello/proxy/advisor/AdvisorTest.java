package hello.proxy.advisor;

import hello.proxy.common.advice.TimeAdvice;
import hello.proxy.common.service.ServiceImpl;
import hello.proxy.common.service.ServiceInterface;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;

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
}
