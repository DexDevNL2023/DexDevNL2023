import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { ISinistre } from '../sinistre.model';

@Component({
  standalone: true,
  selector: 'jhi-sinistre-detail',
  templateUrl: './sinistre-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class SinistreDetailComponent {
  sinistre = input<ISinistre | null>(null);

  previousState(): void {
    window.history.back();
  }
}
