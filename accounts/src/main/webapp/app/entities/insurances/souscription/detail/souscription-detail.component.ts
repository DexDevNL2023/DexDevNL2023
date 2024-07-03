import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { ISouscription } from '../souscription.model';

@Component({
  standalone: true,
  selector: 'jhi-souscription-detail',
  templateUrl: './souscription-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class SouscriptionDetailComponent {
  souscription = input<ISouscription | null>(null);

  previousState(): void {
    window.history.back();
  }
}
