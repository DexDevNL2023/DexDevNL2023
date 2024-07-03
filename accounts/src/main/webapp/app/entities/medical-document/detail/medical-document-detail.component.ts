import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IMedicalDocument } from '../medical-document.model';

@Component({
  standalone: true,
  selector: 'jhi-medical-document-detail',
  templateUrl: './medical-document-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class MedicalDocumentDetailComponent {
  medicalDocument = input<IMedicalDocument | null>(null);

  previousState(): void {
    window.history.back();
  }
}
