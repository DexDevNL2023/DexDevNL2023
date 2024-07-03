import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IFournisseur } from '../fournisseur.model';
import { FournisseurService } from '../service/fournisseur.service';

@Component({
  standalone: true,
  templateUrl: './fournisseur-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class FournisseurDeleteDialogComponent {
  fournisseur?: IFournisseur;

  protected fournisseurService = inject(FournisseurService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.fournisseurService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
