
package local;

public class OrderReceived extends AbstractEvent {

    private Long orderId;

    public Long getOrderId() {
        return orderId;
    }
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
}
