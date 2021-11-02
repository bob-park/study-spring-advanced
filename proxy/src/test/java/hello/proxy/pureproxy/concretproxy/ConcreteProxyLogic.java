package hello.proxy.pureproxy.concretproxy;

import hello.proxy.pureproxy.concretproxy.code.ConcreteClient;
import hello.proxy.pureproxy.concretproxy.code.ConcreteLogic;
import hello.proxy.pureproxy.concretproxy.code.TimeProxy;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class ConcreteProxyLogic {

  @Test
  void noProxy() {
    ConcreteLogic concreteLogic = new ConcreteLogic();
    ConcreteClient client = new ConcreteClient(concreteLogic);

    client.execute();
  }

  @Test
  void addProxy() {
    ConcreteLogic concreteLogic = new ConcreteLogic();

    TimeProxy timeProxy = new TimeProxy(concreteLogic);

    ConcreteClient client = new ConcreteClient(timeProxy);

    client.execute();
  }
}
