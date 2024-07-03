import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IFinanceurSoin } from '../financeur-soin.model';
import { FinanceurSoinService } from '../service/financeur-soin.service';

@Component({
  standalone: true,
  templateUrl: './financeur-soin-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class FinanceurSoinDeleteDialogComponent {
  financeurSoin?: IFinanceurSoin;

  protected financeurSoinService = inject(FinanceurSoinService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.financeurSoinService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
