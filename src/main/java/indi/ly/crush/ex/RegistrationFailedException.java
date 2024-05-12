package indi.ly.crush.ex;

import java.io.Serial;

/**
 * <h2>注册失败异常</h2>
 * <p>
 *     表示在用户注册过程中遇到的特定业务逻辑错误的异常类,
 *     当注册流程中出现无法满足业务要求的情况时(如用户名已存在、数据验证失败等)应抛出此异常. <br /> <br />
 *
 *     示例用法:
 *     <pre>{@code
 *                  if (userExists) {
 *                      throw new RegistrationFailedException("用户名已存在");
 *                  }
 *     }</pre>
 * </pre>
 * </p>
 *
 * @since 1.0
 * @author 云上的云
 * @formatter:off
 */
public class RegistrationFailedException
        extends BusinessException {
    @Serial
    private static final long serialVersionUID = -9011457427178200198L;

    public RegistrationFailedException(String message) {
        this(message, null);
    }

    public RegistrationFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
