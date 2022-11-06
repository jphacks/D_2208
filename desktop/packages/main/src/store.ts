import { CustomPointerType } from "@smartpointer-desktop/shared";
import Store from "electron-store";

type StoreType = {
  customPointerTypes: CustomPointerType[];
};

const initialState: StoreType = {
  customPointerTypes: [],
};

export const store = new Store<StoreType>({
  defaults: initialState,
});
