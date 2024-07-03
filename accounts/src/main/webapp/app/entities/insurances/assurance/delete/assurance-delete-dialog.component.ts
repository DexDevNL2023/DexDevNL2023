import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IAssurance } from '../assurance.model';
import { AssuranceService } from '../service/assurance.service';

@Component({
  standalone: true,
  templateUrl: './assurance-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class AssuranceDeleteDialogComponent {
  assurance?: IAssurance;

  protected assuranceService = inject(AssuranceService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.assuranceService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
