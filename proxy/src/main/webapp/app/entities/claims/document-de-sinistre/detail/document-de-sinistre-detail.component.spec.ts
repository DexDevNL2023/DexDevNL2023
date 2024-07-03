import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { DocumentDeSinistreDetailComponent } from './document-de-sinistre-detail.component';

describe('DocumentDeSinistre Management Detail Component', () => {
  let comp: DocumentDeSinistreDetailComponent;
  let fixture: ComponentFixture<DocumentDeSinistreDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DocumentDeSinistreDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: DocumentDeSinistreDetailComponent,
              resolve: { documentDeSinistre: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(DocumentDeSinistreDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DocumentDeSinistreDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load documentDeSinistre on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', DocumentDeSinistreDetailComponent);

      // THEN
      expect(instance.documentDeSinistre()).toEqual(expect.objectContaining({ id: 123 }));
    });
  });

  describe('PreviousState', () => {
    it('Should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
