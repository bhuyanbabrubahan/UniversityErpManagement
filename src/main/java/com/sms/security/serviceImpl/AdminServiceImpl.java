package com.sms.security.serviceImpl;

import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.location.address.entity.Address;
import com.location.address.repository.AddressRepository;
import com.location.village.entity.Village;
import com.location.village.repository.VillageRepository;
import com.sms.audit.annotation.AuditLog;
import com.sms.audit.enums.AuditAction;
import com.sms.dto.PageResponse;
import com.sms.enums.RecordStatus;
import com.sms.exception.BusinessException;
import com.sms.exception.ResourceNotFoundException;
import com.sms.security.dto.AdminRequestDto;
import com.sms.security.dto.AdminResponseDto;
import com.sms.security.dto.AdminSearchRequestDto;
import com.sms.security.entity.Admin;
import com.sms.security.entity.User;
import com.sms.security.enums.Role;
import com.sms.security.enums.UserStatus;
import com.sms.security.mapper.AdminMapper;
import com.sms.security.repository.AdminRepository;
import com.sms.security.repository.UserRepository;
import com.sms.security.service.AdminService;
import com.sms.security.service.UserAccountService;
import com.sms.security.specification.AdminSpecification;
import com.sms.security.util.SecurityUtils;
import com.sms.sequence.enums.ModuleType;
import com.sms.sequence.service.CodeGeneratorService;
import com.sms.util.PageValidator;
import com.sms.util.SortFieldValidator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminServiceImpl implements AdminService {

	private final AdminRepository adminRepository;
	private final UserRepository userRepository;
	private final AdminMapper adminMapper;
	private final CodeGeneratorService codeGeneratorService;
	private final UserAccountService userAccountService;
	private final AddressRepository addressRepository;
	private final VillageRepository villageRepository;

	private static final Set<String> ALLOWED_SORT_FIELDS = Set.of(
			"id",
			"employeeCode",
			"firstName",
			"lastName",
			"designation",
			"joiningDate",
			"createdAt",
			"updatedAt"
	);

	private Admin getAdmin(Long id) {
		return adminRepository.findByIdAndStatusNot(id, RecordStatus.DELETED)
				.orElseThrow(() ->
						new ResourceNotFoundException(
								"Admin not found with id : " + id
						));
	}

	@Override
	@AuditLog(
			action = AuditAction.CREATE,
			module = "ADMIN",
			description = "Create new admin"
	)
	public AdminResponseDto createAdmin(AdminRequestDto request) {

		if (request.getPassword() == null || request.getPassword().isBlank()) {
			throw new BusinessException("Password is required.");
		}

		User user = userAccountService.createUser(
				request.getEmail(),
				request.getPassword(),
				request.getMobileNumber(),
				Role.ADMIN,
				UserStatus.PENDING_VERIFICATION
		);

		@SuppressWarnings("null")
		Village village = villageRepository
				.findById(request.getAddress().getVillageId())
				.orElseThrow(() ->
						new ResourceNotFoundException("Village not found"));

		Address address = new Address();

		address.setHouseNumber(request.getAddress().getHouseNumber());
		address.setStreet(request.getAddress().getStreet());
		address.setLandmark(request.getAddress().getLandmark());
		address.setAddressType(request.getAddress().getAddressType());
		address.setVillage(village);

		address = addressRepository.save(address);

		Admin admin = adminMapper.toEntity(request);

		admin.setUser(user);
		admin.setAddress(address);

		admin.setEmployeeCode(
				codeGeneratorService.generateCode(
						ModuleType.ADMIN,
						"ADM"
				)
		);

		admin.setStatus(RecordStatus.ACTIVE);

		admin.setCreatedByUserId(
				SecurityUtils.getCurrentUserId()
		);

		admin.setCreatedBy(
				SecurityUtils.getCurrentFullName()
		);


		Admin savedAdmin = adminRepository.save(admin);

		return adminMapper.toDto(savedAdmin);
	}

	@Override
	@Transactional(readOnly = true)
	@AuditLog(
			action = AuditAction.VIEW,
			module = "ADMIN",
			description = "View admin details"
	)
	public AdminResponseDto getAdminById(Long id) {

		return adminMapper.toDto(getAdmin(id));
	}

	@Override
	@Transactional(readOnly = true)
	@AuditLog(
			action = AuditAction.VIEW,
			module = "ADMIN",
			description = "View all admins"
	)
	public PageResponse<AdminResponseDto> getAllAdmins(
			int page,
			int size,
			String sortBy,
			String direction
	) {

		PageValidator.validate(page, size);

		SortFieldValidator.validate(
				sortBy,
				ALLOWED_SORT_FIELDS
		);

		Sort sort = direction.equalsIgnoreCase("asc")
				? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();

		Pageable pageable = PageRequest.of(
				page,
				size,
				sort
		);

		Page<Admin> adminPage =
				adminRepository.findByStatusNot(
						RecordStatus.DELETED,
						pageable
				);

		return toPageResponse(adminPage);
	}

	@Override
	@Transactional(readOnly = true)
	@AuditLog(
			action = AuditAction.SEARCH,
			module = "ADMIN",
			description = "Search admins"
	)
	public PageResponse<AdminResponseDto> searchAdmins(
			AdminSearchRequestDto request,
			int page,
			int size,
			String sortBy,
			String direction
	) {

		PageValidator.validate(page, size);

		SortFieldValidator.validate(
				sortBy,
				ALLOWED_SORT_FIELDS
		);

		Specification<Admin> specification =
				AdminSpecification.search(request);

		Sort sort = direction.equalsIgnoreCase("asc")
				? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();

		Pageable pageable = PageRequest.of(
				page,
				size,
				sort
		);

		Page<Admin> adminPage =
				adminRepository.findAll(
						specification,
						pageable
				);

		return toPageResponse(adminPage);
	}

	@Override
	@AuditLog(
			action = AuditAction.UPDATE,
			module = "ADMIN",
			description = "Update admin details"
	)
	public AdminResponseDto updateAdmin(
			Long id,
			AdminRequestDto request
	) {

		Admin admin = getAdmin(id);

		User user = admin.getUser();

		if (user == null) {
			throw new ResourceNotFoundException(
					"User not found for this admin."
			);
		}

		userAccountService.updateUser(
				user,
				request.getEmail(),
				request.getPassword(),
				request.getMobileNumber()
		);

		if (admin.getEmployeeCode() == null
				|| admin.getEmployeeCode().isBlank()) {

			admin.setEmployeeCode(
					codeGeneratorService.generateCode(
							ModuleType.ADMIN,
							"ADM"
					)
			);
		}

		adminMapper.updateEntity(request, admin);

		updateAddress(admin, request);

		admin.setUpdatedByUserId(
				SecurityUtils.getCurrentUserId()
		);

		admin.setUpdatedBy(
				SecurityUtils.getCurrentFullName()
		);


		Admin updatedAdmin =
				adminRepository.save(admin);

		return adminMapper.toDto(updatedAdmin);
	}

	private void updateAddress(
			Admin admin,
			AdminRequestDto request
	) {

		if (request.getAddress() == null) {
			return;
		}

		@SuppressWarnings("null")
		Village village = villageRepository
				.findById(request.getAddress().getVillageId())
				.orElseThrow(() ->
						new ResourceNotFoundException(
								"Village not found"
						));

		Address address = admin.getAddress();

		if (address == null) {
			address = new Address();
		}

		address.setHouseNumber(
				request.getAddress().getHouseNumber()
		);

		address.setStreet(
				request.getAddress().getStreet()
		);

		address.setLandmark(
				request.getAddress().getLandmark()
		);

		address.setAddressType(
				request.getAddress().getAddressType()
		);

		address.setVillage(village);

		addressRepository.save(address);

		admin.setAddress(address);
	}

	@Override
	@AuditLog(
			action = AuditAction.DELETE,
			module = "ADMIN",
			description = "Delete admin"
	)
	public void deleteAdmin(Long id) {

		Admin admin = getAdmin(id);

		admin.setStatus(RecordStatus.DELETED);

		User user = admin.getUser();

		if (user != null) {
			user.setStatus(UserStatus.INACTIVE);
			userRepository.save(user);
		}

		admin.setUpdatedByUserId(
				SecurityUtils.getCurrentUserId()
		);

		admin.setUpdatedBy(
				SecurityUtils.getCurrentFullName()
		);


		adminRepository.save(admin);
	}

	private PageResponse<AdminResponseDto> toPageResponse(
			Page<Admin> page
	) {

		return new PageResponse<>(
				page.getContent()
						.stream()
						.map(adminMapper::toDto)
						.toList(),
				page.getNumber(),
				page.getSize(),
				page.getTotalElements(),
				page.getTotalPages(),
				page.isLast()
		);
	}
}
