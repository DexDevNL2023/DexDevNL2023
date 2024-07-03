import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IMedicalDocument } from '../medical-document.model';
import { MedicalDocumentService } from '../service/medical-document.service';

@Component({
  standalone: true,
  templateUrl: './medical-document-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class MedicalDocumentDeleteDialogComponent {
  medicalDocument?: IMedicalDocument;

  protected medicalDocumentService = inject(MedicalDocumentService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.medicalDocumentService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
