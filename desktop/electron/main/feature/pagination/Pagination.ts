import { Feature } from "@/feature/type";

export class Pagination extends Feature {
  // @ts-expect-error state いらんけど、型が合わないので (Feature クラスの定義参照)
  private readonly state = {};

  async run() {
    // TODO: subscribe して controller を叩く <ペシペシ！
    // stompClient.on("next", goNext);
    // stompClient.on("previous", goPrevious);
  }
}
