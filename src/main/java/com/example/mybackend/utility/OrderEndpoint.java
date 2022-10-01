package com.example.mybackend.utility;


import com.example.mybackend.entity.OrderItem;
import com.example.mybackend.entity.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.net.http.WebSocket;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@ServerEndpoint(value = "/orderWs/{userId}", encoders = {OrderItemEncoder.class})
public class OrderEndpoint {

    private Session session;
    private Integer userid;

    private static CopyOnWriteArraySet<OrderEndpoint> webSockets =new CopyOnWriteArraySet<>();
    private static ConcurrentHashMap<Integer,Session> sessionPool = new ConcurrentHashMap<Integer,Session>();

    @OnOpen
    public void onOpen(final Session session, @PathParam(value = "userId")Integer userid) {
        System.out.println("got connection userid=" + userid);
        this.session = session;
        this.userid = userid;
        webSockets.add(this);
        sessionPool.put(userid, session);
    }

    @OnError
    public void errorhandler(Session session, Throwable error) {
        System.out.println("error:" + error.getMessage());
    }

    public void sendBuyOne(OrderItemSession value) throws EncodeException, IOException {
        Session session = sessionPool.get(Integer.parseInt(value.getUserid()));
        try {
            if (session!=null && session.isOpen()) {
                System.out.println("send to session userid = " + value.getUserid());
                session.getBasicRemote().sendObject(new Result<OrderItem>(value.getCode(), value.getMsg(), null));
            }
            else System.out.println("session error");
        } catch (EncodeException e) {
            System.out.println("Fatal: encode exception");
        } catch (IOException e) {
            System.out.println("Fatal: io exception");
        }
    }
}
