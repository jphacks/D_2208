import { PointerCoordinate } from "@smartpointer-desktop/shared";

import { roomApi } from "@/api";
import * as model from "@/model";
import { listenRoomSubscription } from "@/stomp";
import { User } from "@/types";

export const initialize = () => {
  model.initialize();
};

export const createRoom = async () => {
  model.startCreatingRoom();

  const { data } = await roomApi.createRoom();

  model.createdRoom(data);

  listenRoomSubscription(data.roomId);
};

export const joinedRoom = (user: User) => {
  model.joinedRoom(user);
};

export const leftRoom = (user: User) => {
  model.leftRoom(user);
};

export const pointerUpdated = (
  userId: User["id"],
  pointer: PointerCoordinate
) => {
  model.updatePointer(userId, pointer);
};

export const pointerDeactivated = (userId: User["id"]) => {
  model.deactivatePointer(userId);
};

export const showInviteLink = () => {
  model.showInviteLink();
};

export const hideInviteLink = () => {
  model.hideInviteLink();
};

export const closeOverlayWindowDevTools = () => {
  model.closeOverlayWindowDevTools();
};

export const openOverlayWindowDevTools = () => {
  model.openOverlayWindowDevTools();
};

export const toggleOverlayWindowDevTools = () => {
  model.toggleOverlayWindowDevTools();
};
