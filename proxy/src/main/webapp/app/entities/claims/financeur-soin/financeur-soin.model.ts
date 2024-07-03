import { IPrestationSoin } from 'app/entities/claims/prestation-soin/prestation-soin.model';
import { FinanceurType } from 'app/entities/enumerations/financeur-type.model';

export interface IFinanceurSoin {
  id: number;
  nom?: string | null;
  type?: keyof typeof FinanceurType | null;
  adresse?: string | null;
  telephone?: string | null;
  email?: string | null;
  servicesFournis?: string | null;
  prestationSoins?: Pick<IPrestationSoin, 'id'>[] | null;
}

export type NewFinanceurSoin = Omit<IFinanceurSoin, 'id'> & { id: null };
