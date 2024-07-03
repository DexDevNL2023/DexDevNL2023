import dayjs from 'dayjs/esm';

import { IReclamation, NewReclamation } from './reclamation.model';

export const sampleWithRequiredData: IReclamation = {
  id: 14301,
  numeroReclamation: 'towards for batter',
  dateReclamation: dayjs('2024-07-02T09:46'),
  description: 'eek where',
  status: 'EN_COURS',
};

export const sampleWithPartialData: IReclamation = {
  id: 5587,
  numeroReclamation: 'generously well-made',
  dateReclamation: dayjs('2024-07-02T08:26'),
  description: 'terrarium banter',
  status: 'EN_COURS',
};

export const sampleWithFullData: IReclamation = {
  id: 26620,
  numeroReclamation: 'fortress doctor',
  dateReclamation: dayjs('2024-07-03T04:58'),
  description: 'urgently almost',
  status: 'EN_COURS',
};

export const sampleWithNewData: NewReclamation = {
  numeroReclamation: 'usually detain outside',
  dateReclamation: dayjs('2024-07-03T06:56'),
  description: 'lobotomise cleverly',
  status: 'EN_COURS',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
