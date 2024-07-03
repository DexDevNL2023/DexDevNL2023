import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IReclamation } from '../reclamation.model';

@Component({
  standalone: true,
  selector: 'jhi-reclamation-detail',
  templateUrl: './reclamation-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class ReclamationDetailComponent {
  reclamation = input<IReclamation | null>(null);

  previousState(): void {
    window.history.back();
  }
}
