import dayjs from 'dayjs/esm';

import { IMedicalRecord, NewMedicalRecord } from './medical-record.model';

export const sampleWithRequiredData: IMedicalRecord = {
  id: 4886,
  numMedicalRecord: 'consequently',
  details: 'considering',
  dateUpdated: dayjs('2024-07-02T11:20'),
};

export const sampleWithPartialData: IMedicalRecord = {
  id: 25833,
  numMedicalRecord: 'bonk ha quintuple',
  details: 'instead',
  dateUpdated: dayjs('2024-07-02T17:40'),
};

export const sampleWithFullData: IMedicalRecord = {
  id: 7190,
  numMedicalRecord: 'even',
  details: 'aha across up',
  dateUpdated: dayjs('2024-07-02T16:03'),
};

export const sampleWithNewData: NewMedicalRecord = {
  numMedicalRecord: 'truthful monitor',
  details: 'yuck arrival daily',
  dateUpdated: dayjs('2024-07-02T15:42'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
