package com.wael.chabchoub.portfoliomanager.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.wael.chabchoub.portfoliomanager.entity.Skill;
import com.wael.chabchoub.portfoliomanager.repository.SkillRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SkillService {
	private final SkillRepository skillRepository;

	public List<Skill> getSkills() {
		return skillRepository.findAll();
	}

	public Skill saveOrUpdateSkill(Skill skill) {
		Optional<Skill> existingSkill = java.util.Optional.empty();

		if (skill.getId() != null) {
			existingSkill = skillRepository.findById(skill.getId());
		}
		;

		if (existingSkill.isPresent()) {
			Skill existing = existingSkill.get();
			existing.setSkill(skill.getSkill());
			existing.setProgress(skill.getProgress());

			return skillRepository.save(existing);
		} else {
			return skillRepository.save(skill);
		}
	}

	public void deleteSkill(long id) {
		Optional<Skill> skill = skillRepository.findById(id);
		if (skill.isPresent()) {
			skillRepository.delete(skill.get());
		} else {
			throw new RuntimeException("Skill not found with ID: " + id);
		}
	}
}
