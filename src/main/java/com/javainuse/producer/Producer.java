package com.javainuse.producer;

import com.javainuse.AppProperties;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class Producer implements Runnable {

    public void run() {
        try { // Create a connection factory.
            String brokerUrl = AppProperties.getPropValue("broker.url");
            ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(brokerUrl); //from activemq terminal console "Listening to ...

            //Create connection.
            Connection connection = factory.createConnection();

            // Start the connection
            connection.start();

            // Create a session which is non transactional
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // Create Destination queue
            Destination queue = session.createQueue("ani-test-Q-1");

            // Create a producer
            MessageProducer producer = session.createProducer(queue);

            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            String msg ;

            // insert message
            for(int i=0;i<10;i++) {
                msg = "Hello " + i;
                TextMessage message = session.createTextMessage(msg);
                System.out.println("Producer Sent: " + msg);
                producer.send(message);
            }
            producer.send( session.createTextMessage("exit"));  //terminate msg for ConsumerO
            session.close();
            connection.close();
        }
        catch (Exception ex) {
            System.out.println(ex);
        }

    }

}
