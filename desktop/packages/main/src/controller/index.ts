import {
  CustomPointerType,
  PointerOrientation,
  PointerType,
  User,
} from "@smartpointer-desktop/shared";
import { randomUUID } from "crypto";

import { request } from "@/api";
import { graphql } from "@/gql";
import { model } from "@/model";
import {
  activate,
  listenRoomSubscription,
  unsubscribeRoomSubscription,
} from "@/stomp";
import { store } from "@/store";
import { view } from "@/view";

export const controller = {
  initialize: () => {
    model.initialize(store.get("customPointerTypes"));

    view.tray.update();
  },

  createRoom: async () => {
    model.startCreatingRoom();

    const data = await request({
      query: graphql(`
        mutation CreateRoom {
          createRoom {
            id
            passcode
          }
        }
      `),
    });

    console.log("created room", data.createRoom);

    await activate();

    model.createdRoom(data.createRoom);

    listenRoomSubscription(data.createRoom.id);

    view.tray.update();
    await view.window.pointerOverlay.show();
    await view.window.inviteLink.show();
  },

  joinedRoom: (user: User) => {
    model.joinedRoom(user);
  },

  leftRoom: (user: User) => {
    model.leftRoom(user);
  },

  closeRoom: () => {
    view.window.pointerOverlay.close();
    view.window.inviteLink.close();
    unsubscribeRoomSubscription();

    model.closeRoom();
  },

  pointerUpdated: (user: User, orientation: PointerOrientation) => {
    model.updatePointer(user, orientation);

    view.window.pointerOverlay.updatePointer();
  },

  pointerDeactivated: (user: User) => {
    model.deactivatePointer(user);

    view.window.pointerOverlay.updatePointer();
  },

  selectedPointer: (selectedPointerType: PointerType) => {
    model.selectedPointer(selectedPointerType);

    view.window.pointerOverlay.updatePointerType();
  },

  showInviteLink: async () => {
    await view.window.inviteLink.show();
  },

  toggleOverlayWindowDevTools: () => {
    view.window.pointerOverlay.toggleDevTools();
  },

  addCustomPointerType: () => {
    const customPointerType: CustomPointerType = {
      id: randomUUID(),
      name: "新規カスタムポインター",
    };

    model.addedCustomPointerType(customPointerType);

    view.tray.update();

    store.set("customPointerTypes", model.state.customPointerTypes);

    view.window.customPointerType.updateCustomPointerType();
  },

  removeCustomPointerType: (customPointerType: CustomPointerType) => {
    model.removedCustomPointerType(customPointerType);

    view.tray.update();

    store.set("customPointerTypes", model.state.customPointerTypes);

    view.window.customPointerType.updateCustomPointerType();
  },

  updateCustomPointerType: (customPointerType: CustomPointerType) => {
    model.updatedCustomPointerType(customPointerType);

    view.tray.update();

    store.set("customPointerTypes", model.state.customPointerTypes);

    view.window.customPointerType.updateCustomPointerType();
  },

  showCustomPointerTypes: () => {
    view.window.customPointerType.show();
  },
};
