import com.rabbitmq.client.*;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class ProducerMessageTtl {
    public static void main(String[] args) throws Exception{

        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setUri("amqp://root:123456@192.168.198.100:5672/%2f");

        try (final Connection connection = connectionFactory.newConnection();
             final Channel channel = connection.createChannel()){

            channel.exchangeDeclare("ttl.exchange", BuiltinExchangeType.DIRECT, true, false, null);


            channel.queueDeclare("ttl.queue", true, false, false, null);
            channel.queueBind("ttl.queue", "ttl.exchange", "ttl.routingkey");


            AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
                    .deliveryMode(2)
                    //为每个消息设置TTL时间
                    .expiration("10000")
                    .build();
            channel.basicPublish("ttl.exchange", "ttl.routingkey", properties, "Hello, message".getBytes(StandardCharsets.UTF_8));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
