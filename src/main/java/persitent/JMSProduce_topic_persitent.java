package persitent;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class JMSProduce_topic_persitent {
	public static final String ACTIVEMQ_URL = "tcp://192.168.136.128:61616";
	public static final String TOPIC_NAME = "jdbc01";
	public static void main(String[] args) throws JMSException {
		ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
		Connection connection = activeMQConnectionFactory.createConnection();

		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Topic topic = session.createTopic(TOPIC_NAME);
		MessageProducer producer = session.createProducer(topic);
		producer.setDeliveryMode(DeliveryMode.PERSISTENT);//topic持久化
		connection.start();
		for (int i =1; i<=3; i++){
			TextMessage textMessage = session.createTextMessage("*****topic-persist" + i);
//			textMessage.setIntProperty("k1", 2);
//			MapMessage mapMessage = session.createMapMessage();
//			mapMessage.setString("k1", "v1");
			producer.send(textMessage);
		}
		producer.close();
		session.close();
		connection.close();
		System.out.println("***topic-persist消息发布完成");
	}
}
