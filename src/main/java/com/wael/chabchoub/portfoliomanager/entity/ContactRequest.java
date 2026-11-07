package com.wael.chabchoub.portfoliomanager.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/** Contact form payload. {@code website} is a honeypot: real users leave it empty. */
public record ContactRequest(
		@NotBlank @Size(max = 100) @Pattern(regexp = "[^\\r\\n]*") String name,
		@NotBlank @Email @Size(max = 254) String email,
		@NotBlank @Size(max = 150) @Pattern(regexp = "[^\\r\\n]*") String subject,
		@NotBlank @Size(max = 5000) String message,
		@Size(max = 200) String website) {
}
