import {SideMenuType} from "../../types";

export type SingleTableProps = {
  selectedId: number | null;
  setLastSelectedTable: (id: number | null) => void;
  setCurrentMenuItem: (menuType: SideMenuType) => void;
};

export type DishInfo = {
  dishName: string;
  price: number;
  quantity: number;
  total: number;
};

export type GuestInfo = {
  positions: DishInfo[];
  sumPerGuest: number;
};

export type TableEstimateResponse = {
  totalSum: number;
  guests: GuestInfo[];
};

export type TableEstimationDataProps = {
  setCurrentMenuItem: (menuType: SideMenuType) => void;
  onCancel: () => void;
  onAccept: () => void;
  onCardDialogOpen: () => void;
  tableId: number;
  card: { id: number, percentage: number } | null;
};
