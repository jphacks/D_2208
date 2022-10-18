class Room {
  users: string[] = [];
  public static async build() {
    const room = new Room();
    room.users = await room.fetchUsers();
    return room;
  }
  private async fetchUsers() {
    // TODO: ここ全部あとでかえる
    await this.wait(1000);
    return ["user1", "user2", "user3"];
  }
  async delete() {
    // TODO: ここ全部あとでかえる
    await this.wait(1000);
  }
  // TODO: あとで消す
  private wait = async (ms: number) =>
    new Promise((resolve) => setTimeout(resolve, ms));
}

export default Room;
