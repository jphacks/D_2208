import type { HttpClient } from "@/api/HttpClient";
import type { StompClient } from "@/api/StompClient";

export abstract class Feature<
  State extends { [K in string]: unknown } | undefined = undefined
> {
  // TODO: conditional optional property が使えるようになったら、State が undefined のとき optional にする
  //  https://github.com/microsoft/TypeScript/issues/44261
  /**
   * 継承先で readonly にしてね
   */
  abstract state: State;

  private stateHandlers: Set<(state: State) => void> = new Set();

  constructor(
    private httpClient: HttpClient,
    private stompClient: StompClient
  ) {}

  abstract run(): Promise<void>;

  protected setState(state: State | ((state: State) => State)) {
    if (typeof state === "function") {
      this.state = state(this.state);
    } else {
      this.state = state;
    }
    this.stateHandlers.forEach((handler) => handler(this.state));
  }

  public subscribeState(handler: (state: State) => void) {
    this.stateHandlers.add(handler);
  }
}
