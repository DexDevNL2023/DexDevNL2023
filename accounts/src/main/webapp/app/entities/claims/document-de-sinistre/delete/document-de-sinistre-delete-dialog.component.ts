import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IDocumentDeSinistre } from '../document-de-sinistre.model';
import { DocumentDeSinistreService } from '../service/document-de-sinistre.service';

@Component({
  standalone: true,
  templateUrl: './document-de-sinistre-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class DocumentDeSinistreDeleteDialogComponent {
  documentDeSinistre?: IDocumentDeSinistre;

  protected documentDeSinistreService = inject(DocumentDeSinistreService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.documentDeSinistreService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
