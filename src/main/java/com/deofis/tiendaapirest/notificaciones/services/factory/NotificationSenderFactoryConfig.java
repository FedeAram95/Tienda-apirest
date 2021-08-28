package com.deofis.tiendaapirest.notificaciones.services.factory;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.ServiceLocatorFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class NotificationSenderFactoryConfig {

    private final BeanFactory beanFactory;
    
    public ServiceLocatorFactoryBean notificationSenderFactoryLocator() {
        final ServiceLocatorFactoryBean locator = new ServiceLocatorFactoryBean();
        locator.setServiceLocatorInterface(NotificationSenderFactory.class);
        locator.setBeanFactory(beanFactory);

        return locator;
    }

    @Bean
    public NotificationSenderFactory notificationSenderFactory() {
        final ServiceLocatorFactoryBean locator = notificationSenderFactoryLocator();
        locator.afterPropertiesSet();
        return (NotificationSenderFactory) locator.getObject();
    }
    
}
