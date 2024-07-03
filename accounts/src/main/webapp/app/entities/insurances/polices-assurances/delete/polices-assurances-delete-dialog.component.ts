import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IPolicesAssurances } from '../polices-assurances.model';
import { PolicesAssurancesService } from '../service/polices-assurances.service';

@Component({
  standalone: true,
  templateUrl: './polices-assurances-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class PolicesAssurancesDeleteDialogComponent {
  policesAssurances?: IPolicesAssurances;

  protected policesAssurancesService = inject(PolicesAssurancesService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.policesAssurancesService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
