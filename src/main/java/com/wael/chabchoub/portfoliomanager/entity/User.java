package com.wael.chabchoub.portfoliomanager.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "user_info")
public class User {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	@Column(name = "age", nullable = false)
	private Integer age;
	@Column(name = "email", nullable = false)
	private String email;
	@Column(name = "title", nullable = false)
	private String title;
	@Column(name = "birthday", nullable = false)
	private LocalDate birthday;
	@Column(name = "website", nullable = false)
	private String website;
	@Column(name = "phone_number", nullable = false)
	private String phoneNumber;
	@Column(name = "city", nullable = false)
	private String city;
	@Column(name = "degree", nullable = false)
	private String degree;
	@Column(name = "is_available_for_free_lance", nullable = false)
	private boolean isAvailableForFreeLance;
}
