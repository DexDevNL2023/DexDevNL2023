import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { PolicesAssurancesDetailComponent } from './polices-assurances-detail.component';

describe('PolicesAssurances Management Detail Component', () => {
  let comp: PolicesAssurancesDetailComponent;
  let fixture: ComponentFixture<PolicesAssurancesDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PolicesAssurancesDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: PolicesAssurancesDetailComponent,
              resolve: { policesAssurances: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(PolicesAssurancesDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PolicesAssurancesDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load policesAssurances on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', PolicesAssurancesDetailComponent);

      // THEN
      expect(instance.policesAssurances()).toEqual(expect.objectContaining({ id: 123 }));
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
