package persitent;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

public class JMSConsumer_topic_persitent {
	public static final String ACTIVEMQ_URL = "tcp://192.168.136.128:61616";
	public static final String TOPIC_NAME = "jdbc01";
	public static void main(String[] args) throws JMSException, IOException {
		System.out.println("***我是三号消费者");
		ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
		Connection connection = activeMQConnectionFactory.createConnection();
		connection.setClientID("三号");
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Topic topic = session.createTopic(TOPIC_NAME);
		TopicSubscriber topicSubscriber = session.createDurableSubscriber(topic, "remark....");
		connection.start();

		Message message = topicSubscriber.receive();
		while (null != message){
			TextMessage textMessage = (TextMessage)message;
			System.out.println("****收到的持久化topic" + textMessage.getText());
			message = topicSubscriber.receive(1000L);
		}
		session.close();
		connection.close();
		/*
			一定要先运行一次消费者，向mq注册，类似订阅主题
			然后运行消费者，无论消费者在线与否，都会收到消息，不在线的话会下次连接的时候都会接收到消息
		 */
	}
}
