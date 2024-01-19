import { IPerk } from 'app/shared/model/perk.model';

export interface IStore {
  id?: number;
  name?: string | null;
  perk?: IPerk | null;
}

export const defaultValue: Readonly<IStore> = {};
