import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ISinistre } from 'app/entities/claims/sinistre/sinistre.model';
import { SinistreService } from 'app/entities/claims/sinistre/service/sinistre.service';
import { IDocumentDeSinistre } from '../document-de-sinistre.model';
import { DocumentDeSinistreService } from '../service/document-de-sinistre.service';
import { DocumentDeSinistreFormService, DocumentDeSinistreFormGroup } from './document-de-sinistre-form.service';

@Component({
  standalone: true,
  selector: 'jhi-document-de-sinistre-update',
  templateUrl: './document-de-sinistre-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class DocumentDeSinistreUpdateComponent implements OnInit {
  isSaving = false;
  documentDeSinistre: IDocumentDeSinistre | null = null;

  sinistresSharedCollection: ISinistre[] = [];

  protected documentDeSinistreService = inject(DocumentDeSinistreService);
  protected documentDeSinistreFormService = inject(DocumentDeSinistreFormService);
  protected sinistreService = inject(SinistreService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: DocumentDeSinistreFormGroup = this.documentDeSinistreFormService.createDocumentDeSinistreFormGroup();

  compareSinistre = (o1: ISinistre | null, o2: ISinistre | null): boolean => this.sinistreService.compareSinistre(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ documentDeSinistre }) => {
      this.documentDeSinistre = documentDeSinistre;
      if (documentDeSinistre) {
        this.updateForm(documentDeSinistre);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const documentDeSinistre = this.documentDeSinistreFormService.getDocumentDeSinistre(this.editForm);
    if (documentDeSinistre.id !== null) {
      this.subscribeToSaveResponse(this.documentDeSinistreService.update(documentDeSinistre));
    } else {
      this.subscribeToSaveResponse(this.documentDeSinistreService.create(documentDeSinistre));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDocumentDeSinistre>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(documentDeSinistre: IDocumentDeSinistre): void {
    this.documentDeSinistre = documentDeSinistre;
    this.documentDeSinistreFormService.resetForm(this.editForm, documentDeSinistre);

    this.sinistresSharedCollection = this.sinistreService.addSinistreToCollectionIfMissing<ISinistre>(
      this.sinistresSharedCollection,
      documentDeSinistre.sinistre,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.sinistreService
      .query()
      .pipe(map((res: HttpResponse<ISinistre[]>) => res.body ?? []))
      .pipe(
        map((sinistres: ISinistre[]) =>
          this.sinistreService.addSinistreToCollectionIfMissing<ISinistre>(sinistres, this.documentDeSinistre?.sinistre),
        ),
      )
      .subscribe((sinistres: ISinistre[]) => (this.sinistresSharedCollection = sinistres));
  }
}
