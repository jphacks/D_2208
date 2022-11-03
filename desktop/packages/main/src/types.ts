import { PointerCoordinate } from "@smartpointer-desktop/shared";

import { RoomResponse } from "./generated/http-client";

export type Room = RoomResponse;

export type User = {
  id: string;
  name: string;
};

export type State = Readonly<
  | {
      status: "READY" | "CREATING";
      room?: undefined;
      joinedUsers?: undefined;
      activePointers?: undefined;
    }
  | {
      status: "CREATED";
      room: Room;
      joinedUsers: Map<User["id"], User>;
      activePointers: Map<User["id"], PointerCoordinate>;
    }
>;
