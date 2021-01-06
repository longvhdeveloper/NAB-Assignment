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
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import vn.co.abc.banking.api.message.AbstractMessage;
import vn.co.abc.banking.api.message.VoucherMessage;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Slf4j
public class KafkaConsumerConfig {

    @Value(value = "${spring.kafka.consumer.bootstrap-servers}")
    private String bootstrapAddress;

    public ConsumerFactory<String, VoucherMessage> voucherConsumerFactory() {

        JsonDeserializer<VoucherMessage> deserializer = new JsonDeserializer<>(VoucherMessage.class);


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
    public ConcurrentKafkaListenerContainerFactory<String, VoucherMessage> voucherKafkaListenerContainerFactory(
            KafkaTemplate<String, AbstractMessage> kafkaTemplate
    ) {
        ConcurrentKafkaListenerContainerFactory<String, VoucherMessage> factory
                = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(voucherConsumerFactory());

//        factory.setRetryTemplate(kafkaRetry());
//        factory.setRecoveryCallback(retryContext -> {
//            ConsumerRecord consumerRecord = (ConsumerRecord) retryContext.getAttribute("record");
//            log.info("Recovery is called for message {} ", consumerRecord.value());
//            kafkaTemplate.send(voucherTopicDLT, (VoucherMessage) consumerRecord.value());
//            return Optional.empty();
//        });

//        factory.setErrorHandler(new SeekToCurrentErrorHandler(new DeadLetterPublishingRecoverer(kafkaTemplate),
//                new FixedBackOff(1500, 1)));

        return factory;
    }
}
