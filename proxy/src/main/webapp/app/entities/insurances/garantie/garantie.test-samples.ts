import dayjs from 'dayjs/esm';

import { IGarantie, NewGarantie } from './garantie.model';

export const sampleWithRequiredData: IGarantie = {
  id: 23884,
  description: 'but with furthermore',
  dateDebut: dayjs('2024-07-03T02:24'),
  dateFin: dayjs('2024-07-03T08:34'),
  status: 'SUSPENDUE',
};

export const sampleWithPartialData: IGarantie = {
  id: 26837,
  description: 'curriculum qua',
  dateDebut: dayjs('2024-07-02T09:39'),
  dateFin: dayjs('2024-07-02T13:45'),
  status: 'ACTIVE',
};

export const sampleWithFullData: IGarantie = {
  id: 30714,
  description: 'paltry',
  dateDebut: dayjs('2024-07-02T11:47'),
  dateFin: dayjs('2024-07-02T21:38'),
  status: 'SUSPENDUE',
};

export const sampleWithNewData: NewGarantie = {
  description: 'tool traumatise',
  dateDebut: dayjs('2024-07-02T11:12'),
  dateFin: dayjs('2024-07-02T19:12'),
  status: 'EXPIREE',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
