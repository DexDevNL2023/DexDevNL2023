import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IPrestationSoin } from '../prestation-soin.model';

@Component({
  standalone: true,
  selector: 'jhi-prestation-soin-detail',
  templateUrl: './prestation-soin-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class PrestationSoinDetailComponent {
  prestationSoin = input<IPrestationSoin | null>(null);

  previousState(): void {
    window.history.back();
  }
}
