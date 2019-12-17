import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class JmsConsumer {
	public static final String ACTIVEMQ_URL = "tcp://192.168.136.128:61616";
	public static final String QUEUE_NAME = "queue01";
	public static void main(String[] args) throws JMSException {

			//1.创建连接工厂,按照给定的URL地址采用默认用户名和密码
			ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);

			//2.通过连接工厂获得连接并启动访问
			Connection connection = activeMQConnectionFactory.createConnection();
			connection.start();

			//3.创建会话session
			//两个参数分别是boolean 事务，int 签收
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

			//4.创建目的地(是主题还是队列)
			Queue queue = session.createQueue(QUEUE_NAME);

			//5.创建消息的消费者
			MessageConsumer consumer = session.createConsumer(queue);
			/*
			同步阻塞方法(receive)
			订阅者或接收者调用MessageConsumer的receive方法来接收消息，receive方法在能够接收到消息之前(或者超时之前)将一直阻塞
			while(true){
				//6.消费者接收消息,接收的消息类型与生产的消息类型一致(TextMessage)
				TextMessage textMessage = (TextMessage)consumer.receive(4000L);//时间4s后没有消息消费就离开
				if(textMessage != null){
					System.out.println("********消费者接收到消息" + textMessage.getText());
				}else {
					break;
				}
			} */
			consumer.close();
			session.close();
			connection.close();
	}
}
