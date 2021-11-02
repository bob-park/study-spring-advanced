package hello.proxy.pureproxy.concretproxy.code;

public class ConcreteClient {

  private final ConcreteLogic concreteLogic;

  public ConcreteClient(ConcreteLogic concreteLogic) {
    this.concreteLogic = concreteLogic;
  }

  public void execute() {
    concreteLogic.operaction();
  }
}
