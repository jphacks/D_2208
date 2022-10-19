import type { Room, User, Coordinate } from "@/types";

type State =
  | {
      name: "READY";
      room?: undefined;
      users?: undefined;
      pointers?: undefined;
    }
  | {
      name: "CREATING";
      room?: undefined;
      users?: undefined;
      pointers?: undefined;
    }
  | {
      name: "CREATED";
      room: Room;
      users: Map<User["id"], User>;
      pointers: Map<User["id"], Coordinate>;
    };

export class AppState {
  #state: State = {
    name: "READY",
  };

  #subscribeListeners = new Set<(state: Readonly<State>) => void>();

  get state(): Readonly<State> {
    return this.#state;
  }

  public setState(state: State | ((state: State) => State)): void {
    if (typeof state === "function") {
      this.#state = state(this.#state);
    } else {
      this.#state = state;
    }
    this.#subscribeListeners.forEach((callback) => callback(this.#state));
  }

  public subscribe(callback: (state: Readonly<State>) => void): void {
    this.#subscribeListeners.add(callback);
  }

  public unsubscribe(callback: (state: Readonly<State>) => void): void {
    this.#subscribeListeners.delete(callback);
  }
}

export default AppState;
