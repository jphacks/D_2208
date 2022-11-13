import {
  CustomPointerType,
  PointerOrientation,
  PointerType,
  User,
} from "@smartpointer-desktop/shared";
import { randomUUID } from "crypto";
import { screen } from "electron";

import {
  closeWsClient,
  initializeWsClient,
  requestHttp,
  requestWs,
} from "@/api";
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
      query: graphql(/* GraphQL */ `
        mutation CreateRoom {
          createRoom(pointerType: "SPOTLIGHT") {
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

    requestWs(
      {
        query: graphql(/* GraphQL */ `
          subscription SubscribeToPointerType($roomId: ID!) {
            subscribeToPointerType(roomId: $roomId)
          }
        `),
        variables: {
          roomId: data.createRoom.id,
        },
      },
      {
        next(value) {
          const data = value.data?.subscribeToPointerType;
          if (data) {
            model.selectedPointer(data);

            view.window.pointerOverlay.updatePointerType();
            view.tray.update();
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
          subscription SubscribeToUsers($roomId: ID!) {
            subscribeToUsers(roomId: $roomId) {
              users {
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
          const data = value.data?.subscribeToUsers;
          if (data) {
            controller.updateUserList(data.users);
          }
        },
        // eslint-disable-next-line @typescript-eslint/no-empty-function
        error() {},
        // eslint-disable-next-line @typescript-eslint/no-empty-function
        complete() {},
      }
    );

    await Promise.all(
      model.state.customPointerTypes.map(async (customPointerType) => {
        requestHttp({
          query: graphql(/* GraphQL */ `
            mutation CreateCustomPointers(
              $roomId: ID!
              $id: ID!
              $label: String!
              $content: String!
            ) {
              createCustomPointer(
                roomId: $roomId
                id: $id
                label: $label
                content: $content
              )
            }
          `),
          variables: {
            roomId: data.createRoom.id,
            id: customPointerType.id,
            label: customPointerType.label,
            content: customPointerType.content.replace(
              /^data:.+;base64,\s*/,
              ""
            ),
          },
        });
      })
    );

    view.tray.update();
    await view.window.pointerOverlay.show();
    await view.window.inviteLink.show();
  },

  closeRoom: async () => {
    if (model.state.status !== "CREATED") {
      throw new Error("Cannot close room when not in created state");
    }

    store.set("customPointerTypes", model.state.customPointerTypes);

    await requestHttp({
      query: graphql(/* GraphQL */ `
        mutation DeleteRoom($roomId: ID!) {
          deleteRoom(roomId: $roomId)
        }
      `),
      variables: {
        roomId: model.state.room.id,
      },
    });

    view.window.pointerOverlay.close();
    view.window.inviteLink.close();

    closeWsClient();
    model.closeRoom();

    view.tray.update();
  },

  pointerUpdated: (user: User, orientation: PointerOrientation) => {
    model.updatePointer(user, orientation);

    view.window.pointerOverlay.updatePointer();
  },

  pointerDeactivated: (userId: User["id"]) => {
    model.deactivatePointer(userId);

    view.window.pointerOverlay.updatePointer();
  },
  showInviteLink: async () => {
    await view.window.inviteLink.show();
  },

  toggleOverlayWindowDevTools: () => {
    view.window.pointerOverlay.toggleDevTools();
  },

  addCustomPointerType: (label: string, content: string) => {
    const id = randomUUID();

    model.addedCustomPointerType({ id, label, content });

    view.tray.update();

    store.set("customPointerTypes", model.state.customPointerTypes);

    view.window.customPointerType.updateCustomPointerType();

    if (model.state.status !== "CREATED") {
      return;
    }

    requestHttp({
      query: graphql(/* GraphQL */ `
        mutation CreateCustomPointer(
          $roomId: ID!
          $id: ID!
          $label: String!
          $content: String!
        ) {
          createCustomPointer(
            roomId: $roomId
            id: $id
            label: $label
            content: $content
          )
        }
      `),
      variables: {
        roomId: model.state.room.id,
        id,
        label,
        content: content.replace(/^data:.+;base64,\s*/, ""),
      },
    });
  },

  removeCustomPointerType: (customPointerTypeId: CustomPointerType["id"]) => {
    if (
      model.state.status === "CREATED" &&
      model.state.selectedPointerTypeId === customPointerTypeId
    ) {
      model.selectedPointer("SPOTLIGHT");
      requestHttp({
        query: graphql(/* GraphQL */ `
          mutation ChangePointerTypeToSpotlight($roomId: ID!) {
            changePointerType(pointerType: "SPOTLIGHT", roomId: $roomId)
          }
        `),
        variables: {
          roomId: model.state.room.id,
        },
      });
    }

    model.removedCustomPointerType(customPointerTypeId);
    view.tray.update();

    store.set("customPointerTypes", model.state.customPointerTypes);

    view.window.customPointerType.updateCustomPointerType();

    if (model.state.status !== "CREATED") {
      return;
    }

    requestHttp({
      query: graphql(/* GraphQL */ `
        mutation DeleteCustomPointer($roomId: ID!, $id: ID!) {
          deleteCustomPointer(id: $id, roomId: $roomId)
        }
      `),
      variables: {
        roomId: model.state.room.id,
        id: customPointerTypeId,
      },
    });
  },

  requestChangePointerType: (pointerTypeId: PointerType["id"]) => {
    if (model.state.status !== "CREATED") {
      throw new Error("Cannot change pointer type when not in created state");
    }

    requestWs(
      {
        query: graphql(/* GraphQL */ `
          mutation ChangePointerType($pointerType: String!, $roomId: ID!) {
            changePointerType(pointerType: $pointerType, roomId: $roomId)
          }
        `),
        variables: {
          pointerType: pointerTypeId,
          roomId: model.state.room.id,
        },
      },
      {
        // eslint-disable-next-line @typescript-eslint/no-empty-function
        next: () => {},
        // eslint-disable-next-line @typescript-eslint/no-empty-function
        error: () => {},
        // eslint-disable-next-line @typescript-eslint/no-empty-function
        complete: () => {},
      }
    );
  },

  showCustomPointerTypes: () => {
    view.window.customPointerType.show();
  },

  updateDisplayToShowPointer: (displayId: number) => {
    model.updatedDisplayToShowPointer(displayId);

    view.window.pointerOverlay.setBoundsToDisplay();

    view.tray.update();
  },

  showUserList: () => {
    view.window.userList.show();
  },

  requestUserList: () => {
    if (model.state.status !== "CREATED") {
      throw new Error("Cannot request user list when not in created state");
    }

    requestWs(
      {
        query: graphql(/* GraphQL */ `
          query GetUsers($roomId: ID!) {
            getUsers(roomId: $roomId) {
              users {
                id
                name
              }
            }
          }
        `),
        variables: {
          roomId: model.state.room.id,
        },
      },
      {
        next(value) {
          const data = value.data?.getUsers;
          if (data) {
            view.window.userList.updateUsers(data.users);
          }
        },
        // eslint-disable-next-line @typescript-eslint/no-empty-function
        error() {},
        // eslint-disable-next-line @typescript-eslint/no-empty-function
        complete() {},
      }
    );
  },

  updateUserList: (users: User[]) => {
    view.window.userList.updateUsers(users);
  },
};
