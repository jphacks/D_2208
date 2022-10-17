import Room from "./Room";
import { updateMenu } from "./menu";

// HACK: 良い感じの名前
class AppState {
  room: Room | undefined;
  // 二度クリックされないための変数、あとで名前変える
  isActivating = false;

  async createRoom() {
    this.isActivating = true;
    updateMenu(this);

    this.room = await Room.build();
    this.isActivating = false;
    updateMenu(this);
  }

  async deleteRoom() {
    this.isActivating = true;
    updateMenu(this);

    await this.room?.delete();
    this.room = undefined;
    this.isActivating = false;
    updateMenu(this);
  }
}

export default AppState;
