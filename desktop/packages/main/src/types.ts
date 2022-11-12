import {
  CustomPointerType,
  PointerOrientation,
  PointerType,
  User,
} from "@smartpointer-desktop/shared";
import { Display } from "electron";

export type Room = {
  id: string;
  passcode: string;
};

export type ActivePointer = {
  user: User;
  orientation: PointerOrientation;
};

export type State = Readonly<
  (
    | {
        status: "READY" | "CREATING";
        room?: undefined;
        activePointers?: undefined;
        selectedPointerTypeId?: undefined;
        displayToShowPointer?: undefined;
      }
    | {
        status: "CREATED";
        room: Room;
        activePointers: Map<ActivePointer["user"]["id"], ActivePointer>;
        selectedPointerTypeId: PointerType["id"];
        displayToShowPointer: Display["id"];
      }
  ) & {
    customPointerTypes: CustomPointerType[];
  }
>;
