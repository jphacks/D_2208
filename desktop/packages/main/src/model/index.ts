import {
  PointerOrientation,
  PointerType,
  User,
  CustomPointerType,
} from "@smartpointer-desktop/shared";
import { Display } from "electron";

import { Room, State } from "@/types";

let state: State;

export const model = {
  get state() {
    return state;
  },

  initialize: (customPointerTypes: CustomPointerType[]) => {
    state = {
      status: "READY",
      customPointerTypes,
    };
  },

  startCreatingRoom: () => {
    if (state.status !== "READY") {
      throw new Error("Cannot start creating room when not in READY state");
    }

    state = {
      ...state,
      status: "CREATING",
    };
  },

  createdRoom: (room: Room, displayToShowPointer: Display["id"]) => {
    if (state.status !== "CREATING") {
      throw new Error("Cannot create room when not in CREATING state");
    }

    state = {
      ...state,
      status: "CREATED",
      room,
      joinedUsers: new Map(),
      activePointers: new Map(),
      selectedPointerTypeId: "SPOTLIGHT",
      displayToShowPointer,
    };
  },

  closeRoom: () => {
    if (state.status !== "CREATED") {
      throw new Error("Cannot close room when not in CREATED state");
    }

    state = {
      ...state,
      status: "READY",
      room: undefined,
      joinedUsers: undefined,
      activePointers: undefined,
      selectedPointerTypeId: undefined,
      displayToShowPointer: undefined,
    };
  },

  joinedRoom: (user: User) => {
    if (state.status !== "CREATED") {
      throw new Error("Cannot join room when not in CREATED state");
    }

    state = {
      ...state,
      joinedUsers: new Map(state.joinedUsers).set(user.id, user),
    };
  },

  leftRoom: (user: User) => {
    if (state.status !== "CREATED") {
      throw new Error("Cannot leave room when not in CREATED state");
    }

    const joinedUsers = new Map(state.joinedUsers);

    joinedUsers.delete(user.id);

    state = {
      ...state,
      joinedUsers,
    };
  },

  updatePointer: (user: User, orientation: PointerOrientation) => {
    if (state.status !== "CREATED") {
      throw new Error("Cannot update pointer when not in CREATED state");
    }

    state = {
      ...state,
      activePointers: new Map(state.activePointers).set(user.id, {
        orientation,
        user,
      }),
    };
  },

  deactivatePointer: (userId: User["id"]) => {
    if (state.status !== "CREATED") {
      throw new Error("Cannot deactivate pointer when not in CREATED state");
    }

    const activePointers = new Map(state.activePointers);

    activePointers.delete(userId);

    state = {
      ...state,
      activePointers,
    };
  },

  selectedPointer: (selectedPointerTypeId: PointerType["id"]) => {
    if (state.status !== "CREATED") {
      throw new Error("Cannot select pointer when not in CREATED state");
    }

    state = {
      ...state,
      selectedPointerTypeId,
    };
  },

  updatedCustomPointerTypes: (customPointerTypes: CustomPointerType[]) => {
    state = {
      ...state,
      customPointerTypes,
    };
  },

  addedCustomPointerType: (customPointerType: CustomPointerType) => {
    state = {
      ...state,
      customPointerTypes: [customPointerType, ...state.customPointerTypes],
    };
  },

  removedCustomPointerType: (customPointerType: CustomPointerType) => {
    state = {
      ...state,
      customPointerTypes: state.customPointerTypes.filter(
        (type) => type.id !== customPointerType.id
      ),
    };
  },

  updatedCustomPointerType: (customPointerType: CustomPointerType) => {
    state = {
      ...state,
      customPointerTypes: state.customPointerTypes.map((type) =>
        type.id === customPointerType.id ? customPointerType : type
      ),
    };
  },

  updatedDisplayToShowPointer: (displayToShowPointer: Display["id"]) => {
    if (state.status !== "CREATED") {
      throw new Error(
        "Cannot update display to show pointer when not in CREATED state"
      );
    }

    state = {
      ...state,
      displayToShowPointer,
    };
  },
};
