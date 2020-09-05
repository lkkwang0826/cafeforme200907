package local;

import java.util.Dictionary;

public class OrderSelected extends AbstractEvent {

    private Long orderId;
    private String product;
    private Integer qty;
    private Integer price;
    private Boolean paymentFail;

    public OrderSelected(){
        super();
        paymentFail = false;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Boolean isPaymentFail() {
        return paymentFail;
    }

    public void setPaymentFail(Boolean paymentFail) {
        this.paymentFail = paymentFail;
    }
}
