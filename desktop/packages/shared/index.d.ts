declare module "@smartpointer-desktop/shared" {
  export type PointerCoordinate = {
    x: number;
    y: number;
  };

  export type Pointers = {
    userId: string;
    name: string;
    pointer: PointerCoordinate;
  }[];
}
