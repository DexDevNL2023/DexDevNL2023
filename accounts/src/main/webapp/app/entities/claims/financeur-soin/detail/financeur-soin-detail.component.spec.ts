import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { FinanceurSoinDetailComponent } from './financeur-soin-detail.component';

describe('FinanceurSoin Management Detail Component', () => {
  let comp: FinanceurSoinDetailComponent;
  let fixture: ComponentFixture<FinanceurSoinDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FinanceurSoinDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: FinanceurSoinDetailComponent,
              resolve: { financeurSoin: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(FinanceurSoinDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FinanceurSoinDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load financeurSoin on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', FinanceurSoinDetailComponent);

      // THEN
      expect(instance.financeurSoin()).toEqual(expect.objectContaining({ id: 123 }));
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
