import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NotificationSearch } from '../models/notification-search.model';
import { NOTIFICATION_STATUSES } from '../shared/notification.constants';

@Component({
  selector: 'app-notification-search-filter', standalone: true,
  imports: [CommonModule, FormsModule], templateUrl: './notification-search-filter.component.html', styleUrl: './notification-search-filter.component.scss'
})
export class NotificationSearchFilterComponent {
  @Output() search = new EventEmitter<NotificationSearch>();
  @Output() reset = new EventEmitter<void>();
  keyword = ''; code = ''; status = '';
  readonly statuses = NOTIFICATION_STATUSES;
  submit(): void { this.search.emit({ keyword: this.keyword.trim() || undefined, code: this.code.trim() || undefined, status: this.status || undefined }); }
  clear(): void { this.keyword=''; this.code=''; this.status=''; this.reset.emit(); }
}
