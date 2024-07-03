import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IMedicalRecord } from '../medical-record.model';

@Component({
  standalone: true,
  selector: 'jhi-medical-record-detail',
  templateUrl: './medical-record-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class MedicalRecordDetailComponent {
  medicalRecord = input<IMedicalRecord | null>(null);

  previousState(): void {
    window.history.back();
  }
}
