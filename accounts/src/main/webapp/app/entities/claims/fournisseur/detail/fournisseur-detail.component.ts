import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IFournisseur } from '../fournisseur.model';

@Component({
  standalone: true,
  selector: 'jhi-fournisseur-detail',
  templateUrl: './fournisseur-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class FournisseurDetailComponent {
  fournisseur = input<IFournisseur | null>(null);

  previousState(): void {
    window.history.back();
  }
}
