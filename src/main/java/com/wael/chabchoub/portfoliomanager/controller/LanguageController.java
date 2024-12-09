package com.wael.chabchoub.portfoliomanager.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wael.chabchoub.portfoliomanager.security.config.PortfolioManagerProperties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RestController
@RequiredArgsConstructor
public class LanguageController {
	private final PortfolioManagerProperties portfolioManagerProperties;

	@GetMapping("/api/lang-config")
	public LangConfig getLangConfig() {
		return new LangConfig(portfolioManagerProperties.getLanguage().getDefaultLanguage(),
				portfolioManagerProperties.getLanguage().getAvailableLanguages());
	}

	@GetMapping("/api/translation-files")
	public List<String> getTranslationFiles() throws IOException {
		List<String> files = new ArrayList<>();
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		String pattern = "classpath:static/assets/i18n/*.json";
		Resource[] resources = resolver.getResources(pattern);

		for (Resource resource : resources) {
			String filename = resource.getFilename();
			if (filename != null && (filename.endsWith("-en.json") || filename.endsWith("-de.json"))) {
				files.add(filename);
			}
		}

		return files;
	}

	@Getter
	@Setter
	public static class LangConfig {
		private String defaultLanguage;
		private String[] availableLanguages;

		public LangConfig(String defaultLanguage, String[] availableLanguages) {
			this.defaultLanguage = defaultLanguage;
			this.availableLanguages = availableLanguages;
		}
	}
}
