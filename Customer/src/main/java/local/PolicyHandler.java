package local;

import local.config.kafka.KafkaProcessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class PolicyHandler{
    @Autowired
    OrderRepository orderRepository;

    // 오더 수락 됨 -> Cancel 불가
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverOrderReceived_CannotOrderCancel(@Payload OrderReceived orderReceived){

        if(orderReceived.isMe()){
            System.out.println("##### listener CannotOrderCencel : " + orderReceived.toJson());

            Order order = orderRepository.findById(orderReceived.getOrderId()).get();
            order.setCannotOrderCanceled(true);
            orderRepository.save(order);
        }
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverOrderRejected_PayBack(@Payload OrderRejected orderRejected){

        if(orderRejected.isMe()){
            System.out.println("#### 오더가 거절 되었습니다. : " + orderRejected.getOrderId().toString());

            // TO-DO : Payback 구현
        }
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverOrderCancelConfirmed_ReceiveCancelConfirmed(@Payload OrderCancelConfirmed orderCancelConfirmed){

        if(orderCancelConfirmed.isMe()){
            System.out.println("#### 오더가 취소 되었습니다. : " + orderCancelConfirmed.getOrderId().toString());

            // TO-DO : Payback 구현
        }
    }

}
