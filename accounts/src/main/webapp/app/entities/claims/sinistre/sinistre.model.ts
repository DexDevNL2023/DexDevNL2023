import dayjs from 'dayjs/esm';
import { IFournisseur } from 'app/entities/claims/fournisseur/fournisseur.model';
import { IPrestationSoin } from 'app/entities/claims/prestation-soin/prestation-soin.model';
import { ClaimStatus } from 'app/entities/enumerations/claim-status.model';

export interface ISinistre {
  id: number;
  numeroSinistre?: string | null;
  dateDeclaration?: dayjs.Dayjs | null;
  dateTraitement?: dayjs.Dayjs | null;
  status?: keyof typeof ClaimStatus | null;
  raison?: string | null;
  montantReclame?: number | null;
  montantApprouve?: number | null;
  souscriptionId?: number | null;
  fournisseur?: Pick<IFournisseur, 'id'> | null;
  prestationsSoin?: Pick<IPrestationSoin, 'id'> | null;
}

export type NewSinistre = Omit<ISinistre, 'id'> & { id: null };
