package com.sms.sequence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sms.sequence.entity.RunningSequence;
import com.sms.sequence.enums.ModuleType;

public interface RunningSequenceRepository extends JpaRepository<RunningSequence, Long> {

	Optional<RunningSequence>

			findByModule(ModuleType module);

}