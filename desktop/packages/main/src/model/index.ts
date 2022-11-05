import {
  PointerOrientation,
  pointers,
  PointerType,
  User,
} from "@smartpointer-desktop/shared";

import { Room, State } from "@/types";

let state: State = {
  status: "READY",
};

export const getState = (): State => state;

export const initialize = () => {
  state = {
    status: "READY",
  };
};

export const startCreatingRoom = () => {
  if (state.status !== "READY") {
    throw new Error("Cannot start creating room when not in READY state");
  }

  state = {
    status: "CREATING",
  };
};

export const createdRoom = (room: Room) => {
  if (state.status !== "CREATING") {
    throw new Error("Cannot create room when not in CREATING state");
  }

  state = {
    status: "CREATED",
    room,
    joinedUsers: new Map(),
    activePointers: new Map(),
    selectedPointerType: pointers[0]!,
  };
};

export const closeRoom = () => {
  if (state.status !== "CREATED") {
    throw new Error("Cannot close room when not in CREATED state");
  }

  state = {
    status: "READY",
  };
};

export const joinedRoom = (user: User) => {
  if (state.status !== "CREATED") {
    throw new Error("Cannot join room when not in CREATED state");
  }

  state = {
    ...state,
    joinedUsers: new Map(state.joinedUsers).set(user.id, user),
  };
};

export const leftRoom = (user: User) => {
  if (state.status !== "CREATED") {
    throw new Error("Cannot leave room when not in CREATED state");
  }

  const joinedUsers = new Map(state.joinedUsers);

  joinedUsers.delete(user.id);

  state = {
    ...state,
    joinedUsers,
  };
};

export const updatePointer = (user: User, orientation: PointerOrientation) => {
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
};

export const deactivatePointer = (user: User) => {
  if (state.status !== "CREATED") {
    throw new Error("Cannot deactivate pointer when not in CREATED state");
  }

  const activePointers = new Map(state.activePointers);

  activePointers.delete(user.id);

  state = {
    ...state,
    activePointers,
  };
};

export const selectedPointer = (selectedPointerType: PointerType) => {
  if (state.status !== "CREATED") {
    throw new Error("Cannot select pointer when not in CREATED state");
  }

  state = {
    ...state,
    selectedPointerType,
  };
};
