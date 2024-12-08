package com.wael.chabchoub.portfoliomanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.wael.chabchoub.portfoliomanager.entity.Skill;

public interface SkillRepository extends JpaRepository<Skill,Long> {
}
