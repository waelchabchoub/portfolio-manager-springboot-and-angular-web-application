package com.wael.chabchoub.portfoliomanager.security.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@Setter
@Getter
@ConfigurationProperties(prefix = "portfoliomanager")
public class PortfolioManagerProperties {
	private JwtProperties jwt;
	private LanguageProperties language;
	private EmailProperties email;
	
	@Getter
    @Setter
    public static class JwtProperties {
        private String secret;
    }

    @Getter
    @Setter
    public static class LanguageProperties {
        private String defaultLanguage;
        private String[] availableLanguages;
    }
    
    @Getter
    @Setter
    public static class EmailProperties {
        private String from;
        private String to;
    }
}
