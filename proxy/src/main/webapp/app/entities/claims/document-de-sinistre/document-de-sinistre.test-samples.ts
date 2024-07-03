import dayjs from 'dayjs/esm';

import { IDocumentDeSinistre, NewDocumentDeSinistre } from './document-de-sinistre.model';

export const sampleWithRequiredData: IDocumentDeSinistre = {
  id: 12824,
  nom: 'cogitate',
  type: 'hacienda wholly',
  url: 'https://miserly-nation.info',
  dateTelechargement: dayjs('2024-07-02T22:25'),
};

export const sampleWithPartialData: IDocumentDeSinistre = {
  id: 339,
  nom: 'valiantly',
  type: 'compete psst boo',
  url: 'https://zigzag-booster.com/',
  dateTelechargement: dayjs('2024-07-02T17:50'),
};

export const sampleWithFullData: IDocumentDeSinistre = {
  id: 4174,
  nom: 'provided pleasing freeze',
  type: 'wit',
  url: 'https://dear-songbird.net',
  dateTelechargement: dayjs('2024-07-03T02:35'),
};

export const sampleWithNewData: NewDocumentDeSinistre = {
  nom: 'triumphantly midst touch',
  type: 'geez oddly mmm',
  url: 'https://unusual-ownership.info',
  dateTelechargement: dayjs('2024-07-03T02:11'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
