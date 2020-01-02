package asyncSend;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

public class Jms_AsyncSend_Consumer {
	public static final String ACTIVEMQ_URL = "tcp://192.168.136.128:61616";
	public static final String QUEUE_NAME = "queue-asyncSend";
	public static void main(String[] args) throws JMSException, IOException {

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
			}
			consumer.close();
			session.close();
			connection.close();*/
			//通过监听的方式消费消息
			/*
			* 异步非阻塞的方式(监听器的onMessage方法)，接收者通过consumer注册的消息监听器监听当消息到达时，系统
			* 自动调用监听器的MessageListener的onMessage(Message message)方法
			* */
			consumer.setMessageListener((message) ->{
				if(null != message && message instanceof TextMessage){
					TextMessage textMessage = (TextMessage)message ;
					try {
						System.out.println("********消费者接收到消息" + textMessage.getText());
					} catch (JMSException e) {
						e.printStackTrace();
					}
				}
			});
			System.in.read();//保证控制台不关,保证一直监听消息
			consumer.close();
			session.close();
			connection.close();

	}
}
