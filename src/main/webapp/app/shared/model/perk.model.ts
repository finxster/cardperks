import dayjs from 'dayjs';
import { IStore } from 'app/shared/model/store.model';
import { ICard } from 'app/shared/model/card.model';

export interface IPerk {
  id?: number;
  name?: string | null;
  description?: string | null;
  expirationDate?: dayjs.Dayjs | null;
  active?: boolean | null;
  expired?: boolean | null;
  stores?: IStore[] | null;
  card?: ICard | null;
}

export const defaultValue: Readonly<IPerk> = {
  active: false,
  expired: false,
};
