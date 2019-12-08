package com.github.kshashov.telegram.autoconfigure.processors.arguments;

import com.github.kshashov.telegram.handler.TelegramRequest;
import com.github.kshashov.telegram.handler.processor.BotHandlerMethodArgumentResolver;
import com.github.kshashov.telegram.handler.processor.TelegramSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Resolves method parameters by delegating to a list of registered {@link BotHandlerMethodArgumentResolver} resolvers.
 */
@Slf4j
public class BotHandlerMethodArgumentResolverComposite implements BotHandlerMethodArgumentResolver {

    private final List<BotHandlerMethodArgumentResolver> argumentResolvers;

    private final Map<MethodParameter, BotHandlerMethodArgumentResolver> argumentResolverCache =
            new ConcurrentHashMap<>(256);

    /**
     * Create a composite resolver for the given {@link BotHandlerMethodArgumentResolver}s.
     *
     * @param resolvers to add.
     */
    public BotHandlerMethodArgumentResolverComposite(@NotNull List<BotHandlerMethodArgumentResolver> resolvers) {
        argumentResolvers = new ArrayList<>(resolvers);
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return (getArgumentResolver(parameter) != null);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, TelegramRequest telegramRequest, TelegramSession telegramSession) {
        BotHandlerMethodArgumentResolver resolver = getArgumentResolver(parameter);
        if (resolver == null) {
            log.error("Unknown parameter type [" + parameter.getParameterType().getName() + "]");
            return null;
        }
        return resolver.resolveArgument(parameter, telegramRequest, telegramSession);
    }

    /**
     * Find a registered {@link BotHandlerMethodArgumentResolver} that supports the given method parameter.
     *
     * @param parameter for which you need to find the argument resolver
     */
    private BotHandlerMethodArgumentResolver getArgumentResolver(MethodParameter parameter) {
        BotHandlerMethodArgumentResolver result = this.argumentResolverCache.get(parameter);
        if (result == null) {
            for (BotHandlerMethodArgumentResolver methodArgumentResolver : this.argumentResolvers) {
                if (log.isTraceEnabled()) {
                    log.trace("Testing if argument resolver [" + methodArgumentResolver + "] supports [" +
                            parameter.getGenericParameterType() + "]");
                }
                if (methodArgumentResolver.supportsParameter(parameter)) {
                    result = methodArgumentResolver;
                    this.argumentResolverCache.put(parameter, result);
                    break;
                }
            }
        }
        return result;
    }
}
