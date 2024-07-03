import dayjs from 'dayjs/esm';
import { ISouscription } from 'app/entities/insurances/souscription/souscription.model';
import { PaymentType } from 'app/entities/enumerations/payment-type.model';
import { PaymentStatus } from 'app/entities/enumerations/payment-status.model';

export interface IPaiement {
  id: number;
  datePaiement?: dayjs.Dayjs | null;
  montant?: number | null;
  type?: keyof typeof PaymentType | null;
  status?: keyof typeof PaymentStatus | null;
  souscription?: Pick<ISouscription, 'id'> | null;
}

export type NewPaiement = Omit<IPaiement, 'id'> & { id: null };
