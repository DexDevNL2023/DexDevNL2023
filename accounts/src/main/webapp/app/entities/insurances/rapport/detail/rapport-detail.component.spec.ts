import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { RapportDetailComponent } from './rapport-detail.component';

describe('Rapport Management Detail Component', () => {
  let comp: RapportDetailComponent;
  let fixture: ComponentFixture<RapportDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RapportDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: RapportDetailComponent,
              resolve: { rapport: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(RapportDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RapportDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load rapport on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', RapportDetailComponent);

      // THEN
      expect(instance.rapport()).toEqual(expect.objectContaining({ id: 123 }));
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
