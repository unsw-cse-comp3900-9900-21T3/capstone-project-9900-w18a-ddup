package dev.shawnking07.ecomm_system_backend.listener;

import dev.shawnking07.ecomm_system_backend.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class KeyExpirationListener implements MessageListener {
    private final ProductService productService;

    public KeyExpirationListener(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String key = new String(message.getBody(), StandardCharsets.UTF_8);
        key = StringUtils.unwrap(key, "\"");
        log.info("[Redis Key Expiration]: key: {}", key);
        if (StringUtils.containsIgnoreCase(key, "order.number.")) {
            productService.correctProductAmountInCache();
        }
    }
}
