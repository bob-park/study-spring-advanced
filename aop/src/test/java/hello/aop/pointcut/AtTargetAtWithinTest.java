package hello.aop.pointcut;

import hello.aop.member.annotation.ClassAop;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@Slf4j
@Import({AtTargetAtWithinTest.Config.class})
@SpringBootTest
class AtTargetAtWithinTest {

    @Autowired
    private Child child;

    @Test
    void success() {
        log.info("child proxy={}", child.getClass());
        child.childMethod(); // 부모, 자식 모두 있는 메서드
        child.parentMethod();// 부모 클래스만 있는 메서드
    }


    static class Config {

        @Bean
        public Parent parent() {
            return new Parent();
        }

        @Bean
        public Child child() {
            return new Child();
        }

        @Bean
        public AtTargetAtWithinAspect atTargetAtWithinAspect() {
            return new AtTargetAtWithinAspect();
        }
    }


    static class Parent {

        public void parentMethod() {
            // 부모에만 있는 메서드
        }
    }

    @ClassAop
    static class Child extends Parent {

        public void childMethod() {
            // 자식에만 있는 메서드
        }
    }

    @Slf4j
    @Aspect
    static class AtTargetAtWithinAspect {

        /*
         ! @target, @within 은 반드시 단독으로 사용하면 안된다.
         
         * args, @args, @target 은 실제 객체 인스턴스가 생성되고 실행될때 Advice 적용 여부를 확인할 수 있다.
         ! 실행 시점에 일어나는 포인트컷 적용 여부도 결국 프록시가 있어야 실행 시점에 판단할 수 있으므로, 프록시가 없다면, 판단 자체가 불가능
         
         * Spring Container 가 proxy 를 생성하는 시점은 Spring Container 가 만들어지는 Application Loading 시점에 적용할 수 있다.
         * args, @args, @target 같은 포인트컷 지시자가 있으면 Spring 은 모든 Spring Bean 에 AOP 를 적용하려고 시도하지만, proxy 가 없는 경우 실행 시점 판단 자체가 불가능
         
         ! 문제는 모든 Spring Bean 에 AOP 를 적용하려고 하면 Spring 내부에서 사용하는 Bean 중 final 로 지정된 bean 들도 있기 때문에 오류가 발생
         
         ! 따라서, 이러한 표현식은 최대한 Proxy 적용 대상을 축소하는 표현식과 함계 사용해야한다.

         */

        // @target: 인스턴스 기준으로 모든 메서드의 조인 포인트를 선정, 부모 타입의 메서드도 적용
        @Around("execution(* hello.aop..*(..)) && @target(hello.aop.member.annotation.ClassAop)")
        public Object atTarget(ProceedingJoinPoint joinPoint) throws Throwable {
            log.info("[@target] {}", joinPoint.getSignature());

            return joinPoint.proceed();
        }

        // @within: 선택된 클래스 내부에 있는 메서드만 조인 포인트로 선정, 부모 타입의 메서드는 적용되지 않음
        @Around("execution(* hello.aop..*(..)) && @within(hello.aop.member.annotation.ClassAop)")
        public Object atWithin(ProceedingJoinPoint joinPoint) throws Throwable {
            log.info("[@within] {}", joinPoint.getSignature());

            return joinPoint.proceed();
        }
    }


}
