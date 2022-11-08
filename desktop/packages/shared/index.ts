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

export type GetPointerResult = {
  pointers: UpdatePointersMessage;
  pointerType: PointerType;
};

export type BuiltInPointerId = "SPOTLIGHT" | "ARROW" | "FINGER";

export type BuiltInPointerType = {
  id: BuiltInPointerId;
  name: string;
};

export type CustomPointerType = {
  id: string;
  name: string;
  image?: string;
};

export type PointerType = BuiltInPointerType | CustomPointerType;

export const builtInPointers: PointerType[] = [
  {
    id: "SPOTLIGHT",
    name: "スポットライト",
  },
  {
    id: "ARROW",
    name: "矢印",
  },
  {
    id: "FINGER",
    name: "人差し指",
  },
];
