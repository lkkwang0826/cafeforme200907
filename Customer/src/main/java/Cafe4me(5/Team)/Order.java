package Cafe4me(5.Team);

import javax.persistence.*;
import org.springframework.beans.BeanUtils;
import java.util.List;

@Entity
@Table(name="Order_table")
public class Order {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String product;
    private Integer qty;
    private Integer price;

    @PrePersist
    public void onPrePersist(){
        OrderSelected orderSelected = new OrderSelected();
        BeanUtils.copyProperties(this, orderSelected);
        orderSelected.publishAfterCommit();


        PaymentCompleted paymentCompleted = new PaymentCompleted();
        BeanUtils.copyProperties(this, paymentCompleted);
        paymentCompleted.publishAfterCommit();


        OrderCancelRequested orderCancelRequested = new OrderCancelRequested();
        BeanUtils.copyProperties(this, orderCancelRequested);
        orderCancelRequested.publishAfterCommit();


        OrderCanceled orderCanceled = new OrderCanceled();
        BeanUtils.copyProperties(this, orderCanceled);
        orderCanceled.publishAfterCommit();


    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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




}
