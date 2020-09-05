package Cafe4me(5.Team);

import Cafe4me(5.Team).config.kafka.KafkaProcessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class PolicyHandler{
    
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverOrderReceived_CannotOrderCencel(@Payload OrderReceived orderReceived){

        if(orderReceived.isMe()){
            System.out.println("##### listener CannotOrderCencel : " + orderReceived.toJson());
        }
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverOrderRejected_CannotOrderCencel(@Payload OrderRejected orderRejected){

        if(orderRejected.isMe()){
            System.out.println("##### listener CannotOrderCencel : " + orderRejected.toJson());
        }
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverOrderRejected_OrderCancelFromDelivery(@Payload OrderRejected orderRejected){

        if(orderRejected.isMe()){
            System.out.println("##### listener OrderCancelFromDelivery : " + orderRejected.toJson());
        }
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverProductInserted_OrderStart(@Payload ProductInserted productInserted){

        if(productInserted.isMe()){
            System.out.println("##### listener OrderStart : " + productInserted.toJson());
        }
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverProductDeleted_OrderStart(@Payload ProductDeleted productDeleted){

        if(productDeleted.isMe()){
            System.out.println("##### listener OrderStart : " + productDeleted.toJson());
        }
    }

}
