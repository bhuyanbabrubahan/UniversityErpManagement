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
import com.sms.exception.ResourceNotFoundException;
import com.sms.security.dto.SuperAdminRequestDto;
import com.sms.security.dto.SuperAdminResponseDto;
import com.sms.security.dto.SuperAdminSearchRequestDto;
import com.sms.security.entity.SuperAdmin;
import com.sms.security.entity.User;
import com.sms.security.enums.Role;
import com.sms.security.enums.UserStatus;
import com.sms.security.mapper.SuperAdminMapper;
import com.sms.security.repository.SuperAdminRepository;
import com.sms.security.repository.UserRepository;
import com.sms.security.service.SuperAdminService;
import com.sms.security.service.UserAccountService;
import com.sms.security.specification.SuperAdminSpecification;
import com.sms.security.util.SecurityUtils;
import com.sms.sequence.enums.ModuleType;
import com.sms.sequence.service.CodeGeneratorService;
import com.sms.util.PageValidator;
import com.sms.util.SortFieldValidator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class SuperAdminServiceImpl implements SuperAdminService {

    private final UserRepository userRepository;

    private final SuperAdminRepository superAdminRepository;

    private final SuperAdminMapper superAdminMapper;

    private final UserAccountService userAccountService;

    private final CodeGeneratorService codeGeneratorService;
    
    private final AddressRepository addressRepository;

    private final VillageRepository villageRepository;

    // =====================================================
    // Validate Super Admin
    // =====================================================

    public SuperAdmin getSuperAdmin(Long id){

        return superAdminRepository
                .findByIdAndStatusNot(
                        id,
                        RecordStatus.DELETED
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Super Admin not found with id : "
                                + id
                        )
                );
    }
    
    
    
    @Override
    @Transactional
    @AuditLog(
            action = AuditAction.CREATE,
            module = "SUPER_ADMIN",
            description = "Create new super admin"
    )
    public SuperAdminResponseDto createSuperAdmin(
            SuperAdminRequestDto request
    ) {

        // Create user account
        User user = userAccountService.createUser(
                request.getEmail(),
                request.getPassword(),
                request.getMobileNumber(),
                Role.SUPER_ADMIN,
                UserStatus.PENDING_VERIFICATION
        );

        // Find village
        @SuppressWarnings("null")
		Village village = villageRepository
                .findById(request.getAddress().getVillageId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Village not found"
                        )
                );

        // Create address
        Address address = new Address();

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

        address = addressRepository.save(address);

        // Convert request to entity
        SuperAdmin superAdmin =
                superAdminMapper.toEntity(request);

        // Generate employee code
        superAdmin.setEmployeeCode(
                codeGeneratorService.generateCode(
                        ModuleType.SUPER_ADMIN,
                        "SADM"
                )
        );

        // Set active profile status
        superAdmin.setStatus(
                RecordStatus.ACTIVE
        );

        // Set audit information
        superAdmin.setCreatedByUserId(
                SecurityUtils.getCurrentUserId()
        );

        superAdmin.setCreatedBy(
                SecurityUtils.getCurrentFullName()
        );

        // Set relationships
        superAdmin.setUser(user);
        superAdmin.setAddress(address);

        // Save super admin
        SuperAdmin savedSuperAdmin =
                superAdminRepository.save(
                        superAdmin
                );

        // Return response
        return superAdminMapper.toDto(
                savedSuperAdmin
        );
    }
    
    
	 
    @Override
    @Transactional(readOnly = true)
    @AuditLog(
            action = AuditAction.VIEW,
            module = "SUPER_ADMIN",
            description = "View super admin details"
    )
    public SuperAdminResponseDto getSuperAdminById(
            Long id
    ) {

        // Find super admin
        SuperAdmin superAdmin =
                superAdminRepository
                        .findByIdAndStatusNot(
                                id,
                                RecordStatus.DELETED
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Super Admin not found with id : " + id
                                )
                        );


        // Return response
        return superAdminMapper.toDto(
                superAdmin
        );

    }
    
	 
	 
    @Override
    @Transactional(readOnly = true)
    @AuditLog(
            action = AuditAction.VIEW,
            module = "SUPER_ADMIN",
            description = "View all super admins"
    )
    public PageResponse<SuperAdminResponseDto> getAllSuperAdmins(

            int page,

            int size,

            String sortBy,

            String direction

    ) {

        // Validate pagination
        PageValidator.validate(
                page,
                size
        );


        // Validate sorting field
        SortFieldValidator.validate(
                sortBy,
                ALLOWED_SORT_FIELDS
        );


        // Create sorting
        Sort sort = direction.equalsIgnoreCase("asc")

                ? Sort.by(sortBy).ascending()

                : Sort.by(sortBy).descending();


        // Create pageable
        Pageable pageable =
                PageRequest.of(
                        page,
                        size,
                        sort
                );


        // Fetch active super admins
        Page<SuperAdmin> superAdminPage =
                superAdminRepository.findByStatusNot(
                        RecordStatus.DELETED,
                        pageable
                );


        // Convert response
        return new PageResponse<>(

                superAdminPage.getContent()

                        .stream()

                        .map(superAdminMapper::toDto)

                        .toList(),


                superAdminPage.getNumber(),

                superAdminPage.getSize(),

                superAdminPage.getTotalElements(),

                superAdminPage.getTotalPages(),

                superAdminPage.isLast()

        );

    }
	 
	 
	 
    @Override
    @Transactional(readOnly = true)
    @AuditLog(
            action = AuditAction.SEARCH,
            module = "SUPER_ADMIN",
            description = "Search super admins"
    )
    public PageResponse<SuperAdminResponseDto> searchSuperAdmins(

            SuperAdminSearchRequestDto request,

            int page,

            int size,

            String sortBy,

            String direction

    ) {

        // Validate pagination
        PageValidator.validate(
                page,
                size
        );


        // Validate sorting field
        SortFieldValidator.validate(
                sortBy,
                ALLOWED_SORT_FIELDS
        );


        // Create specification
        Specification<SuperAdmin> specification =
                SuperAdminSpecification.search(
                        request
                );


        // Create sorting
        Sort sort = direction.equalsIgnoreCase("asc")

                ? Sort.by(sortBy).ascending()

                : Sort.by(sortBy).descending();


        // Create pageable
        Pageable pageable =
                PageRequest.of(
                        page,
                        size,
                        sort
                );


        // Execute search
        Page<SuperAdmin> superAdminPage =
                superAdminRepository.findAll(
                        specification,
                        pageable
                );


        // Convert response
        return new PageResponse<>(

                superAdminPage.getContent()

                        .stream()

                        .map(superAdminMapper::toDto)

                        .toList(),


                superAdminPage.getNumber(),

                superAdminPage.getSize(),

                superAdminPage.getTotalElements(),

                superAdminPage.getTotalPages(),

                superAdminPage.isLast()

        );

    }
	 
	 
	 
	 
 // Update Super Admin

    @Transactional
    @Override
    @AuditLog(
            action = AuditAction.UPDATE,
            module = "SUPER_ADMIN",
            description = "Update super admin details"
    )
    public SuperAdminResponseDto updateSuperAdmin(
            Long id,
            SuperAdminRequestDto request
    ) {

        // Validate super admin
        SuperAdmin superAdmin =
                superAdminRepository
                        .findByIdAndStatusNot(
                                id,
                                RecordStatus.DELETED
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Super Admin not found with id : " + id
                                )
                        );

        // Validate user relationship
        User user = superAdmin.getUser();

        if (user == null) {
            throw new ResourceNotFoundException(
                    "User not found for this super admin."
            );
        }

        // Update user account
        userAccountService.updateUser(
                user,
                request.getEmail(),
                request.getPassword(),
                request.getMobileNumber()
        );

        // Validate village
        @SuppressWarnings("null")
		Village village =
                villageRepository
                        .findById(
                                request.getAddress().getVillageId()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Village not found"
                                )
                        );

        // Create address if missing
        Address address = superAdmin.getAddress();

        if (address == null) {

            address = new Address();

        }

        // Update address details
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

        // Save address
        address =
                addressRepository.save(address);

        // Set address relationship
        superAdmin.setAddress(address);

        // Update super admin details
        superAdminMapper.updateEntity(
                request,
                superAdmin
        );

        // Generate employee code if missing
        if (superAdmin.getEmployeeCode() == null
                || superAdmin.getEmployeeCode().isBlank()) {

            superAdmin.setEmployeeCode(
                    codeGeneratorService.generateCode(
                            ModuleType.SUPER_ADMIN,
                            "SADM"
                    )
            );
        }

        // Update audit information
        superAdmin.setUpdatedByUserId(
                SecurityUtils.getCurrentUserId()
        );

        superAdmin.setUpdatedBy(
                SecurityUtils.getCurrentFullName()
        );


        // Save super admin
        SuperAdmin updatedSuperAdmin =
                superAdminRepository.save(
                        superAdmin
                );

        // Return response
        return superAdminMapper.toDto(
                updatedSuperAdmin
        );
    }
	 
	 
	 
    @Override
    @Transactional
    @AuditLog(
            action = AuditAction.DELETE,
            module = "SUPER_ADMIN",
            description = "Delete super admin"
    )
    public void deleteSuperAdmin(
            Long id
    ) {

        // Find super admin
        SuperAdmin superAdmin =
                superAdminRepository
                        .findByIdAndStatusNot(
                                id,
                                RecordStatus.DELETED
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Super Admin not found with id : " + id
                                )
                        );


        // Soft delete super admin
        superAdmin.setStatus(
                RecordStatus.DELETED
        );


        // Disable user login
        User user =
                superAdmin.getUser();


        if (user != null) {

            user.setStatus(
                    UserStatus.INACTIVE
            );

            userRepository.save(user);

        }


        // Save super admin
        superAdminRepository.save(
                superAdmin
        );

    }
	 
	 
	 
	 
	 
	 
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
	 
	 
}    