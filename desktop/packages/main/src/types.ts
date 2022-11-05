import {
  PointerId,
  PointerOrientation,
  PointerType,
  User,
} from "@smartpointer-desktop/shared";

import { RoomResponse } from "./generated/http-client";

export type Room = RoomResponse;

export type ActivePointer = {
  user: User;
  orientation: PointerOrientation;
};

export type State = Readonly<
  | {
      status: "READY" | "CREATING";
      room?: undefined;
      joinedUsers?: undefined;
      activePointers?: undefined;
      selectedPointerType?: undefined;
    }
  | {
      status: "CREATED";
      room: Room;
      joinedUsers: Map<User["id"], User>;
      activePointers: Map<ActivePointer["user"]["id"], ActivePointer>;
      selectedPointerType: PointerType;
    }
>;
