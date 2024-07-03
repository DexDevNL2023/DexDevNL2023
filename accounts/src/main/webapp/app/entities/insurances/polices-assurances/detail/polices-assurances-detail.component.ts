import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IPolicesAssurances } from '../polices-assurances.model';

@Component({
  standalone: true,
  selector: 'jhi-polices-assurances-detail',
  templateUrl: './polices-assurances-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class PolicesAssurancesDetailComponent {
  policesAssurances = input<IPolicesAssurances | null>(null);

  previousState(): void {
    window.history.back();
  }
}
