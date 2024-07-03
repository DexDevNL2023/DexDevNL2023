import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IFinanceurSoin } from '../financeur-soin.model';

@Component({
  standalone: true,
  selector: 'jhi-financeur-soin-detail',
  templateUrl: './financeur-soin-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class FinanceurSoinDetailComponent {
  financeurSoin = input<IFinanceurSoin | null>(null);

  previousState(): void {
    window.history.back();
  }
}
