import dayjs from 'dayjs/esm';

import { INotification, NewNotification } from './notification.model';

export const sampleWithRequiredData: INotification = {
  id: 29555,
  message: 'readjust',
  dateEnvoi: dayjs('2024-07-02T13:29'),
  type: 'PAIEMENT',
};

export const sampleWithPartialData: INotification = {
  id: 9225,
  message: 'however so riffle',
  dateEnvoi: dayjs('2024-07-03T01:13'),
  type: 'PAIEMENT',
};

export const sampleWithFullData: INotification = {
  id: 3257,
  message: 'save grizzled diverge',
  dateEnvoi: dayjs('2024-07-02T08:36'),
  type: 'RENOUVELLEMENT',
};

export const sampleWithNewData: NewNotification = {
  message: 'sizzling',
  dateEnvoi: dayjs('2024-07-02T22:17'),
  type: 'RENOUVELLEMENT',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
