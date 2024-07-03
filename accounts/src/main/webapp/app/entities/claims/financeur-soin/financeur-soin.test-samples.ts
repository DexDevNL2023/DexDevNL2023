import { IFinanceurSoin, NewFinanceurSoin } from './financeur-soin.model';

export const sampleWithRequiredData: IFinanceurSoin = {
  id: 635,
  nom: 'boohoo carjack',
  type: 'ASSUREUR',
  adresse: 'adduce',
  telephone: '1-765-632-4890 x9181',
  email: 'Clinton29@yahoo.com',
  servicesFournis: 'haven inscribe',
};

export const sampleWithPartialData: IFinanceurSoin = {
  id: 23274,
  nom: 'garden',
  type: 'MUTUELLE',
  adresse: 'descriptive',
  telephone: '630-331-4164 x57967',
  email: 'Leta2@hotmail.com',
  servicesFournis: 'incidentally',
};

export const sampleWithFullData: IFinanceurSoin = {
  id: 5127,
  nom: 'beautifully yet',
  type: 'MUTUELLE',
  adresse: 'rotating',
  telephone: '(962) 952-8314 x67980',
  email: 'Deron47@gmail.com',
  servicesFournis: 'abnormally pfft lodge',
};

export const sampleWithNewData: NewFinanceurSoin = {
  nom: 'now',
  type: 'MUTUELLE',
  adresse: 'kindly lane dangerous',
  telephone: '(982) 472-3313 x54760',
  email: 'Florine68@gmail.com',
  servicesFournis: 'reflecting eventually',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
