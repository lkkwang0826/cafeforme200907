package Cafe4me(5.Team);

public class OrderRejected extends AbstractEvent {

    private Long id;
    private Long orderId;

    public OrderRejected(){
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
}
