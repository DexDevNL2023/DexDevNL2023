import dayjs from 'dayjs/esm';

import { IPolicesAssurances, NewPolicesAssurances } from './polices-assurances.model';

export const sampleWithRequiredData: IPolicesAssurances = {
  id: 11099,
  numeroPolice: 'stained cruelly diver',
  dateEmission: dayjs('2024-07-03T06:15'),
  dateExpiration: dayjs('2024-07-02T11:35'),
  montantAssure: 1694.2,
  plafondAssure: 2738.1,
};

export const sampleWithPartialData: IPolicesAssurances = {
  id: 6047,
  numeroPolice: 'while bah',
  dateEmission: dayjs('2024-07-02T23:48'),
  dateExpiration: dayjs('2024-07-03T04:25'),
  montantAssure: 26030.73,
  plafondAssure: 32310.93,
};

export const sampleWithFullData: IPolicesAssurances = {
  id: 25054,
  numeroPolice: 'eventually about speak',
  dateEmission: dayjs('2024-07-03T01:56'),
  dateExpiration: dayjs('2024-07-02T23:41'),
  description: 'sweaty',
  montantAssure: 11066.85,
  plafondAssure: 19813.9,
};

export const sampleWithNewData: NewPolicesAssurances = {
  numeroPolice: 'um grown',
  dateEmission: dayjs('2024-07-02T12:50'),
  dateExpiration: dayjs('2024-07-02T11:55'),
  montantAssure: 23934.8,
  plafondAssure: 20031.85,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
