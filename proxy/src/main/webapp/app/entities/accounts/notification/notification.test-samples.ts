import dayjs from 'dayjs/esm';

import { INotification, NewNotification } from './notification.model';

export const sampleWithRequiredData: INotification = {
  id: 5389,
  message: 'thoroughly except',
  dateEnvoi: dayjs('2024-07-02T23:08'),
  type: 'SINISTRE',
};

export const sampleWithPartialData: INotification = {
  id: 32574,
  message: 'about incidentally deeply',
  dateEnvoi: dayjs('2024-07-02T13:57'),
  type: 'RENOUVELLEMENT',
};

export const sampleWithFullData: INotification = {
  id: 6348,
  message: 'wry',
  dateEnvoi: dayjs('2024-07-02T09:18'),
  type: 'RENOUVELLEMENT',
};

export const sampleWithNewData: NewNotification = {
  message: 'wine',
  dateEnvoi: dayjs('2024-07-02T17:29'),
  type: 'SINISTRE',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
