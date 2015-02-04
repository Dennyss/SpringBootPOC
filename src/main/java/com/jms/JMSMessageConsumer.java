package com.jms;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.Reactor;
import reactor.event.Event;
import reactor.event.selector.Selectors;
import reactor.function.Consumer;

import javax.jms.*;

/**
 * Created by Denys Kovalenko on 10/6/2014.
 */
public class JMSMessageConsumer implements MessageListener, InitializingBean {
    private static String messageQueueName = "sm.stateh";
    private Session session;

    @Autowired
    private Reactor reactor;

    @Override
    public void afterPropertiesSet() {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_BROKER_URL);
        try {
            Connection connection = connectionFactory.createConnection();
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination queue = session.createQueue(messageQueueName);

            MessageConsumer consumer = session.createConsumer(queue);
            consumer.setMessageListener(this);
        } catch (JMSException e) {
            e.printStackTrace();
        }

        reactor.on(Selectors.object("topic"), new Consumer<Event<String>>() {
            public void accept(Event<String> ev) {
                System.out.println("greeting: " + ev.getData());
            }
        });
    }


    @Override
    public void onMessage(Message message) {
        TextMessage textMessage = (TextMessage) message;
        try {
            reactor.notify("topic", Event.wrap(textMessage.getText()));
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

}