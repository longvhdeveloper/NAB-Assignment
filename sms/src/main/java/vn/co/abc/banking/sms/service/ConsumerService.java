package vn.co.abc.banking.sms.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import vn.co.abc.banking.sms.message.SMSVoucherMessage;

@Service
@Slf4j
public class ConsumerService {

    @KafkaListener(topics = "${spring.kafka.topic.sendSMS}", containerFactory = "smsVoucherKafkaListenerContainerFactory")
    public void listen(SMSVoucherMessage message) {
        log.info("Received send sms voucher message: {}", message);

        log.info("Send voucher {} to phone number {}......", message.getVoucherCode(), message.getPhoneNumber());
    }
}
