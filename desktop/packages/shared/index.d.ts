declare module "@smartpointer-desktop/shared" {
  export type PointerCoordinate = {
    x: number;
    y: number;
  };

  export type PointerOrientation = {
    alpha: number;
    beta: number;
    gamma: number;
  };

  export type User = {
    id: string;
    name: string;
  };

  export type UpdatePointersMessage = {
    user: User;
    coordinate: PointerCoordinate;
  }[];
}
