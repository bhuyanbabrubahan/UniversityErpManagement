import {
  ChangeDetectionStrategy,
  Component,
  Input
} from '@angular/core';

@Component({
  selector: 'app-attendance-summary',
  standalone: true,
  templateUrl: './attendance-summary.component.html',
  styleUrl: './attendance-summary.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class AttendanceSummaryComponent {

  @Input() total = 0;

  @Input() present = 0;

  @Input() absent = 0;

  @Input() leave = 0;

  @Input() halfDay = 0;

}