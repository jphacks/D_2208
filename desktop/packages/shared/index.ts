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
  label: string;
};

export type CustomPointerType = {
  id: string;
  label: string;
  content: string;
};

export type PointerType = BuiltInPointerType | CustomPointerType;

export const builtInPointers: PointerType[] = [
  {
    id: "SPOTLIGHT",
    label: "スポットライト",
  },
  {
    id: "ARROW",
    label: "矢印",
  },
  {
    id: "FINGER",
    label: "人差し指",
  },
];
