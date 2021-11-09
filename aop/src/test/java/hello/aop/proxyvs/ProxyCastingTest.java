package hello.aop.proxyvs;

import hello.aop.member.MemberService;
import hello.aop.member.MemberServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactory;

/**
 *
 * jdk dynamic proxy 는 대상 객체인 Concrete Class 로 casting 이 불가능
 * CGLIB proxy 는 대상 객체인 Concrete Class 로 casting 이 가능
 *
 */
@Slf4j
class ProxyCastingTest {

    @Test
    void jdkProxy() {
        MemberServiceImpl target = new MemberServiceImpl();

        ProxyFactory proxyFactory = new ProxyFactory(target);

        proxyFactory.setProxyTargetClass(false);

        // proxy -> interface cast
        MemberService memberServiceProxy = (MemberService) proxyFactory.getProxy(); // success

        // interface 로 proxy 를 생성했기 때문 - 실패
        Assertions.assertThrows(ClassCastException.class,
            () -> {
                MemberServiceImpl castingMemberService = (MemberServiceImpl) memberServiceProxy; // failed
            });

    }

    @Test
    void cglibProxy() {
        MemberServiceImpl target = new MemberServiceImpl();

        ProxyFactory proxyFactory = new ProxyFactory(target);

        proxyFactory.setProxyTargetClass(true);

        // proxy -> interface cast
        MemberService memberServiceProxy = (MemberService) proxyFactory.getProxy(); // success

        // concrete class 로 proxy 를 생성했기 때문 - 성공
        MemberServiceImpl castingMemberService = (MemberServiceImpl) memberServiceProxy; // 성공

    }
}
