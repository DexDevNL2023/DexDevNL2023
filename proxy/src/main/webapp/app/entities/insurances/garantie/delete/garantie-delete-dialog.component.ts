import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IGarantie } from '../garantie.model';
import { GarantieService } from '../service/garantie.service';

@Component({
  standalone: true,
  templateUrl: './garantie-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class GarantieDeleteDialogComponent {
  garantie?: IGarantie;

  protected garantieService = inject(GarantieService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.garantieService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
