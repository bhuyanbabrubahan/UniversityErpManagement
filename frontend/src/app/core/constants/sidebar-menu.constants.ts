import { NavigationItem } from '../../shared/models/navigation.model';
import { Role } from '../../shared/enums/role.enum';

const ALL: Role[] = Object.values(Role);
const MANAGEMENT = [Role.SUPER_ADMIN, Role.ADMIN];
const ACADEMIC_LEADERS = [Role.SUPER_ADMIN, Role.ADMIN, Role.VC, Role.REGISTRAR, Role.PRINCIPAL, Role.DEAN, Role.HOD];
const TEACHING = [...ACADEMIC_LEADERS, Role.FACULTY];
const FINANCE = [Role.SUPER_ADMIN, Role.ADMIN, Role.ACCOUNTANT];
const EXAM = [Role.SUPER_ADMIN, Role.ADMIN, Role.EXAM_CONTROLLER, Role.HOD, Role.FACULTY, Role.STUDENT];

export const SIDEBAR_MENU: NavigationItem[] = [
 {label:'Dashboard',icon:'dashboard',route:'/dashboard',roles:ALL},
 {label:'Academics',icon:'school',roles:TEACHING,children:[
  {label:'Students',icon:'groups',route:'/dashboard/students',roles:[...MANAGEMENT,Role.HOD,Role.FACULTY]},
  {label:'Faculty',icon:'groups',route:'/dashboard/faculty',roles:[...MANAGEMENT,Role.DEAN,Role.HOD]},
  {label:'Departments',icon:'account_balance',route:'/dashboard/department',roles:[...MANAGEMENT,Role.VC,Role.REGISTRAR,Role.DEAN]},
  {label:'Courses',icon:'menu_book',route:'/dashboard/course',roles:[...ACADEMIC_LEADERS,Role.FACULTY,Role.STUDENT]},
  {label:'Subjects',icon:'auto_stories',route:'/dashboard/subject',roles:[...TEACHING,Role.STUDENT]},
  {label:'Semesters',icon:'calendar_month',route:'/dashboard/semester',roles:[...TEACHING,Role.STUDENT]},
  {label:'Admissions',icon:'how_to_reg',route:'/dashboard/admission',roles:[...MANAGEMENT,Role.REGISTRAR,Role.ADMISSION_OFFICER]},
  {label:'Curriculum',icon:'account_tree',route:'/dashboard/curriculum',roles:ACADEMIC_LEADERS},
  {label:'Timetable',icon:'schedule',route:'/dashboard/timetable',roles:[...ACADEMIC_LEADERS,Role.FACULTY,Role.STUDENT]},
  {label:'HOD',icon:'supervisor_account',route:'/dashboard/hod',roles:[...ACADEMIC_LEADERS]},
  {label:'Marks',icon:'grading',route:'/dashboard/marks',roles:EXAM},
 ]},
 {label:'Attendance',icon:'fact_check',roles:[...MANAGEMENT,Role.HOD,Role.FACULTY,Role.STUDENT],children:[
  {label:'Mark Attendance',icon:'edit_calendar',route:'/dashboard/attendance/mark',roles:[...MANAGEMENT,Role.HOD,Role.FACULTY]},
  {label:'Attendance History',icon:'history',route:'/dashboard/attendance/history',roles:[...MANAGEMENT,Role.HOD,Role.FACULTY]},
  {label:'Attendance Reports',icon:'assessment',route:'/dashboard/attendance/report',roles:[...MANAGEMENT,Role.HOD,Role.FACULTY]},
  {label:'My Attendance',icon:'event_available',route:'/dashboard/attendance/student',roles:[Role.STUDENT]},
 ]},
 {label:'Examination',icon:'assignment',roles:EXAM,children:[
  {label:'Exams',icon:'quiz',route:'/dashboard/examination',roles:[...MANAGEMENT,Role.REGISTRAR,Role.EXAM_CONTROLLER,Role.HOD,Role.FACULTY]},
  {label:'Marks Entry',icon:'grading',route:'/dashboard/marks',roles:[...MANAGEMENT,Role.EXAM_CONTROLLER,Role.HOD,Role.FACULTY]},
  {label:'My Results',icon:'workspace_premium',route:'/dashboard/examination',roles:[Role.STUDENT]},
 ]},
 {label:'Finance',icon:'payments',roles:[...FINANCE,Role.STUDENT,Role.PARENT],children:[
  {label:'Finance Dashboard',icon:'insights',route:'/dashboard/finance',roles:FINANCE},
  {label:'Payments',icon:'payments',route:'/dashboard/payment',roles:[...FINANCE,Role.STUDENT,Role.PARENT]},
  {label:'Installments',icon:'event_repeat',route:'/dashboard/installment',roles:[...FINANCE,Role.STUDENT,Role.PARENT]},
  {label:'Scholarships',icon:'school',route:'/dashboard/scholarship',roles:[...FINANCE,Role.STUDENT]},
  {label:'Refunds',icon:'currency_exchange',route:'/dashboard/refund',roles:FINANCE},
  {label:'Receipts',icon:'receipt_long',route:'/dashboard/reports/fees',roles:FINANCE},
 ]},
 {label:'Reports',icon:'bar_chart',roles:[...MANAGEMENT,Role.VC,Role.REGISTRAR,Role.PRINCIPAL,Role.DEAN,Role.HOD,Role.FACULTY,Role.ACCOUNTANT,Role.EXAM_CONTROLLER,Role.LIBRARIAN,Role.HR],children:[
  {label:'Report Centre',icon:'description',route:'/dashboard/reports',roles:[...MANAGEMENT,Role.VC,Role.REGISTRAR,Role.PRINCIPAL,Role.DEAN,Role.HOD,Role.ACCOUNTANT,Role.EXAM_CONTROLLER,Role.LIBRARIAN,Role.HR]},
  {label:'Attendance Reports',icon:'fact_check',route:'/dashboard/attendance/report',roles:[...MANAGEMENT,Role.HOD,Role.FACULTY]},
  {label:'Audit Logs',icon:'security',route:'/dashboard/audit',roles:MANAGEMENT},
 ]},
 {label:'Operations',icon:'business',roles:[...MANAGEMENT,Role.HR,Role.STAFF,Role.LIBRARIAN,Role.HOSTEL_WARDEN,Role.TRANSPORT_MANAGER,Role.PLACEMENT_OFFICER,Role.STUDENT,Role.FACULTY],children:[
  {label:'User Management',icon:'manage_accounts',route:'/dashboard/accounts',roles:MANAGEMENT},
  {label:'Staff',icon:'badge',route:'/dashboard/staff',roles:[...MANAGEMENT,Role.HR]},
  {label:'Library',icon:'local_library',route:'/dashboard/library',roles:[...MANAGEMENT,Role.LIBRARIAN,Role.FACULTY,Role.STUDENT]},
  {label:'Hostel',icon:'hotel',route:'/dashboard/hostel',roles:[...MANAGEMENT,Role.HOSTEL_WARDEN,Role.STUDENT]},
  {label:'Transport',icon:'directions_bus',route:'/dashboard/transport',roles:[...MANAGEMENT,Role.TRANSPORT_MANAGER,Role.STAFF,Role.STUDENT]},
  {label:'Placement',icon:'work',route:'/dashboard/placement',roles:[...MANAGEMENT,Role.PLACEMENT_OFFICER,Role.STUDENT]},
  {label:'Certificates',icon:'verified',route:'/dashboard/certificate',roles:[...MANAGEMENT,Role.REGISTRAR,Role.STUDENT,Role.ALUMNI]},
  {label:'Research',icon:'science',route:'/dashboard/research',roles:[...MANAGEMENT,Role.DEAN,Role.FACULTY]},
  {label:'Grievance / Help',icon:'support_agent',route:'/dashboard/grievance',roles:ALL},
 ]},
 {label:'System',icon:'settings',roles:ALL,children:[
  {label:'Profile',icon:'person',route:'/dashboard/profile',roles:ALL},
  {label:'Notifications',icon:'notifications',route:'/dashboard/notifications',roles:ALL},
  {label:'Settings',icon:'tune',route:'/dashboard/settings',roles:ALL},
  {label:'Audit Logs',icon:'policy',route:'/dashboard/audit',roles:MANAGEMENT},
 ]},
];
