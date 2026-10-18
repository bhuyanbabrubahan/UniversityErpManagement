package com.sms.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.sms.security.filter.JwtAuthenticationFilter;
import com.sms.security.service.CustomUserDetailsService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    private final PasswordEncoder passwordEncoder;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final JwtAuthenticationEntryPoint authenticationEntryPoint;


    @Bean
    SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http

            // =====================================================
            // CSRF
            // =====================================================
            /*
             * JWT is sent through Authorization header.
             * Application is stateless, so CSRF protection is
             * normally disabled for this architecture.
             */
            .csrf(csrf -> csrf.disable())


            // =====================================================
            // CORS
            // =====================================================
            .cors(Customizer.withDefaults())


            // =====================================================
            // HTTP SECURITY HEADERS
            // =====================================================
            .headers(headers -> headers

                /*
                 * Allow iframe only from same origin.
                 * Useful if H2 console or similar same-origin
                 * functionality is required.
                 */
                .frameOptions(frame ->
                    frame.sameOrigin()
                )

                /*
                 * Prevent MIME type sniffing.
                 */
                .contentTypeOptions(
                    Customizer.withDefaults()
                )

                /*
                 * Force HTTPS.
                 */
                .httpStrictTransportSecurity(hsts ->
                    hsts
                        .includeSubDomains(true)
                        .maxAgeInSeconds(31536000)
                )
            )


            // =====================================================
            // AUTHORIZATION
            // =====================================================
            .authorizeHttpRequests(auth -> auth

                // Angular SPA routes must be reachable without a server-side JWT.
                // Angular's authGuard protects the client route after index.html loads.
                .requestMatchers(
                    "/dashboard",
                    "/dashboard/**",
                    "/login",
                    "/forgot-password",
                    "/verify-forgot-password-otp",
                    "/reset-password",
                    "/verify-account",
                    "/verify-email"
                ).permitAll()

            		.requestMatchers(
            			    "/",
            			    "/index.html",
            			    "/favicon.ico",
            			    "/favicon.svg",
            			    "/*.js",
            			    "/*.css",
            			    "/images/**"
            			).permitAll()
                // =================================================
                // PUBLIC AUTHENTICATION APIs
                // =================================================

                .requestMatchers(
                    "/api/auth/register",
                    "/api/auth/login",
                    "/api/auth/forgot-password",
                    "/api/auth/verify-forgot-password-otp",
                    "/api/auth/reset-password",
                    "/api/auth/resend-verification",
                    "/api/auth/refresh-token"
                ).permitAll()


                // =================================================
                // PUBLIC EMAIL VERIFICATION
                // =================================================

                .requestMatchers(
                    HttpMethod.GET,
                    "/api/auth/verify-email"
                ).permitAll()


                // =================================================
                // PUBLIC MOBILE / OTP APIs
                // =================================================

                .requestMatchers(
                    HttpMethod.POST,
                    "/api/auth/mobile/send",
                    "/api/auth/mobile/verify",
                    "/api/otp/**"
                ).permitAll()


                // =================================================
                // CORS PREFLIGHT
                // =================================================

                .requestMatchers(
                    HttpMethod.OPTIONS,
                    "/**"
                ).permitAll()


                // =================================================
                // API DOCUMENTATION
                // =================================================

                /*
                 * Keep these public only if Swagger is intended
                 * to be publicly accessible.
                 *
                 * For production, consider restricting these
                 * endpoints or disabling Swagger completely.
                 */
                .requestMatchers(
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/v3/api-docs/**",
                    "/swagger-resources/**",
                    "/webjars/**"
                ).permitAll()


                // =================================================
                // ACTUATOR
                // =================================================

                .requestMatchers(
                    "/actuator/health",
                    "/actuator/info"
                ).permitAll()


                // =================================================
                // PUBLIC PROFILE PHOTO
                // =================================================

                .requestMatchers(
                    HttpMethod.GET,
                    "/api/profile/photo/**"
                ).permitAll()

               
                // =================================================
                // ADMIN MANAGEMENT
                // =================================================

                .requestMatchers(
                    "/api/admin/**"
                ).hasRole("SUPER_ADMIN")


                // =================================================
                // DEPARTMENT
                // =================================================

                .requestMatchers(
                    HttpMethod.GET,
                    "/api/departments",
                    "/api/departments/**"
                ).hasAnyRole(
                    "SUPER_ADMIN",
                    "ADMIN",
                    "FACULTY",
                    "STUDENT"
                )

                .requestMatchers(
                    HttpMethod.POST,
                    "/api/departments"
                ).hasAnyRole(
                    "SUPER_ADMIN",
                    "ADMIN"
                )

                .requestMatchers(
                    HttpMethod.PUT,
                    "/api/departments/**"
                ).hasAnyRole(
                    "SUPER_ADMIN",
                    "ADMIN"
                )

                .requestMatchers(
                    HttpMethod.DELETE,
                    "/api/departments/**"
                ).hasAnyRole(
                    "SUPER_ADMIN",
                    "ADMIN"
                )


                // =================================================
                // COURSE
                // =================================================

                .requestMatchers(
                    HttpMethod.POST,
                    "/api/courses"
                ).hasAnyRole(
                    "SUPER_ADMIN",
                    "ADMIN"
                )

                .requestMatchers(
                    HttpMethod.PUT,
                    "/api/courses/**"
                ).hasAnyRole(
                    "SUPER_ADMIN",
                    "ADMIN"
                )

                .requestMatchers(
                    HttpMethod.DELETE,
                    "/api/courses/**"
                ).hasAnyRole(
                    "SUPER_ADMIN",
                    "ADMIN"
                )


                // =================================================
                // STUDENT
                // =================================================

                .requestMatchers(
                    HttpMethod.GET,
                    "/api/students",
                    "/api/students/**"
                ).hasAnyRole(
                    "SUPER_ADMIN",
                    "ADMIN",
                    "REGISTRAR",
                    "DEAN",
                    "HOD",
                    "FACULTY",
                    "ADMISSION_OFFICER",
                    "EXAM_CONTROLLER"
                )

                .requestMatchers(
                    HttpMethod.POST,
                    "/api/students"
                ).hasAnyRole(
                    "SUPER_ADMIN",
                    "ADMIN",
                    "REGISTRAR",
                    "ADMISSION_OFFICER"
                )

                .requestMatchers(
                    HttpMethod.PUT,
                    "/api/students/**"
                ).hasAnyRole(
                    "SUPER_ADMIN",
                    "ADMIN",
                    "REGISTRAR",
                    "ADMISSION_OFFICER"
                )

                .requestMatchers(
                    HttpMethod.DELETE,
                    "/api/students/**"
                ).hasAnyRole(
                    "SUPER_ADMIN",
                    "ADMIN",
                    "REGISTRAR"
                )


                // =================================================
                // ADMISSION
                // =================================================

                .requestMatchers(
                    "/api/admissions",
                    "/api/admissions/**"
                ).hasAnyRole(
                    "SUPER_ADMIN",
                    "ADMIN",
                    "REGISTRAR",
                    "ADMISSION_OFFICER"
                )


                // =================================================
                // LOCATION MASTER
                // =================================================

                .requestMatchers(
                    "/api/countries",
                    "/api/countries/**",
                    "/api/states",
                    "/api/states/**",
                    "/api/districts",
                    "/api/districts/**",
                    "/api/tehsils",
                    "/api/tehsils/**",
                    "/api/villages",
                    "/api/villages/**"
                ).hasAnyRole(
                    "SUPER_ADMIN",
                    "ADMIN",
                    "STUDENT"
                )


                // =================================================
                // FACULTY
                // =================================================

                .requestMatchers(
                    HttpMethod.GET,
                    "/api/faculties",
                    "/api/faculties/**"
                ).hasAnyRole(
                    "SUPER_ADMIN",
                    "ADMIN",
                    "FACULTY"
                )

                .requestMatchers(
                    HttpMethod.POST,
                    "/api/faculties"
                ).hasAnyRole(
                    "SUPER_ADMIN",
                    "ADMIN"
                )

                .requestMatchers(
                    HttpMethod.PUT,
                    "/api/faculties/**"
                ).hasAnyRole(
                    "SUPER_ADMIN",
                    "ADMIN"
                )

                .requestMatchers(
                    HttpMethod.DELETE,
                    "/api/faculties/**"
                ).hasAnyRole(
                    "SUPER_ADMIN",
                    "ADMIN"
                )


                // =================================================
                // SEMESTER
                // =================================================

                .requestMatchers(
                    HttpMethod.GET,
                    "/api/semesters",
                    "/api/semesters/**"
                ).hasAnyRole(
                    "SUPER_ADMIN",
                    "ADMIN",
                    "FACULTY",
                    "STUDENT"
                )

                .requestMatchers(
                    HttpMethod.POST,
                    "/api/semesters/search"
                ).hasAnyRole(
                    "SUPER_ADMIN",
                    "ADMIN",
                    "FACULTY"
                )

                .requestMatchers(
                    HttpMethod.POST,
                    "/api/semesters"
                ).hasAnyRole(
                    "SUPER_ADMIN",
                    "ADMIN"
                )

                .requestMatchers(
                    HttpMethod.PUT,
                    "/api/semesters/**"
                ).hasAnyRole(
                    "SUPER_ADMIN",
                    "ADMIN"
                )

                .requestMatchers(
                    HttpMethod.DELETE,
                    "/api/semesters/**"
                ).hasAnyRole(
                    "SUPER_ADMIN",
                    "ADMIN"
                )


                // =================================================
                // FACULTY SUBJECT
                // =================================================

                .requestMatchers(
                    HttpMethod.GET,
                    "/api/faculty-subjects/my-subjects"
                ).hasRole("FACULTY")

                .requestMatchers(
                    HttpMethod.GET,
                    "/api/faculty-subjects/faculty/*/subjects"
                ).hasAnyRole(
                    "SUPER_ADMIN",
                    "ADMIN",
                    "FACULTY"
                )

                .requestMatchers(
                    HttpMethod.GET,
                    "/api/faculty-subjects",
                    "/api/faculty-subjects/**"
                ).hasAnyRole(
                    "SUPER_ADMIN",
                    "ADMIN",
                    "FACULTY"
                )

                .requestMatchers(
                    HttpMethod.POST,
                    "/api/faculty-subjects/search"
                ).hasAnyRole(
                    "SUPER_ADMIN",
                    "ADMIN",
                    "FACULTY"
                )

                .requestMatchers(
                    HttpMethod.POST,
                    "/api/faculty-subjects"
                ).hasAnyRole(
                    "SUPER_ADMIN",
                    "ADMIN"
                )

                .requestMatchers(
                    HttpMethod.PUT,
                    "/api/faculty-subjects/**"
                ).hasAnyRole(
                    "SUPER_ADMIN",
                    "ADMIN"
                )

                .requestMatchers(
                    HttpMethod.DELETE,
                    "/api/faculty-subjects/**"
                ).hasAnyRole(
                    "SUPER_ADMIN",
                    "ADMIN"
                )


                // =================================================
                // SUBJECT
                // =================================================

                .requestMatchers(
                    HttpMethod.GET,
                    "/api/subjects",
                    "/api/subjects/**"
                ).hasAnyRole(
                    "SUPER_ADMIN",
                    "ADMIN",
                    "FACULTY",
                    "STUDENT"
                )

                .requestMatchers(
                    HttpMethod.POST,
                    "/api/subjects"
                ).hasAnyRole(
                    "SUPER_ADMIN",
                    "ADMIN"
                )

                .requestMatchers(
                    HttpMethod.PUT,
                    "/api/subjects/**"
                ).hasAnyRole(
                    "SUPER_ADMIN",
                    "ADMIN"
                )

                .requestMatchers(
                    HttpMethod.DELETE,
                    "/api/subjects/**"
                ).hasAnyRole(
                    "SUPER_ADMIN",
                    "ADMIN"
                )


                // =================================================
                // ATTENDANCE REPORTS
                // =================================================

                .requestMatchers(
                    HttpMethod.GET,
                    "/api/attendance-reports/**"
                ).hasAnyRole(
                    "SUPER_ADMIN",
                    "ADMIN",
                    "FACULTY"
                )


                // =================================================
                // ATTENDANCE
                // =================================================

                .requestMatchers(
                    HttpMethod.GET,
                    "/api/attendances",
                    "/api/attendances/**"
                ).hasAnyRole(
                    "SUPER_ADMIN",
                    "ADMIN",
                    "FACULTY",
                    "STUDENT"
                )

                .requestMatchers(
                    HttpMethod.POST,
                    "/api/attendances/search"
                ).hasAnyRole(
                    "SUPER_ADMIN",
                    "ADMIN",
                    "FACULTY"
                )

                .requestMatchers(
                    HttpMethod.POST,
                    "/api/attendances/class",
                    "/api/attendances",
                    "/api/attendances/bulk"
                ).hasRole("FACULTY")

                .requestMatchers(
                    HttpMethod.PUT,
                    "/api/attendances/**"
                ).hasRole("FACULTY")

                .requestMatchers(
                    HttpMethod.DELETE,
                    "/api/attendances/**"
                ).hasRole("FACULTY")


                // =================================================
                // ATTENDANCE SESSION
                // =================================================

                .requestMatchers(
                    HttpMethod.POST,
                    "/api/attendance-sessions/open"
                ).hasRole("FACULTY")

                .requestMatchers(
                    HttpMethod.GET,
                    "/api/attendance-sessions/*",
                    "/api/attendance-sessions/mapping/*/date",
                    "/api/attendance-sessions/mapping/*/history"
                ).hasRole("FACULTY")

                .requestMatchers(
                    HttpMethod.POST,
                    "/api/attendance-sessions/*/complete",
                    "/api/attendance-sessions/*/cancel"
                ).hasRole("FACULTY")


                // =================================================
                // EXAM
                // =================================================

                .requestMatchers(
                    HttpMethod.GET,
                    "/api/exams",
                    "/api/exams/**"
                ).hasAnyRole(
                    "SUPER_ADMIN",
                    "ADMIN",
                    "FACULTY",
                    "STUDENT"
                )

                .requestMatchers(
                    HttpMethod.POST,
                    "/api/exams/search"
                ).hasAnyRole(
                    "SUPER_ADMIN",
                    "ADMIN",
                    "FACULTY"
                )

                .requestMatchers(
                    HttpMethod.POST,
                    "/api/exams"
                ).hasAnyRole(
                    "SUPER_ADMIN",
                    "ADMIN",
                    "FACULTY"
                )

                .requestMatchers(
                    HttpMethod.PUT,
                    "/api/exams/**"
                ).hasAnyRole(
                    "SUPER_ADMIN",
                    "ADMIN"
                )

                .requestMatchers(
                    HttpMethod.DELETE,
                    "/api/exams/**"
                ).hasAnyRole(
                    "SUPER_ADMIN",
                    "ADMIN"
                )


                // =================================================
                // RESULT
                // =================================================

                .requestMatchers(
                    HttpMethod.GET,
                    "/api/results",
                    "/api/results/**"
                ).hasAnyRole(
                    "SUPER_ADMIN",
                    "ADMIN",
                    "FACULTY",
                    "STUDENT"
                )

                .requestMatchers(
                    HttpMethod.POST,
                    "/api/results/search"
                ).hasAnyRole(
                    "SUPER_ADMIN",
                    "ADMIN",
                    "FACULTY"
                )

                .requestMatchers(
                    HttpMethod.POST,
                    "/api/results"
                ).hasAnyRole(
                    "SUPER_ADMIN",
                    "ADMIN"
                )

                .requestMatchers(
                    HttpMethod.PUT,
                    "/api/results/**"
                ).hasAnyRole(
                    "SUPER_ADMIN",
                    "ADMIN"
                )

                .requestMatchers(
                    HttpMethod.DELETE,
                    "/api/results/**"
                ).hasAnyRole(
                    "SUPER_ADMIN",
                    "ADMIN"
                )


                // =================================================
                // STUDENT DOCUMENT
                // =================================================

                .requestMatchers(
                    HttpMethod.POST,
                    "/api/student-documents/upload"
                ).hasAnyRole(
                    "SUPER_ADMIN",
                    "ADMIN",
                    "STUDENT"
                )


                // =================================================
                // PROFILE
                // =================================================

                .requestMatchers(
                    HttpMethod.GET,
                    "/api/profile/me"
                ).hasAnyRole(
                    "SUPER_ADMIN",
                    "ADMIN",
                    "VC",
                    "REGISTRAR",
                    "PRINCIPAL",
                    "DEAN",
                    "HOD",
                    "FACULTY",
                    "ACCOUNTANT",
                    "ADMISSION_OFFICER",
                    "EXAM_CONTROLLER",
                    "LIBRARIAN",
                    "HOSTEL_WARDEN",
                    "TRANSPORT_MANAGER",
                    "PLACEMENT_OFFICER",
                    "HR",
                    "STAFF",
                    "STUDENT",
                    "PARENT",
                    "ALUMNI"
                )

                .requestMatchers(
                    HttpMethod.PUT,
                    "/api/profile"
                ).hasAnyRole(
                    "SUPER_ADMIN",
                    "ADMIN",
                    "VC",
                    "REGISTRAR",
                    "PRINCIPAL",
                    "DEAN",
                    "HOD",
                    "FACULTY",
                    "ACCOUNTANT",
                    "ADMISSION_OFFICER",
                    "EXAM_CONTROLLER",
                    "LIBRARIAN",
                    "HOSTEL_WARDEN",
                    "TRANSPORT_MANAGER",
                    "PLACEMENT_OFFICER",
                    "HR",
                    "STAFF",
                    "STUDENT",
                    "PARENT",
                    "ALUMNI"
                )

                .requestMatchers(
                    HttpMethod.POST,
                    "/api/profile/photo/**"
                ).hasAnyRole(
                    "SUPER_ADMIN",
                    "ADMIN",
                    "VC",
                    "REGISTRAR",
                    "PRINCIPAL",
                    "DEAN",
                    "HOD",
                    "FACULTY",
                    "ACCOUNTANT",
                    "ADMISSION_OFFICER",
                    "EXAM_CONTROLLER",
                    "LIBRARIAN",
                    "HOSTEL_WARDEN",
                    "TRANSPORT_MANAGER",
                    "PLACEMENT_OFFICER",
                    "HR",
                    "STAFF",
                    "STUDENT",
                    "PARENT",
                    "ALUMNI"
                )

                .requestMatchers(
                    HttpMethod.DELETE,
                    "/api/profile/photo/**"
                ).hasAnyRole(
                    "SUPER_ADMIN",
                    "ADMIN",
                    "ACCOUNTANT",
                    "FACULTY",
                    "STUDENT"
                )

                .requestMatchers(
                    HttpMethod.PUT,
                    "/api/profile/change-password"
                ).hasAnyRole(
                    "SUPER_ADMIN",
                    "ADMIN",
                    "ACCOUNTANT",
                    "FACULTY",
                    "STUDENT"
                )


                // =================================================
                // LOGOUT
                // =================================================

                .requestMatchers(
                    HttpMethod.POST,
                    "/api/auth/logout"
                ).authenticated()


                // =================================================
                // DASHBOARD
                // =================================================

                .requestMatchers(
                    "/api/dashboard",
                    "/api/dashboard/**"
                ).hasAnyRole(
                    "SUPER_ADMIN",
                    "ADMIN",
                    "VC",
                    "REGISTRAR",
                    "PRINCIPAL",
                    "DEAN",
                    "HOD",
                    "FACULTY",
                    "ACCOUNTANT",
                    "ADMISSION_OFFICER",
                    "EXAM_CONTROLLER",
                    "LIBRARIAN",
                    "HOSTEL_WARDEN",
                    "TRANSPORT_MANAGER",
                    "PLACEMENT_OFFICER",
                    "HR",
                    "STAFF",
                    "STUDENT",
                    "PARENT",
                    "ALUMNI"
                )


                // =================================================
                // FEE REPORTS
                // =================================================

                .requestMatchers(
                    "/api/v1/fee-reports/**"
                ).hasAnyRole(
                    "SUPER_ADMIN",
                    "ADMIN",
                    "ACCOUNTANT"
                )


                // =================================================
                // FEE DASHBOARD
                // =================================================

                .requestMatchers(
                    "/api/v1/fee-dashboard/**"
                ).hasAnyRole(
                    "SUPER_ADMIN",
                    "ADMIN",
                    "ACCOUNTANT"
                )


                // =================================================
                // FEE STRUCTURE
                // =================================================

                .requestMatchers(
                    HttpMethod.GET,
                    "/api/v1/fee-structures",
                    "/api/v1/fee-structures/**"
                ).hasAnyRole(
                    "SUPER_ADMIN",
                    "ADMIN",
                    "ACCOUNTANT"
                )

                .requestMatchers(
                    HttpMethod.POST,
                    "/api/v1/fee-structures/search"
                ).hasAnyRole(
                    "SUPER_ADMIN",
                    "ADMIN",
                    "ACCOUNTANT"
                )

                .requestMatchers(
                    HttpMethod.POST,
                    "/api/v1/fee-structures"
                ).hasAnyRole(
                    "SUPER_ADMIN",
                    "ADMIN"
                )

                .requestMatchers(
                    HttpMethod.PUT,
                    "/api/v1/fee-structures/**"
                ).hasAnyRole(
                    "SUPER_ADMIN",
                    "ADMIN"
                )

                .requestMatchers(
                    HttpMethod.DELETE,
                    "/api/v1/fee-structures/**"
                ).hasAnyRole(
                    "SUPER_ADMIN",
                    "ADMIN"
                )


                // =================================================
                // STUDENT FEE PAYMENT
                // =================================================

                .requestMatchers(
                    "/api/v1/student-fee-payments/**"
                ).hasAnyRole(
                    "SUPER_ADMIN",
                    "ADMIN",
                    "ACCOUNTANT"
                )


                // =================================================
                // FEE REFUND
                // =================================================

                /*
                 * IMPORTANT:
                 * Specific approval/rejection endpoints MUST come
                 * before the broad /fee-refunds/** rule.
                 */

                .requestMatchers(
                    HttpMethod.PUT,
                    "/api/v1/fee-refunds/*/approve",
                    "/api/v1/fee-refunds/*/reject"
                ).hasAnyRole(
                    "SUPER_ADMIN",
                    "ADMIN"
                )

                .requestMatchers(
                    HttpMethod.PUT,
                    "/api/v1/fee-refunds/*/complete"
                ).hasAnyRole(
                    "SUPER_ADMIN",
                    "ADMIN"
                )

                .requestMatchers(
                    HttpMethod.POST,
                    "/api/v1/fee-refunds"
                ).hasAnyRole(
                    "SUPER_ADMIN",
                    "ADMIN",
                    "ACCOUNTANT"
                )

                .requestMatchers(
                    HttpMethod.POST,
                    "/api/v1/fee-refunds/search"
                ).hasAnyRole(
                    "SUPER_ADMIN",
                    "ADMIN",
                    "ACCOUNTANT"
                )

                .requestMatchers(
                    HttpMethod.GET,
                    "/api/v1/fee-refunds/**"
                ).hasAnyRole(
                    "SUPER_ADMIN",
                    "ADMIN",
                    "ACCOUNTANT"
                )

                .requestMatchers(
                    HttpMethod.DELETE,
                    "/api/v1/fee-refunds/**"
                ).hasAnyRole(
                    "SUPER_ADMIN",
                    "ADMIN"
                )


                // =================================================
                // FEE RECEIPT
                // =================================================

                .requestMatchers(
                    HttpMethod.POST,
                    "/api/v1/fee-receipts/payments/*/receipt"
                ).hasAnyRole(
                    "SUPER_ADMIN",
                    "ADMIN",
                    "ACCOUNTANT"
                )

                .requestMatchers(
                    HttpMethod.GET,
                    "/api/v1/fee-receipts",
                    "/api/v1/fee-receipts/*",
                    "/api/v1/fee-receipts/*/download"
                ).hasAnyRole(
                    "SUPER_ADMIN",
                    "ADMIN",
                    "ACCOUNTANT"
                )

                .requestMatchers(
                    HttpMethod.POST,
                    "/api/v1/fee-receipts/search"
                ).hasAnyRole(
                    "SUPER_ADMIN",
                    "ADMIN",
                    "ACCOUNTANT"
                )

                .requestMatchers(
                    HttpMethod.DELETE,
                    "/api/v1/fee-receipts/*"
                ).hasAnyRole(
                    "SUPER_ADMIN",
                    "ADMIN"
                )


                // =================================================
                // SCHOLARSHIP
                // =================================================

                /*
                 * Specific approval endpoints FIRST.
                 */

                .requestMatchers(
                    HttpMethod.PUT,
                    "/api/v1/scholarships/*/approve",
                    "/api/v1/scholarships/*/reject"
                ).hasAnyRole(
                    "SUPER_ADMIN",
                    "ADMIN"
                )

                /*
                 * General scholarship APIs.
                 */
                .requestMatchers(
                    "/api/v1/scholarships/**"
                ).hasAnyRole(
                    "SUPER_ADMIN",
                    "ADMIN",
                    "ACCOUNTANT"
                )

                .requestMatchers(
                    HttpMethod.GET,
                    "/api/v1/reports/scholarships"
                ).hasAnyRole(
                    "SUPER_ADMIN",
                    "ADMIN",
                    "ACCOUNTANT"
                )


                // =================================================
                // INSTALLMENT
                // =================================================

                .requestMatchers(
                    HttpMethod.POST,
                    "/api/v1/fee-installments"
                ).hasAnyRole(
                    "SUPER_ADMIN",
                    "ADMIN",
                    "ACCOUNTANT"
                )

                .requestMatchers(
                    HttpMethod.PUT,
                    "/api/v1/fee-installments/**"
                ).hasAnyRole(
                    "SUPER_ADMIN",
                    "ADMIN",
                    "ACCOUNTANT"
                )

                .requestMatchers(
                    HttpMethod.PATCH,
                    "/api/v1/fee-installments/*/pay"
                ).hasAnyRole(
                    "SUPER_ADMIN",
                    "ADMIN",
                    "ACCOUNTANT"
                )

                .requestMatchers(
                    HttpMethod.POST,
                    "/api/v1/fee-installments/search"
                ).hasAnyRole(
                    "SUPER_ADMIN",
                    "ADMIN",
                    "ACCOUNTANT"
                )

                .requestMatchers(
                    HttpMethod.GET,
                    "/api/v1/fee-installments",
                    "/api/v1/fee-installments/*"
                ).hasAnyRole(
                    "SUPER_ADMIN",
                    "ADMIN",
                    "ACCOUNTANT",
                    "STUDENT"
                )

                .requestMatchers(
                    HttpMethod.DELETE,
                    "/api/v1/fee-installments/*"
                ).hasAnyRole(
                    "SUPER_ADMIN",
                    "ADMIN"
                )


                // =================================================
                // EVERYTHING ELSE
                // =================================================

                /*
                 * Fail closed:
                 * Any endpoint not explicitly public or authorized
                 * above requires authentication.
                 */
                .anyRequest()
                .authenticated()
            )


            // =====================================================
            // STATELESS SESSION
            // =====================================================

            .sessionManagement(session ->
                session.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS
                )
            )


            // =====================================================
            // AUTHENTICATION ENTRY POINT
            // =====================================================

            .exceptionHandling(exception ->
                exception.authenticationEntryPoint(
                    authenticationEntryPoint
                )
            )


            // =====================================================
            // AUTHENTICATION PROVIDER
            // =====================================================

            .authenticationProvider(
                authenticationProvider()
            )


            // =====================================================
            // JWT FILTER
            // =====================================================

            .addFilterBefore(
                jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class
            );


        return http.build();
    }


    // =============================================================
    // AUTHENTICATION PROVIDER
    // =============================================================

    @Bean
    AuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider();

        provider.setUserDetailsService(
                userDetailsService
        );

        provider.setPasswordEncoder(
                passwordEncoder
        );

        return provider;
    }


    // =============================================================
    // AUTHENTICATION MANAGER
    // =============================================================

    @Bean
    AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration
    ) throws Exception {

        return configuration.getAuthenticationManager();
    }
}
