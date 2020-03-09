package com.javainuse.consumer;

import com.javainuse.AppProperties;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class Consumer implements Runnable {

    public void run() {
        try {
            String text = "";
            Session session = null;
            Connection connection = null;

                String brokerUrl = AppProperties.getPropValue("broker.url");
                ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(brokerUrl);

                //Create Connection
                connection = factory.createConnection();

                // Start the connection
                connection.start();

                // Create Session
                session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

                //Create queue
                Destination queue = session.createQueue("ani-test-Q-1");

                MessageConsumer consumer = session.createConsumer(queue);
             do {
                Message message = consumer.receive(1000);

                if (message instanceof TextMessage) {
                    TextMessage textMessage = (TextMessage) message;
                    text = textMessage.getText();
                    System.out.println("Consumer Received: " + text);
                }
            } while (!text.equalsIgnoreCase("exit"));
            session.close();
            connection.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }

    }

}
