import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { SinistreDetailComponent } from './sinistre-detail.component';

describe('Sinistre Management Detail Component', () => {
  let comp: SinistreDetailComponent;
  let fixture: ComponentFixture<SinistreDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SinistreDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: SinistreDetailComponent,
              resolve: { sinistre: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(SinistreDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SinistreDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load sinistre on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', SinistreDetailComponent);

      // THEN
      expect(instance.sinistre()).toEqual(expect.objectContaining({ id: 123 }));
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
