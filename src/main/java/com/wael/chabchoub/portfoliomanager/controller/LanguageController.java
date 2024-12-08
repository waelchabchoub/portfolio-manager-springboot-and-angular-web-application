package com.wael.chabchoub.portfoliomanager.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
	public List<String> getTranslationFiles() {
		List<String> files = new ArrayList<>();
		File dir = new File("src/main/resources/static/assets/i18n");
		if (dir.exists() && dir.isDirectory()) {
			for (File file : dir.listFiles()) {
				if (file.getName().endsWith("-en.json") || file.getName().endsWith("-de.json")) {
					files.add(file.getName());
				}
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
