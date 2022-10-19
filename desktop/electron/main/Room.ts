import type AppState from "@/AppState";

const delay = (ms: number) => new Promise((resolve) => setTimeout(resolve, ms));

export const createRoom =
  ({
    appState,
  }: // TODO: httpClient,
  {
    appState: AppState;
    // httpClient: HttpClient;
  }) =>
  async () => {
    if (appState.state.name !== "READY") {
      throw new Error("なんしとんねん");
    }

    appState.setState({
      name: "CREATING",
    });

    console.log("ルームを作るよ");
    // TODO: httpClient からルームを作成する
    await delay(1000); // TODO: ここは消す

    const roomId = "aaa";

    appState.setState({
      name: "CREATED",
      room: { id: roomId },
      users: new Map(),
      pointers: new Map(),
    });

    console.log(appState);
  };
