package br.com.bookstore.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableCaching
public class RedisConfig {

    private static final Duration CACHE_DURATION = Duration.ofMinutes(60);

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        ObjectMapper typingMapper = new ObjectMapper();
        typingMapper.activateDefaultTyping(
                LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.PROPERTY
        );

        Jackson2JsonRedisSerializer<Object> typingSerializer = new Jackson2JsonRedisSerializer<>(typingMapper, Object.class);

        ObjectMapper defaultMapper = new ObjectMapper();
        Jackson2JsonRedisSerializer<Object> defaultSerializer = new Jackson2JsonRedisSerializer<>(defaultMapper, Object.class);
        defaultMapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);

        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(defaultSerializer))
                .entryTtl(CACHE_DURATION);

        RedisCacheConfiguration withRelationship = RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(typingSerializer))
                .entryTtl(CACHE_DURATION);

        Map<String, RedisCacheConfiguration> configs = new HashMap<>();
        configs.put("booksById", withRelationship);

        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(defaultConfig)
                .withInitialCacheConfigurations(configs)
                .build();
    }
}