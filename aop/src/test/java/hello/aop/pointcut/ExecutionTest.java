package hello.aop.pointcut;

import static org.assertj.core.api.Assertions.assertThat;

import hello.aop.member.MemberServiceImpl;
import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

@Slf4j
class ExecutionTest {

    AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();

    Method helloMethod;

    @BeforeEach
    public void init() throws NoSuchMethodException {
        helloMethod = MemberServiceImpl.class.getMethod("hello", String.class);
    }

    @Test
    void printMethod() {

        log.info("helloMethod={}", helloMethod);
    }

    @Test
    void exactMatch() {
        // execution(접근제어자? 반환타입 선언타입?.메서드이름(파라미터))
        pointcut.setExpression(
            "execution(public String hello.aop.member.MemberServiceImpl.hello(String))");

        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void allMatch() {
        pointcut.setExpression("execution(* *(..))");

        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void nameMatch() {

        pointcut.setExpression("execution(* hello(..))");

        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();

    }

    @Test
    void nameMatchStar1() {

        pointcut.setExpression("execution(* hel*(..))");

        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();

    }

    @Test
    void nameMatchStar2() {

        pointcut.setExpression("execution(* *el*(..))");

        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();

    }

    @Test
    void nameMatchFalse() {

        pointcut.setExpression("execution(* none(..))");

        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isFalse();

    }

    @Test
    void packageMatch1() {

        pointcut.setExpression("execution(* hello.aop.member.MemberServiceImpl.hello(..))");

        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();

    }

    @Test
    void packageMatch2() {
        pointcut.setExpression("execution(* hello.aop.member.*.*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void packageMatchFalse() {
        // ! .*.* 인 경우 무조건 패키지 하위 레벨이 맞아야된다.
        pointcut.setExpression("execution(* hello.aop.*.*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isFalse();
    }

    @Test
    void packageMatchTrue() {
        // ! . 과 .. 의 차이를 알아두어야 한다.
        pointcut.setExpression("execution(* hello.aop..*.*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void typeExactMatch() {
        pointcut.setExpression("execution(* hello.aop.member.MemberServiceImpl.*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void typeMatchSuperType() {
        // execution
        // 부모 타입인 경우 - 당연히 다형성으로 인해 매치가 된다.
        // 단, 부모 타입에 선언한 메서드만 가능
        pointcut.setExpression("execution(* hello.aop.member.MemberService.*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void typeMatchNoSuperTypeMethodFalse() throws NoSuchMethodException {
        pointcut.setExpression("execution(* hello.aop.member.MemberService.*(..))");

        Method internalMethod = MemberServiceImpl.class.getMethod("internal", String.class);

        assertThat(pointcut.matches(internalMethod, MemberServiceImpl.class)).isFalse();
    }

    @Test
    void typeMatchInternal() throws NoSuchMethodException {
        pointcut.setExpression("execution(* hello.aop.member.MemberServiceImpl.*(..))");

        Method internalMethod = MemberServiceImpl.class.getMethod("internal", String.class);

        assertThat(pointcut.matches(internalMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void argsMatch() {
        // String Type 의 파라미터
        pointcut.setExpression("execution(* *(String))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void argsMatchNoArgs() {
        // 파라미터가 없는 경우
        pointcut.setExpression("execution(* *())");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isFalse();
    }

    @Test
    void argsMatchStar() {
        // 모든 타입의 하나의 파라미터
        pointcut.setExpression("execution(* *(*))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void argsMatchComplex() {
        // String 으로 시작하고, 나머지는 모든 타입의 파라미터
        pointcut.setExpression("execution(* *(String, ..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

}
