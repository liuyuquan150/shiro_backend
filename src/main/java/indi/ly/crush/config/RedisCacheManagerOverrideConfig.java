package indi.ly.crush.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.crazycake.shiro.CacheManagerProperties;
import org.crazycake.shiro.IRedisManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.ShiroRedisAutoConfiguration;
import org.crazycake.shiro.exception.SerializationException;
import org.crazycake.shiro.serializer.ObjectSerializer;
import org.crazycake.shiro.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * <h2>{@link RedisCacheManager} 覆盖配置</h2>
 * <p>
 *     本类专门用于覆盖 {@link RedisCacheManager RedisCacheManager Bean} 的一些默认配置, 如 {@code VALUE} 的序列化器:
 *     <pre>{@code
 *                  public class RedisCacheManager implements CacheManager {
 *                      private RedisSerializer valueSerializer = new ObjectSerializer();
 *                  }
 *     }</pre>
 *     将其替换为基于 {@code JSON} 的序列化器, 旨在提高 {@code Redis} 中存储数据的可读性和兼容性. <br /> <br />
 *
 *     {@link RedisCacheManager RedisCacheManager Bean} 来自引入的 {@code shiro-redis-spring-boot-starter},
 *     而且对外暴露的 {@link CacheManagerProperties} 并没有提供灵活的配置选项来设置 {@code VALUE} 序列化器:
 *     <pre>{@code
 *                  @ConfigurationProperties(prefix = "shiro-redis.cache-manager")
 *                  public class CacheManagerProperties {
 *                      private String principalIdFieldName;
 *                      private Integer expire;
 *                      private String keyPrefix;
 *                      // ......
 *                  }
 *     }</pre>
 *     因此只好将 {@link RedisCacheManager RedisCacheManager Bean} 引入并覆盖它默认的序列化器({@link ObjectSerializer}). <br />
 *     使用 {@code Java} 原生的序列化方式, 缺点是序列化后的数据不易于阅读和调试, 因为它是二进制格式. <br /> <br />
 *
 *     注意事项
 *     <ol>
 *         <li>
 *             覆盖第三方库的 {@code Bean} 可能会影响库的预期行为, 特别是如果这个 {@code Bean} 涉及到复杂的初始化逻辑或者是库内部紧密依赖的组件. <br />
 *             在覆盖或修改之前, 确保理解该 {@code Bean} 在库中的作用、对第三方库的修改不会引入未预期的副作用.
 *         </li>
 *         <li>
 *             如果第三方库没有提供灵活的配置选项来修改 {@code Bean} 的属性, 可能需要联系库的维护者或查阅文档, 看是否有推荐的扩展点或配置方法.
 *         </li>
 *     </ol>
 * </p>
 *
 * @since 1.0
 * @see RedisCacheManager#valueSerializer
 * @see ShiroRedisAutoConfiguration#redisCacheManager(IRedisManager)
 * @see CacheManagerProperties
 * @author 云上的云
 * @formatter:off
 */
@Component
public class RedisCacheManagerOverrideConfig {
    private final RedisCacheManager redisCacheManager;
    private final ObjectMapper objectMapper;

    public RedisCacheManagerOverrideConfig(
            RedisCacheManager redisCacheManager,
            ObjectMapper objectMapper
    ) {
        this.redisCacheManager = redisCacheManager;
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void overrideValueSerializerConfig() {
        this.redisCacheManager.setValueSerializer(new RedisSerializer<>() {
            @Override
            public byte[] serialize(Object o) throws SerializationException {
                if (o == null) return new byte[0];

                try {
                    return objectMapper.writeValueAsBytes(o);
                } catch (Exception ex) {
                    throw new SerializationException("Could not write JSON: %s.".formatted(ex.getMessage()), ex);
                }
            }

            @Override
            public Object deserialize(byte[] bytes) throws SerializationException {
                if (bytes == null || bytes.length == 0) return null;

                try {
                    return objectMapper.readValue(bytes, 0, bytes.length, Object.class);
                } catch (Exception ex) {
                    throw new SerializationException("Could not read JSON: %s.".formatted(ex.getMessage()), ex);
                }
            }
        });
    }
}
