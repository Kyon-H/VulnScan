package com.atlxc.VulnScan.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Configuration;

/**
 * @author Kyon-H
 * @date 2025/5/3 22:13
 */
@Configuration
public class EnvConfig {
    static {
        Dotenv dotenv = Dotenv.configure().load();
        dotenv.entries().forEach(dotenvEntry ->
                System.setProperty(dotenvEntry.getKey(), dotenvEntry.getValue())
        );
    }
}
