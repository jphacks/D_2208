import {
  CustomPointerType,
  PointerOrientation,
  PointerType,
  User,
} from "@smartpointer-desktop/shared";
import { randomUUID } from "crypto";
import { screen } from "electron";

import { initializeWsClient, requestHttp, requestWs } from "@/api";
import { graphql } from "@/gql";
import { model } from "@/model";
import { goNext, goPrevious } from "@/pagination";
import { store } from "@/store";
import { view } from "@/view";

export const controller = {
  initialize: () => {
    model.initialize(store.get("customPointerTypes"));

    view.tray.update();

    screen.on("display-added", () => {
      view.tray.update();
    });

    screen.on("display-removed", (_, oldDisplay) => {
      const state = model.state;
      if (state.status !== "CREATED") {
        return;
      }

      if (state.displayToShowPointer === oldDisplay.id) {
        controller.updateDisplayToShowPointer(screen.getPrimaryDisplay().id);
      } else {
        view.tray.update();
      }
    });

    screen.on("display-metrics-changed", (_, display) => {
      const state = model.state;
      if (state.status !== "CREATED") {
        return;
      }

      if (state.displayToShowPointer === display.id) {
        view.window.pointerOverlay.setBoundsToDisplay();
      }

      view.tray.update();
    });
  },

  createRoom: async () => {
    model.startCreatingRoom();

    const data = await requestHttp({
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

    initializeWsClient();

    model.createdRoom(data.createRoom, screen.getPrimaryDisplay().id);

    requestWs(
      {
        query: graphql(/* GraphQL */ `
          subscription SubscribeToSlideControl($roomId: ID!) {
            subscribeToSlideControl(roomId: $roomId)
          }
        `),
        variables: {
          roomId: data.createRoom.id,
        },
      },
      {
        next({ data }) {
          if (data) {
            switch (data.subscribeToSlideControl) {
              case "NEXT": {
                goNext();
                return;
              }
              case "PREVIOUS": {
                goPrevious();
              }
            }
          }
        },
        // eslint-disable-next-line @typescript-eslint/no-empty-function
        error() {},
        // eslint-disable-next-line @typescript-eslint/no-empty-function
        complete() {},
      }
    );

    requestWs(
      {
        query: graphql(/* GraphQL */ `
          subscription SubscribeToPointer($roomId: ID!) {
            subscribeToPointer(roomId: $roomId) {
              orientation {
                alpha
                beta
                gamma
              }
              user {
                id
                name
              }
            }
          }
        `),
        variables: {
          roomId: data.createRoom.id,
        },
      },
      {
        next(value) {
          const data = value.data?.subscribeToPointer;
          if (data) {
            controller.pointerUpdated(data.user, data.orientation);
          }
        },
        // eslint-disable-next-line @typescript-eslint/no-empty-function
        error() {},
        // eslint-disable-next-line @typescript-eslint/no-empty-function
        complete() {},
      }
    );

    requestWs(
      {
        query: graphql(/* GraphQL */ `
          subscription SubscribeToPointerDisconnectEvent($roomId: ID!) {
            subscribeToPointerDisconnectEvent(roomId: $roomId) {
              id
            }
          }
        `),
        variables: {
          roomId: data.createRoom.id,
        },
      },
      {
        next(value) {
          const data = value.data?.subscribeToPointerDisconnectEvent;
          if (data) {
            controller.pointerDeactivated(data.id);
          }
        },
        // eslint-disable-next-line @typescript-eslint/no-empty-function
        error() {},
        // eslint-disable-next-line @typescript-eslint/no-empty-function
        complete() {},
      }
    );

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
    // TODO: GraphQL 移行
    // unsubscribeRoomSubscription();

    model.closeRoom();
  },

  pointerUpdated: (user: User, orientation: PointerOrientation) => {
    model.updatePointer(user, orientation);

    view.window.pointerOverlay.updatePointer();
  },

  pointerDeactivated: (userId: User["id"]) => {
    model.deactivatePointer(userId);

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

  updateDisplayToShowPointer: (displayId: number) => {
    model.updatedDisplayToShowPointer(displayId);

    view.window.pointerOverlay.setBoundsToDisplay();

    view.tray.update();
  },
};
