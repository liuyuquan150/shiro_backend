package indi.ly.crush;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <h2>公共应用程序</h2>
 *
 * <h3>4 个影响程序员效率的行为</h3>
 * <ol>
 *     <li>在功能不清的情况下开始编程.</li>
 *     <li>在无效社交上浪费时间.</li>
 *     <li>不会休息导致体力不佳.</li>
 *     <li>目标远大、行动滞后.</li>
 * </ol>
 *
 * <h3>特殊注释的含义</h3>
 * <ul>
 *     <li>
 *         BUG: 该注释下方的代码存在 BUG.
 *     </li>
 *     <li>
 *         HACK: 该注释下方的代码实现走了一个捷径, 应当包含为何使用 HACK 的原因, 这也可能表明该问题可能会有更好的解决方法.
 *     </li>
 *     <li>
 *         FIXME: 该注释下方的代码是有问题的, 应尽快修复.
 *     </li>
 *     <li>
 *         TODO: 该注释下方的代码还未完成, 应当包含下一步要做的事情.
 *     </li>
 *     <li>
 *         XXX: 该注释下方的代码虽然实现了功能, 但是实现的方法有待商榷.
 *     </li>
 *     <li>
 *         NOTE: 该注释下方的代码, 说明工作方式.
 *     </li>
 * </ul>
 *
 * @since 1.0
 * @author 云上的云
 * @formatter:off
 */
@SpringBootApplication(scanBasePackages = {"indi.ly.crush"})
public class ShiroBackendApplication {
	public static void main(String[] args) {
		// 打包带注释: mvn clean source:jar install -DskipTests
		SpringApplication application = new SpringApplication(ShiroBackendApplication.class);
		application.run(args);
	}
}
