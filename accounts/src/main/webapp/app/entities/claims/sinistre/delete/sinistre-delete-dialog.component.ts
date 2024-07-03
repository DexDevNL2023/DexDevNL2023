import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ISinistre } from '../sinistre.model';
import { SinistreService } from '../service/sinistre.service';

@Component({
  standalone: true,
  templateUrl: './sinistre-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class SinistreDeleteDialogComponent {
  sinistre?: ISinistre;

  protected sinistreService = inject(SinistreService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.sinistreService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
