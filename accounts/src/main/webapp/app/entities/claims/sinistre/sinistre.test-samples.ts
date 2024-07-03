import dayjs from 'dayjs/esm';

import { ISinistre, NewSinistre } from './sinistre.model';

export const sampleWithRequiredData: ISinistre = {
  id: 8086,
  numeroSinistre: 'sheet wonderfully',
  dateDeclaration: dayjs('2024-07-02T11:34'),
  status: 'APPROUVE',
  raison: 'table shred',
  montantReclame: 14114.24,
  souscriptionId: 22402,
};

export const sampleWithPartialData: ISinistre = {
  id: 12554,
  numeroSinistre: 'defensive across boo',
  dateDeclaration: dayjs('2024-07-02T13:48'),
  status: 'ANNULE',
  raison: 'with',
  montantReclame: 26660.96,
  montantApprouve: 21768.45,
  souscriptionId: 29457,
};

export const sampleWithFullData: ISinistre = {
  id: 6784,
  numeroSinistre: 'but compassionate',
  dateDeclaration: dayjs('2024-07-02T11:23'),
  dateTraitement: dayjs('2024-07-03T02:00'),
  status: 'EN_ATTENTE',
  raison: 'minus',
  montantReclame: 28345.61,
  montantApprouve: 30210.89,
  souscriptionId: 5384,
};

export const sampleWithNewData: NewSinistre = {
  numeroSinistre: 'powerfully via',
  dateDeclaration: dayjs('2024-07-02T10:29'),
  status: 'ANNULE',
  raison: 'yahoo sweet',
  montantReclame: 1573.37,
  souscriptionId: 4875,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
