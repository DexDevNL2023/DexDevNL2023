import dayjs from 'dayjs/esm';

import { IDocumentDeSinistre, NewDocumentDeSinistre } from './document-de-sinistre.model';

export const sampleWithRequiredData: IDocumentDeSinistre = {
  id: 463,
  nom: 'synchronize jaggedly squawk',
  type: 'yuck',
  url: 'https://different-scorpion.info/',
  dateTelechargement: dayjs('2024-07-03T05:00'),
};

export const sampleWithPartialData: IDocumentDeSinistre = {
  id: 20149,
  nom: 'wool till unselfish',
  type: 'hence beside',
  url: 'https://horrible-sponge.biz',
  dateTelechargement: dayjs('2024-07-02T20:54'),
};

export const sampleWithFullData: IDocumentDeSinistre = {
  id: 23278,
  nom: 'upbeat concerning',
  type: 'meanwhile',
  url: 'https://heartfelt-impostor.info/',
  dateTelechargement: dayjs('2024-07-02T14:40'),
};

export const sampleWithNewData: NewDocumentDeSinistre = {
  nom: 'skiing',
  type: 'bloom mmm',
  url: 'https://dapper-whey.com/',
  dateTelechargement: dayjs('2024-07-02T20:26'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
