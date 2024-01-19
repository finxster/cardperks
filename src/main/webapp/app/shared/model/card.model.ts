import { IPerk } from 'app/shared/model/perk.model';

export interface ICard {
  id?: number;
  name?: string | null;
  perks?: IPerk[] | null;
}

export const defaultValue: Readonly<ICard> = {};
