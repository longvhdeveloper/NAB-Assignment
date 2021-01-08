package vn.co.abc.banking.api.configuration;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import vn.co.abc.banking.api.message.DeletePassCodeMessage;
import vn.co.abc.banking.api.message.ResendVoucherMessage;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Slf4j
public class KafkaConsumerConfig {
    @Value(value = "${spring.kafka.consumer.bootstrap-servers}")
    private String bootstrapAddress;

    public ConsumerFactory<String, ResendVoucherMessage> voucherConsumerFactory() {

        JsonDeserializer<ResendVoucherMessage> deserializer = new JsonDeserializer<>(ResendVoucherMessage.class);


        deserializer.setRemoveTypeHeaders(false);
        deserializer.addTrustedPackages("*");
        deserializer.setUseTypeMapperForKey(true);

        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "groupDownloadStarter");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer);
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ResendVoucherMessage> voucherKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, ResendVoucherMessage> factory
                = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(voucherConsumerFactory());
        return factory;
    }

    public ConsumerFactory<String, DeletePassCodeMessage> passCodeConsumerFactory() {

        JsonDeserializer<DeletePassCodeMessage> deserializer = new JsonDeserializer<>(DeletePassCodeMessage.class);


        deserializer.setRemoveTypeHeaders(false);
        deserializer.addTrustedPackages("*");
        deserializer.setUseTypeMapperForKey(true);

        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "groupDownloadStarter");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer);
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, DeletePassCodeMessage> passcodeKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, DeletePassCodeMessage> factory
                = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(passCodeConsumerFactory());
        return factory;
    }
}
