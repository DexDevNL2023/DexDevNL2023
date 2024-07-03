import dayjs from 'dayjs/esm';

import { IPaiement, NewPaiement } from './paiement.model';

export const sampleWithRequiredData: IPaiement = {
  id: 26882,
  datePaiement: dayjs('2024-07-02T23:15'),
  montant: 26208.68,
  type: 'REMBOURSEMENT',
  status: 'FAILED',
};

export const sampleWithPartialData: IPaiement = {
  id: 1486,
  datePaiement: dayjs('2024-07-02T14:09'),
  montant: 13313.19,
  type: 'PRIME',
  status: 'FAILED',
};

export const sampleWithFullData: IPaiement = {
  id: 31475,
  datePaiement: dayjs('2024-07-02T22:38'),
  montant: 29477.88,
  type: 'PRIME',
  status: 'PENDING',
};

export const sampleWithNewData: NewPaiement = {
  datePaiement: dayjs('2024-07-02T18:21'),
  montant: 7229.18,
  type: 'REMBOURSEMENT',
  status: 'COMPLETED',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
