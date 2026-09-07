import {
  Component,
  OnInit,
  output
} from '@angular/core';

import {
  FormBuilder,
  ReactiveFormsModule,
  FormGroup
} from '@angular/forms';

import { MATERIAL_MODULES } from '../../../../shared/material/material';

import { StudentSearchRequest } from '../../models/student-search-request.model';

import { Gender } from '../../../../shared/enums/gender.enum';

import { RecordStatus } from '../../../../shared/enums/record-status.enum';


@Component({
  selector: 'app-student-search-filter',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    ...MATERIAL_MODULES
  ],
  templateUrl: './student-search-filter.component.html',
  styleUrl: './student-search-filter.component.scss'
})
export class StudentSearchFilterComponent implements OnInit {

  // Search event
  readonly search = output<StudentSearchRequest>();

  // Reset event
  readonly reset = output<void>();


  searchForm!: FormGroup;


  constructor(
    private fb: FormBuilder
  ) {}


  ngOnInit(): void {

    this.searchForm = this.fb.group({

      firstName: [''],

      lastName: [''],

      rollNumber: [''],

      gender: [null as Gender | null],

      status: [null as RecordStatus | null],

      district: [''],

      pincode: ['']

    });

  }


  submitSearch(): void {

    const value = this.searchForm.value;

    this.search.emit({

      firstName: value.firstName || undefined,

      lastName: value.lastName || undefined,

      rollNumber: value.rollNumber || undefined,

      gender: value.gender || undefined,

      status: value.status || undefined,

      district: value.district || undefined,

      pincode: value.pincode || undefined

    });

  }


  clear(): void {

    this.searchForm.reset();

    this.reset.emit();

  }

}