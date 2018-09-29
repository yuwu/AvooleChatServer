import io.netty.handler.codec.mqtt.MqttConnectPayload;
import io.netty.handler.codec.mqtt.MqttMessageBuilders;

public class Main {

    public static void main(String[] args) {
        System.out.println("Hello World!");

        MqttMessageBuilders.ConnectBuilder builder = MqttMessageBuilders.connect();
        builder.willMessage("hellos");

        MqttConnectPayload mqttConnectPayload = builder.build().payload();
        System.out.println(mqttConnectPayload.willMessage());

        builder = MqttMessageBuilders.connect();
        builder.willMessage((byte[]) null);

        mqttConnectPayload = builder.build().payload();

        System.out.println(mqttConnectPayload.willMessageInBytes());
        System.out.println(mqttConnectPayload.willMessage());
    }
}
