package com.solux.piccountbe.config.security;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties("security")
public class SecurityProperties {
	private List<String> whitelist;
}
