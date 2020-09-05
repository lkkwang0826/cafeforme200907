package local;

import local.config.kafka.KafkaProcessor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class OrderStatusViewHandler {

    @Autowired
    private OrderStatusRepository orderStatusRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whenOrderSelected_then_CREATE_1 (@Payload OrderSelected orderSelected) {
        try {
            if (orderSelected.isMe()) {
                if (orderSelected.isPaymentFail())
                {
                    OrderStatus orderStatus = orderStatusRepository.findById(orderSelected.getOrderId()).get();
                    orderStatus.setStatus("pay_fail");
                    orderStatusRepository.save(orderStatus);
                }
                else
                {
                    OrderStatus orderStatus = new OrderStatus();
                    BeanUtils.copyProperties(orderSelected, orderStatus);
                    orderStatus.setStatus("init");
                    orderStatusRepository.save(orderStatus);
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @StreamListener(KafkaProcessor.INPUT)
    public void whenPaymentCompleted_then_UPDATE_1 (@Payload PaymentCompleted paymentCompleted) {
        try {
            if (paymentCompleted.isMe()) {
                OrderStatus orderStatus = orderStatusRepository.findById(paymentCompleted.getOrderId()).get();
                orderStatus.setStatus("pay_success");
                orderStatusRepository.save(orderStatus);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void whenOrderReceived_then_UPDATE_2 (@Payload OrderReceived orderReceived) {
        try {
            if (orderReceived.isMe()) {
                OrderStatus orderStatus = orderStatusRepository.findById(orderReceived.getOrderId()).get();
                orderStatus.setStatus("order_received");
                orderStatusRepository.save(orderStatus);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @StreamListener(KafkaProcessor.INPUT)
    public void whenOrderReceived_then_UPDATE_2 (@Payload OrderCancelConfirmed orderCancelConfirmed) {
        try {
            if (orderCancelConfirmed.isMe()) {
                OrderStatus orderStatus = orderStatusRepository.findById(orderCancelConfirmed.getOrderId()).get();
                orderStatus.setStatus("order_cancel");
                orderStatusRepository.save(orderStatus);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}