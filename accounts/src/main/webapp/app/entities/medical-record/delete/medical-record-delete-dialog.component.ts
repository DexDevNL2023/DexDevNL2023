import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IMedicalRecord } from '../medical-record.model';
import { MedicalRecordService } from '../service/medical-record.service';

@Component({
  standalone: true,
  templateUrl: './medical-record-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class MedicalRecordDeleteDialogComponent {
  medicalRecord?: IMedicalRecord;

  protected medicalRecordService = inject(MedicalRecordService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.medicalRecordService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
