import dayjs from 'dayjs/esm';

import { IPolicesAssurances, NewPolicesAssurances } from './polices-assurances.model';

export const sampleWithRequiredData: IPolicesAssurances = {
  id: 29134,
  numeroPolice: 'that',
  dateEmission: dayjs('2024-07-02T19:51'),
  dateExpiration: dayjs('2024-07-03T01:49'),
  montantAssure: 2069.29,
  plafondAssure: 19664.06,
};

export const sampleWithPartialData: IPolicesAssurances = {
  id: 27439,
  numeroPolice: 'claim rite',
  dateEmission: dayjs('2024-07-02T11:22'),
  dateExpiration: dayjs('2024-07-03T05:51'),
  description: 'nutty',
  montantAssure: 6872.02,
  plafondAssure: 13758.46,
};

export const sampleWithFullData: IPolicesAssurances = {
  id: 26960,
  numeroPolice: 'ha reclassify',
  dateEmission: dayjs('2024-07-03T03:14'),
  dateExpiration: dayjs('2024-07-02T10:51'),
  description: 'whistle during',
  montantAssure: 15445.55,
  plafondAssure: 17005.79,
};

export const sampleWithNewData: NewPolicesAssurances = {
  numeroPolice: 'dusk',
  dateEmission: dayjs('2024-07-03T02:23'),
  dateExpiration: dayjs('2024-07-03T08:24'),
  montantAssure: 8325.85,
  plafondAssure: 2215.07,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
