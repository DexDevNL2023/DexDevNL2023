import { IMedicalDocument, NewMedicalDocument } from './medical-document.model';

export const sampleWithRequiredData: IMedicalDocument = {
  id: 22117,
  nom: 'pinafore starchy',
  type: 'than and pot',
  url: 'https://eminent-catch.org/',
};

export const sampleWithPartialData: IMedicalDocument = {
  id: 31029,
  nom: 'yuppify',
  type: 'landscape or',
  url: 'https://frozen-perfume.info',
};

export const sampleWithFullData: IMedicalDocument = {
  id: 7684,
  nom: 'since stealthily deliberately',
  type: 'gee offbeat blind',
  url: 'https://yawning-comfortable.info',
};

export const sampleWithNewData: NewMedicalDocument = {
  nom: 'adventurously considering',
  type: 'woot',
  url: 'https://emotional-harmonize.name/',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
