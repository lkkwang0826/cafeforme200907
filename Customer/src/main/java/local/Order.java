package local;

import javax.persistence.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import local.config.kafka.KafkaProcessor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.MimeTypeUtils;

import java.util.Dictionary;
import java.util.List;

@Entity
@Table(name="Order_table")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long orderId;
    private String product;
    private Integer qty;
    private Integer price;
    private String status;
    private Boolean cannotOrderCanceled = false;

    @PostPersist
    public void eventPublish() throws JsonProcessingException {
        OrderSelected orderSelected = new OrderSelected();
        BeanUtils.copyProperties(this, orderSelected);
        orderSelected.publish();

        // 결제 시작
        if (Math.random() > 0.5){
            // 결제 성공
            PaymentCompleted paymentCompleted = new PaymentCompleted();
            BeanUtils.copyProperties(orderSelected, paymentCompleted);
            paymentCompleted.publish();

        } else {
            // 결제 실패
            orderSelected.setPaymentFail(true);
            orderSelected.publish();
        }
    }

    @PostUpdate
    public void onPostUpdate() throws JsonProcessingException {
        if ("order_cancel_request".equals(this.status)) {
            if (cannotOrderCanceled) {
                // 수락이 확인되어 요청을 보낼 수 없는 경우
                System.out.println("##### 주문 제작이 시작되어 취소할 수 없습니다.");
            } else {
                // 요청을 보낼 수 있는 경우 우선 취소 요청을 보냄
                System.out.println("##### 주문이 취소 요청 되었습니다. 이미 제작이 시작 된 경우 취소 요청이 거절될 수 있습니다.");
                OrderCancelRequested orderCancelRequested = new OrderCancelRequested();
                BeanUtils.copyProperties(this, orderCancelRequested);
                orderCancelRequested.publish();
            }
        }
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getCannotOrderCanceled() {
        return cannotOrderCanceled;
    }

    public void setCannotOrderCanceled(Boolean cannotOrderCanceled) {
        this.cannotOrderCanceled = cannotOrderCanceled;
    }
}