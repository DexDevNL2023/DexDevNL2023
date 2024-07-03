import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { PrestationSoinDetailComponent } from './prestation-soin-detail.component';

describe('PrestationSoin Management Detail Component', () => {
  let comp: PrestationSoinDetailComponent;
  let fixture: ComponentFixture<PrestationSoinDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PrestationSoinDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: PrestationSoinDetailComponent,
              resolve: { prestationSoin: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(PrestationSoinDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PrestationSoinDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load prestationSoin on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', PrestationSoinDetailComponent);

      // THEN
      expect(instance.prestationSoin()).toEqual(expect.objectContaining({ id: 123 }));
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
