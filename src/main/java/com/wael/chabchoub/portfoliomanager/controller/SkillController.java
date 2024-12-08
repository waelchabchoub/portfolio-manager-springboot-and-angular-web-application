package com.wael.chabchoub.portfoliomanager.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wael.chabchoub.portfoliomanager.entity.Skill;
import com.wael.chabchoub.portfoliomanager.service.SkillService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/skill")
public class SkillController {
	private final SkillService skillService;

	@GetMapping("/get")
	public List<Skill> getSkills() {
		return skillService.getSkills();
	}

	@PostMapping("/save")
	public Skill saveOrUpdateSkill(@RequestBody Skill skill) {

		return skillService.saveOrUpdateSkill(skill);
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deleteSkill(@PathVariable("id") Long id) {
		try {
			skillService.deleteSkill(id);
			return ResponseEntity.ok("Skill deleted successfully.");
		} catch (RuntimeException e) {
			return ResponseEntity.status(404).body(e.getMessage());
		}
	}

}
