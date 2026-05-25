package com.backendsystemdesign.config;

import java.time.Duration;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;

import org.springframework.data.redis.connection.RedisConnectionFactory;

import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableCaching
public class RedisConfig {

    @Bean
    public RedisCacheManager cacheManager(
            RedisConnectionFactory connectionFactory) {

        GenericJackson2JsonRedisSerializer valueSerializer =
                new GenericJackson2JsonRedisSerializer();

        StringRedisSerializer keySerializer =
                new StringRedisSerializer();

        RedisCacheConfiguration config =
                RedisCacheConfiguration
                        .defaultCacheConfig()

                        // TTL
                        .entryTtl(Duration.ofMinutes(10))

                        // Key Serializer
                        .serializeKeysWith(
                                RedisSerializationContext
                                        .SerializationPair
                                        .fromSerializer(keySerializer)
                        )

                        // Value Serializer
                        .serializeValuesWith(
                                RedisSerializationContext
                                        .SerializationPair
                                        .fromSerializer(valueSerializer)
                        )

                        // Prevent caching null values
                        .disableCachingNullValues();

        return RedisCacheManager
                .builder(connectionFactory)
                .cacheDefaults(config)
                .build();
    }
}