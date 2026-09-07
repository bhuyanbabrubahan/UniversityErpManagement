package com.sms.sequence.service;

import com.sms.sequence.enums.ModuleType;

public interface CodeGeneratorService {

	String generateCode(ModuleType module, String prefix);

}