//package com.project.airbnb.configuration.Redis;
//
//package com.project.airbnb.configuration.Redis;
//
//import com.project.airbnb.service.Notification.BookingNotificationListener;
//import com.project.airbnb.service.Notification.ReviewNotificationListener;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.listener.ChannelTopic;
//import org.springframework.data.redis.listener.RedisMessageListenerContainer;
//import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
//
//@Configuration
//public class RedisMessageConfig {
//
//    @Bean
//    public RedisMessageListenerContainer redisMessageListenerContainer(
//            RedisConnectionFactory connectionFactory,
//            MessageListenerAdapter bookingListenerAdapter,
//            MessageListenerAdapter reviewListenerAdapter) {
//
//        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
//        container.setConnectionFactory(connectionFactory);
//        container.addMessageListener(bookingListenerAdapter, new ChannelTopic("booking:notifications"));
//        container.addMessageListener(reviewListenerAdapter, new ChannelTopic("review:notifications"));
//        return container;
//    }
//
//    @Bean
//    public MessageListenerAdapter bookingListenerAdapter(BookingNotificationListener listener) {
//        return new MessageListenerAdapter(listener, "onMessage");
//    }
//
//    @Bean
//    public MessageListenerAdapter reviewListenerAdapter(ReviewNotificationListener listener) {
//        return new MessageListenerAdapter(listener, "onMessage");
//    }
//}