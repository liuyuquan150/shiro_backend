package indi.ly.crush.filter;

import indi.ly.crush.util.support.BaseJacksonUtil;
import lombok.NonNull;
import org.springframework.http.MediaType;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * <h2>{@code JSON} 响应构建器</h2>
 *
 * @since 1.0
 * @author 云上的云
 * @formatter:off
 */
final class JsonResponseBuilder {
    /**
     * <p>
     *     允许将给定的结果对象以 {@code JSON} 格式写入 {@code HTTP} 响应中. <br />
     *     该方法设置 {@code HTTP} 状态码, 指定内容类型为 {@code JSON}, 并使用 {@code UTF-8} 字符集编码, 确保跨语言和平台的兼容性. <br />
     *     利用 {@code Jackson} 库将结果对象序列化为 {@code JSON} 字符串, 最后将此字符串写入到响应体中, 为前端或调用者提供标准化的数据交互格式.
     * </p>
     *
     * @param response   {@code HTTP} 响应.
     * @param httpStatus {@code HTTP} 状态码.
     * @param result     待写入到响应体中的结果对象, 会被序列化为 {@code JSON} 字符串.
     * @throws IOException {@link ServletResponse#getWriter()} 如果出现输入或输出异常.
     */
    public static void buildJsonResponse(@NonNull HttpServletResponse response, int httpStatus, @NonNull Object result) throws IOException {
        response.setStatus(httpStatus);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        String msg = BaseJacksonUtil.wrap(om -> om.writeValueAsString(result));
        response.getWriter().write(msg);
    }
}