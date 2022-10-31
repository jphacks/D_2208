import { PointerCoordinate } from "@smartpointer-desktop/shared";

import { view } from "../view";

import { Room, State, User } from "@/types";

class StateInternal {
  #data: State | undefined;

  get data() {
    if (!this.#data) {
      throw new Error("State is not initialized");
    }

    return this.#data;
  }
  set data(newState: State) {
    if (import.meta.env.DEV) {
      console.log("=".repeat(80));
      console.log("State updated");
      console.log(newState);
    }

    this.#data = newState;
    view(newState);
  }
}

const state = new StateInternal();

export const initialize = () => {
  state.data = {
    status: "READY",
  };
};

export const startCreatingRoom = () => {
  if (state.data.status !== "READY") {
    throw new Error("Cannot start creating room when not in READY state");
  }

  state.data = {
    status: "CREATING",
  };
};

export const createdRoom = (room: Room) => {
  if (state.data.status !== "CREATING") {
    throw new Error("Cannot create room when not in CREATING state");
  }

  state.data = {
    status: "CREATED",
    room,
    joinedUsers: new Map(),
    activePointers: new Map(),
    showInviteLink: true,
    showOverlayWindowDevTools: import.meta.env.DEV,
  };
};

export const joinedRoom = (user: User) => {
  if (state.data.status !== "CREATED") {
    throw new Error("Cannot join room when not in CREATED state");
  }

  state.data = {
    ...state.data,
    joinedUsers: new Map(state.data.joinedUsers).set(user.id, user),
  };
};

export const leftRoom = (user: User) => {
  if (state.data.status !== "CREATED") {
    throw new Error("Cannot leave room when not in CREATED state");
  }

  const joinedUsers = new Map(state.data.joinedUsers);

  joinedUsers.delete(user.id);

  state.data = {
    ...state.data,
    joinedUsers,
  };
};

export const updatePointer = (
  userId: User["id"],
  pointer: PointerCoordinate
) => {
  if (state.data.status !== "CREATED") {
    throw new Error("Cannot update pointer when not in CREATED state");
  }

  state.data = {
    ...state.data,
    activePointers: new Map(state.data.activePointers).set(userId, pointer),
  };
};

export const deactivatePointer = (userId: User["id"]) => {
  if (state.data.status !== "CREATED") {
    throw new Error("Cannot deactivate pointer when not in CREATED state");
  }

  const activePointers = new Map(state.data.activePointers);

  activePointers.delete(userId);

  state.data = {
    ...state.data,
    activePointers,
  };
};

export const showInviteLink = () => {
  if (state.data.status !== "CREATED") {
    throw new Error("Cannot show invite link when not in CREATED state");
  }

  if (state.data.showInviteLink) {
    return;
  }

  state.data = {
    ...state.data,
    showInviteLink: true,
  };
};

export const hideInviteLink = () => {
  if (state.data.status !== "CREATED") {
    throw new Error("Cannot hide invite link when not in CREATED state");
  }

  if (!state.data.showInviteLink) {
    return;
  }

  state.data = {
    ...state.data,
    showInviteLink: false,
  };
};

export const closeOverlayWindowDevTools = () => {
  if (state.data.status !== "CREATED") {
    throw new Error(
      "Cannot toggle overlay window dev tools when not in CREATED state"
    );
  }

  state.data = {
    ...state.data,
    showOverlayWindowDevTools: false,
  };
};

export const openOverlayWindowDevTools = () => {
  if (state.data.status !== "CREATED") {
    throw new Error(
      "Cannot toggle overlay window dev tools when not in CREATED state"
    );
  }

  state.data = {
    ...state.data,
    showOverlayWindowDevTools: true,
  };
};

export const toggleOverlayWindowDevTools = () => {
  if (state.data.status !== "CREATED") {
    throw new Error(
      "Cannot toggle overlay window dev tools when not in CREATED state"
    );
  }

  state.data = {
    ...state.data,
    showOverlayWindowDevTools: !state.data.showOverlayWindowDevTools,
  };
};
