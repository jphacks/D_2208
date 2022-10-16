import Room from "./Room";

// HACK: 良い感じの名前
class AppState {
  room: Room | undefined;
  isActivating: boolean = false;
  createRoom() {
    this.isActivating = true; // FIXME: ここでエラーが出ている
    this.room = new Room();
    this.isActivating = false;
  }
  deleteRoom() {
    this.room?.delete();
    this.room = undefined;
  }
}

export default AppState;
