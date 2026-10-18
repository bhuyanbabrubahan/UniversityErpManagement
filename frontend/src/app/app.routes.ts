import { Routes } from "@angular/router";

import { AUTH_ROUTES } from "./features/auth/auth.routes";

import { DashboardComponent } from "./features/dashboard/pages/dashboard/dashboard.component";

import { DashboardLayoutComponent } from "./core/layouts/dashboard-layout/dashboard-layout.component";

import { authGuard } from "./core/guards/auth.guard";

import { roleGuard } from "./core/guards/role.guard";

import { ROLES } from "./core/constants/role.constants";

export const routes: Routes = [
  // =====================================================
  // ROOT
  // =====================================================

  {
    path: "",
    redirectTo: "login",
    pathMatch: "full",
  },

  // =====================================================
  // AUTH
  // =====================================================

  ...AUTH_ROUTES,

  // =====================================================
  // DASHBOARD
  // =====================================================

  {
    path: "dashboard",

    component: DashboardLayoutComponent,

    canActivate: [authGuard],

    children: [
      // ===================================================
      // DASHBOARD HOME
      // ===================================================

      {
        path: "",
        component: DashboardComponent,
      },

      // ===================================================
      // PROFILE
      // ===================================================

      {
        path: "profile",

        loadChildren: () =>
          import("./features/profile/profile.routes").then(
            (m) => m.PROFILE_ROUTES,
          ),
      },

      // ===================================================
      // STUDENTS
      // SUPER_ADMIN / ADMIN / FACULTY
      // ===================================================

      {
        path: "students",

        canActivate: [
          roleGuard([
            ROLES.SUPER_ADMIN,
            ROLES.ADMIN,
            ROLES.HOD,
            ROLES.FACULTY,
            ROLES.DEAN,
            ROLES.REGISTRAR,
            ROLES.EXAM_CONTROLLER,
            ROLES.ADMISSION_OFFICER,
          ]),
        ],

        loadChildren: () =>
          import("./features/student/student.routes").then(
            (m) => m.STUDENT_ROUTES,
          ),
      },

      // ===================================================
      // FACULTY
      // SUPER_ADMIN / ADMIN
      // ===================================================

      {
        path: "faculty",

        canActivate: [roleGuard([ROLES.SUPER_ADMIN, ROLES.ADMIN])],

        loadChildren: () =>
          import("./features/faculty/faculty.routes").then(
            (m) => m.FACULTY_ROUTES,
          ),
      },

      // ===================================================
      // DEPARTMENT
      // SUPER_ADMIN / ADMIN
      // ===================================================

      {
        path: "department",

        canActivate: [roleGuard([ROLES.SUPER_ADMIN, ROLES.ADMIN])],

        loadChildren: () =>
          import("./features/department/department.routes").then(
            (m) => m.DEPARTMENT_ROUTES,
          ),
      },

      // ===================================================
      // COURSE
      // SUPER_ADMIN / ADMIN / FACULTY
      // ===================================================

      {
        path: "course",

        canActivate: [
          roleGuard([
            ROLES.SUPER_ADMIN,
            ROLES.ADMIN,
            ROLES.VC,
            ROLES.REGISTRAR,
            ROLES.PRINCIPAL,
            ROLES.DEAN,
            ROLES.HOD,
            ROLES.FACULTY,
            ROLES.STUDENT,
          ]),
        ],

        loadChildren: () =>
          import("./features/course/course.routes").then(
            (m) => m.COURSE_ROUTES,
          ),
      },

      // ===================================================
      // SEMESTER
      // SUPER_ADMIN / ADMIN / FACULTY
      // =====================================================

      {
        path: "semester",

        canActivate: [
          roleGuard([
            ROLES.SUPER_ADMIN,
            ROLES.ADMIN,
            ROLES.VC,
            ROLES.REGISTRAR,
            ROLES.PRINCIPAL,
            ROLES.DEAN,
            ROLES.HOD,
            ROLES.FACULTY,
            ROLES.STUDENT,
          ]),
        ],

        loadChildren: () =>
          import("./features/semester/semester.routes").then(
            (m) => m.SEMESTER_ROUTES,
          ),
      },

      // =====================================================
      // PAYMENT
      // SUPER_ADMIN / ADMIN / STUDENT
      // =====================================================

      {
        path: "payment",

        canActivate: [
          roleGuard([ROLES.SUPER_ADMIN, ROLES.ADMIN, ROLES.STUDENT]),
        ],

        loadChildren: () =>
          import("./features/payment/payment.routes").then(
            (m) => m.PAYMENT_ROUTES,
          ),
      },

      // =====================================================
      // SUBJECT
      // SUPER_ADMIN / ADMIN / STUDENT
      // =====================================================

      {
        path: "subject",

        canActivate: [
          roleGuard([ROLES.SUPER_ADMIN, ROLES.ADMIN, ROLES.STUDENT]),
        ],

        loadChildren: () =>
          import("./features/subject/subject.routes").then(
            (m) => m.SUBJECT_ROUTES,
          ),
      },

      // =====================================================
      // STUDENT DOCUMENT
      // SUPER_ADMIN / ADMIN / FACULTY / STUDENT
      // =====================================================

      {
        path: "student-documents",

        canActivate: [
          roleGuard([
            ROLES.SUPER_ADMIN,
            ROLES.ADMIN,
            ROLES.HOD,
            ROLES.FACULTY,
            ROLES.STUDENT,
          ]),
        ],

        loadChildren: () =>
          import("./features/student-document/student-document.routes").then(
            (m) => m.STUDENT_DOCUMENT_ROUTES,
          ),
      },

      // =====================================================
      // EXAMINATION
      // =====================================================

      {
        path: "examination",

        canActivate: [
          roleGuard([
            ROLES.SUPER_ADMIN,
            ROLES.ADMIN,
            ROLES.HOD,
            ROLES.FACULTY,
            ROLES.STUDENT,
          ]),
        ],

        loadComponent: () =>
          import("./features/examination/examination.component").then(
            (m) => m.ExaminationComponent,
          ),
      },

      // =====================================================
      // FINANCE
      // =====================================================

      {
        path: "finance",

        canActivate: [
          roleGuard([ROLES.SUPER_ADMIN, ROLES.ADMIN, ROLES.ACCOUNTANT]),
        ],

        loadComponent: () =>
          import("./features/workspace/pages/finance/finance.component").then(
            (m) => m.FinanceComponent,
          ),
      },

      // =====================================================
      // REPORTS
      // =====================================================

      {
        path: "reports",

        canActivate: [
          roleGuard([
            ROLES.SUPER_ADMIN,
            ROLES.ADMIN,
            ROLES.VC,
            ROLES.REGISTRAR,
            ROLES.PRINCIPAL,
            ROLES.DEAN,
            ROLES.HOD,
            ROLES.FACULTY,
            ROLES.ACCOUNTANT,
            ROLES.EXAM_CONTROLLER,
            ROLES.LIBRARIAN,
            ROLES.HR,
          ]),
        ],

        loadComponent: () =>
          import("./features/workspace/pages/reports/reports.component").then(
            (m) => m.ReportsWorkspaceComponent,
          ),
      },

      // =====================================================
      // AUDIT
      // =====================================================

      {
        path: "audit",

        canActivate: [roleGuard([ROLES.SUPER_ADMIN, ROLES.ADMIN])],

        loadComponent: () =>
          import("./features/workspace/pages/audit/audit.component").then(
            (m) => m.AuditComponent,
          ),
      },

      // =====================================================
      // NOTIFICATIONS
      // =====================================================

      {
        path: "notifications",

        canActivate: [roleGuard(Object.values(ROLES))],

        loadChildren: () =>
          import("./features/notification/notification.routes").then(
            (m) => m.NOTIFICATION_ROUTES,
          ),
      },

      // =====================================================
      // SETTINGS
      // =====================================================

      {
        path: "settings",
        canActivate: [roleGuard(Object.values(ROLES))],

        loadComponent: () =>
          import("./features/workspace/pages/settings/settings.component").then(
            (m) => m.SettingsComponent,
          ),
      },

      // =====================================================
      // ACCOUNT MANAGEMENT
      // =====================================================

      {
        path: "accounts",
        canActivate: [roleGuard([ROLES.SUPER_ADMIN, ROLES.ADMIN])],
        loadComponent: () =>
          import("./features/admin/pages/account-management/account-management.component").then(
            (m) => m.AccountManagementComponent,
          ),
      },

      // =====================================================
      // STAFF
      // SUPER_ADMIN / ADMIN / HR
      // =====================================================

      {
        path: "staff",

        canActivate: [roleGuard([ROLES.SUPER_ADMIN, ROLES.ADMIN, ROLES.HR])],

        loadChildren: () =>
          import("./features/staff/staff.routes").then((m) => m.STAFF_ROUTES),
      },

      // =====================================================
      // HOD
      // SUPER_ADMIN / ADMIN / DEAN / HOD
      // =====================================================

      {
        path: "hod",

        canActivate: [
          roleGuard([ROLES.SUPER_ADMIN, ROLES.ADMIN, ROLES.DEAN, ROLES.HOD]),
        ],

        loadChildren: () =>
          import("./features/hod/hod.routes").then((m) => m.HOD_ROUTES),
      },

      // =====================================================
      // CURRICULUM
      // =====================================================

      {
        path: "curriculum",

        canActivate: [
          roleGuard([
            ROLES.SUPER_ADMIN,
            ROLES.ADMIN,
            ROLES.REGISTRAR,
            ROLES.DEAN,
            ROLES.HOD,
          ]),
        ],

        loadChildren: () =>
          import("./features/curriculum/curriculum.routes").then(
            (m) => m.CURRICULUM_ROUTES,
          ),
      },

      // =====================================================
      // TIMETABLE
      // =====================================================

      {
        path: "timetable",

        canActivate: [
          roleGuard([
            ROLES.SUPER_ADMIN,
            ROLES.ADMIN,
            ROLES.REGISTRAR,
            ROLES.DEAN,
            ROLES.HOD,
            ROLES.FACULTY,
          ]),
        ],

        loadChildren: () =>
          import("./features/timetable/timetable.routes").then(
            (m) => m.TIMETABLE_ROUTES,
          ),
      },

      // =====================================================
      // MARKS
      // =====================================================

      {
        path: "marks",

        canActivate: [
          roleGuard([
            ROLES.SUPER_ADMIN,
            ROLES.ADMIN,
            ROLES.EXAM_CONTROLLER,
            ROLES.HOD,
            ROLES.FACULTY,
            ROLES.STUDENT,
          ]),
        ],

        loadChildren: () =>
          import("./features/marks/marks.routes").then((m) => m.MARKS_ROUTES),
      },

      // =====================================================
      // INSTALLMENT
      // =====================================================

      {
        path: "installment",

        canActivate: [
          roleGuard([
            ROLES.SUPER_ADMIN,
            ROLES.ADMIN,
            ROLES.ACCOUNTANT,
            ROLES.STUDENT,
            ROLES.PARENT,
          ]),
        ],

        loadChildren: () =>
          import("./features/installment/installment.routes").then(
            (m) => m.INSTALLMENT_ROUTES,
          ),
      },

      // =====================================================
      // REFUND
      // =====================================================

      {
        path: "refund",
        canActivate: [
          roleGuard([ROLES.SUPER_ADMIN, ROLES.ADMIN, ROLES.ACCOUNTANT]),
        ],
        loadComponent: () =>
          import("./features/workspace/pages/refund/refund.component").then(
            (m) => m.RefundComponent,
          ),
      },

      // =====================================================
      // SCHOLARSHIP
      // =====================================================

      {
        path: "scholarship",

        canActivate: [
          roleGuard([
            ROLES.SUPER_ADMIN,
            ROLES.ADMIN,
            ROLES.ACCOUNTANT,
            ROLES.STUDENT,
          ]),
        ],

        loadChildren: () =>
          import("./features/scholarship/scholarship.routes").then(
            (m) => m.SCHOLARSHIP_ROUTES,
          ),
      },

      // =====================================================
      // LIBRARY
      // =====================================================

      {
        path: "library",

        canActivate: [
          roleGuard([
            ROLES.SUPER_ADMIN,
            ROLES.ADMIN,
            ROLES.LIBRARIAN,
            ROLES.STUDENT,
            ROLES.FACULTY,
          ]),
        ],

        loadChildren: () =>
          import("./features/library/library.routes").then(
            (m) => m.LIBRARY_ROUTES,
          ),
      },

      // =====================================================
      // HOSTEL
      // =====================================================

      {
        path: "hostel",

        canActivate: [
          roleGuard([
            ROLES.SUPER_ADMIN,
            ROLES.ADMIN,
            ROLES.HOSTEL_WARDEN,
            ROLES.STUDENT,
          ]),
        ],

        loadChildren: () =>
          import("./features/hostel/hostel.routes").then(
            (m) => m.HOSTEL_ROUTES,
          ),
      },

      // =====================================================
      // TRANSPORT
      // =====================================================

      {
        path: "transport",

        canActivate: [
          roleGuard([
            ROLES.SUPER_ADMIN,
            ROLES.ADMIN,
            ROLES.TRANSPORT_MANAGER,
            ROLES.STUDENT,
            ROLES.STAFF,
          ]),
        ],

        loadChildren: () =>
          import("./features/transport/transport.routes").then(
            (m) => m.TRANSPORT_ROUTES,
          ),
      },

      // =====================================================
      // PLACEMENT
      // =====================================================

      {
        path: "placement",

        canActivate: [
          roleGuard([
            ROLES.SUPER_ADMIN,
            ROLES.ADMIN,
            ROLES.PLACEMENT_OFFICER,
            ROLES.STUDENT,
          ]),
        ],

        loadChildren: () =>
          import("./features/placement/placement.routes").then(
            (m) => m.PLACEMENT_ROUTES,
          ),
      },

      // =====================================================
      // CERTIFICATE
      // =====================================================

      {
        path: "certificate",

        canActivate: [
          roleGuard([
            ROLES.SUPER_ADMIN,
            ROLES.ADMIN,
            ROLES.REGISTRAR,
            ROLES.STUDENT,
            ROLES.ALUMNI,
          ]),
        ],

        loadChildren: () =>
          import("./features/certificate/certificate.routes").then(
            (m) => m.CERTIFICATE_ROUTES,
          ),
      },

      // =====================================================
      // NOTIFICATION
      // =====================================================

      {
        path: "notification",

        canActivate: [roleGuard(Object.values(ROLES))],

        loadChildren: () =>
          import("./features/notification/notification.routes").then(
            (m) => m.NOTIFICATION_ROUTES,
          ),
      },

      // =====================================================
      // RESEARCH
      // =====================================================

      {
        path: "research",
        canActivate: [
          roleGuard([
            ROLES.SUPER_ADMIN,
            ROLES.ADMIN,
            ROLES.DEAN,
            ROLES.FACULTY,
          ]),
        ],
        loadChildren: () =>
          import("./features/research/research.routes").then(
            (m) => m.RESEARCH_ROUTES,
          ),
      },

      // =====================================================
      // GRIEVANCE
      // =====================================================

      {
        path: "grievance",

        canActivate: [roleGuard(Object.values(ROLES))],

        loadChildren: () =>
          import("./features/grievance/grievance.routes").then(
            (m) => m.GRIEVANCE_ROUTES,
          ),
      },

      // =====================================================
      // ADMISSION
      // SUPER_ADMIN / ADMIN
      // =====================================================

      {
        path: "admission",

        canActivate: [
          roleGuard([
            ROLES.SUPER_ADMIN,
            ROLES.ADMIN,
            ROLES.REGISTRAR,
            ROLES.ADMISSION_OFFICER,
          ]),
        ],

        loadChildren: () =>
          import("./features/admission/admission.routes").then(
            (m) => m.ADMISSION_ROUTES,
          ),
      },

      // =====================================================
      // ATTENDANCE
      // =====================================================

      {
        path: "attendance",

        canActivate: [
          roleGuard([
            ROLES.SUPER_ADMIN,
            ROLES.ADMIN,
            ROLES.HOD,
            ROLES.FACULTY,
            ROLES.STUDENT,
          ]),
        ],

        loadChildren: () =>
          import("./features/attendance/attendance.routes").then(
            (m) => m.ATTENDANCE_ROUTES,
          ),
      },
    ],
  },

  // =====================================================
  // WILDCARD
  // =====================================================

  {
    path: "**",
    redirectTo: "login",
  },
];
