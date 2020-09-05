package local;

public class OrderCancelConfirmed extends AbstractEvent {

    private Long orderId;

    public OrderCancelConfirmed(){
        super();
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
}
