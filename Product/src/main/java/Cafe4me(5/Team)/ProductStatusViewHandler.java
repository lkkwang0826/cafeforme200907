package Cafe4me(5.Team);

import Cafe4me(5.Team).config.kafka.KafkaProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ProductStatusViewHandler {


    @Autowired
    private ProductStatusRepository productStatusRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whenProductInserted_then_CREATE_1 (@Payload ProductInserted productInserted) {
        try {
            if (productInserted.isMe()) {
                // view 객체 생성
                ProductStatus productStatus = new ProductStatus();
                // view 객체에 이벤트의 Value 를 set 함
                productStatus.setId(productInserted.getProductId);
                productStatus.setProductStatus(productInserted.getStatus);
                // view 레파지 토리에 save
                productStatusRepository.save(productStatus);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }



    @StreamListener(KafkaProcessor.INPUT)
    public void whenProductDeleted_then_DELETE_1(@Payload ProductDeleted productDeleted) {
        try {
            if (productDeleted.isMe()) {
                // view 레파지 토리에 삭제 쿼리
                productStatusRepository.deleteById(productDeleted.getId);
                // view 레파지 토리에 삭제 쿼리
                productStatusRepository.deleteByProductId(productDeleted.getId);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}