import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { FournisseurDetailComponent } from './fournisseur-detail.component';

describe('Fournisseur Management Detail Component', () => {
  let comp: FournisseurDetailComponent;
  let fixture: ComponentFixture<FournisseurDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FournisseurDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: FournisseurDetailComponent,
              resolve: { fournisseur: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(FournisseurDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FournisseurDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load fournisseur on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', FournisseurDetailComponent);

      // THEN
      expect(instance.fournisseur()).toEqual(expect.objectContaining({ id: 123 }));
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
