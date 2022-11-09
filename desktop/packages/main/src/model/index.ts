import {
  PointerOrientation,
  builtInPointers,
  PointerType,
  User,
  CustomPointerType,
} from "@smartpointer-desktop/shared";

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

  createdRoom: (room: Room) => {
    if (state.status !== "CREATING") {
      throw new Error("Cannot create room when not in CREATING state");
    }

    state = {
      ...state,
      status: "CREATED",
      room,
      joinedUsers: new Map(),
      activePointers: new Map(),
      selectedPointerType: builtInPointers[0]!,
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

  deactivatePointer: (user: User) => {
    if (state.status !== "CREATED") {
      throw new Error("Cannot deactivate pointer when not in CREATED state");
    }

    const activePointers = new Map(state.activePointers);

    activePointers.delete(user.id);

    state = {
      ...state,
      activePointers,
    };
  },

  selectedPointer: (selectedPointerType: PointerType) => {
    if (state.status !== "CREATED") {
      throw new Error("Cannot select pointer when not in CREATED state");
    }

    state = {
      ...state,
      selectedPointerType,
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
};
