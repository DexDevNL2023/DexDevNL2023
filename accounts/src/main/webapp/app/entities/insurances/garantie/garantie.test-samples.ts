import dayjs from 'dayjs/esm';

import { IGarantie, NewGarantie } from './garantie.model';

export const sampleWithRequiredData: IGarantie = {
  id: 22164,
  description: 'while miserably within',
  dateDebut: dayjs('2024-07-03T05:13'),
  dateFin: dayjs('2024-07-02T15:45'),
  status: 'EXPIREE',
};

export const sampleWithPartialData: IGarantie = {
  id: 15338,
  description: 'netball yuck',
  dateDebut: dayjs('2024-07-02T20:00'),
  dateFin: dayjs('2024-07-03T01:34'),
  status: 'EXPIREE',
};

export const sampleWithFullData: IGarantie = {
  id: 16954,
  description: 'not although unfolded',
  dateDebut: dayjs('2024-07-03T03:57'),
  dateFin: dayjs('2024-07-02T11:03'),
  status: 'EXPIREE',
};

export const sampleWithNewData: NewGarantie = {
  description: 'milepost',
  dateDebut: dayjs('2024-07-02T11:39'),
  dateFin: dayjs('2024-07-02T16:14'),
  status: 'ACTIVE',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
