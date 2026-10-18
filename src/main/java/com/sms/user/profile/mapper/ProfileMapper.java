package com.sms.user.profile.mapper;

import org.springframework.stereotype.Component;

import com.location.address.entity.Address;
import com.sms.common.entity.BaseEntity;
import com.sms.entity.Faculty;
import com.sms.entity.Student;
import com.sms.security.entity.Admin;
import com.sms.security.entity.SuperAdmin;
import com.sms.security.entity.User;
import com.sms.user.profile.dto.ProfileResponseDto;

@Component
public class ProfileMapper {

    // =====================================================
    // STUDENT
    // =====================================================

    public ProfileResponseDto convertToDto(Student student) {

        ProfileResponseDto dto = new ProfileResponseDto();

        User user = student.getUser();

        mapCommonUserInformation(dto, user);

        // =================================================
        // STUDENT INFORMATION
        // =================================================

        dto.setStudentId(student.getId());
        dto.setRollNumber(student.getRollNumber());
        dto.setRegistrationNumber(student.getRegistrationNumber());
        dto.setAdmissionNumber(student.getAdmissionNumber());
        dto.setAdmissionDate(student.getAdmissionDate());

        // =================================================
        // COMMON PERSONAL INFORMATION
        // =================================================

        dto.setFirstName(student.getFirstName());
        dto.setMiddleName(student.getMiddleName());
        dto.setLastName(student.getLastName());
        dto.setFullName(student.getFullName());
        dto.setGender(student.getGender());
        dto.setDateOfBirth(student.getDateOfBirth());

        dto.setProfileImage(student.getProfileImage());
        dto.setSignatureImage(student.getSignatureImage());

        // =================================================
        // EXTRA PERSONAL INFORMATION
        // =================================================

        dto.setBloodGroup(student.getBloodGroup());
        dto.setCategory(student.getCategory());
        dto.setReligion(student.getReligion());
        dto.setNationality(student.getNationality());
        dto.setAlternateMobile(student.getAlternateMobile());
        dto.setAlternateEmail(student.getAlternateEmail());
        dto.setAadhaarNumber(student.getAadhaarNumber());
        dto.setPanNumber(student.getPanNumber());

        // =================================================
        // ACADEMIC INFORMATION
        // =================================================

        dto.setAcademicYear(student.getAcademicYear());
        dto.setSection(student.getSection());
        dto.setBatch(student.getBatch());
        dto.setCurrentYear(student.getCurrentYear());
        dto.setStudentType(student.getStudentType());
        dto.setStudentStatus(student.getStudentStatus());
        //dto.setFees(student.getFees());

        if (student.getDepartment() != null) {
            dto.setDepartmentId(
                    student.getDepartment().getId()
            );

            dto.setDepartmentName(
                    student.getDepartment().getDepartmentName()
            );
        }

        if (student.getCourse() != null) {
            dto.setCourseId(
                    student.getCourse().getId()
            );

            dto.setCourseName(
                    student.getCourse().getCourseName()
            );
        }

        if (student.getCurrentSemester() != null) {
            dto.setCurrentSemesterId(
                    student.getCurrentSemester().getId()
            );

            dto.setSemesterName(
                    student.getCurrentSemester().getSemesterName()
            );
        }

        // =================================================
        // RECORD STATUS
        // =================================================

        dto.setRecordStatus(student.getStatus());

        // =================================================
        // AUDIT
        // =================================================

        mapAuditInformation(dto, student);

        // =================================================
        // ADDRESS
        // =================================================

        mapAddress(dto, student.getAddress());

        return dto;
    }

    // =====================================================
    // FACULTY
    // =====================================================

    public ProfileResponseDto convertFacultyToDto(
            Faculty faculty
    ) {

        ProfileResponseDto dto = new ProfileResponseDto();

        User user = faculty.getUser();

        mapCommonUserInformation(dto, user);

        // =================================================
        // COMMON PERSONAL INFORMATION
        // =================================================

        dto.setFirstName(faculty.getFirstName());
        dto.setMiddleName(faculty.getMiddleName());
        dto.setLastName(faculty.getLastName());

        dto.setFullName(
                buildFullName(
                        faculty.getFirstName(),
                        null,
                        faculty.getLastName()
                )
        );

        dto.setGender(faculty.getGender());
        dto.setDateOfBirth(faculty.getDateOfBirth());

        dto.setProfileImage(
                faculty.getProfileImage()
        );

        // =================================================
        // FACULTY INFORMATION
        // =================================================

        dto.setFacultyId(faculty.getId());

        dto.setEmployeeCode(
                faculty.getEmployeeCode()
        );

        dto.setFacultyDesignation(
                faculty.getDesignation()
        );

        dto.setQualification(
                faculty.getQualification()
        );

        dto.setJoiningDate(
                faculty.getJoiningDate()
        );

        dto.setSalary(
                faculty.getSalary()
        );

        // =================================================
        // DEPARTMENT
        // =================================================

        if (faculty.getDepartment() != null) {

            dto.setDepartmentId(
                    faculty.getDepartment().getId()
            );

            dto.setDepartmentName(
                    faculty.getDepartment().getDepartmentName()
            );
        }

        // =================================================
        // RECORD STATUS
        // =================================================

        dto.setRecordStatus(
                faculty.getStatus()
        );

        // =================================================
        // AUDIT
        // =================================================

        mapAuditInformation(dto, faculty);

        // =================================================
        // ADDRESS
        // =================================================

        mapAddress(
                dto,
                faculty.getAddress()
        );

        return dto;
    }

    // =====================================================
    // ADMIN
    // =====================================================

    public ProfileResponseDto convertAdminToDto(
            Admin admin
    ) {

        ProfileResponseDto dto = new ProfileResponseDto();

        User user = admin.getUser();

        mapCommonUserInformation(dto, user);

        // =================================================
        // COMMON PERSONAL INFORMATION
        // =================================================

        dto.setFirstName(admin.getFirstName());
        dto.setLastName(admin.getLastName());

        dto.setFullName(
                admin.getFullName()
        );

        dto.setGender(admin.getGender());
        dto.setDateOfBirth(admin.getDateOfBirth());

        dto.setProfileImage(
                admin.getProfileImage()
        );

        // =================================================
        // ADMIN INFORMATION
        // =================================================

        dto.setAdminId(admin.getId());

        dto.setEmployeeCode(
                admin.getEmployeeCode()
        );

        dto.setAdminDesignation(
                admin.getDesignation()
        );

        dto.setJoiningDate(
                admin.getJoiningDate()
        );

        dto.setSalary(
                admin.getSalary()
        );

        // =================================================
        // RECORD STATUS
        // =================================================

        dto.setRecordStatus(
                admin.getStatus()
        );

        // =================================================
        // AUDIT
        // =================================================

        mapAuditInformation(dto, admin);

        // =================================================
        // ADDRESS
        // =================================================

        mapAddress(
                dto,
                admin.getAddress()
        );

        return dto;
    }

    // =====================================================
    // SUPER ADMIN
    // =====================================================

    public ProfileResponseDto convertSuperAdminToDto(
            SuperAdmin superAdmin
    ) {

        ProfileResponseDto dto = new ProfileResponseDto();

        User user = superAdmin.getUser();

        mapCommonUserInformation(dto, user);

        // =================================================
        // COMMON PERSONAL INFORMATION
        // =================================================

        dto.setFirstName(
                superAdmin.getFirstName()
        );

        dto.setLastName(
                superAdmin.getLastName()
        );

        dto.setFullName(
                superAdmin.getFullName()
        );

        dto.setGender(
                superAdmin.getGender()
        );

        dto.setDateOfBirth(
                superAdmin.getDateOfBirth()
        );

        dto.setProfileImage(
                superAdmin.getProfileImage()
        );

        // =================================================
        // SUPER ADMIN INFORMATION
        // =================================================

        dto.setSuperAdminId(
                superAdmin.getId()
        );

        dto.setEmployeeCode(
                superAdmin.getEmployeeCode()
        );

        dto.setSuperAdminDesignation(
                superAdmin.getDesignation()
        );

        dto.setJoiningDate(
                superAdmin.getJoiningDate()
        );

        dto.setSalary(
                superAdmin.getSalary()
        );

        // =================================================
        // RECORD STATUS
        // =================================================

        dto.setRecordStatus(
                superAdmin.getStatus()
        );

        // =================================================
        // AUDIT
        // =================================================

        mapAuditInformation(
                dto,
                superAdmin
        );

        // =================================================
        // ADDRESS
        // =================================================

        mapAddress(
                dto,
                superAdmin.getAddress()
        );

        return dto;
    }

    // =====================================================
    // COMMON USER INFORMATION
    // =====================================================

    private void mapCommonUserInformation(
            ProfileResponseDto dto,
            User user
    ) {

        if (user == null) {
            return;
        }

        dto.setUserId(
                user.getId()
        );

        dto.setEmail(
                user.getEmail()
        );

        dto.setRole(
                user.getRole() != null
                        ? user.getRole().name()
                        : null
        );

        dto.setMobileNumber(
                user.getMobileNumber()
        );

        dto.setMobileVerified(
                user.getMobileVerified()
        );

        dto.setEmailVerified(
                user.getEmailVerified()
        );

        dto.setStatus(
                user.getStatus()
        );
    }

    // =====================================================
    // ADDRESS
    // =====================================================

    private void mapAddress(
            ProfileResponseDto dto,
            Address address
    ) {

        if (address == null) {
            return;
        }

        dto.setAddressId(
                address.getId()
        );

        dto.setHouseNumber(
                address.getHouseNumber()
        );

        dto.setStreet(
                address.getStreet()
        );

        dto.setLandmark(
                address.getLandmark()
        );

        dto.setAddressType(
                address.getAddressType()
        );

        if (address.getVillage() == null) {
            return;
        }

        var village = address.getVillage();

        dto.setVillageId(
                village.getId()
        );

        dto.setVillageName(
                village.getVillageName()
        );

        dto.setPincode(
                village.getPincode()
        );

        if (village.getTehsil() != null) {

            dto.setTehsilId(
                    village.getTehsil().getId()
            );

            dto.setTehsilName(
                    village.getTehsil().getTehsilName()
            );

            if (village.getTehsil().getDistrict() != null) {

                var district =
                        village.getTehsil().getDistrict();

                dto.setDistrictId(
                        district.getId()
                );

                dto.setDistrictName(
                        district.getDistrictName()
                );

                if (district.getState() != null) {

                    var state =
                            district.getState();

                    dto.setStateId(
                            state.getId()
                    );

                    dto.setStateName(
                            state.getStateName()
                    );

                    if (state.getCountry() != null) {

                        var country =
                                state.getCountry();

                        dto.setCountryId(
                                country.getId()
                        );

                        dto.setCountryName(
                                country.getCountryName()
                        );
                    }
                }
            }
        }
    }

    // =====================================================
    // AUDIT INFORMATION
    // =====================================================

    private void mapAuditInformation(
            ProfileResponseDto dto,
            BaseEntity entity
    ) {

        dto.setCreatedByUserId(
                entity.getCreatedByUserId()
        );

        dto.setCreatedBy(
                entity.getCreatedBy()
        );

        dto.setCreatedAt(
                entity.getCreatedAt()
        );

        dto.setUpdatedByUserId(
                entity.getUpdatedByUserId()
        );

        dto.setUpdatedBy(
                entity.getUpdatedBy()
        );


        dto.setUpdatedAt(
                entity.getUpdatedAt()
        );
    }

    // =====================================================
    // FULL NAME
    // =====================================================

    private String buildFullName(
            String firstName,
            String middleName,
            String lastName
    ) {

        return java.util.stream.Stream.of(
                    firstName,
                    middleName,
                    lastName
                )
                .filter(java.util.Objects::nonNull)
                .map(String::trim)
                .filter(value -> !value.isBlank())
                .collect(
                        java.util.stream.Collectors.joining(" ")
                );
    }
}