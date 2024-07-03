import dayjs from 'dayjs/esm';

import { IRapport, NewRapport } from './rapport.model';

export const sampleWithRequiredData: IRapport = {
  id: 14457,
  titre: 'yesterday until',
  type: 'PAIEMENT',
  dateGeneration: dayjs('2024-07-03T05:52'),
  url: 'https://instructive-mover.name/',
};

export const sampleWithPartialData: IRapport = {
  id: 17533,
  titre: 'beyond aboard although',
  description: 'supplier round',
  type: 'PAIEMENT',
  dateGeneration: dayjs('2024-07-02T17:13'),
  url: 'https://acceptable-segment.biz',
};

export const sampleWithFullData: IRapport = {
  id: 29377,
  titre: 'unfolded even silent',
  description: 'always slim pfft',
  type: 'PAIEMENT',
  dateGeneration: dayjs('2024-07-03T04:28'),
  url: 'https://crazy-teammate.org',
};

export const sampleWithNewData: NewRapport = {
  titre: 'combat heavenly whenever',
  type: 'SINISTRE',
  dateGeneration: dayjs('2024-07-03T05:16'),
  url: 'https://weary-retouching.info',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
