import dayjs from 'dayjs/esm';

import { IGarantie, NewGarantie } from './garantie.model';

export const sampleWithRequiredData: IGarantie = {
  id: 22164,
  description: 'while miserably within',
  dateDebut: dayjs('2024-07-03T05:15'),
  dateFin: dayjs('2024-07-02T15:47'),
  status: 'EXPIREE',
};

export const sampleWithPartialData: IGarantie = {
  id: 15338,
  description: 'netball yuck',
  dateDebut: dayjs('2024-07-02T20:02'),
  dateFin: dayjs('2024-07-03T01:36'),
  status: 'EXPIREE',
};

export const sampleWithFullData: IGarantie = {
  id: 16954,
  description: 'not although unfolded',
  dateDebut: dayjs('2024-07-03T03:59'),
  dateFin: dayjs('2024-07-02T11:05'),
  status: 'EXPIREE',
};

export const sampleWithNewData: NewGarantie = {
  description: 'milepost',
  dateDebut: dayjs('2024-07-02T11:41'),
  dateFin: dayjs('2024-07-02T16:16'),
  status: 'ACTIVE',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
