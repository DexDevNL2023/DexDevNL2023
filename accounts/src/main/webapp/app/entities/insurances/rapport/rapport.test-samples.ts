import dayjs from 'dayjs/esm';

import { IRapport, NewRapport } from './rapport.model';

export const sampleWithRequiredData: IRapport = {
  id: 1183,
  titre: 'xylophone that',
  type: 'PAIEMENT',
  dateGeneration: dayjs('2024-07-03T05:22'),
  url: 'https://elated-customer.org',
};

export const sampleWithPartialData: IRapport = {
  id: 19865,
  titre: 'atop duh',
  type: 'PAIEMENT',
  dateGeneration: dayjs('2024-07-02T14:05'),
  url: 'https://some-cod.com/',
};

export const sampleWithFullData: IRapport = {
  id: 31565,
  titre: 'clumsy zowie',
  description: 'er spearhead excluding',
  type: 'PERFORMANCE',
  dateGeneration: dayjs('2024-07-03T02:33'),
  url: 'https://imaginative-rheumatism.name/',
};

export const sampleWithNewData: NewRapport = {
  titre: 'owlishly heartwood',
  type: 'PAIEMENT',
  dateGeneration: dayjs('2024-07-02T07:54'),
  url: 'https://natural-skullduggery.com/',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
