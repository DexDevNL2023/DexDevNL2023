import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { AssuranceDetailComponent } from './assurance-detail.component';

describe('Assurance Management Detail Component', () => {
  let comp: AssuranceDetailComponent;
  let fixture: ComponentFixture<AssuranceDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AssuranceDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: AssuranceDetailComponent,
              resolve: { assurance: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(AssuranceDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AssuranceDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load assurance on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', AssuranceDetailComponent);

      // THEN
      expect(instance.assurance()).toEqual(expect.objectContaining({ id: 123 }));
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
