package com.wael.chabchoub.portfoliomanager.security;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.wael.chabchoub.portfoliomanager.security.config.SecurityConfig;
import com.wael.chabchoub.portfoliomanager.security.jwt.JwtUtils;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Checks the URL access rules only (no database needed): a denied request
 * answers 401, a permitted request to a path without a controller answers 404.
 */
@WebMvcTest(controllers = SecurityConfigTest.class)
@Import(SecurityConfig.class)
class SecurityConfigTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private JwtUtils jwtUtils;

	@Test
	void adminEndpointsRequireJwt() throws Exception {
		mockMvc.perform(post("/auth/change-password").contentType(MediaType.APPLICATION_JSON).content("{}"))
				.andExpect(status().isUnauthorized());
		mockMvc.perform(post("/api/skill/save").contentType(MediaType.APPLICATION_JSON).content("{}"))
				.andExpect(status().isUnauthorized());
		mockMvc.perform(delete("/api/skill/delete/1")).andExpect(status().isUnauthorized());
		mockMvc.perform(post("/api/userinfo/save").contentType(MediaType.APPLICATION_JSON).content("{}"))
				.andExpect(status().isUnauthorized());
	}

	@Test
	void unknownAndDataRestPathsAreClosed() throws Exception {
		mockMvc.perform(get("/admins")).andExpect(status().isNotFound());
		mockMvc.perform(get("/api/admins")).andExpect(status().isUnauthorized());
		mockMvc.perform(put("/api/skill/get")).andExpect(status().isUnauthorized());
		mockMvc.perform(get("/actuator/env")).andExpect(status().isUnauthorized());
	}

	@Test
	void publicEndpointsStayOpen() throws Exception {
		mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content("{}"))
				.andExpect(status().isNotFound());
		mockMvc.perform(post("/api/emails/send")).andExpect(status().isNotFound());
		mockMvc.perform(get("/api/skill/get")).andExpect(status().isNotFound());
		mockMvc.perform(get("/api/userinfo/get")).andExpect(status().isNotFound());
		mockMvc.perform(get("/api/lang-config")).andExpect(status().isNotFound());
		mockMvc.perform(get("/api/translation-files")).andExpect(status().isNotFound());
		mockMvc.perform(get("/")).andExpect(result -> assertNotEquals(401, result.getResponse().getStatus()));
		mockMvc.perform(get("/assets/i18n/resume-en.json"))
				.andExpect(result -> assertNotEquals(401, result.getResponse().getStatus()));
	}

	@Test
	void validJwtOpensAdminEndpoints() throws Exception {
		when(jwtUtils.validateToken(anyString())).thenReturn(true);
		when(jwtUtils.extractLogin(anyString())).thenReturn("admin");

		mockMvc.perform(post("/api/skill/save").header(HttpHeaders.AUTHORIZATION, "Bearer token")
				.contentType(MediaType.APPLICATION_JSON).content("{}")).andExpect(status().isNotFound());
	}
}
