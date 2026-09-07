import { Component, OnInit, inject } from '@angular/core';
import { Router } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';

import { AuthService } from '../../../auth/services/auth.service';
import { DashboardService } from '../../../auth/services/dashboard.service';

import { StatCardComponent } from '../../components/stat-card/stat-card.component';
import { DashboardStat } from '../../models/dashboard-stat.model';

import { ProfileResponse } from '../../../profile/models/profile-response.model';
import { ProfileCardComponent } from '@app/shared/components/profile-card/profile-card.component';

import { Role } from '@app/shared/enums/role.enum';

@Component({
  selector: 'app-dashboard-home',
  standalone: true,
  imports: [
    MatCardModule,
    MatIconModule,
    MatButtonModule,
    StatCardComponent
],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})
export class DashboardComponent implements OnInit {

  private readonly dashboardService = inject(DashboardService);
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);

  stats: DashboardStat[] = [];

  role: Role | null = null;

  profile: ProfileResponse | null = null;
  private dashboardData: any = {};

  get dashboardTitle(): string { const names:any={SUPER_ADMIN:'Super Admin Workspace',ADMIN:'Administration Workspace',VC:'VC Workspace',REGISTRAR:'Registrar Workspace',PRINCIPAL:'Principal Workspace',DEAN:'Dean Workspace',HOD:'HOD Workspace',FACULTY:'Faculty Workspace',ACCOUNTANT:'Finance Workspace',ADMISSION_OFFICER:'Admissions Workspace',EXAM_CONTROLLER:'Examination Workspace',LIBRARIAN:'Library Workspace',HOSTEL_WARDEN:'Hostel Workspace',TRANSPORT_MANAGER:'Transport Workspace',PLACEMENT_OFFICER:'Placement Workspace',HR:'HR Workspace',STAFF:'Staff Workspace',STUDENT:'Student Workspace',PARENT:'Parent Workspace',ALUMNI:'Alumni Workspace'}; return names[this.role||'']||'ERP Workspace'; }
  get dashboardSubtitle(): string { if(this.role===Role.STUDENT)return 'Your academics, attendance, examinations and fees in one place.'; if(this.role===Role.FACULTY)return 'Teaching, attendance, examinations and assigned student operations.'; if(this.role===Role.ACCOUNTANT)return 'Fee collection, dues, refunds, scholarships and finance reporting.'; return 'Role-aware institutional workspace with controlled operational access.'; }
  get quickActions(): any[] { const map:any={SUPER_ADMIN:[['User Management','manage_accounts','/dashboard/accounts'],['Reports','description','/dashboard/reports'],['Audit Logs','security','/dashboard/audit'],['Settings','tune','/dashboard/settings']],ADMIN:[['Students','groups','/dashboard/students'],['Admissions','how_to_reg','/dashboard/admission'],['Reports','description','/dashboard/reports'],['Accounts','manage_accounts','/dashboard/accounts']],HOD:[['Department Students','groups','/dashboard/students'],['Attendance','fact_check','/dashboard/attendance'],['Marks','grading','/dashboard/marks'],['Reports','description','/dashboard/reports']],FACULTY:[['Mark Attendance','fact_check','/dashboard/attendance/mark'],['Students','groups','/dashboard/students'],['Marks','grading','/dashboard/marks'],['Exams','quiz','/dashboard/examination']],ACCOUNTANT:[['Finance Dashboard','insights','/dashboard/finance'],['Payments','payments','/dashboard/payment'],['Installments','event_repeat','/dashboard/installment'],['Reports','description','/dashboard/reports']],STUDENT:[['My Attendance','event_available','/dashboard/attendance/student'],['My Results','grading','/dashboard/examination'],['Fees','payments','/dashboard/payment'],['Profile','person','/dashboard/profile']],EXAM_CONTROLLER:[['Exams','quiz','/dashboard/examination'],['Marks','grading','/dashboard/marks'],['Reports','description','/dashboard/reports']],ADMISSION_OFFICER:[['Admissions','how_to_reg','/dashboard/admission'],['Students','groups','/dashboard/students'],['Reports','description','/dashboard/reports']],LIBRARIAN:[['Library','local_library','/dashboard/library'],['Reports','description','/dashboard/reports']],HOSTEL_WARDEN:[['Hostel','hotel','/dashboard/hostel']],TRANSPORT_MANAGER:[['Transport','directions_bus','/dashboard/transport']],PLACEMENT_OFFICER:[['Placement','work','/dashboard/placement']],HR:[['Staff','badge','/dashboard/staff'],['Reports','description','/dashboard/reports']]}; return (map[this.role||'']||[['Profile','person','/dashboard/profile'],['Notifications','notifications','/dashboard/notifications']]).map((x:any)=>({label:x[0],icon:x[1],route:x[2]})); }
  get activities(): any[] { const d=this.dashboardData; return [{title:'Dashboard synchronized',text:`${this.stats.length} key metrics are available for ${this.role||'user'}.`,route:'/dashboard'},{title:'Attendance status',text:`Today: ${d.todayAttendance ?? d.studentAttendance ?? d.todayAttendanceByFaculty ?? 0}.`,route:this.role===Role.STUDENT?'/dashboard/attendance/student':'/dashboard/attendance/report'},{title:'Examination workspace',text:`${d.totalExams ?? d.myExams ?? d.studentExams ?? 0} exams available.`,route:'/dashboard/examination'},{title:'Secure session active',text:'Authenticated ERP session is active.',route:'/dashboard/profile'}]; }
  openOperationalOverview(){ this.navigate(this.role===Role.STUDENT?'/dashboard/subject':'/dashboard'); }
  navigate(route:string){ this.router.navigateByUrl(route); }

  ngOnInit(): void {
    this.role = this.authService.getRole();
    this.loadDashboard();
  }

  private loadDashboard(): void {
    this.dashboardService.getDashboard().subscribe({
      next: response => {
        this.dashboardData = response.data || {};
        this.stats = this.buildStats(this.dashboardData);
      },

      error: error => {
        console.error('Dashboard load failed.', error);
        this.stats = [];
      }
    });
  }

  private buildStats(data: any): DashboardStat[] {

    switch (this.role) {

      case Role.SUPER_ADMIN:
      case Role.ADMIN:
        return [
          this.createStat(
            'Students',
            data.totalStudents,
            'school'
          ),

          this.createStat(
            'Faculty',
            data.totalFaculties,
            'groups'
          ),

          this.createStat(
            'Departments',
            data.totalDepartments,
            'account_balance'
          ),

          this.createStat(
            'Courses',
            data.totalCourses,
            'menu_book'
          ),

          this.createStat(
            'Admissions',
            data.totalAdmissions,
            'assignment'
          ),

          this.createStat(
            'Attendance Today',
            data.todayAttendance,
            'event_available'
          ),

          this.createStat(
            'Exams',
            data.totalExams,
            'quiz'
          ),

          this.createStat(
            'Results',
            data.totalResults,
            'grading'
          )
        ];

      case Role.FACULTY:
        return [
          this.createStat(
            'My Subjects',
            data.mySubjects,
            'menu_book'
          ),

          this.createStat(
            'My Students',
            data.myStudents,
            'groups'
          ),

          this.createStat(
            "Today's Attendance",
            data.todayAttendanceByFaculty,
            'event_available'
          ),

          this.createStat(
            'My Exams',
            data.myExams,
            'quiz'
          ),

          this.createStat(
            'My Results',
            data.myResults,
            'grading'
          )
        ];

      case Role.STUDENT:
        return [
          this.createStat(
            'Total Fee',
            data.studentTotalFee,
            'payments'
          ),

          this.createStat(
            'Paid Fee',
            data.studentPaidFee,
            'check_circle'
          ),

          this.createStat(
            'Pending Fee',
            data.studentPendingFee,
            'pending'
          ),

          this.createStat(
            'Attendance',
            data.studentAttendance,
            'event_available'
          ),

          this.createStat(
            'Results',
            data.studentResults,
            'grading'
          ),

          this.createStat(
            'Exams',
            data.studentExams,
            'quiz'
          )
        ];

      case Role.ACCOUNTANT:
        return [this.createStat('Today Collection', data.todayCollection, 'payments'),this.createStat('Monthly Collection', data.monthlyCollection, 'calendar_month'),this.createStat('Pending Due', data.pendingDue, 'pending'),this.createStat('Refunds', data.totalRefunds, 'currency_exchange')];
      case Role.EXAM_CONTROLLER:
      case Role.HOD:
      case Role.REGISTRAR:
      case Role.DEAN:
      case Role.PRINCIPAL:
      case Role.VC:
      case Role.ADMISSION_OFFICER:
      case Role.LIBRARIAN:
      case Role.HOSTEL_WARDEN:
      case Role.TRANSPORT_MANAGER:
      case Role.PLACEMENT_OFFICER:
      case Role.HR:
      case Role.STAFF:
      case Role.PARENT:
      case Role.ALUMNI:
        return [this.createStat('Students', data.totalStudents, 'school'),this.createStat('Faculty', data.totalFaculties, 'groups'),this.createStat('Courses', data.totalCourses, 'menu_book'),this.createStat('Exams', data.totalExams, 'quiz'),this.createStat('Results', data.totalResults, 'grading')];
      default:
        return [];
    }
  }

  private createStat(
    title: string,
    value: number | null | undefined,
    icon: string
  ): DashboardStat {

    return {
      title,
      value: value ?? 0,
      icon
    };
  }
}