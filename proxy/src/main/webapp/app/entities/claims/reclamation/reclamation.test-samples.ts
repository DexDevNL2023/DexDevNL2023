import dayjs from 'dayjs/esm';

import { IReclamation, NewReclamation } from './reclamation.model';

export const sampleWithRequiredData: IReclamation = {
  id: 32433,
  numeroReclamation: 'charming decimal segment',
  dateReclamation: dayjs('2024-07-02T09:04'),
  description: 'wrathful certainly amongst',
  status: 'REJETEE',
};

export const sampleWithPartialData: IReclamation = {
  id: 9530,
  numeroReclamation: 'lovingly knavishly although',
  dateReclamation: dayjs('2024-07-02T12:42'),
  description: 'wreak where styling',
  status: 'RESOLUE',
};

export const sampleWithFullData: IReclamation = {
  id: 13277,
  numeroReclamation: 'rumor dethrone',
  dateReclamation: dayjs('2024-07-02T20:58'),
  description: 'stake',
  status: 'REJETEE',
};

export const sampleWithNewData: NewReclamation = {
  numeroReclamation: 'how',
  dateReclamation: dayjs('2024-07-02T19:01'),
  description: 'bogus enrol pfft',
  status: 'REJETEE',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
