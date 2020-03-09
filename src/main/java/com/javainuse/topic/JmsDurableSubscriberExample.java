package com.javainuse.topic;

import com.javainuse.AppProperties;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.net.URISyntaxException;

public class JmsDurableSubscriberExample {
    public static void main(String[] args) throws URISyntaxException, Exception {
        // BrokerService broker = BrokerFactory.createBroker(new URI(
        //      "broker:(tcp://localhost:61616)"));
        // broker.start();
        Connection connection = null;
        try {
            String brokerUrl = AppProperties.getPropValue("broker.url");
            // Producer
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
                    brokerUrl);
            connection = connectionFactory.createConnection();
            connection.setClientID("DurabilityTest");
            Session session = connection.createSession(false,
                    Session.AUTO_ACKNOWLEDGE);
            Topic topic = session.createTopic("ani-test-durable-topic-1");

            // Publish
            String payload = "Hello from durable topic ..producer ";
            TextMessage msg = session.createTextMessage(payload);
            MessageProducer publisher = session.createProducer(topic);
            System.out.println("Sending text '" + payload + "'");
            publisher.send(msg, javax.jms.DeliveryMode.PERSISTENT, Message.DEFAULT_PRIORITY, Message.DEFAULT_TIME_TO_LIVE);

            // Consumer1 subscribes to customerTopic
            MessageConsumer consumer1 = session.createDurableSubscriber(topic, "consumer1", "", false);

            // Consumer2 subscribes to customerTopic
            MessageConsumer consumer2 = session.createDurableSubscriber(topic, "consumer2", "", false);

            connection.start(); // connection starts here.. imp

            msg = (TextMessage) consumer1.receive();
            System.out.println("Consumer1 receives " + msg.getText());


            msg = (TextMessage) consumer2.receive();
            System.out.println("Consumer2 receives " + msg.getText());

            session.close();
        } finally {
            if (connection != null) {
                connection.close();
            }
            //  broker.stop();
        }
    }
}