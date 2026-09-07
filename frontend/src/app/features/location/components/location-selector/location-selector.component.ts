import { Component, Input, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormGroup, ReactiveFormsModule } from '@angular/forms';

import { MATERIAL_MODULES } from '../../../../shared/material/material';

import { CountryService } from '../../services/country.service';
import { StateService } from '../../services/state.service';
import { DistrictService } from '../../services/district.service';
import { TehsilService } from '../../services/tehsil.service';
import { VillageService } from '../../services/village.service';

@Component({
  selector: 'app-location-selector',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    ...MATERIAL_MODULES
  ],
  templateUrl: './location-selector.component.html',
  styleUrl: './location-selector.component.scss'
})
export class LocationSelectorComponent implements OnInit {

  Number = Number;

  @Input({ required: true })
  form!: FormGroup;

  private readonly countryService = inject(CountryService);
  private readonly stateService = inject(StateService);
  private readonly districtService = inject(DistrictService);
  private readonly tehsilService = inject(TehsilService);
  private readonly villageService = inject(VillageService);

  countries: any[] = [];
  states: any[] = [];
  districts: any[] = [];
  tehsils: any[] = [];
  villages: any[] = [];

  ngOnInit(): void {
    this.loadCountries();
    setTimeout(() => {
      this.loadEditLocation();
    }, 500);
  }

  loadCountries(): void {

    this.countryService.getAllCountries()
      .subscribe({

        next: (response) => {

          this.countries =
            response.data.content ??
            response.data;

          this.loadEditLocation();

        }

      });

  }


  loadEditLocation(): void {

    const countryId = Number(this.form.get('countryId')?.value);
    const stateId = Number(this.form.get('stateId')?.value);
    const districtId = Number(this.form.get('districtId')?.value);
    const tehsilId = Number(this.form.get('tehsilId')?.value);
    const villageId = Number(this.form.get('villageId')?.value);


    if (!countryId) {
      return;
    }


    this.stateService.getStatesByCountry(countryId)
      .subscribe({

        next: response => {

          this.states =
            response.data.content ?? response.data;


          this.form.patchValue({
            countryId
          });


          if (!stateId) {
            return;
          }


          this.form.patchValue({
            stateId
          });


          this.districtService.getDistrictsByState(stateId)
            .subscribe({

              next: response => {

                this.districts =
                  response.data.content ?? response.data;


                this.form.patchValue({
                  districtId
                });


                this.tehsilService.getTehsilsByDistrict(districtId)
                  .subscribe({

                    next: response => {

                      this.tehsils =
                        response.data.content ?? response.data;


                      this.form.patchValue({
                        tehsilId
                      });


                      this.villageService.getVillagesByTehsil(tehsilId)
                        .subscribe({

                          next: response => {

                            this.villages =
                              response.data.content ?? response.data;


                            setTimeout(() => {

                              this.form.patchValue({

                                villageId

                              });


                              const village =
                                this.villages.find(
                                  v => Number(v.id) === villageId
                                );


                              if (village) {

                                this.form.patchValue({

                                  pincode: village.pincode

                                });

                              }

                            }, 100);


                          }

                        });


                    }

                  });


              }

            });


        }

      });

  }


  onCountryChange(): void {

    const countryId =
      this.form.get('countryId')?.value;


    this.states = [];
    this.districts = [];
    this.tehsils = [];
    this.villages = [];


    this.form.patchValue({

      stateId: null,
      districtId: null,
      tehsilId: null,
      villageId: null,
      pincode: ''

    });


    if (countryId) {

      this.stateService.getStatesByCountry(countryId)
        .subscribe({

          next: (response) => {

            this.states =
              response.data.content ??
              response.data;

          }

        });

    }

  }


  onStateChange(): void {

    const stateId =
      this.form.get('stateId')?.value;


    this.districts = [];
    this.tehsils = [];
    this.villages = [];


    this.form.patchValue({

      districtId: null,
      tehsilId: null,
      villageId: null,
      pincode: ''

    });


    if (stateId) {

      this.districtService.getDistrictsByState(stateId)
        .subscribe({

          next: (response) => {

            this.districts =
              response.data.content ??
              response.data;

          }

        });

    }

  }


  onDistrictChange(): void {

    const districtId =
      this.form.get('districtId')?.value;


    this.tehsils = [];
    this.villages = [];


    this.form.patchValue({

      tehsilId: null,
      villageId: null,
      pincode: ''

    });


    if (districtId) {

      this.tehsilService.getTehsilsByDistrict(districtId)
        .subscribe({

          next: (response) => {

            this.tehsils =
              response.data.content ??
              response.data;

          }

        });

    }

  }


  onTehsilChange(): void {

    const tehsilId =
      this.form.get('tehsilId')?.value;


    this.villages = [];


    this.form.patchValue({

      villageId: null,
      pincode: ''

    });


    if (tehsilId) {

      this.villageService.getVillagesByTehsil(tehsilId)
        .subscribe({

          next: (response) => {

            this.villages =
              response.data.content ??
              response.data;

          }

        });

    }

  }


  onVillageChange(): void {

    const villageId =
      Number(this.form.get('villageId')?.value);


    const village =
      this.villages.find(
        v => Number(v.id) === villageId
      );


    if (village) {

      this.form.patchValue({

        pincode: village.pincode

      });

    }

  }

}