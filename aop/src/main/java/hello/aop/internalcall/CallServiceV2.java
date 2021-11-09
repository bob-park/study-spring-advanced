package hello.aop.internalcall;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CallServiceV2 {

    /*
     * 지연 조회
     */
    // ! 절대 ApplicationContext 를 주입 받으며 사용하지 말자 - 기능이 너무 많아 성능상 무리가 될 수 있다.
//    private final ApplicationContext applicationContext;
//
//    public CallServiceV2(ApplicationContext applicationContext) {
//        this.applicationContext = applicationContext;
//    }

    // Object Provider 에 특화되어 있음
    // 스프링 컨테이너에서 조회하는 것을 생성시점이 아닌 사용 시점에서 확인
    private final ObjectProvider<CallServiceV2> callServiceProvider;

    public CallServiceV2(
        ObjectProvider<CallServiceV2> callServiceProvider) {
        this.callServiceProvider = callServiceProvider;
    }

    public void external() {
        log.info("call external");

//        CallServiceV2 callService = applicationContext.getBean(CallServiceV2.class);

        CallServiceV2 callService = callServiceProvider.getObject(); // 사용 시점에 bean 을 주입함

        callService.internal(); // 내부 메서드 호출 (this.internal())
    }

    public void internal() {
        log.info("call internal");
    }

}
