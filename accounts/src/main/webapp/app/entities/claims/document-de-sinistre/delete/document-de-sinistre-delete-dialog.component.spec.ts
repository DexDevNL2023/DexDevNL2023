jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { provideHttpClient, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { DocumentDeSinistreService } from '../service/document-de-sinistre.service';

import { DocumentDeSinistreDeleteDialogComponent } from './document-de-sinistre-delete-dialog.component';

describe('DocumentDeSinistre Management Delete Component', () => {
  let comp: DocumentDeSinistreDeleteDialogComponent;
  let fixture: ComponentFixture<DocumentDeSinistreDeleteDialogComponent>;
  let service: DocumentDeSinistreService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [DocumentDeSinistreDeleteDialogComponent],
      providers: [provideHttpClient(), NgbActiveModal],
    })
      .overrideTemplate(DocumentDeSinistreDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(DocumentDeSinistreDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(DocumentDeSinistreService);
    mockActiveModal = TestBed.inject(NgbActiveModal);
  });

  describe('confirmDelete', () => {
    it('Should call delete service on confirmDelete', inject(
      [],
      fakeAsync(() => {
        // GIVEN
        jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({ body: {} })));

        // WHEN
        comp.confirmDelete(123);
        tick();

        // THEN
        expect(service.delete).toHaveBeenCalledWith(123);
        expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
      }),
    ));

    it('Should not call delete service on clear', () => {
      // GIVEN
      jest.spyOn(service, 'delete');

      // WHEN
      comp.cancel();

      // THEN
      expect(service.delete).not.toHaveBeenCalled();
      expect(mockActiveModal.close).not.toHaveBeenCalled();
      expect(mockActiveModal.dismiss).toHaveBeenCalled();
    });
  });
});
