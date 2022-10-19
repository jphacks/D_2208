import { Feature } from "@/feature/type";

type State = {
  roomId: string | null;
};

export class Room extends Feature<State> {
  state: State = {
    roomId: null,
  };

  async run() {
    // TODO:
  }

  public create() {
    // TODO: Menu から呼び出されて、Room を作成して、Room の状態を更新する
  }
}
