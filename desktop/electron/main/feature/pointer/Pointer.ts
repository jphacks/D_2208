import { BrowserWindow } from "electron";

import { Feature } from "@/feature/type";

type Coodinate = {
  x: number;
  y: number;
};

type UserId = string;

type State = {
  pointers: Map<UserId, Coodinate>;
};

export class Pointer extends Feature<State> {
  state: State = {
    pointers: new Map(),
  };

  private overlayWindow = new BrowserWindow({
    //
  });

  private onPointerMove(userId: UserId, coordinate: Coodinate) {
    this.setState((state) => {
      state.pointers.set(userId, coordinate);
      return state;
    });
  }

  async run() {
    // this.stompClient.on("pointer", (userId: UserId, coordinate: Coodinate) => {
    //   this.setState((state) => {
    //     state.pointers.set(userId, coordinate);
    //     return state;
    //   });
    // });
  }
}
