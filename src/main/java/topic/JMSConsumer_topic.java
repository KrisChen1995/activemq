package topic;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

public class JMSConsumer_topic {
	public static final String ACTIVEMQ_URL = "tcp://192.168.136.128:61616";
	public static final String TOPIC_NAME = "topic02";
	public static void main(String[] args) throws JMSException, IOException {
		System.out.println("***我是三号消费者");
		ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
		Connection connection = activeMQConnectionFactory.createConnection();
		connection.start();
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Topic topic = session.createTopic(TOPIC_NAME);
		MessageConsumer consumer = session.createConsumer(topic);
		consumer.setMessageListener((message -> {
			if(null != message && message instanceof TextMessage){
				TextMessage textMessage = (TextMessage)message ;
				try {
					System.out.println("***topic消费者接收到的消息" + textMessage.getText());
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		}));
		System.in.read();
		consumer.close();
		session.close();
		connection.close();
	}
}
