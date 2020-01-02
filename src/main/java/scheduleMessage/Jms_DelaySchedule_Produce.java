package scheduleMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ScheduledMessage;

import javax.jms.*;

public class Jms_DelaySchedule_Produce {
	public static final String ACTIVEMQ_URL = "tcp://192.168.136.128:61616";
	public static final String QUEUE_NAME = "queue-delay";
	public static final long DALAY = 3 * 1000 ; //延迟投递的时间
	public static final long PERIOD = 4 * 1000 ; //重复投递的时间间隔
	public static final int REPEAT = 5 ; //重复投递的次数
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

		//5.创建消息的生产者
		MessageProducer producer = session.createProducer(queue);

		producer.setDeliveryMode(DeliveryMode.PERSISTENT);

		//6.通过使用MessageProducer生产3条消息发送到MQ的队列里面
		for(int i=1 ; i<=3 ; i++){
			//7.创建消息
			TextMessage textMessage = session.createTextMessage("msg-----" + i);//理解为一个字符串
			textMessage.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, DALAY);
			textMessage.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_PERIOD, PERIOD);
			textMessage.setIntProperty(ScheduledMessage.AMQ_SCHEDULED_REPEAT, REPEAT);
			//8.通过MessageProducer发送给MQ
			producer.send(textMessage);
		}
		producer.close();
		session.close();
		connection.close();
		System.out.println("*******************消息发布到MQ完成");
	}
}
