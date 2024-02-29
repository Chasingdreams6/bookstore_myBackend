package com.example.mybackend.utility;

import com.example.mybackend.entity.Cart;
import com.example.mybackend.entity.OrderItem;
import com.example.mybackend.entity.Result;
import com.example.mybackend.service.OrderService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import javax.websocket.EncodeException;
import java.io.IOException;

@Component
public class OrderListener {

    @Autowired
    private OrderService orderService;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private OrderEndpoint orderEndpoint;

    @KafkaListener(topics = "buyTopic", groupId = "group_buy")
    public void buyListener(ConsumerRecord<String, String> record) {
        String[] value = record.value().split(",");
        Result<OrderItem> res =
                orderService.buyBookByISBN(value[0],
                        Integer.parseInt(value[1]),
                        Integer.parseInt(value[2]));
        String data = res.getCode() + "," + value[1] + "," + res.getMsg();
        kafkaTemplate.send("buyResultTopic", "key", data);
    }

    @KafkaListener(topics = "buyAllTopic", groupId = "group_buy_all")
    public void buyAllListener(ConsumerRecord<String, String> record) {
        String userid = record.value();
        Result<Cart> res = orderService.buyBooks(Integer.parseInt(userid));
        String data = res.getCode() + "," + userid + "," + res.getMsg();
        kafkaTemplate.send("buyResultTopic", "key", data);
    }

    @KafkaListener(topics = "buyResultTopic", groupId = "group_buy_result")
    public void buyResultListener(ConsumerRecord<String, String> record) throws EncodeException, IOException {
        String[] value = record.value().split(",");
        try {
            orderEndpoint.sendBuyOne(new OrderItemSession(Integer.parseInt(value[0]), value[2], value[1]));
        } catch (EncodeException e) {
            System.out.println("fatal: encode exception");
        } catch (IOException e) {
            System.out.println("fatal: io exception");
        }
    }
}
