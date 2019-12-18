package persitent;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class JMSProduce_topic_persitent {
	public static final String ACTIVEMQ_URL = "tcp://192.168.136.128:61616";
	public static final String TOPIC_NAME = "topic02";
	public static void main(String[] args) throws JMSException {
		ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
		Connection connection = activeMQConnectionFactory.createConnection();
		connection.start();
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Topic topic = session.createTopic(TOPIC_NAME);
		MessageProducer producer = session.createProducer(topic);
		producer.setDeliveryMode(DeliveryMode.PERSISTENT);//队列默认持久化
		for (int i =1; i<=3; i++){
			TextMessage textMessage = session.createTextMessage("*****TOPIC_NAME" + i);
//			textMessage.setIntProperty("k1", 2);
//			MapMessage mapMessage = session.createMapMessage();
//			mapMessage.setString("k1", "v1");
			producer.send(textMessage);
		}
		producer.close();
		session.close();
		connection.close();
		System.out.println("***topic消息发布完成");
	}
}
