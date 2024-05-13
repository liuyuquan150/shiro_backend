package indi.ly.crush.authenticator;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.realm.Realm;
import org.springframework.lang.NonNull;

import java.util.Collection;
import java.util.LinkedList;
import java.util.stream.Collectors;

/**
 * <h2>自定义模块化{@link Realm 安全域}认证器</h2>
 * <p>
 *     {@link ModularRealmAuthenticator} 支持认证策略的灵活应用, 默认采用 {@link AtLeastOneSuccessfulStrategy} 认证策略
 *     <pre>{@code
 *                  public ModularRealmAuthenticator() {
 *                      this.authenticationStrategy = new AtLeastOneSuccessfulStrategy();
 *                  }
 *     }</pre>
 *     服务于 {@link ModularRealmAuthenticator#doMultiRealmAuthentication doMultiRealmAuthentication} 方法
 *     <pre>{@code
 *                  protected AuthenticationInfo doMultiRealmAuthentication(
 *                      Collection<Realm> realms, AuthenticationToken token
 *                  ) {
 *                      AuthenticationStrategy strategy = getAuthenticationStrategy();
 *
 *                      // 省略剩下代码......
 *                  }
 *     }</pre>
 * </p>
 *
 * @since 1.0
 * @see AtLeastOneSuccessfulStrategy
 * @see ModularRealmAuthenticator#doSingleRealmAuthentication
 * @see ModularRealmAuthenticator#doMultiRealmAuthentication 
 * @author 云上的云
 * @formatter:off
 */
public class CustomModularRealmAuthenticator
        extends ModularRealmAuthenticator {
    @Override
    protected AuthenticationInfo doAuthenticate(AuthenticationToken token) throws AuthenticationException {
        super.assertRealmsConfigured();

        LinkedList<Realm> supportingRealms = this.getSupportedRealmsForToken(token);
        if (supportingRealms.isEmpty()) {
            throw new AuthenticationException("找不到支持给定认证令牌 [%s] 的安全域.".formatted(token));
        }

        if (supportingRealms.size() == 1) {
            return this.doSingleRealmAuthentication(supportingRealms.get(0), token);
        }

        return this.doMultiRealmAuthentication(supportingRealms, token);
    }

    @Override
    protected AuthenticationInfo doSingleRealmAuthentication(Realm realm, AuthenticationToken token) {
        // 省略 super#doSingleRealmAuthentication 方法的 supports 检查, 因为本类在此之前已经做了全面的检查.

        AuthenticationInfo info = realm.getAuthenticationInfo(token);
        if (info == null) {
            throw new UnknownAccountException("安全域 [%s] 无法找到提交的认证令牌 [%s] 的帐户数据.".formatted(realm, token));
        }
        return info;
    }

    /**
     * <p>
     *     根据传入的认证令牌返回所有支持该令牌的安全域.
     * </p>
     *
     * @param token 认证令牌.
     * @return 支持该令牌的所有安全域.
     * @see AuthenticatingRealm#supports(AuthenticationToken)
     */
    private @NonNull LinkedList<Realm> getSupportedRealmsForToken(@NonNull AuthenticationToken token) {
        // 获取所有已配置的 Realm.
        Collection<Realm> realms = super.getRealms();

        return realms
                    .stream()
                    .filter(realm -> realm.supports(token))
                    .collect(Collectors.toCollection(LinkedList :: new));
    }
}
