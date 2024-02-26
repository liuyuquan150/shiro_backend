package indi.ly.crush.encryp;

import lombok.NonNull;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.SimpleByteSource;

/**
 * <h2>密码加密</h2>
 * <p>
 *     基于 {@code Shiro API} 实现的密码加密工具类.
 * </p>
 *
 * @since 1.0
 * @author 云上的云
 * @formatter:off
 */
public final class PasswordEncryption {

    private static final SecureRandomNumberGenerator RANDOM_NUMBER_GENERATOR = new SecureRandomNumberGenerator();

    /**
     * <p>
     *     使用 {@code Shiro} 提供的 {@link SimpleHash} 类进行密码加密.
     * </p>
     *
     * @param algorithmName 加密算法名称, 如 {@code SHA-256}、{@code MD5} 等. 选择算法时需要考虑其安全性, 推荐使用 {@code SHA-256} 或更安全的算法.
     * @param password      原始密码. 在实际应用中, 用户提供的密码.
     * @param salt          盐值, 用于增加加密的复杂度. 为了提高安全性, 每个用户的盐值应该是唯一的并保持安全.
     * @param iterations    散列迭代次数, 增加该值可以提高密码的安全性, 但同时也会增加加密过程的计算量. 推荐的迭代次数至少为 {@code 1024}.
     * @return 返回加密后的密码散列值, 即 {@code 16} 进制的字符串形式.
     */
    public static String encryptPassword(
            @NonNull String algorithmName, @NonNull String password,
            @NonNull String salt, int iterations
    ) {
        return new SimpleHash(algorithmName, password, new SimpleByteSource(salt), iterations).toHex();
    }

    /**
     * <p>
     *     生成唯一的盐值.
     * </p>
     *
     * @return 返回生成的盐值, {@code 16} 进制字符串形式.
     */
    public static @NonNull String generateSalt() {
        return RANDOM_NUMBER_GENERATOR.nextBytes().toHex();
    }
}