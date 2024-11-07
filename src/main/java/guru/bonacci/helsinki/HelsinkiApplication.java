package guru.bonacci.helsinki;

import java.util.Arrays;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HelsinkiApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(HelsinkiApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {
		String secret = "not found";
		for (String arg : args) {
       if (!arg.startsWith("--"))
      	 secret = arg;
		}

    System.out.println(secret);

		try (MqttClient client = new MqttClient("tcp://mqtt.digitransit.fi:1883?digitransit-subscription-key=" + secret, MqttClient.generateClientId(),
				new MemoryPersistence())) {
			client.setCallback(new MqttCallback() {

				@Override
				public void connectionLost(Throwable cause) { 
					cause.printStackTrace();
				}

				@Override
				public void messageArrived(String topic, MqttMessage message) throws Exception {
					
					
					System.out.println(topic + ": " + Arrays.toString(message.getPayload()));
				}

				@Override
				public void deliveryComplete(IMqttDeliveryToken token) {
				}
			});

			client.connect();
			client.subscribe("/gtfsrt/vp/tampere/+/+/+/5/+/Keskustori/#", 1);
		}

	}

}
