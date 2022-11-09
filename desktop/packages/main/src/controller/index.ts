import {
  CustomPointerType,
  PointerOrientation,
  PointerType,
  User,
} from "@smartpointer-desktop/shared";
import { randomUUID } from "crypto";

import { roomApi } from "@/api";
import { model } from "@/model";
import {
  activate,
  listenRoomSubscription,
  unsubscribeRoomSubscription,
} from "@/stomp";
import { store } from "@/store";
import { view } from "@/view";

export const initialize = () => {
  model.initialize(store.get("customPointerTypes"));

  view.tray.update();
};

export const createRoom = async () => {
  model.startCreatingRoom();

  const { data } = await roomApi.createRoom();

  console.log("created room", data);

  await activate();

  model.createdRoom(data);

  listenRoomSubscription(data.roomId);

  view.tray.update();
  await view.window.pointerOverlay.show();
  await showInviteLink();
};

export const joinedRoom = (user: User) => {
  model.joinedRoom(user);
};

export const leftRoom = (user: User) => {
  model.leftRoom(user);
};

export const closeRoom = () => {
  view.window.pointerOverlay.close();
  view.window.inviteLink.close();
  unsubscribeRoomSubscription();

  model.closeRoom();
};

export const pointerUpdated = (user: User, orientation: PointerOrientation) => {
  model.updatePointer(user, orientation);

  view.window.pointerOverlay.updatePointer();
};

export const pointerDeactivated = (user: User) => {
  model.deactivatePointer(user);

  view.window.pointerOverlay.updatePointer();
};

export const selectedPointer = (selectedPointerType: PointerType) => {
  model.selectedPointer(selectedPointerType);

  view.window.pointerOverlay.updatePointerType();
};

export const showInviteLink = async () => {
  await view.window.inviteLink.show();
};

export const toggleOverlayWindowDevTools = () => {
  view.window.pointerOverlay.toggleDevTools();
};

export const addCustomPointerType = () => {
  const customPointerType: CustomPointerType = {
    id: randomUUID(),
    name: "新規カスタムポインター",
  };

  model.addedCustomPointerType(customPointerType);

  view.tray.update();

  store.set("customPointerTypes", model.state.customPointerTypes);

  view.window.customPointerType.updateCustomPointerType();
};

export const removeCustomPointerType = (
  customPointerType: CustomPointerType
) => {
  model.removedCustomPointerType(customPointerType);

  view.tray.update();

  store.set("customPointerTypes", model.state.customPointerTypes);

  view.window.customPointerType.updateCustomPointerType();
};

export const updateCustomPointerType = (
  customPointerType: CustomPointerType
) => {
  model.updatedCustomPointerType(customPointerType);

  view.tray.update();

  store.set("customPointerTypes", model.state.customPointerTypes);

  view.window.customPointerType.updateCustomPointerType();
};

export const showCustomPointerTypes = () => {
  view.window.customPointerType.show();
};
