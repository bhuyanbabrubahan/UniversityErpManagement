package com.sms.sequence.serviceImpl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sms.sequence.entity.RunningSequence;
import com.sms.sequence.enums.ModuleType;
import com.sms.sequence.repository.RunningSequenceRepository;
import com.sms.sequence.service.CodeGeneratorService;

@Service
public class CodeGeneratorServiceImpl implements CodeGeneratorService {

	private final RunningSequenceRepository repository;

	public CodeGeneratorServiceImpl(RunningSequenceRepository repository) {
		this.repository = repository;
	}

	@Override
	@Transactional
	public String generateCode(ModuleType module, String prefix) {

		RunningSequence sequence = repository.findByModule(module).orElseGet(() -> {

			RunningSequence rs = new RunningSequence();

			rs.setModule(module);

			rs.setLastNumber(0L);

			return repository.save(rs);

		});

		Long nextNumber = sequence.getLastNumber() + 1;

		sequence.setLastNumber(nextNumber);

		repository.save(sequence);

		return String.format("%s2026%04d", prefix, nextNumber);

	}

}