class Room {
  users: string[] = [];
  constructor() {
    // TODO: あとでかえる
    this.fetchUsers();
  }
  private fetchUsers() {
    // TODO: あとでかえる
    this.users = ["ユーザ1", "ユーザ2"];
  }
  delete() {}
}

export default Room;
