package com.javainuse.topic;

import com.javainuse.AppProperties;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.net.URISyntaxException;


//non-durable subscribing
public class JmsTopicExample {
    public static void main(String[] args) throws URISyntaxException, Exception {
        // BrokerService broker = BrokerFactory.createBroker(new URI(
        //       "broker:(tcp://localhost:61616)"));
        //  broker.start();
        Connection connection = null;
        try {
            String brokerUrl = AppProperties.getPropValue("broker.url");
            // Producer
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
                    brokerUrl);
            connection = connectionFactory.createConnection();
            Session session = connection.createSession(false,
                    Session.AUTO_ACKNOWLEDGE);
            Topic topic = session.createTopic("ani-test-topic-2");

            // Consumer1 subscribes to customerTopic
            MessageConsumer consumer1 = session.createConsumer(topic);
            consumer1.setMessageListener(new ConsumerMessageListener("Consumer1"));

            // Consumer2 subscribes to customerTopic
            MessageConsumer consumer2 = session.createConsumer(topic);
            consumer2.setMessageListener(new ConsumerMessageListener("Consumer2"));

            connection.start();

            // Publish
            String payload = "Hello from topic producer ";
            Message msg = session.createTextMessage(payload);
            MessageProducer producer = session.createProducer(topic);
            System.out.println("Sending text '" + payload + "'");
            producer.send(msg);

            Thread.sleep(3000);
            session.close();
        } finally {
            if (connection != null) {
                connection.close();
            }
            // broker.stop();
        }
    }
}