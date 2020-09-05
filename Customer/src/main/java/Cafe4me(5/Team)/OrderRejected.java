
package Cafe4me(5.Team);

public class OrderRejected extends AbstractEvent {

    private Long id;
    private Long OrderID;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Long getOrderId() {
        return OrderID;
    }

    public void setOrderId(Long OrderID) {
        this.OrderID = OrderID;
    }
}
