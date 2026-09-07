package com.sms.serviceImpl;

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
import com.sms.dto.FacultyRequestDto;
import com.sms.dto.FacultyResponseDto;
import com.sms.dto.FacultySearchRequest;
import com.sms.dto.PageResponse;
import com.sms.entity.Department;
import com.sms.entity.Faculty;
import com.sms.enums.RecordStatus;
import com.sms.exception.ResourceNotFoundException;
import com.sms.mapper.FacultyMapper;
import com.sms.repository.DepartmentRepository;
import com.sms.repository.FacultyRepository;
import com.sms.security.entity.User;
import com.sms.security.enums.Role;
import com.sms.security.enums.UserStatus;
import com.sms.security.service.UserAccountService;
import com.sms.security.util.SecurityUtils;
import com.sms.sequence.enums.ModuleType;
import com.sms.sequence.service.CodeGeneratorService;
import com.sms.service.FacultyService;
import com.sms.specification.FacultySpecification;
import com.sms.util.PageValidator;
import com.sms.util.SortFieldValidator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class FacultyServiceImpl implements FacultyService {

    private final FacultyRepository facultyRepository;

    private final DepartmentRepository departmentRepository;

    private final VillageRepository villageRepository;

    private final AddressRepository addressRepository;

    private final FacultyMapper mapper;

    private final CodeGeneratorService codeGeneratorService;

    private final UserAccountService userAccountService;


    // =========================================================
    // GET FACULTY ENTITY
    // =========================================================

    private Faculty getFaculty(Long id) {

        return facultyRepository
                .findByIdAndStatus(
                        id,
                        RecordStatus.ACTIVE
                )
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Faculty not found with id : " + id
                        )
                );
    }


    // =========================================================
    // CREATE FACULTY
    // =========================================================

    @Override
    @AuditLog(
            action = AuditAction.CREATE,
            module = "FACULTY",
            description = "Create new faculty"
    )
    public FacultyResponseDto createFaculty(
            FacultyRequestDto request
    ) {

        Department department =
                departmentRepository
                        .findByIdAndStatus(
                                request.getDepartmentId(),
                                RecordStatus.ACTIVE
                        )
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Department not found or inactive"
                                )
                        );


        User user =
                userAccountService.createUser(
                        request.getEmail().trim(),
                        request.getPassword(),
                        request.getMobileNumber().trim(),
                        Role.FACULTY,
                        UserStatus.PENDING_VERIFICATION
                );


        Address address =
                createAddress(request);


        Faculty faculty =
                mapper.convertToEntity(request);


        faculty.setEmployeeCode(
                codeGeneratorService.generateCode(
                        ModuleType.FACULTY,
                        "FAC"
                )
        );


        faculty.setDepartment(department);

        faculty.setAddress(address);

        faculty.setUser(user);

        faculty.setStatus(
                RecordStatus.ACTIVE
        );

        faculty.setCreatedByUserId(
                SecurityUtils.getCurrentUserId()
        );

        faculty.setCreatedBy(
                SecurityUtils.getCurrentFullName()
        );


        Faculty savedFaculty =
                facultyRepository.save(faculty);


        return mapper.convertToResponseDto(
                savedFaculty
        );
    }


    // =========================================================
    // CREATE ADDRESS
    // =========================================================

    private Address createAddress(
            FacultyRequestDto request
    ) {

        if (
                request.getAddress() == null ||
                request.getAddress().getVillageId() == null
        ) {
            throw new ResourceNotFoundException(
                    "Address village is required"
            );
        }


        Village village =
                villageRepository
                        .findById(
                                request.getAddress().getVillageId()
                        )
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Village not found"
                                )
                        );


        Address address =
                new Address();


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

        address.setVillage(
                village
        );


        return addressRepository.save(address);
    }


    // =========================================================
    // GET BY ID
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    @AuditLog(
            action = AuditAction.VIEW,
            module = "FACULTY",
            description = "View faculty details"
    )
    public FacultyResponseDto getFacultyById(
            Long id
    ) {

        return mapper.convertToResponseDto(
                getFaculty(id)
        );
    }


    // =========================================================
    // GET ALL FACULTIES
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    @AuditLog(
        action = AuditAction.VIEW,
        module = "FACULTY",
        description = "View all faculties"
    )
    public PageResponse<FacultyResponseDto> getAllFaculties(
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

        Pageable pageable = createPageable(
            page,
            size,
            sortBy,
            direction
        );

        Page<Faculty> facultyPage =
            facultyRepository.findByStatus(
                RecordStatus.ACTIVE,
                pageable
            );

        return PageResponse.from(
            facultyPage.map(
                mapper::convertToResponseDto
            )
        );
    }


    // =========================================================
    // SEARCH FACULTIES
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    @AuditLog(
            action = AuditAction.SEARCH,
            module = "FACULTY",
            description = "Search faculties"
    )
    public PageResponse<FacultyResponseDto> searchFaculties(
            FacultySearchRequest request,
            int page,
            int size,
            String sortBy,
            String direction
    ) {

        PageValidator.validate(
                page,
                size
        );


        SortFieldValidator.validate(
                sortBy,
                ALLOWED_SORT_FIELDS
        );


        Specification<Faculty> specification =
                FacultySpecification.search(request);


        Pageable pageable =
                createPageable(
                        page,
                        size,
                        sortBy,
                        direction
                );


        Page<Faculty> result =
                facultyRepository.findAll(
                        specification,
                        pageable
                );


        return toPageResponse(result);
    }


    // =========================================================
    // UPDATE FACULTY
    // =========================================================

    @Override
    @AuditLog(
            action = AuditAction.UPDATE,
            module = "FACULTY",
            description = "Update faculty details"
    )
    public FacultyResponseDto updateFaculty(
            Long id,
            FacultyRequestDto request
    ) {

        Faculty faculty =
                getFaculty(id);


        User user =
                faculty.getUser();


        if (user == null) {

            throw new ResourceNotFoundException(
                    "User not found for this faculty."
            );
        }


        Department department =
                departmentRepository
                        .findByIdAndStatus(
                                request.getDepartmentId(),
                                RecordStatus.ACTIVE
                        )
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Department not found or inactive"
                                )
                        );


        userAccountService.updateUser(
                user,
                request.getEmail().trim(),
                request.getPassword(),
                request.getMobileNumber().trim()
        );


        mapper.updateEntity(
                faculty,
                request
        );


        faculty.setDepartment(
                department
        );


        updateAddress(
                faculty,
                request
        );


        faculty.setUpdatedByUserId(
                SecurityUtils.getCurrentUserId()
        );

        faculty.setUpdatedBy(
                SecurityUtils.getCurrentFullName()
        );


        Faculty updatedFaculty =
                facultyRepository.save(
                        faculty
                );


        return mapper.convertToResponseDto(
                updatedFaculty
        );
    }


    // =========================================================
    // UPDATE ADDRESS
    // =========================================================

    private void updateAddress(
            Faculty faculty,
            FacultyRequestDto request
    ) {

        if (
                request.getAddress() == null ||
                request.getAddress().getVillageId() == null
        ) {

            throw new ResourceNotFoundException(
                    "Address village is required"
            );
        }


        Address address =
                faculty.getAddress();


        if (address == null) {

            faculty.setAddress(
                    createAddress(request)
            );

            return;
        }


        Village village =
                villageRepository
                        .findById(
                                request.getAddress().getVillageId()
                        )
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Village not found"
                                )
                        );


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

        address.setVillage(
                village
        );


        addressRepository.save(address);
    }


    // =========================================================
    // DELETE FACULTY
    // =========================================================

    @Override
    @AuditLog(
            action = AuditAction.DELETE,
            module = "FACULTY",
            description = "Delete faculty"
    )
    public void deleteFaculty(
            Long id
    ) {

        Faculty faculty =
                getFaculty(id);


        faculty.setStatus(
                RecordStatus.DELETED
        );


        User user =
                faculty.getUser();


        if (user != null) {

            user.setStatus(
                    UserStatus.INACTIVE
            );
        }


        facultyRepository.save(
                faculty
        );
    }


    // =========================================================
    // CREATE PAGEABLE
    // =========================================================

    private Pageable createPageable(
            int page,
            int size,
            String sortBy,
            String direction
    ) {

        Sort sort;


        if (
                direction != null &&
                direction.equalsIgnoreCase("desc")
        ) {

            sort =
                    Sort.by(sortBy)
                            .descending();

        } else {

            sort =
                    Sort.by(sortBy)
                            .ascending();
        }


        return PageRequest.of(
                page,
                size,
                sort
        );
    }


    // =========================================================
    // PAGE CONVERSION
    // =========================================================

    private PageResponse<FacultyResponseDto> toPageResponse(
            Page<Faculty> page
    ) {

        return new PageResponse<>(
                page.getContent()
                        .stream()
                        .map(mapper::convertToResponseDto)
                        .toList(),

                page.getNumber(),

                page.getSize(),

                page.getTotalElements(),

                page.getTotalPages(),

                page.isLast()
        );
    }


    // =========================================================
    // ALLOWED SORT FIELDS
    // =========================================================

    private static final Set<String> ALLOWED_SORT_FIELDS =
            Set.of(
                    "id",
                    "employeeCode",
                    "firstName",
                    "lastName",
                    "designation",
                    "qualification",
                    "joiningDate",
                    "createdAt",
                    "updatedAt"
            );

}
