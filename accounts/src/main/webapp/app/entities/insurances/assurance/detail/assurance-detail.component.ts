import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IAssurance } from '../assurance.model';

@Component({
  standalone: true,
  selector: 'jhi-assurance-detail',
  templateUrl: './assurance-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class AssuranceDetailComponent {
  assurance = input<IAssurance | null>(null);

  previousState(): void {
    window.history.back();
  }
}
