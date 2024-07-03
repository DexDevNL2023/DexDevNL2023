import { IMedicalDocument, NewMedicalDocument } from './medical-document.model';

export const sampleWithRequiredData: IMedicalDocument = {
  id: 4919,
  nom: 'apud',
  type: 'since',
  url: 'https://windy-harp.net/',
};

export const sampleWithPartialData: IMedicalDocument = {
  id: 4206,
  nom: 'mix round canter',
  type: 'aside sharply',
  url: 'https://edible-quiche.biz/',
};

export const sampleWithFullData: IMedicalDocument = {
  id: 15523,
  nom: 'pretty',
  type: 'where acidly accomplished',
  url: 'https://voluminous-paranoia.net/',
};

export const sampleWithNewData: NewMedicalDocument = {
  nom: 'geez polite upside-down',
  type: 'upside-down gah beyond',
  url: 'https://wee-vulture.biz/',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
