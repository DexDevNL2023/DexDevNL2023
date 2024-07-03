import dayjs from 'dayjs/esm';

import { IMedicalRecord, NewMedicalRecord } from './medical-record.model';

export const sampleWithRequiredData: IMedicalRecord = {
  id: 23790,
  numMedicalRecord: 'meanwhile',
  details: 'consign',
  dateUpdated: dayjs('2024-07-02T15:29'),
};

export const sampleWithPartialData: IMedicalRecord = {
  id: 13793,
  numMedicalRecord: 'before',
  details: 'before aw',
  dateUpdated: dayjs('2024-07-03T04:55'),
};

export const sampleWithFullData: IMedicalRecord = {
  id: 24523,
  numMedicalRecord: 'yuck energetically',
  details: 'than',
  dateUpdated: dayjs('2024-07-02T16:04'),
};

export const sampleWithNewData: NewMedicalRecord = {
  numMedicalRecord: 'because mostly boo',
  details: 'brr while mechanically',
  dateUpdated: dayjs('2024-07-02T15:54'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
