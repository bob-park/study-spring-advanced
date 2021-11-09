package hello.aop.proxyvs;

import hello.aop.member.MemberService;
import hello.aop.member.MemberServiceImpl;
import hello.aop.proxyvs.code.ProxyDIAspect;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Slf4j
@Import(ProxyDIAspect.class)
//@SpringBootTest(properties = {"spring.aop.proxy-target-class=false"}) // JDK Dynamic Proxy 우선
@SpringBootTest(properties = {"spring.aop.proxy-target-class=true"}) // JDK Dynamic Proxy 우선
class ProxyDITest {

    // ! CGLIB Proxy 는 Concrete Class 을 상속받아 Proxy 를 생성함
    // 당연히, Interface 에 Casting 이 가능하다.
    @Autowired
    MemberService memberService;

    // ! JDK Dynamic Proxy 인 경우 DI 안됨 - Casting 이 안됨
    // Interface 기반으로 Proxy 로 만들기 때문에, 당연히 Interface 를 구현한 class 에 Casting 되지 않는다..
    @Autowired
    MemberServiceImpl memberServiceImpl;

    @Test
    void go() {
        log.info("memberService class={}", memberService.getClass());
        log.info("memberServiceImpl class={}", memberServiceImpl.getClass());

        memberServiceImpl.hello("helloA");
    }
}
