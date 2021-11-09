package hello.aop.internalcall;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CallServiceV1 {

    /*
     ! 생성자로 넣으면 안된다. - 순환 참조로 인해 exception 발생

     * setter 로 주입받는다.
     */
    private CallServiceV1 callService;

    public void external() {
        log.info("call external");

        callService.internal(); // 내부 메서드 호출 (this.internal())
    }

    public void internal() {
        log.info("call internal");
    }

    @Autowired
    public void setCallServiceV1(CallServiceV1 callService) {
        this.callService = callService;
    }
}
