import { roomApi } from "@/api";
import type AppState from "@/AppState";
import { showOverlayWindow } from "@/pointer";

export const createRoom = (appState: AppState) => async () => {
  if (appState.state.name !== "READY") {
    throw new Error("なんしとんねん");
  }

  appState.setState({
    name: "CREATING",
  });

  try {
    const {
      data: { roomId, passcode },
    } = await roomApi.createRoom();

    appState.setState({
      name: "CREATED",
      room: { id: roomId, passcode },
      users: new Map(),
      pointers: new Map(),
    });

    await showOverlayWindow(appState);
  } catch (e) {
    console.error(e);
  }
};
