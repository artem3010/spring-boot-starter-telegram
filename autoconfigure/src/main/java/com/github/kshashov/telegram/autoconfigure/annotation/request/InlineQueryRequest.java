package com.github.kshashov.telegram.autoconfigure.annotation.request;


import com.github.kshashov.telegram.autoconfigure.annotation.BotRequest;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

import static com.github.kshashov.telegram.handler.MessageType.INLINE_QUERY;

/**
 * Annotation for mapping inline query requests onto specific handler methods. Specifically, {@link InlineQueryRequest}
 * is a composed annotation that acts as a shortcut for {@code BotRequest(method = RequestMethod.INLINE_QUERY)}.
 *
 * @see BotRequest
 * @see MessageRequest
 * @see EditedMessageRequest
 * @see ChannelPostRequest
 * @see EditedChannelPostRequest
 * @see CallbackQueryRequest
 * @see ChosenInlineResultRequest
 * @see ShippingQueryRequest
 * @see PreCheckoutQueryRequest
 * @see PollRequest
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@BotRequest(type = INLINE_QUERY)
public @interface InlineQueryRequest {

    /**
     * Alias for {@link BotRequest#value()}.
     * @return Request mapping templates.
     */
    @AliasFor(annotation = BotRequest.class)
    String[] value() default {};

    /**
     * Alias for {@link BotRequest#path()}.
     * @return Request mapping templates.
     */
    @AliasFor(annotation = BotRequest.class)
    String[] path() default {};
}
