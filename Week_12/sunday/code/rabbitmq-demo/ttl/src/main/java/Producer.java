import com.rabbitmq.client.*;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Producer {
    public static void main(String[] args) throws Exception{

        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setUri("amqp://root:123456@192.168.198.100:5672/%2f");

        try (final Connection connection = connectionFactory.newConnection();
             final Channel channel = connection.createChannel()){

            channel.exchangeDeclare("ttl.exchange", BuiltinExchangeType.DIRECT, true, false, null);

            Map<String, Object> argument = new HashMap<>();
            //队列中的消息多长时间没有被消费掉就会被删除
            argument.put("x-message-ttl", 1000 * 10);
            //队列中多长时间没有消费者进行消费， 队列会被删除
            argument.put("x-expires", 1000 * 40);
            channel.queueDeclare("ttl.queue", true, false, false, argument);
            channel.queueBind("ttl.queue", "ttl.exchange", "ttl.routingkey");


            AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
                    .deliveryMode(2).build();
            channel.basicPublish("ttl.exchange", "ttl.routingkey", properties, "Hello, message".getBytes(StandardCharsets.UTF_8));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
