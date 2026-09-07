package com.yatharth.distributedurlshortener.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
public class KafkaErrorHandlingConfig {

    @Bean
    public DeadLetterPublishingRecoverer deadLetterPublishingRecoverer(
            KafkaTemplate<String, Object> kafkaTemplate) {

        return new DeadLetterPublishingRecoverer(
                kafkaTemplate,
                (ConsumerRecord<?, ?> record, Exception exception) ->
                        new TopicPartition(
                                record.topic() + ".DLT",
                                record.partition()
                        )
        );
    }

    @Bean
    public DefaultErrorHandler kafkaErrorHandler(
            DeadLetterPublishingRecoverer recoverer) {

        FixedBackOff backOff = new FixedBackOff(
                2000L,
                2L
        );

        return new DefaultErrorHandler(
                recoverer,
                backOff
        );
    }

    @Bean
    public NewTopic urlEventsDlt() {
        return new NewTopic(
                "url-events.DLT",
                1,
                (short) 1
        );
    }

    @Bean
    public NewTopic urlClickEventsTopic() {
        return new NewTopic(
                "url-click-events",
                1,
                (short) 1
        );
    }
}