import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IDocumentDeSinistre } from '../document-de-sinistre.model';

@Component({
  standalone: true,
  selector: 'jhi-document-de-sinistre-detail',
  templateUrl: './document-de-sinistre-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class DocumentDeSinistreDetailComponent {
  documentDeSinistre = input<IDocumentDeSinistre | null>(null);

  previousState(): void {
    window.history.back();
  }
}
