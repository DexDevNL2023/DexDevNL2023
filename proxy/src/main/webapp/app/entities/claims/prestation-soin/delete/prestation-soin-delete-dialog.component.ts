import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IPrestationSoin } from '../prestation-soin.model';
import { PrestationSoinService } from '../service/prestation-soin.service';

@Component({
  standalone: true,
  templateUrl: './prestation-soin-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class PrestationSoinDeleteDialogComponent {
  prestationSoin?: IPrestationSoin;

  protected prestationSoinService = inject(PrestationSoinService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.prestationSoinService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
