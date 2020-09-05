package Cafe4me(5.Team);

import javax.persistence.*;
import org.springframework.beans.BeanUtils;
import java.util.List;

@Entity
@Table(name="Delivery_table")
public class Delivery {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private Long orderId;
    private Long product;
    private Integer qty;

    @PrePersist
    public void onPrePersist(){
        OrderReceived orderReceived = new OrderReceived();
        BeanUtils.copyProperties(this, orderReceived);
        orderReceived.publishAfterCommit();


        OrderRejected orderRejected = new OrderRejected();
        BeanUtils.copyProperties(this, orderRejected);
        orderRejected.publishAfterCommit();


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
    public Long getProduct() {
        return product;
    }

    public void setProduct(Long product) {
        this.product = product;
    }
    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }




}
